package org.opengeoportal.mapserver.api.client;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;


//{"list":{"string":["GISPORTAL.GISOWNER01.NEWENGESAPTS98COPY_PROJECT",
public class AvailableVectorsResponseJson {
	@JsonProperty("list")
	LayerList layerList;
	
	public class LayerList {
		@JsonProperty("string")
		ArrayList<String> string;
	}
}
