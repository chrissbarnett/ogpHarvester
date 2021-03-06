/**
 * IngestJobStatusService.java
 *
 * Copyright (C) 2014
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
 * Authors:: Juan Luis Rodríguez (mailto:juanluisrp@geocat.net)
 */
package org.opengeoportal.harvester.api.service;

import java.util.List;

import org.opengeoportal.harvester.api.domain.Ingest;
import org.opengeoportal.harvester.api.domain.IngestJobStatus;

/**
 * @author <a href="mailto:juanluisrp@geocat.net">Juan Luis Rodríguez</a>.
 * 
 */
public interface IngestJobStatusService {
	/**
	 * Save an {@link IngestJobStatus}.
	 * 
	 * @param jobStatus
	 *            the {@link IngestJobStatus} to save.
	 * @return the saved {@link IngestJobStatus} instance.
	 */
	IngestJobStatus save(IngestJobStatus jobStatus);

	/**
	 * Return the list of statuses for an ingest
	 * 
	 * @param ingestId
	 *            ingest identifier.
	 * @return the list of {@link IngestJobStatus} associated to the
	 *         {@link Ingest}.
	 */
	List<IngestJobStatus> getStatusesForIngest(Long ingestId);

	/**
	 * Find the last IngestJobStatus available for the ingest with the passed
	 * identifier.
	 * 
	 * @param id
	 *            Ingest identifier.
	 * @return the last Ingest available for the ingest or null if it has never
	 *         been executed.
	 */
	IngestJobStatus findLastStatusForIngest(Long id);

}
