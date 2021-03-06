/**
 * IngestJobFactory.java
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
package org.opengeoportal.harvester.api.component;

import org.opengeoportal.harvester.api.domain.Ingest;

/**
 * An IngestJobFactory is responsible for producing instances of concrete
 * BaseIngestJob according with the Ingest passed to it.
 * 
 * @author <a href="mailto:juanluisrp@geocat.net">Juan Luis Rodríguez</a>.
 * 
 */
public interface IngestJobFactory {
	/**
	 * Create a new ingest job based on the ingest passed as parameter.
	 * 
	 * @param ingest
	 *            the ingest.
	 * @return a concrete {@link BaseIngestJob} subclass. This instance must be
	 *         inited calling its method <code>init</code>.
	 */
	BaseIngestJob newIngestJob(Ingest ingest);

}
