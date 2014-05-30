package org.opengeoportal.mapserver.api.client;

import java.io.File;

import org.opengeoportal.harvester.api.domain.DataStoreNode;
import org.opengeoportal.harvester.api.metadata.model.AccessLevel;
import org.opengeoportal.harvester.api.metadata.model.GeometryType;
import org.opengeoportal.harvester.api.metadata.model.Metadata;
import org.opengeoportal.harvester.api.service.DataStoreNodeService;
import org.springframework.beans.factory.annotation.Autowired;

public class MapServerClientImpl {
	@Autowired
	private DataStoreNodeService dataStoreNodeService;
	
	public boolean addLayer(Metadata metadata, String fileUrl) throws Exception{
		AccessLevel access = metadata.getAccess();
		String geom = getBaseGeometryType(metadata.getGeometryType());
		
		DataStoreNode dsNode = getActiveDataStoreNode(geom, access);
		
		if (geom.equalsIgnoreCase("vector")){
			return addVectorLayer(dsNode, metadata, fileUrl, false);
		} else if (geom.equalsIgnoreCase("raster")){
			return addRasterLayer(dsNode, metadata, fileUrl);
		} else {
			return false;
		}
	}
	
	public boolean addLayer(Metadata metadata, File file) throws Exception{
		AccessLevel access = metadata.getAccess();
		String geom = getBaseGeometryType(metadata.getGeometryType());
		
		DataStoreNode dsNode = getActiveDataStoreNode(geom, access);
		
		//check for a datastore in the GeoServerNode object.  If one has been configured, 
		//check to see if it is a database.  If so, we'll 

		if (geom.equalsIgnoreCase("vector")){
			return addVectorLayer(dsNode, metadata, file, false);
		} else if (geom.equalsIgnoreCase("raster")){
			return addRasterLayer(dsNode, metadata, file);
		} else {
			return false;
		}
	}
	
	public boolean addLayer(Metadata metadata) throws Exception{
		AccessLevel access = metadata.getAccess();
		String geom = getBaseGeometryType(metadata.getGeometryType());
		
		DataStoreNode dsNode = getActiveDataStoreNode(geom, access);
				
		if (geom.equalsIgnoreCase("vector")){
			return addVectorLayer(dsNode, metadata, false);
		} else if (geom.equalsIgnoreCase("raster")){
			return addRasterLayer(dsNode, metadata);
		} else {
			return false;
		}
	}
	
	private String getBaseGeometryType(GeometryType geom) throws Exception{
		if (GeometryType.isRaster(geom)){
			return "raster";
		} else if (GeometryType.isVector(geom)){
			return "vector";
		} else {
			throw new Exception("Unrecognized geometry type.");
		}
	}
	
	private DataStoreNode getActiveDataStoreNode(String geom, AccessLevel access) throws RoleParseException{
		Role role = Role.parseRole(geom, access);
		return dataStoreNodeService.findByRoleAndActiveAndDeletedFalse(role);
	}
	
	public boolean addVectorLayer(DataStoreNode dsNode, Metadata metadata, File file, boolean createStyle) throws Exception{
		GeoserverRestClient geoserverRestClient = new GeoserverRestClientImpl(dsNode, metadata);

		geoserverRestClient.addVectorFileToDataStore(file);
		
		geoserverRestClient.createFeatureType(metadata);
		//geoserverRestClient.modifyFeatureType(metadata);
		
		return true;
	}
	
	public boolean addVectorLayer(DataStoreNode dsNode, Metadata metadata, String fileUrl, boolean createStyle) throws Exception{
		GeoserverRestClient geoserverRestClient = new GeoserverRestClientImpl(dsNode, metadata);

		geoserverRestClient.addVectorFileToDataStore(fileUrl);
		
		geoserverRestClient.createFeatureType(metadata);
		//geoserverRestClient.modifyFeatureType(metadata);

		return true;
	}
	
	public boolean addVectorLayer(DataStoreNode dsNode, Metadata metadata, boolean createStyle) throws Exception{
		GeoserverRestClient geoserverRestClient = new GeoserverRestClientImpl(dsNode, metadata);
		
		geoserverRestClient.createFeatureType(metadata);
		
		return true;
	}
	
	public boolean addRasterLayer(DataStoreNode dsNode, Metadata metadata){
		GeoserverRestClient geoserverRestClient = new GeoserverRestClientImpl(dsNode, metadata);

		return true;
	}
	
	public boolean addRasterLayer(DataStoreNode dsNode, Metadata metadata, File file){
		GeoserverRestClient geoserverRestClient = new GeoserverRestClientImpl(dsNode, metadata);

		return true;
	}
	
	public boolean addRasterLayer(DataStoreNode dsNode, Metadata metadata, String fileUrl){
		GeoserverRestClient geoserverRestClient = new GeoserverRestClientImpl(dsNode, metadata);

		return true;
	}
}
