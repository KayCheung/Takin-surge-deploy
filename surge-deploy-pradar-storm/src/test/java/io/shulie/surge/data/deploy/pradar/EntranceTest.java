/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shulie.surge.data.deploy.pradar;

import com.alibaba.fastjson.JSON;
import com.google.inject.Injector;
import io.shulie.surge.data.deploy.pradar.config.PradarSupplierConfiguration;
import io.shulie.surge.data.deploy.pradar.digester.LogDigester;
import io.shulie.surge.data.deploy.pradar.link.processor.EntranceProcessor;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class EntranceTest {

    //TODO use mock
   /* @Test
    public void entranceProcessor_share() throws IOException {
        Injector injector = new PradarSupplierConfiguration().initDataRuntime().getInstance(Injector.class);
        EntranceProcessor entranceProcessor = injector.getInstance(EntranceProcessor.class);
        entranceProcessor.share(1);
    }

    @Test
    public void entranceProcessor_saveLinkEntrance() throws ParseException {
        Injector injector = new PradarSupplierConfiguration().initDataRuntime().getInstance(Injector.class);
        EntranceProcessor entranceProcessor = injector.getInstance(EntranceProcessor.class);
        entranceProcessor.saveLinkEntrance("app7",DateUtils.parseDate("2021-03-18 00:00:00","yyyy-MM-dd hh:mm:ss"));
    }

    @Test
    public void entranceProcessor_queryAppNames() throws ParseException {
        Injector injector = new PradarSupplierConfiguration().initDataRuntime().getInstance(Injector.class);
        EntranceProcessor entranceProcessor = injector.getInstance(EntranceProcessor.class);
        System.out.println(JSON.toJSON(entranceProcessor.queryAppNames(DateUtils.parseDate("2021-03-18 00:00:00","yyyy-MM-dd hh:mm:ss"))));
    }

    @Test
    public void entranceProcessor_queryEntrance() throws ParseException {
        Injector injector = new PradarSupplierConfiguration().initDataRuntime().getInstance(Injector.class);
        EntranceProcessor entranceProcessor = injector.getInstance(EntranceProcessor.class);
        entranceProcessor.queryEntrance("app7",DateUtils.parseDate("2021-03-18 00:00:00","yyyy-MM-dd hh:mm:ss"));
    }

    @Test
    public void entranceProcessor_shareExpire() {
        Injector injector = new PradarSupplierConfiguration().initDataRuntime().getInstance(Injector.class);
        EntranceProcessor entranceProcessor = injector.getInstance(EntranceProcessor.class);
        entranceProcessor.shareExpire(1);
    }*/

}
