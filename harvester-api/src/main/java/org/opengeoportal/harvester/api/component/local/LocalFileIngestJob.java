package org.opengeoportal.harvester.api.component.local;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.opengeoportal.harvester.api.component.BaseIngestJob;
import org.opengeoportal.harvester.api.domain.IngestLocalFile;
import org.opengeoportal.harvester.api.domain.IngestReportError;
import org.opengeoportal.harvester.api.domain.IngestReportErrorType;
import org.opengeoportal.harvester.api.metadata.model.Metadata;
import org.opengeoportal.harvester.api.metadata.parser.MetadataParser;
import org.opengeoportal.harvester.api.metadata.parser.MetadataParserResponse;
import org.opengeoportal.harvester.api.util.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.google.common.collect.ImmutableList;

public class LocalFileIngestJob extends BaseIngestJob {
    /**
     * Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
	@Override
	protected void ingest() {
        IngestLocalFile localIngest = (IngestLocalFile) ingest;
        List<File> uploadedFiles = new ArrayList<File>();//localIngest.getUploadedFiles();
		try{
			long failedRecordsCount = 0;
			for (File metadataFile : uploadedFiles){
				try {
					failedRecordsCount = processFile(metadataFile);
				} catch (Exception e) {
					saveException(e, IngestReportErrorType.SYSTEM_ERROR);
				} 
			}
			
			report.setFailedRecordsCount(failedRecordsCount);
			
		} finally {
			cleanupFiles(uploadedFiles);
		}
	}


	
	private long processFile(File file){
        long failedRecordsCount = 0;

        //if a zip file, unzip and iterate over xml files (check for nested directories)
        
        //else, just process the record if xml
        failedRecordsCount += processRecord(file);
        
        return failedRecordsCount;
	}
	
    /**
     * Process the xml metadata file, validating it and ingesting in the
     * system.
     *
     * @param file XML metadata file.
     * @return count of invalid records processed.
     */
    private long processRecord(File file) {
        long failedRecordsCount = 0;
        Document document = null;
        try {
            // Retrieve file content

            document = XmlUtil.load(new FileInputStream(file));
            MetadataParser parser = parserProvider.getMetadataParser(document);
            MetadataParserResponse parserResult = parser.parse(document);

            Metadata metadata = parserResult.getMetadata();
            metadata.setInstitution(ingest.getNameOgpRepository());

            boolean valid = metadataValidator.validate(metadata, report);
            if (valid) {
                metadataIngester.ingest(ImmutableList.of(metadata),
                        getIngestReport());
            } else {
                failedRecordsCount++;
            }

        } catch (Exception e) {
            failedRecordsCount++;
            logger.error("Error in Local File Ingest: " + this.ingest.getName()
                    + " (processing file:" + file.getName() + ")", e);
            saveException(e, IngestReportErrorType.SYSTEM_ERROR, document);
        }
        return failedRecordsCount;
    }
    
	private void cleanupFiles(List<File> uploadedFiles){
		try {
			File parentDir = uploadedFiles.get(0).getParentFile();
			for (File file : uploadedFiles){
				file.delete();
			}
			parentDir.delete();
			
		} catch (Exception e){
			IngestReportError error = new IngestReportError();
			error.setMessage("Failed to delete temp files");
			error.setType(IngestReportErrorType.SYSTEM_ERROR);
			report.addError(error);
		}
	}
}
