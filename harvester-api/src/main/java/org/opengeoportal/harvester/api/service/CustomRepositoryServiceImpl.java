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

import java.net.URL;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.opengeoportal.harvester.api.dao.CustomRepositoryRepository;
import org.opengeoportal.harvester.api.domain.CustomRepository;
import org.opengeoportal.harvester.api.domain.InstanceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

@Service
public class CustomRepositoryServiceImpl implements CustomRepositoryService {

	@Resource
	private CustomRepositoryRepository customRepositoryRepository;

	@Override
	@Transactional
	public CustomRepository save(CustomRepository customRepository) {
		return customRepositoryRepository.save(customRepository);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		customRepositoryRepository.delete(id);
	}

	@Override
	@Transactional(readOnly = true)
	public CustomRepository findByName(String name) {
		return customRepositoryRepository.findByName(name);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CustomRepository> findAll(Pageable pageable) {
		Page<CustomRepository> page = customRepositoryRepository
				.findAll(pageable);
		return page;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengeoportal.harvester.api.service.CustomRepositoryService#
	 * getAllGroupByType()
	 */
	@Override
	@Transactional
	public ListMultimap<String, CustomRepository> getAllGroupByType() {
		Sort typeSortAsc = new Sort(new Order(
				CustomRepository.COLUMN_SERVICE_TYPE));
		List<CustomRepository> repositories = customRepositoryRepository
				.findAll(typeSortAsc);
		ListMultimap<String, CustomRepository> map = ArrayListMultimap.create();
		for (CustomRepository repository : repositories) {
			map.put(repository.getServiceType(), repository);
		}
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengeoportal.harvester.api.service.CustomRepositoryService#
	 * getRemoteRepositories
	 * (org.opengeoportal.harvester.api.domain.InstanceType, java.net.URL)
	 */
	@Override
	public List<SimpleEntry<String, String>> getRemoteRepositories(
			InstanceType repoType, URL url) {
		// TODO connect to url, parse response, and search remote origins. Build
		// the response list
		List<SimpleEntry<String, String>> result = new ArrayList<SimpleEntry<String, String>>();
		result.add(new SimpleEntry<String, String>("guid1",
				"Remote repository 1"));
		result.add(new SimpleEntry<String, String>("guid2",
				"Remote repository 2"));
		result.add(new SimpleEntry<String, String>("guid3",
				"Remote repository 3"));
		result.add(new SimpleEntry<String, String>("guid4",
				"Remote repository 4"));
		result.add(new SimpleEntry<String, String>("guid5",
				"Remote repository 5"));
		result.add(new SimpleEntry<String, String>("guid10",
				"Remote repository 10"));

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengeoportal.harvester.api.service.CustomRepositoryService#
	 * getLocalSolrInstitutions()
	 */
	@Override
	public List<SimpleEntry<String, String>> getLocalSolrInstitutions() {
		// TODO connect to local Solr index and get the institutions
		List<SimpleEntry<String, String>> result = new ArrayList<SimpleEntry<String, String>>();
		result.add(new SimpleEntry<String, String>("Tufts", "Tufts"));
		result.add(new SimpleEntry<String, String>("Harvard", "Harvard"));
		result.add(new SimpleEntry<String, String>("Berkeley", "Berkeley"));
		result.add(new SimpleEntry<String, String>("MIT", "MIT"));
		result.add(new SimpleEntry<String, String>("MassGIS", "MassGIS"));

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengeoportal.harvester.api.service.CustomRepositoryService#
	 * getRemoteRepositoriesByRepoId(java.lang.Long)
	 */
	@Override
	public List<SimpleEntry<String, String>> getRemoteRepositoriesByRepoId(
			Long repoId) {
		CustomRepository repository = customRepositoryRepository
				.findOne(repoId);
		List<SimpleEntry<String, String>> result = new ArrayList<SimpleEntry<String, String>>();
		if (repository != null) {
			String url = repository.getUrl();
			String serviceType = repository.getServiceType();

			// TODO with the repository URL and its type, fecth the URL and look
			// for remote sources
			for (int i = 0; i < 6; i++) {
				result.add(new SimpleEntry<String, String>("guid" + i,
						"Remote source " + serviceType + " " + i));
			}

		}
		return result;
	}
}
