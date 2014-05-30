/*
 * IngestFormBean.java
 *
 * Copyright (C) 2013
 *
 * This file is part of Open Geoportal Harvester.
 *
 * This software is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this library; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 *
 * As a special exception, if you link this library with other files to produce
 * an executable, this library does not by itself cause the resulting executable
 * to be covered by the GNU General Public License. This exception does not
 * however invalidate any other reasons why the executable file might be covered
 * by the GNU General Public License.
 *
 * Authors:: Jose García (mailto:jose.garcia@geocat.net)
 */
package org.opengeoportal.harvester.api.service;

import org.opengeoportal.harvester.api.domain.DataStoreNode;
import org.opengeoportal.mapserver.api.client.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.google.common.collect.ListMultimap;

public interface DataStoreNodeService {
	public DataStoreNode save(DataStoreNode dataStoreNode);

	/**
	 * Mark {@link DataStoreNode} with id <code>id</code> passed 
	 * 
	 * @param id custom repository identifier.
	 */
	public void logicalDelete(Long id);

	public DataStoreNode findByName(String name);

	public Page<DataStoreNode> findAll(Pageable pageable);

	public ListMultimap<Role, DataStoreNode> getAllGroupByRole();

	/**
	 * Retrieve the {@link DataStoreNode} with the identifier passed.
	 * 
	 * @param id
	 *            the {@link DataStoreNode} identifier.
	 * @return the {@link DataStoreNode} or null if can not be found.
	 */
	public DataStoreNode findById(Long id);

	/**
	 * Check if exists a non deleted {@link DataStoreNode} with the same name
	 * and role.
	 * 
	 * @param name
	 *            the datastore name.
	 * @param role
	 *            the datastore role.
	 * @return <code>true</code> if does not exist any DataStoreNode with
	 *         deleted=false, and the name and role passed, <code>false</code>
	 *         otherwise.
	 */
	public boolean checkExistDataStoreNodeNameAndRole(String name, Role role);

	public DataStoreNode findByRoleAndActiveAndDeletedFalse(Role role);
	
}
