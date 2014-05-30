package org.opengeoportal.mapserver.api.client;

import org.opengeoportal.harvester.api.metadata.model.AccessLevel;

public enum Role {
	PUBLIC_VECTOR, PUBLIC_RASTER, RESTRICTED_VECTOR, RESTRICTED_RASTER;

	public static Role parseRole(String geom, AccessLevel access) throws RoleParseException {
		if (access.equals(AccessLevel.Public)){
			if (geom.equalsIgnoreCase("vector")){
				return Role.PUBLIC_VECTOR;
			} else if (geom.equalsIgnoreCase("raster")){
				return Role.PUBLIC_RASTER;
			} else {
				throw new RoleParseException("Unrecognized Role.");
			}
		} else if (access.equals(AccessLevel.Restricted)){
			if (geom.equalsIgnoreCase("vector")){
				return Role.PUBLIC_VECTOR;
			} else if (geom.equalsIgnoreCase("raster")){
				return Role.PUBLIC_RASTER;
			} else {
				throw new RoleParseException("Unrecognized Role.");
			}
		} else {
			throw new RoleParseException("Unrecognized Role.");
		}
	}
}
