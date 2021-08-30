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

package io.shulie.surge.data.deploy.pradar.parser.fs;

import com.pamirs.pradar.log.parser.ProtocolParserFactory;
import com.pamirs.pradar.log.parser.trace.RpcBased;
import com.pamirs.pradar.log.parser.trace.TraceProtocolParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @Author: xingchen
 * @ClassName: FsRpcBasedParserTests
 * @Package: io.shulie.surge.data.deploy.pradar.parser.fs
 * @Date: 2021/1/2715:13
 * @Description:
 */
public class FsRpcBasedParserTests {
    FsRpcBasedParser fsRpcBasedParser = new FsRpcBasedParser();
    RpcBased rpcBased = new RpcBased();

    @Before
    public void init() {
        String log = "e364a8c016117158014512614dd359|1611715968180|wsdemosoftkafka-consumer-0.0.1-SNAPSHOT|0.1.5|8|wsdemo-soft-kafka-210113|113|oss|http://tank-bucket.oss-cn-beijing.aliyuncs.com/tank/ossFile2021-01-27%2010%3A33%3A13|PUT|200|{}||false~null~false~false||#1|@wsdemo-soft-210113~http://192.168.1.118:22001/kafka/sendToKafka~GET|@wsdemo-soft-210113~tank-bucket.oss-cn-beijing.aliyuncs.com~-1~56~0|@s@657ebff7@|@|192.168.1.118|1.6";
        TraceProtocolParser traceLogParser = ProtocolParserFactory.getFactory().getTraceProtocolParser("1.6");
        rpcBased = traceLogParser.parse(log);
    }

    @Test
    public void parse() {
        Assert.assertEquals("oss tank-bucket.oss-cn-beijing.aliyuncs.com",fsRpcBasedParser.appNameParse(rpcBased));
    }
}
