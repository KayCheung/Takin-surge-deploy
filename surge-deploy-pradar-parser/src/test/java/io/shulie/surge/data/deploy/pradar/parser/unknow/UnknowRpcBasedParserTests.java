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

package io.shulie.surge.data.deploy.pradar.parser.unknow;

import com.pamirs.pradar.log.parser.ProtocolParserFactory;
import com.pamirs.pradar.log.parser.trace.RpcBased;
import com.pamirs.pradar.log.parser.trace.TraceProtocolParser;
import io.shulie.surge.data.deploy.pradar.parser.fs.FsRpcBasedParser;
import io.shulie.surge.data.deploy.pradar.parser.unknown.UnknownNodeRpcBasedParser;
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
public class UnknowRpcBasedParserTests {
    UnknownNodeRpcBasedParser unknownNodeRpcBasedParser = new UnknownNodeRpcBasedParser();
    RpcBased rpcBased = new RpcBased();

    @Before
    public void init() {
        rpcBased.setUpAppName("upAppName");
        rpcBased.setAppName("appName");
    }

    @Test
    public void parse() {
        Assert.assertEquals("appName", unknownNodeRpcBasedParser.fromAppTags("", rpcBased).get("appName"));
    }
}
