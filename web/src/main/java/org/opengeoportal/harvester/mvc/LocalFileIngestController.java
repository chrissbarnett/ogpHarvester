package org.opengeoportal.harvester.mvc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.opengeoportal.harvester.api.domain.Ingest;
import org.opengeoportal.harvester.api.domain.IngestLocalFile;
import org.opengeoportal.harvester.api.service.IngestService;
import org.opengeoportal.harvester.mvc.bean.LocalIngestFormBean;
//import org.opengeoportal.harvester.mvc.bean.JsonResponse.STATUS;
import org.opengeoportal.harvester.mvc.exception.ItemNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Maps;

/**
 * Web controller that manage the Ingests.
 *
 * @author <a href="mailto:juanluisrp@geocat.net">Juan Luis Rodríguez</a>.
 *
 */
@Controller
@SessionAttributes(types = {LocalIngestFormBean.class})
public class LocalFileIngestController {

    @Resource
    private IngestService ingestService;

    @RequestMapping(value = "/rest/fileingest/new", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> createIngest(
            @RequestBody LocalIngestFormBean ingestFormBean, @RequestParam("metadataFile[]") MultipartFile[] metadataFile) throws IOException {
        Ingest ingest = null;
        boolean updating = ingestFormBean.getId() != null;
        if (updating) {
            ingest = ingestService.findById(ingestFormBean.getId());
            if (ingest == null) {
                throw new ItemNotFoundException(
                        "Cannot find an Ingest with id "
                        + ingestFormBean.getId());
            }
        } else {

        	ingest = new IngestLocalFile();

        }

		IngestLocalFile localIngest = (IngestLocalFile) ingest;
		//localIngest.setUploadedFiles(processUpload(metadataFile));
		
        ingest.setNameOgpRepository(ingestFormBean.getNameOgpRepository());

        // remove old required fields and add the new ones
        ingest.getRequiredFields().clear();
        for (Entry<String, Boolean> requiredField : ingestFormBean
                .getRequiredFields().entrySet()) {
            if (requiredField.getValue()) {
                ingest.addRequiredField(requiredField.getKey());
            }
        }

        ingest = ingestService.saveAndSchedule(ingest);

        Map<String, Object> result = Maps.newHashMap();
        result.put("success", true);
        Map<String, Object> data = Maps.newHashMap();
        data.put("id", ingest.getId());
        data.put("name", ingest.getName());
        result.put("data", data);

        return result;
    }

    private List<File> processUpload(MultipartFile[] metadataFile) throws IOException{
		List<File> uploadedFiles = new ArrayList<File>();
		File tempDir = File.createTempFile("metadata", "_" + new Date());
		tempDir.mkdir();
		
		for (MultipartFile file : metadataFile){
			String fileSuffix;
			String fileName = file.getOriginalFilename();
			
			String contentType = file.getContentType().toLowerCase();

			if (contentType.contains("xml")){
				fileSuffix = "xml";
			} else if (contentType.contains("zip")){
				fileSuffix = "zip";
			} else {
				//skip the file...we don't want it
				continue;
			}

			File tempFile = new File(tempDir, fileName.substring(0, fileName.indexOf(".")) + "." + fileSuffix); 
			file.transferTo(tempFile);
			uploadedFiles.add(tempFile);
		}
		
		return uploadedFiles;
    }
    
    /**
     * Return details about an ingest.
     *
     * @param id ingest identifier.
     * @return JSON with ingest details.
     */
    @RequestMapping(value = "/rest/fileingest/{id}/details")
    @ResponseBody
    public LocalIngestFormBean getDetails(@PathVariable("id") Long id) {
        LocalIngestFormBean result = new LocalIngestFormBean();

        // Check if ingest exist
        Ingest ingest = ingestService.findById(id);
        if (ingest == null) {
            throw new ItemNotFoundException("Cannot find an ingest with id "
                    + id);
        }

        // common fields
        result.setBeginDate(ingest.getBeginDate());
        result.setIngestName(ingest.getName());
        result.setNameOgpRepository(ingest.getNameOgpRepository());
        result.setId(ingest.getId());
        result.setUrl(ingest.getUrl());

        Map<String, Boolean> requiredFields = result.getRequiredFields();
        if (requiredFields == null) {
            requiredFields = Maps.newHashMap();
            result.setRequiredFields(requiredFields);
        }
        for (String requiredField : ingest.getRequiredFields()) {
            requiredFields.put(requiredField, Boolean.TRUE);
        }

        return result;
    }


    /*@RequestMapping("/")
    public String angular() {
        return "ngView";
    }*/

}
