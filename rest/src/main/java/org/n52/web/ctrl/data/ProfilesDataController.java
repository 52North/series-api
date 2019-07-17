/*
 * Copyright (C) 2013-2019 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public License
 * version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 */

package org.n52.web.ctrl.data;

import java.text.MessageFormat;

import org.n52.io.handler.DefaultIoFactory;
import org.n52.io.request.IoParameters;
import org.n52.io.response.dataset.AbstractValue;
import org.n52.io.response.dataset.Data;
import org.n52.io.response.dataset.DatasetOutput;
import org.n52.series.spi.srv.DataService;
import org.n52.series.spi.srv.ParameterService;
import org.n52.web.ctrl.UrlSettings;
import org.n52.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = UrlSettings.COLLECTION_PROFILES, produces = {
    "application/json"
})
public class ProfilesDataController extends DataController {

    @Autowired
    public ProfilesDataController(DefaultIoFactory<DatasetOutput<AbstractValue< ? >>, AbstractValue< ? >> ioFactory,
                                  ParameterService<DatasetOutput<AbstractValue< ? >>> datasetService,
                                  DataService<Data<AbstractValue< ? >>> dataService) {
        super(ioFactory, datasetService, dataService);
    }

    @Override
    protected String getValueType(IoParameters map, String requestUrl) {
        DatasetOutput<AbstractValue< ? >> item = getFirstDatasetOutput(map);
        String observationType = item.getObservationType();
        if (!"profile".equalsIgnoreCase(observationType)) {
            String expectedType = UrlSettings.COLLECTION_PROFILES;
            String template = "The dataset with id ''{0}'' was not found for ''{1}''.";
            String message = MessageFormat.format(template, item.getId(), expectedType);
            throw new ResourceNotFoundException(message);
        }
        return isProfileType(item)
                ? PROFILE
                : item.getValueType();
    }

}