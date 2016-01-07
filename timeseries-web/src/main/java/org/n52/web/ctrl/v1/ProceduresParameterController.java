/**
 * Copyright (C) 2013-2016 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as publishedby the Free
 * Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of the
 * following licenses, the combination of the program with the linked library is
 * not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed under
 * the aforementioned licenses, is permitted by the copyright holders if the
 * distribution is compliant with both the GNU General Public License version 2
 * and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */
package org.n52.web.ctrl.v1;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.n52.io.request.IoParameters;
import org.n52.io.request.QueryParameters;
import org.n52.io.v1.data.RawFormats;
import static org.n52.web.ctrl.v1.RestfulUrls.COLLECTION_PROCEDURES;
import org.n52.web.exception.BadRequestException;
import org.n52.web.exception.InternalServerException;
import org.n52.web.exception.ResourceNotFoundException;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = COLLECTION_PROCEDURES)
public class ProceduresParameterController extends ParameterControllerV1Adapter {
	
	@RequestMapping(value = "/{item}", method = GET, params = { RawFormats.RAW_FORMAT })
	public void getRawData(HttpServletResponse response,
			@PathVariable("item") String id,
			@RequestParam MultiValueMap<String, String> query) {
		if (getParameterService().supportsRawData()) {
			IoParameters queryMap = QueryParameters.createFromQuery(query);
			try (InputStream inputStream = getParameterService().getRawDataService().getRawData(id, queryMap)) {
                if (inputStream == null) {
                    throw new ResourceNotFoundException("Found no parameter for id '" + id + "'.");
                }
				IOUtils.copyLarge(inputStream, response.getOutputStream());
			} catch (IOException e) {
				throw new InternalServerException("Error while querying raw procedure data", e);
			}
		} else {
			throw new BadRequestException(
					"Querying of raw procedure data is not supported by the underlying service!");
		}
	}

}
