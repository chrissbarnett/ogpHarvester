package org.opengeoportal.harvester.api.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.opengeoportal.mapserver.api.client.Role;
import org.opengeoportal.mapserver.api.client.StoreType;
import org.springframework.data.jpa.domain.AbstractPersistable;


/**
 * A Geoserver datastore node configuration
 * 
 */
@Entity
public class DataStoreNode extends AbstractPersistable<Long>{

	/**
	 * 
	 */
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_ROLE = "role";

	
	private static final long serialVersionUID = -1264718063117819790L;
	
	@Column(nullable = true)
	private String name;
	@Column(nullable = false)
	private String serverUrl;
	@Column(nullable = false)
	private String workspacePrefix;
	@Column(nullable = true)
	private String username;
	@Column(nullable = true)
	private String userpassword;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private StoreType storeType;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;
	@Column
	private boolean deleted = false;
	@Column
	private boolean active;

	public DataStoreNode() {
		this.deleted = false;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getWorkspacePrefix() {
		return workspacePrefix;
	}

	public void setWorkspacePrefix(String workspacePrefix) {
		this.workspacePrefix = workspacePrefix;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserpassword() {
		return userpassword;
	}

	public void setUserpassword(String userpassword) {
		this.userpassword = userpassword;
	}

	public StoreType getStoreType() {
		return storeType;
	}

	public void setStoreType(StoreType storeType) {
		this.storeType = storeType;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}



}
