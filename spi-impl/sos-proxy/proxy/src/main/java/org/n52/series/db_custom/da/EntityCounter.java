/*
 * Copyright (C) 2013-2016 52°North Initiative for Geospatial Open Source
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
package org.n52.series.db_custom.da;

import org.hibernate.Session;
import org.n52.io.request.IoParameters;
import org.n52.io.request.Parameters;
import org.n52.series.db.DataAccessException;
import org.n52.series.db.HibernateSessionStore;
import org.n52.series.db.beans.DatasetEntity;
import org.n52.series.db.dao.PlatformDao;
import org.n52.series.db.dao.ProxyCategoryDao;
import org.n52.series.db.dao.ProxyDatasetDao;
import org.n52.series.db.dao.ProxyDbQuery;
import org.n52.series.db.dao.ProxyFeatureDao;
import org.n52.series.db.dao.ProxyPhenomenonDao;
import org.n52.series.db.dao.ProxyProcedureDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityCounter {

    @Autowired
    private HibernateSessionStore sessionStore;

    public Integer countFeatures(ProxyDbQuery query) throws DataAccessException {
        Session session = sessionStore.getSession();
        try {
            return new ProxyFeatureDao(session).getCount(query);
        } finally {
            sessionStore.returnSession(session);
        }
    }

    public Integer countOfferings(ProxyDbQuery query) throws DataAccessException {
        // offerings equals procedures in our case
        return countProcedures(query);
    }

    public Integer countProcedures(ProxyDbQuery query) throws DataAccessException {
        Session session = sessionStore.getSession();
        try {
            return new ProxyProcedureDao(session).getCount(query);
        } finally {
            sessionStore.returnSession(session);
        }
    }

    public Integer countPhenomena(ProxyDbQuery query) throws DataAccessException {
        Session session = sessionStore.getSession();
        try {
            return new ProxyPhenomenonDao(session).getCount(query);
        } finally {
            sessionStore.returnSession(session);
        }
    }

    public Integer countCategories(ProxyDbQuery query) throws DataAccessException {
        Session session = sessionStore.getSession();
        try {
            return new ProxyCategoryDao(session).getCount(query);
        } finally {
            sessionStore.returnSession(session);
        }
    }

    public Integer countPlatforms(ProxyDbQuery query) throws DataAccessException {
        Session session = sessionStore.getSession();
        try {
            return new PlatformDao(session).getCount(query);
        } finally {
            sessionStore.returnSession(session);
        }
    }

    public Integer countDatasets(ProxyDbQuery query) throws DataAccessException {
        Session session = sessionStore.getSession();
        try {
            return new ProxyDatasetDao<DatasetEntity>(session, DatasetEntity.class).getCount(query);
        } finally {
            sessionStore.returnSession(session);
        }
    }

    public Integer countStations() throws DataAccessException {
        Session session = sessionStore.getSession();
        try {
            ProxyDbQuery query = createBackwardsCompatibleQuery();
            return countFeatures(query);
        } finally {
            sessionStore.returnSession(session);
        }
    }

    public Integer countTimeseries() throws DataAccessException {
        Session session = sessionStore.getSession();
        try {
            ProxyDbQuery query = createBackwardsCompatibleQuery();
            return countDatasets(query);
        } finally {
            sessionStore.returnSession(session);
        }
    }

    private ProxyDbQuery createBackwardsCompatibleQuery() {
        return ProxyDbQuery.createFrom(IoParameters.createDefaults()
                .extendWith(Parameters.FILTER_PLATFORM_TYPES, "stationary", "insitu")
                .extendWith(Parameters.FILTER_DATASET_TYPES, "measurement"));
    }

}