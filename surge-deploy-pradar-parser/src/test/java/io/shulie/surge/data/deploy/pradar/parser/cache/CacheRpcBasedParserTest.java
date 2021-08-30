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

package io.shulie.surge.data.deploy.pradar.parser.cache;

import com.pamirs.pradar.log.parser.ProtocolParserFactory;
import com.pamirs.pradar.log.parser.trace.RpcBased;
import com.pamirs.pradar.log.parser.trace.TraceProtocolParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @Author: xingchen
 * @ClassName: CacheRpcBasedParserTest
 * @Package: io.shulie.surge.data.deploy.pradar.parser.cache
 * @Date: 2021/1/2714:11
 * @Description:
 */
public class CacheRpcBasedParserTest {
    CacheRpcBasedParser cacheRpcBasedParser = new CacheRpcBasedParser();
    RpcBased rpcBased = new RpcBased();

    @Before
    public void init() {
        String log = "33327b0a16230321190143246d0001|1623032119014|10.123.50.51-1|3a53fa6a686c5fac1e8d58a1843beaa6|null|9|2|5|pqs-pqb-service04|pqs-pqb-service04|pqs-pqb-service04|0|google-guava|com.google.common.cache.LocalCache$LocalManualCache|getIfPresent|||00|0|0|||0||192.168.1.118|1.5";
        TraceProtocolParser traceLogParser = ProtocolParserFactory.getFactory().getTraceProtocolParser("1.5");
        rpcBased = traceLogParser.parse(log);
    }

    @Test
    public void parse() {
        String x = cacheRpcBasedParser.appNameParse(rpcBased);
        System.out.println(x);
    }

    @Test
    public void parse15() {
        rpcBased.setServiceName("set");
        rpcBased.setMethodName("key");
        Assert.assertEquals(cacheRpcBasedParser.serviceParse(rpcBased), "0");
        Assert.assertEquals(cacheRpcBasedParser.methodParse(rpcBased), "set");
    }

    @Test
    public void parse16() {
        rpcBased.setServiceName("1:get");
        rpcBased.setMethodName("set");
        Assert.assertEquals(cacheRpcBasedParser.serviceParse(rpcBased), "1");
    }

    @Test
    public void parse17() {
        cacheRpcBasedParser.appNameParse(rpcBased);
    }
}
