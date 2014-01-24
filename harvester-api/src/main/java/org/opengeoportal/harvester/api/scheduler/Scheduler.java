/**
 * Scheduler.java
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
package org.opengeoportal.harvester.api.scheduler;

import org.opengeoportal.harvester.api.domain.Ingest;
import org.quartz.SchedulerException;

/**
 * @author <a href="mailto:juanluisrp@geocat.net">Juan Luis Rodríguez</a>.
 * 
 */
public interface Scheduler {
	/**
	 * Schedule the ingest.
	 * 
	 * @param ingest
	 *            the ingest.
	 * @return <code>true</code> if ingest can be scheduled, <code>false</code>
	 *         otherwise.
	 */
	boolean scheduleIngest(Ingest ingest);

	/**
	 * Unschedule an ingest forever. Remove the job and all its triggers from
	 * the scheduler.
	 * 
	 * @param ingest
	 *            ingest to be unscheduled.
	 * @return <code>true</code> if the Job was found and deleted.
	 * 
	 * @throws SchedulerException
	 *             if there is any problem while unscheduling the ingest.
	 */
	boolean unschedule(Ingest ingest) throws SchedulerException;

}
