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

package io.shulie.surge.data.deploy.pradar.link.processor;

import io.shulie.surge.data.sink.clickhouse.ClickHouseShardSupport;
import io.shulie.surge.data.sink.clickhouse.ClickHouseSupport;
import io.shulie.surge.data.sink.mysql.MysqlSupport;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

//
@RunWith(PowerMockRunner.class)
@PrepareForTest({ClickHouseSupport.class})
public class EntranceProcessorTest extends TestCase {
    EntranceProcessor entranceProcessor = new EntranceProcessor();

    @Test
    public void testNoShare() throws Exception {
        List<String> names = getNames();
        // EntranceProcessor
        EntranceProcessor entranceProcessor = PowerMockito.mock(EntranceProcessor.class);
        // 任何操作clickhouse的地方都返回mock参数
        //PowerMockito.when(entranceProcessor.queryAppNames(new Date())).thenReturn(names);
        entranceProcessor.noShare();
    }

    private List<String> getNames() {
        return Arrays.asList("mock1","mock2");
    }

    public void testShare() {
    }

    public void testTestShare() {
    }

    public void testTestShare1() {
    }

    public void testShareExpire() {
    }

    public void testSaveLinkEntrance() {
    }

    public void testQueryAppNames() {
    }

    public void testQueryEntrance() {
    }

    public void testExpireData() {
    }
}