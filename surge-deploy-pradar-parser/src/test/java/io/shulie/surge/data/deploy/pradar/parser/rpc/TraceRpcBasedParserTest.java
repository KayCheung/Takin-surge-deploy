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

package io.shulie.surge.data.deploy.pradar.parser.rpc;

import com.pamirs.pradar.log.parser.ProtocolParserFactory;
import com.pamirs.pradar.log.parser.trace.RpcBased;
import com.pamirs.pradar.log.parser.trace.TraceProtocolParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

//TODO P2 验证节点是否可以解析出来
public class TraceRpcBasedParserTest {
    TraceRpcBasedParser traceRpcBasedParser = new TraceRpcBasedParser();
    static String logVersion = "1.5";
    RpcBased rpcBased = new RpcBased();

    @Before
    public void init() {
        String log = "ffffffff1615430180424000000000|1615430180424|link_creator-tomcat-1|cccccccccccccccccccccccccccccccc|null|0|1|0|link_creator-tomcat-1|link_creator-tomcat-1|link_creator-tomcat-1|20|tomcat|http://1.2.3.4:1/tomcat/get|GET|10.230.10.10|888|00|0|0|||1||!0.2|@s@1127585c@|@|192.168.1.118|1.5";
        TraceProtocolParser traceLogParser = ProtocolParserFactory.getFactory().getTraceProtocolParser(logVersion);
        rpcBased = traceLogParser.parse(log);
    }

    @Test
    public void testServerAppNameParse() {
        Assert.assertEquals("link_creator-tomcat-1", traceRpcBasedParser.serverAppNameParse(rpcBased));
    }
}