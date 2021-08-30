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

import com.pamirs.pradar.log.parser.trace.RpcBased;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MQProducerRpcBasedParserTest {
    private MQProducerRpcBasedParser parser;
    private RpcBased rpcBased = new RpcBased();

    @Before
    public void init() {
        rpcBased.setMethodName("MQ_INST_1942653734864712_BawJ4gx4%__ONS_PRODUCER_DEFAULT_GROUP:210:773071906038628_210_900001_1607407176000");
        rpcBased.setMiddlewareName("ons");
        rpcBased.setAppName("test1234");
        rpcBased.setServiceName("abc");
        parser = new MQProducerRpcBasedParser();
    }

    @Test
    public void serverAppnameParse() {
        Assert.assertEquals(parser.appNameParse(rpcBased), "MQ_INST_1942653734864712_BawJ4gx4");
    }

    @Test
    public void methodParse() {
        Assert.assertEquals("__ONS_PRODUCER_DEFAULT_GROUP", parser.methodParse(rpcBased));
    }

    @Test
    public void service() {
        Assert.assertEquals("abc", parser.serviceParse(rpcBased));
    }
}