/*
 * Copyright (C) 2013-2021 52°North Initiative for Geospatial Open Source
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
package org.n52.web.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.util.Optional;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;

public class OffsetBasedPaginationTest {

    @Test
    public void startingWithOutOfBoundsOffsetThenLastPageEmpty() {
        OffsetBasedPagination pagination = new OffsetBasedPagination(201, 20);
        Optional<Pagination> last = pagination.last(200);
        assertFalse(last.isPresent());
    }

    @Test
    public void startingWithZeroOffsetThenFirstPageDetermined() {
        OffsetBasedPagination pagination = new OffsetBasedPagination(0, 20);
        Optional<Pagination> last = pagination.first(200);
        assertThat(last.get().getOffset(), Is.is(0L));
        assertThat(last.get().getLimit(), Is.is(20L));
    }

    @Test
    public void startingWithZeroOffsetThenLastPageDetermined() {
        OffsetBasedPagination pagination = new OffsetBasedPagination(0, 20);
        Optional<Pagination> last = pagination.last(200);
        assertThat(last.get().getOffset(), Is.is(180L));
        assertThat(last.get().getLimit(), Is.is(20L));
    }

}
