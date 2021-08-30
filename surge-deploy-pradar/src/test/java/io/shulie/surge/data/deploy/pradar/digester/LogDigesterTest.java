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

package io.shulie.surge.data.deploy.pradar.digester;

import io.shulie.surge.data.deploy.pradar.digester.command.BaseCommand;
import io.shulie.surge.data.deploy.pradar.digester.command.ClickhouseFacade;
import io.shulie.surge.data.deploy.pradar.digester.command.FlagCommand;
import io.shulie.surge.data.deploy.pradar.digester.command.LinkCommand;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;

public class LogDigesterTest extends TestCase {
    private ClickhouseFacade clickhouseFacade = ClickhouseFacade.Factory.getInstace();

    @Before
    public void init() {
        clickhouseFacade.addCommond(new BaseCommand());
        clickhouseFacade.addCommond(new LinkCommand());
        clickhouseFacade.addCommond(new FlagCommand());
    }

    public void testSql() {
        Assert.assertEquals(clickhouseFacade.getCols().split(",").length,clickhouseFacade.getParam().split(",").length);
    }
}