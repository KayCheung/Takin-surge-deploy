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

package io.shulie.surge.data.deploy.pradar.parser;

import com.google.common.collect.Maps;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class DefaultRpcBasedParserTest extends TestCase {

    @Test
    public void testLinkTags() {
        DefaultRpcBasedParser defaultRpcBasedParser = new DefaultRpcBasedParser();
        Map<String, Object> tags = Maps.newHashMap();

        tags.put("service", "http:///api/cart/list");
        tags.put("method", "POST");
        tags.put("appName", "api-rest");
        tags.put("rpcType", 0);
        tags.put("extend", "");

        String linkId = defaultRpcBasedParser.linkId(tags);

        /**
         * put 'pradar-linkid-config','bb245ef7e29cee7418d66c2c0aed0227','cf:service','http:///api/cart/list'
         * put 'pradar-linkid-config','bb245ef7e29cee7418d66c2c0aed0227','cf:method','POST'
         * put 'pradar-linkid-config','bb245ef7e29cee7418d66c2c0aed0227','cf:appName','api-rest'
         * put 'pradar-linkid-config','bb245ef7e29cee7418d66c2c0aed0227','cf:rpcType','0'
         * put 'pradar-linkid-config','bb245ef7e29cee7418d66c2c0aed0227','cf:extend',''
         */
        Assert.assertEquals("bb245ef7e29cee7418d66c2c0aed0227", linkId);
    }

    @Test
    public void testLinkTags2() {
        /*{
            "serice":"http://galaxy-receive-test.sto.cn/scan/message",
                "method":"POST",
                "appName":"galaxy-receive",
                "rpcType":"0",
                "extend",""
        }*/
        DefaultRpcBasedParser defaultRpcBasedParser = new DefaultRpcBasedParser();
        Map<String, Object> tags = Maps.newHashMap();

        String service = "http:///scan/message";
        String method = "POST";
        String appName = "galaxy-receive";
        tags.put("service", service);
        tags.put("method", method);
        tags.put("appName", appName);
        tags.put("rpcType", 0);
        tags.put("extend", "");

        String linkId = defaultRpcBasedParser.linkId(tags);

        /**
         * put 'pradar-linkid-config','bb245ef7e29cee7418d66c2c0aed0227','cf:service','http:///api/cart/list'
         * put 'pradar-linkid-config','bb245ef7e29cee7418d66c2c0aed0227','cf:method','POST'
         * put 'pradar-linkid-config','bb245ef7e29cee7418d66c2c0aed0227','cf:appName','api-rest'
         * put 'pradar-linkid-config','bb245ef7e29cee7418d66c2c0aed0227','cf:rpcType','0'
         * put 'pradar-linkid-config','bb245ef7e29cee7418d66c2c0aed0227','cf:extend',''
         */
        Assert.assertEquals("ac4d5795374001953bee728a189f15d8", linkId);

    }

    public void testTestLinkTags() {
    }
}