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
 * Authors:: Juan Luis Rodriguez Ponce (mailto:juanluisrp@geocat.net)
 */
package org.opengeoportal.harvester.mvc.bean;

import java.util.Date;
import java.util.Map;

import org.opengeoportal.harvester.api.domain.Frequency;
import org.opengeoportal.harvester.api.domain.InstanceType;
import org.opengeoportal.harvester.mvc.utils.CustomJsonDateDeserializer;
import org.opengeoportal.harvester.mvc.utils.CustomJsonDateSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author jlrodriguez
 * 
 */
public class LocalIngestFormBean {
	private Long id;
	private Boolean scheduled;
	private InstanceType typeOfInstance;
	private String nameOgpRepository;
	private String url;
	

	private Map<String, Boolean> requiredFields;


	private String ingestName;

	@DateTimeFormat(pattern = "MM/dd/yyyy")
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	private Date beginDate;

	private Frequency frequency;


	/**
	 * 
	 */
	public LocalIngestFormBean() {

	}

	/**
	 * @return the typeOfInstance
	 */
	public InstanceType getTypeOfInstance() {
		return typeOfInstance;
	}

	/**
	 * @param typeOfInstance
	 *            the typeOfInstance to set
	 */
	public void setTypeOfInstance(InstanceType typeOfInstance) {
		this.typeOfInstance = typeOfInstance;
	}



	/**
	 * @return the nameOgpRepository
	 */
	public String getNameOgpRepository() {
		return nameOgpRepository;
	}

	/**
	 * @param nameOgpRepository
	 *            the nameOgpRepository to set
	 */
	public void setNameOgpRepository(String nameOgpRepository) {
		this.nameOgpRepository = nameOgpRepository;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the requiredFields
	 */
	public Map<String, Boolean> getRequiredFields() {
		return requiredFields;
	}

	/**
	 * @param requiredFields
	 *            the requiredFields to set
	 */
	public void setRequiredFields(Map<String, Boolean> requiredFields) {
		this.requiredFields = requiredFields;
	}
	
	/**
	 * @return the ingestName
	 */
	public String getIngestName() {
		return ingestName;
	}

	/**
	 * @param ingestName
	 *            the ingestName to set
	 */
	public void setIngestName(String ingestName) {
		this.ingestName = ingestName;
	}

	/**
	 * @return the beginDate
	 */
	public Date getBeginDate() {
		return beginDate;
	}

	/**
	 * @param beginDate
	 *            the beginDate to set
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}


	/**
	 * @return the frequency
	 */
	public Frequency getFrequency() {
		return frequency;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getScheduled() {
		return scheduled;
	}


}
