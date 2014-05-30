package org.opengeoportal.mapserver.api.client;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.opengeoportal.harvester.api.metadata.model.Metadata;

public interface GeoserverRestClient {

	boolean createFeatureType(Metadata metadata) throws IOException;
	
	boolean featureTypeExists(String ftName);

	ArrayList<String> queryAvailableFeatureTypes();

	boolean createCoverage(Metadata metadata, String cs);

	boolean createCoverage(Metadata metadata);

	String addCoverageStore(Metadata metadata)
			throws Exception;

	String reloadConfig();

	boolean modifyFeatureType(Metadata metadata);

	boolean addVectorFileToDataStore(File file);

	boolean addVectorFileToDataStore(String fileUrl);

	String addCoverageStore(Metadata metadata, File file) throws Exception;

	String addCoverageStore(Metadata metadata, String fileUrl) throws Exception;





}
