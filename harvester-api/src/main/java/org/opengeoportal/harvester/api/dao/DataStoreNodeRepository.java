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
package org.opengeoportal.harvester.api.dao;

import java.util.List;

import org.opengeoportal.harvester.api.domain.DataStoreNode;
import org.opengeoportal.mapserver.api.client.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataStoreNodeRepository extends
		JpaRepository<DataStoreNode, Long> {

	DataStoreNode findByName(String name);

	/**
	 * Find all not deleted data store nodes with the role passed.
	 * 
	 * @param role
	 *            the role {@link Role}.
	 * @param deleted 
	 * @return the list of all {@link DataStoreNode} with deleted=false, role
	 *         
	 */
	List<DataStoreNode> findByRoleAndDeletedFalse(Role role, boolean deleted);
	
	/**
	 * Find active, not deleted data store node with the role passed.
	 * 
	 * @param role
	 *            the role {@link Role}.            	 
	 * @param active 
	 * @param deleted 
	 * @return datastore node {@link DataStoreNode} with deleted=false, role, active=true
	 *         
	 */
	DataStoreNode findByRoleAndActiveAndDeleted(
			Role role, boolean active, boolean deleted);
	
	List<DataStoreNode> findByDeletedFalse(Sort sort);

	/**
	 * @param pageable
	 * @return
	 */
	Page<DataStoreNode> findByDeletedFalse(Pageable pageable);
}