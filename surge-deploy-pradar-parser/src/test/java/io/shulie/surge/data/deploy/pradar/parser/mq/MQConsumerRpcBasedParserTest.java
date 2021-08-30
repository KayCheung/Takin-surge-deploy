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

package io.shulie.surge.data.deploy.pradar.parser.mq;


import com.pamirs.pradar.log.parser.ProtocolParserFactory;
import com.pamirs.pradar.log.parser.trace.RpcBased;
import com.pamirs.pradar.log.parser.trace.TraceProtocolParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MQConsumerRpcBasedParserTest {
    private MQConsumerRpcBasedParser parser = new MQConsumerRpcBasedParser();
    private RpcBased rpcBased = new RpcBased();
    static String logVersion = "1.5";

    @Before
    public void init() {
        String log = "ffffffff1615430180398000000000|1615430180398|link_creator-RocketMQ-9|cccccccccccccccccccccccccccccccc|null|0|3|3|link_creator-RocketMQ-9|link_creator-RocketMQ-9|link_creator-RocketMQ-9|20|apache-rocketmq|PT_order_create-RocketMQ-topic|PT_RocketMQ-consumer|10.230.10.10|888|00|0|0|||1||!0.2|@s@1127585c@|@";
        TraceProtocolParser traceLogParser = ProtocolParserFactory.getFactory().getTraceProtocolParser(logVersion);
        rpcBased = traceLogParser.parse(log);
    }

    @Test
    public void testParseOns() {
        Assert.assertEquals(parser.methodParse(rpcBased), "PT_RocketMQ-consumer");
    }

    @Test
    public void serverAppNameParse() {
        Assert.assertEquals(parser.serverAppNameParse(rpcBased), "apache-rocketmq10.230.10.10:888");
    }

    @Test
    public void serviceParse() {
        Assert.assertEquals("PT_order_create-RocketMQ-topic", parser.serviceParse(rpcBased));
    }
}