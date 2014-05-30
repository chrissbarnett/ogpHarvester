/*
 * CustomRepositoryController.java
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
 * Authors:: Juan Luis Rodríguez (mailto:juanluisrp@geocat.net)
 */
package org.opengeoportal.harvester.mvc;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;

import org.opengeoportal.harvester.api.domain.DataStoreNode;
import org.opengeoportal.harvester.api.service.DataStoreNodeService;
import org.opengeoportal.harvester.mvc.bean.DataStoreNodeFormBean;
import org.opengeoportal.harvester.mvc.bean.JsonResponse;
import org.opengeoportal.harvester.mvc.bean.JsonResponse.STATUS;
import org.opengeoportal.mapserver.api.client.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Controller
@SessionAttributes(types = { DataStoreNodeFormBean.class })
public class DataStoreNodeController {
	/** The logger. */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private DataStoreNodeService dataStoreNodeService;

	@Autowired
	private Validator validator;

	@RequestMapping(value = "/rest/datastores", method = RequestMethod.GET)
	@ResponseBody
	public Map<Role, List<SimpleEntry<Long, String>>> getDataStoreNodes() {

		ListMultimap<Role, DataStoreNode> multimap = dataStoreNodeService.getAllGroupByRole();
		Map<Role, List<SimpleEntry<Long, String>>> result = Maps
				.newHashMap();

		for (Role role : Role.values()) {
			List<DataStoreNode> dsOfRole = multimap.get(role);
			List<SimpleEntry<Long, String>> dataStores = Lists.newArrayList();

			for (DataStoreNode ds : dsOfRole) {
				SimpleEntry<Long, String> entry = new SimpleEntry<Long, String>(
						ds.getId(), ds.getName());	//needs some love; what info do we need about the data store to be displayed?
				dataStores.add(entry);
			}
			result.put(role, dataStores);
		}
		return result;
	}

	/**
	 * Create a new {@link DataStoreNode} in the database. The caller user
	 * must be ADMIN.
	 * 
	 * @param dataStoreNode
	 *            form bean with info.
	 * @return the saved {@link DataStoreNode}.
	 */
	@RequestMapping(value = "/rest/datastores", method = RequestMethod.POST)
	@Secured({ "ROLE_ADMIN" })
	@ResponseBody
	public JsonResponse saveRepository(
			@RequestBody DataStoreNodeFormBean dsNode, Errors errors) {
		JsonResponse res = new JsonResponse();

		validator.validate(dsNode, errors);
		if (errors.hasErrors()) {
			res.setStatus(STATUS.FAIL);
			res.setResult(errors.getAllErrors());
			return res;

		}
		logger.info("Name:" + dsNode.getName());
		logger.info("Role: " + dsNode.getRole().toString());

		boolean existOther = dataStoreNodeService.checkExistDataStoreNodeNameAndRole(dsNode.getName(), dsNode.getRole());

		if (existOther) {
			res.setStatus(STATUS.FAIL);
			errors.rejectValue("name", "ERROR_DATASTORE_ALREADY_ADDED");
			res.setResult(errors.getAllErrors());
			return res;
		}

		DataStoreNode entity = new DataStoreNode();
		entity.setName(dsNode.getName());
		entity.setServerUrl(dsNode.getServerUrl());
		entity.setRole(dsNode.getRole());
		entity.setStoreType(dsNode.getStoreType());
		entity.setUsername(dsNode.getUsername());
		entity.setUserpassword(dsNode.getUserPassword());
		entity.setWorkspacePrefix(dsNode.getWorkspacePrefix());

		entity = dataStoreNodeService.save(entity);

		res.setResult(entity);
		res.setStatus(STATUS.SUCCESS);

		return res;
	}

	@RequestMapping(value = "/rest/datastores/{dsId}", method = RequestMethod.DELETE)
	@Secured({ "ROLE_ADMIN" })
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteDsNode(@PathVariable Long dsId) {
		dataStoreNodeService.logicalDelete(dsId);

	}



	@RequestMapping(value = "/rest/checkIfOtherDatastoreExist")
	@ResponseBody
	public Map<String, Object> checkExistingActiveRepositoryName(
			@RequestParam String name, @RequestParam Role role) {
		Map<String, Object> resultMap = Maps.newHashMap();
		boolean exists = dataStoreNodeService.checkExistDataStoreNodeNameAndRole(name, role);
		resultMap.put("anotherExist", exists);

		return resultMap;

	}

}
