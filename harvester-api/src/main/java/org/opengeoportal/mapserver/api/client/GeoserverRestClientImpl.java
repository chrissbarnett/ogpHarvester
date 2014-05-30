package org.opengeoportal.mapserver.api.client;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opengeoportal.harvester.api.domain.DataStoreNode;
import org.opengeoportal.harvester.api.metadata.model.Metadata;
import org.opengeoportal.harvester.api.metadata.model.ThemeKeywords;
import org.opengeoportal.mapserver.api.client.AddVectorRequestJson.FeatureType;
import org.opengeoportal.mapserver.api.client.DataStoreInfoResponseJson.DataStore.ConnectionParameters.ValueMap;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class GeoserverRestClientImpl implements GeoserverRestClient {
	
	    private final RestTemplate restTemplate;
	    
	    private DataStoreNode dataStoreNode;
		private Metadata metadata;
		private String datastore;
		
		public final static String METHOD_POST = "POST";
		public final static String METHOD_GET = "GET";
		public final static String METHOD_PUT = "PUT";
		public final static String METHOD_DELETE = "DELETE";
		
		private final static String DATA_STORE_PATH = "{serverName}/rest/workspaces/{workspace}/datastores.json";
		private final static String DATA_STORE_DETAILS_PATH = "{serverName}/rest/workspaces/{workspace}/datastores/{datastore}.json";
		private final static String ADD_FILE_TO_DATA_STORE_PATH = "{serverName}/rest/workspaces/{workspace}/datastores/{datastore}/{filename}";
		private final static String FEATURE_TYPES_PATH = "{serverName}/rest/workspaces/{workspace}/datastores/{datastore}/featuretypes.json";
		private final static String FEATURE_TYPE_DETAILS_PATH = "{serverName}/rest/workspaces/{workspace}/datastores/{datastore}/featuretypes/{featuretypename}.json";
		private final static String AVAILABLE_FEATURE_TYPES_PATH = "{serverName}/rest/workspaces/{workspace}/datastores/{datastore}/featuretypes.json?list=available";
		private final static String COVERAGES_PATH = "{serverName}/rest/workspaces/{workspace}/coveragestores/{coveragestore}/coverages";
		private final static String COVERAGE_STORES_PATH = "{serverName}/rest/workspaces/{workspace}/coveragestores";
		private final static String RELOAD_PATH = "{serverName}/rest/reload";		
		
		final Logger logger = LoggerFactory.getLogger(this.getClass());

		

		public GeoserverRestClientImpl(DataStoreNode dataStoreNode, Metadata metadata) {
			
			this.setDataStoreNode(dataStoreNode);
			this.setMetadata(metadata);

			
			this.restTemplate = createAuthenticatingRestTemplate(dataStoreNode.getServerUrl(), dataStoreNode.getUsername(), dataStoreNode.getUserpassword());

	    }
	
	    
		private RestTemplate createAuthenticatingRestTemplate(String url, String username, String userpass){
			
	        Credentials credentials = new UsernamePasswordCredentials(username, userpass);
	    	DefaultHttpClient httpclient = new DefaultHttpClient();
	    	HttpHost targetHost = new HttpHost(urlToHost(url));
			httpclient.getCredentialsProvider().setCredentials(
	    	        new AuthScope(AuthScope.ANY), 
	    	        credentials);
	    	
			// Create AuthCache instance
			AuthCache authCache = new BasicAuthCache();
			// Generate BASIC scheme object and add it to the local auth cache
			BasicScheme basicAuth = new BasicScheme();
			authCache.put(targetHost, basicAuth);

			// Add AuthCache to the execution context
			BasicHttpContext localcontext = new BasicHttpContext();
			localcontext.setAttribute(ClientContext.AUTH_CACHE, authCache);

			HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpclient);
			
	        return new RestTemplate(factory);
		}
		
		private String urlToHost(String url){
			url = url.replace("http://", "");
			url = url.replace("https://", "");
			if (url.indexOf("/") > 0){
				url = url.substring(0, url.indexOf("/"));
			}
			
			return url;
		}
		
		
		@Override
		public boolean addVectorFileToDataStore(File file){
			//what's the name of this?
    		Map<String, String> vars = new HashMap<String, String>();
    		vars.put("serverName", this.dataStoreNode.getServerUrl());
    		vars.put("workspace", this.dataStoreNode.getWorkspacePrefix());
    		vars.put("datastore", this.getDatastore(true));
    		vars.put("filename", file.getName() + ".shp");
    		try{
    			this.restTemplate.put(ADD_FILE_TO_DATA_STORE_PATH, file, vars);
    		} catch (Exception e){
    			return false;
    		}
			return true;
		}
		
		@Override
		public boolean addVectorFileToDataStore(String fileUrl){
    		Map<String, String> vars = new HashMap<String, String>();
    		vars.put("serverName", this.dataStoreNode.getServerUrl());
    		vars.put("workspace", this.dataStoreNode.getWorkspacePrefix());
    		vars.put("datastore", this.getDatastore(true));
    		URL url = null;
			try {
				url = new URL(fileUrl);
	    		vars.put("filename", url.getFile() + ".shp");
				this.restTemplate.put(ADD_FILE_TO_DATA_STORE_PATH, fileUrl, vars);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			
			return true;
		}
		
		@Override
		public boolean createFeatureType(Metadata metadata) throws IOException {
			
    		AddVectorRequestJson addRequestJson = this.createAddVectorRequestObject(metadata);
    		//rest/workspaces/sde/datastores/arcsde10/featuretypes/GISPORTAL.GISOWNER01.NEWENGESAPTS98COPY_PROJECT.json
    		//tells if already configured
    		Map<String, String> vars = new HashMap<String, String>();
    		vars.put("serverName", this.dataStoreNode.getServerUrl());
    		vars.put("workspace", this.dataStoreNode.getWorkspacePrefix());
    		vars.put("datastore", this.getDatastore());
    		
    		return createItem(METHOD_POST, FEATURE_TYPES_PATH, vars, addRequestJson, String.class);

		}
		
	



		private AddVectorRequestJson createAddVectorRequestObject(Metadata metadata) {
	    	
	    	AddVectorRequestJson addVectorRequestJson = new AddVectorRequestJson();
	    	FeatureType ft = addVectorRequestJson.getFeatureType();
	    	ft.setName(metadata.getOwsName());
	    	//addVectorRequestJson.featureType.setNativeName(layerPrefix + layerName);
	    	ft.setTitle(metadata.getTitle());
	    	ft.setDescription(metadata.getDescription());
	    	for (ThemeKeywords keywords: metadata.getThemeKeywords()){
	    		for (String keyword: keywords.getKeywords()){
	    			ft.getKeywords().getString().add(keyword);
	    		}
	    	}
	    	//
	    	//addVectorRequestJson.featureType.metadataLinks.metadataLink.get(0).content = this.metadataUrl;
	    	//addVectorRequestJson.featureType.metadataLinks.metadataLink.get(0).metadataType = this.metadataType;
	    	//addVectorRequestJson.featureType.metadataLinks.metadataLink.get(0).type = this.metadataContentType;

	    	return addVectorRequestJson;
	    }
	    
		private boolean createItem(String method, String path, Map<String,String> vars, Object requestObj, Class<?> responseClass){
    		

    		try {
    			this.sendRequest(method, path, requestObj, vars, responseClass); 

    		} catch (HttpClientErrorException e){
    			HttpStatus status = e.getStatusCode();
	    		if ((status == HttpStatus.INTERNAL_SERVER_ERROR)||(status == HttpStatus.SERVICE_UNAVAILABLE)){
	    			//wait and try again
	                try {
						Thread.sleep(10000);
		    			this.sendRequest(method, path, requestObj, vars, responseClass); 


					} catch (Exception e1) {
						e1.printStackTrace();
		    			logger.error("failed to add layer");
		    			return false;
					}
	    		} else {
	    			logger.error("failed to add layer");
	    			return false;
	    		}
	    	} 
    		catch (Exception e){
    			logger.error("failed to add layer");
    			return false;
    		}
    		return true;	
		}


	    private Object sendRequest(String method, String path, Object postObj, Map<String, String> pathParams, Class<?> responseType) throws Exception{
	    	if (method.equalsIgnoreCase(METHOD_GET)){
	    		return this.restTemplate.getForObject(path, 
	    				responseType, pathParams);
	    	} else if (method.equalsIgnoreCase(METHOD_POST)){
		    	return this.restTemplate.postForObject(path, postObj,
		    			responseType, pathParams);
	    	} else {
	    		throw new Exception("Unsupported method: " + method);
	    	}
	    	
	    }
		
		
		
	    @Override
	    public boolean featureTypeExists(String ftName){
	    	//rest/workspaces/sde/datastores/arcsde10/featuretypes/GISPORTAL.GISOWNER01.NEWENGESAPTS98COPY_PROJECT.json
	    	//tells if already configured
	    	Map<String, String> vars = new HashMap<String, String>();
	    	vars.put("serverName", this.dataStoreNode.getServerUrl());
	    	vars.put("workspace", this.dataStoreNode.getWorkspacePrefix());
	    	vars.put("datastore", this.getDatastore());
	    	vars.put("featuretypename", ftName);
	    	
	    	try {
	    		String result = this.restTemplate.getForObject(FEATURE_TYPE_DETAILS_PATH, 
	    			String.class, vars);
	    	} catch (HttpClientErrorException e){
	    		return false;
	    	}

	    	return true;
	    	
	    }
	    
	    @Override
	    public boolean modifyFeatureType(Metadata metadata){
	    	//rest/workspaces/sde/datastores/arcsde10/featuretypes/GISPORTAL.GISOWNER01.NEWENGESAPTS98COPY_PROJECT.json
	    	//tells if already configured
	    	Map<String, String> vars = new HashMap<String, String>();
	    	vars.put("serverName", this.dataStoreNode.getServerUrl());
	    	vars.put("workspace", this.dataStoreNode.getWorkspacePrefix());
	    	vars.put("datastore", this.getDatastore());
	    	vars.put("featuretypename", metadata.getOwsName());
	    	AddVectorRequestJson vectorRequest = this.createAddVectorRequestObject(metadata);
	    	try {
	    		this.restTemplate.put(FEATURE_TYPE_DETAILS_PATH, 
	    			vectorRequest, vars);
	    	} catch (HttpClientErrorException e){
	    		return false;
	    	}

	    	return true;
	    	
	    }

	    
	    @Override
	    public ArrayList<String> queryAvailableFeatureTypes(){
	    	Map<String, String> vars = new HashMap<String, String>();
	    	vars.put("serverName", this.dataStoreNode.getServerUrl());
	    	vars.put("workspace", this.dataStoreNode.getWorkspacePrefix());
	    	vars.put("datastore", this.getDatastore());
	    	//System.out.println(this.geoserverUrl + "/rest/workspaces/" + this.workspace + "/datastores/" + this.datastore + "/featuretypes.json?list=available");
	    	AvailableVectorsResponseJson result = null;
	    	try {
	    		result = this.restTemplate.getForObject(AVAILABLE_FEATURE_TYPES_PATH, 
	    			AvailableVectorsResponseJson.class, vars);
	    	} catch (HttpClientErrorException e){
	    		HttpStatus status = e.getStatusCode();
	    		logger.warn(status.toString());
	    		if ((status == HttpStatus.INTERNAL_SERVER_ERROR)||(status == HttpStatus.SERVICE_UNAVAILABLE)){
	    			//retry once
	                try {
						Thread.sleep(10000);
			    		result = this.restTemplate.getForObject(AVAILABLE_FEATURE_TYPES_PATH, 
				    			AvailableVectorsResponseJson.class, vars);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
		    			logger.error("failed to query available layers");
					}
	    		} else {
	    			logger.error("failed to query available layers:" + e.getStatusCode().getReasonPhrase());
	    		}
	    	}
	    	return result.layerList.string;
	    }
	    
	    
	    
	    @Override
		public boolean createCoverage(Metadata metadata, String cs){
	    	//http://geoserverurl/rest/workspaces/wsname/coveragestores
    		Map<String, String> vars = new HashMap<String, String>();
    		vars.put("serverName", this.dataStoreNode.getServerUrl());
    		vars.put("workspace", this.dataStoreNode.getWorkspacePrefix());
    		vars.put("coveragestore", cs); 	//layerName.replace(".", "_"));
    		
    		AddCoverageRequestJson coverageRequest = createAddCoverageRequestJson(metadata);
    		
    		return createItem(METHOD_POST, COVERAGES_PATH, vars, coverageRequest, String.class);

	    }
		
	    @Override
		public boolean createCoverage(Metadata metadata){
			//In this case create the coverage store from the default data store, if possible
			String cs = "";
			return createCoverage(metadata, cs);
		}
	    
	    public AddCoverageRequestJson createAddCoverageRequestJson(Metadata metadata){
	    	AddCoverageRequestJson coverageRequest = new AddCoverageRequestJson();
	    	coverageRequest.getCoverage().setName(metadata.getOwsName());
	    	return coverageRequest;
	    }
	    
	    @Override
	    public String addCoverageStore(Metadata metadata, File file) throws Exception{
	    	//stub
    		return "";
	    }
	    
	    @Override
	    public String addCoverageStore(Metadata metadata, String fileUrl) throws Exception{
	    	//stub
    		return "";
	    }
	    
	    @Override
	    public String addCoverageStore(Metadata metadata) throws Exception{
    		Map<String, String> vars = new HashMap<String, String>();
    		vars.put("serverName", this.dataStoreNode.getServerUrl());
    		vars.put("workspace", this.dataStoreNode.getWorkspacePrefix());
    		vars.put("datastore", this.getDatastore());
    		//http://delisle.mit.edu:8080/geoserver/rest/workspaces/sde/datastores/ArrowsmithSDE.json
    		AddCoverageStoreRequestJson addCoverageStoreRequest = createAddCoverageStoreRequestJson(metadata);
    		String result = restTemplate.postForObject(COVERAGE_STORES_PATH, 
    				addCoverageStoreRequest, String.class, vars);
    		return result;
	    }

	    
	    public AddCoverageStoreRequestJson createAddCoverageStoreRequestJson(Metadata metadata) throws Exception{
	    	AddCoverageStoreRequestJson addCoverageStoreRequest = new AddCoverageStoreRequestJson();
	    	addCoverageStoreRequest.getCoverageStore().setType("ArcSDE Raster");
	    	addCoverageStoreRequest.getCoverageStore().setEnabled("true");
	    	String connectionString = getDBCoverageStoreConnectionStringFromDataStore();
	    	String layerName = metadata.getOwsName();
	    	addCoverageStoreRequest.getCoverageStore().setUrl(connectionString + layerName);
	    	addCoverageStoreRequest.getCoverageStore().setName(layerName.replace(".", "_"));
	    	return addCoverageStoreRequest;
	    }
	    
	    private String getDBCoverageStoreConnectionStringFromDataStore() throws Exception {
    		//http://delisle.mit.edu:8080/geoserver/rest/workspaces/sde/datastores/ArrowsmithSDE.json
	    	//"sde://sde_data:gis@arrowsmith.mit.edu:5150/#"
	    	//{"dataStore":{"name":"ArrowsmithSDE","type":"ArcSDE","enabled":true,"workspace":{"name":"sde","href":"http:\/\/delisle.mit.edu:8080\/geoserver\/rest\/workspaces\/sde.json"},
	    	//"connectionParameters":{"entry":[{"@key":"port","$":"5150"},{"@key":"dbtype","$":"arcsde"},{"@key":"datastore.allowNonSpatialTables","$":"false"},{"@key":"pool.timeOut","$":"500"},{"@key":"server","$":"arrowsmith.mit.edu"},{"@key":"pool.maxConnections","$":"6"},{"@key":"password","$":"gis"},{"@key":"user","$":"sde_data"},{"@key":"pool.minConnections","$":"2"},{"@key":"namespace","$":"http:\/\/geoserver.sf.net"}]},"__default":false,"featureTypes":"http:\/\/delisle.mit.edu:8080\/geoserver\/rest\/workspaces\/sde\/datastores\/ArrowsmithSDE\/featuretypes.json"}}
    		Map<String, String> vars = new HashMap<String, String>();
    		vars.put("serverName", this.dataStoreNode.getServerUrl());
    		vars.put("workspace", this.dataStoreNode.getWorkspacePrefix());
    		//we need a very specific data store here....  can we scan existing data stores to find it, or do we need to set it somewhere?
    		vars.put("datastore", "");
	    	DataStoreInfoResponseJson dataStoreResponse = restTemplate.getForObject(DATA_STORE_DETAILS_PATH, 
	    			DataStoreInfoResponseJson.class, vars);
	    	String user = null;
	    	String password = null;
	    	String server = null;
	    	String port = null;
	    	String dbtype = null;
	    	
	    	for (ValueMap valueMap : dataStoreResponse.dataStore.connectionParameters.entry){
	    		String key = valueMap.key;
	    		if (key.equalsIgnoreCase("port")){
	    			port = valueMap.value;
	    		} else if (key.equalsIgnoreCase("server")){
	    			server = valueMap.value;
	    		} else if (key.equalsIgnoreCase("user")){
	    			user = valueMap.value;
	    		} else if (key.equalsIgnoreCase("password")){
	    			password = valueMap.value;
	    		} else if (key.equalsIgnoreCase("dbtype")){
	    			dbtype = valueMap.value;
	    		}
	    	}
	    	if ((user == null)||(password == null)||(port == null)||(server == null)){
	    		throw new Exception("Valid connection parameters not found");
	    	}
			String connectionString = "sde://" + user + ":" + password + "@" + server + ":" + port + "/#";
			return connectionString;
		}
	    
	    @Override
	    public String reloadConfig(){
	    	Map<String, String> vars = new HashMap<String, String>();
	    	vars.put("serverName", this.dataStoreNode.getServerUrl());
	    	String result = restTemplate.postForObject(RELOAD_PATH, 
	    			null, String.class, vars);
	    	return result;
	    }
	    
	    public void seedLayer(String layerName){
	    	// curl -XPOST -u ${username}:${password} -H "Content-type: application/json" -d "{'seedRequest':{'name':'${layernameWithWorkspace}','srs':{'number':${epsgCode},'bounds':{'coords':{'double':['${bounds1}','${bounds2}','${bounds3}','${bounds4}']}},'zoomStart':1,'zoomStop':12,'format':'image\/png','type':'seed','threadCount':4}}}" http://${geoserverhostandpath}/gwc/rest/seed/${layernameWithWorkspace}.json
	    }


		public Metadata getMetadata() {
			return metadata;
		}


		public void setMetadata(Metadata metadata) {
			this.metadata = metadata;
		}


		public DataStoreNode getDataStoreNode() {
			return dataStoreNode;
		}


		public void setDataStoreNode(DataStoreNode dataStoreNode) {
			this.dataStoreNode = dataStoreNode;
		}


		public void setDatastore(String datastore) {
			this.datastore = datastore;
		}
		
		public String getDatastore(){
			return getDatastore(false);
		}
		
		public List<String> getAllDatastores(){
			List<String> datastores = new ArrayList<String>();
			
			return datastores;
		}
		
		private boolean inDatastores(String datastore){
			List<String> datastores = getAllDatastores();
			for (String ds: datastores){
				if (ds.equalsIgnoreCase(datastore)){
					return true;
				}
			}
			return false;
		}
		
		public String getSafeDatastoreName(){
			String name = getMetadata().getOwsName().replace(".", "_");
			int i = 1;
			while (inDatastores(name)){
				name += Integer.toString(i);
			}
			
			return name;
		}
		
		public String getDatastore(boolean createIfAbsent){
			if (this.datastore.isEmpty()){
				String ds = this.getDataStoreNode().getName();
					if (ds.isEmpty() && createIfAbsent){
						//provide a unique name for the data store

						ds = getSafeDatastoreName();
					} 
					setDatastore(ds);
					return ds;
			} else {
				return this.datastore;

			}
		}

	   


}
