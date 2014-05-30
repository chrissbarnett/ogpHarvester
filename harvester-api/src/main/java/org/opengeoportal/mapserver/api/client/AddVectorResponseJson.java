package org.opengeoportal.mapserver.api.client;

import com.fasterxml.jackson.annotation.JsonProperty;


public class AddVectorResponseJson {
/*
 * what is the response?
 */
	@JsonProperty("location")
		String location;

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}
}
