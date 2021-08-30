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

package io.shulie.surge.data.deploy.pradar.parser.db;

import com.pamirs.pradar.log.parser.ProtocolParserFactory;
import com.pamirs.pradar.log.parser.trace.RpcBased;
import com.pamirs.pradar.log.parser.trace.TraceProtocolParser;
import io.shulie.surge.data.deploy.pradar.parser.cache.CacheRpcBasedParser;
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
public class DbClientRpcBasedParserTest {
    DBClientRpcBasedParser dbClientRpcBasedParser = new DBClientRpcBasedParser();
    RpcBased rpcBased = new RpcBased();

    @Before
    public void init() {
        String log = "7001a8c016122396918871211d266f|1612239691901|192.168.122.1-106971|9775e6ce3b4d1ec65458666fcd270808|30cb5fc4fb6b470378c68faede44cc95|0.1.1.1|2|4|rds-rds-mysql-demo-1.0.0|rds-egg-consumer-0.0.1-SNAPSHOT|rds-egg-provider-0.0.1-SNAPSHOT|4|mysql|jdbc:mysql://192.168.1.107:3306/trodb?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowMultiQueries=true|trodb|192.168.1.107|3306|00|0|0|{123,6}|true|1|UPDATE PT_USER   SET `password` = ? WHERE ID = ?|@|@|192.168.1.118|1.5";
        TraceProtocolParser traceLogParser = ProtocolParserFactory.getFactory().getTraceProtocolParser("1.5");
        rpcBased = traceLogParser.parse(log);
    }

    @Test
    public void serviceParse() {
        Assert.assertEquals("jdbc:mysql://192.168.1.107:3306/trodb",dbClientRpcBasedParser.serviceParse(rpcBased));
        Assert.assertEquals("mysql 192.168.1.107:3306:jdbc:mysql://192.168.1.107:3306/trodb",dbClientRpcBasedParser.appNameParse(rpcBased));
    }

}
