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

package io.shulie.surge.data.deploy.pradar.link;

import io.shulie.surge.data.sink.mysql.MysqlSupport;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MysqlSupport.class})
public class LinkCacheTest {

    AbstractLinkCache linkCache = new AbstractLinkCache() {
        @Override
        public void save(String linkId, LinkedBlockingQueue<String> linkedBlockingQueue) {

        }
    };

    @Before
    public void before() {
        PowerMockito.mock(MysqlSupport.class);

    }

    @Test
    public void testCacheUpdate() throws Exception {
        MysqlSupport mysqlSupport = PowerMockito.mock(MysqlSupport.class);
        //linkCache.autoRefresh(mysqlSupport);
//        LinkCache linkCache = PowerMockito.mock(LinkCache.class);
        PowerMockito.whenNew(MysqlSupport.class).withAnyArguments().thenReturn(mysqlSupport);
//        PowerMockito.whenNew(LinkCache.class).withAnyArguments().thenReturn(linkCache);
//        PowerMockito.when(linkCache, "refresh", mysqlSupport).then(new Answer<Object>() {
//            @Override
//            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
//
//                System.out.println("缓存已刷新");
//                return null;
//            }
//        });
//        PowerMockito.doAnswer(new Answer<Object>() {
//            @Override
//            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
//                invocationOnMock.getMethod().invoke(mysqlSupport);
//                System.out.println("缓存已刷新");
//                return null;
//            }
//        }).when(linkCache, "refresh", mysqlSupport);
        // 配置数据更新为200条
        updateConfig(mysqlSupport, getConfigList(1, 200));
        linkCache.refresh(mysqlSupport);
        //判断字段是否为空
        Assert.assertNotNull(linkCache.getLinkConfig().get("d3c46d2a890acd93a12db52674429705").get("appName"));
        Assert.assertNotNull(linkCache.getLinkConfig().get("d3c46d2a890acd93a12db52674429705").get("rpcType"));
        // 断言，从数据库查询的数据数量和预期数量一致
        Assert.assertEquals(200, selectConfigCount(mysqlSupport));
        // 断言，缓存中的配置数据数量和预期数据量一致
        Assert.assertEquals(200, linkCache.getLinkConfig().size());
        // 配置数据更新为300条
        updateConfig(mysqlSupport, getConfigList(1, 300));
        linkCache.refresh(mysqlSupport);
        // 断言，从数据库查询的数据数量和预期数量一致
        Assert.assertEquals(300, selectConfigCount(mysqlSupport));
        linkCache.autoRefresh(mysqlSupport);
        TimeUnit.SECONDS.sleep(6);
        // 断言，缓存中的配置数据数量和预期数据量一致
        Assert.assertEquals(300, linkCache.getLinkConfig().size());
        // 配置数据更新为100条
        updateConfig(mysqlSupport, getConfigList(101, 200));
        TimeUnit.SECONDS.sleep(6);
        // 断言，从数据库查询的数据数量和预期数量一致
        Assert.assertEquals(100, selectConfigCount(mysqlSupport));
        // 断言，缓存中的配置数据数量和预期数据量一致
        Assert.assertEquals(100, linkCache.getLinkConfig().size());
    }

    /**
     * 更新配置表
     *
     * @param mysqlSupport
     * @param configList
     */
    private void updateConfig(MysqlSupport mysqlSupport, List<Map<String, Object>> configList) {
        PowerMockito.when(mysqlSupport.queryForList("select * from t_amdb_pradar_link_config limit 9999")).thenReturn(configList);
    }

    /**
     * 获取配置表数据数量
     *
     * @param mysqlSupport
     * @return
     */
    private int selectConfigCount(MysqlSupport mysqlSupport) {
        return mysqlSupport.queryForList("select * from t_amdb_pradar_link_config limit 9999").size();
    }

    /**
     * 构造批量配置数据
     *
     * @param start
     * @param end
     * @return
     */
    private List<Map<String, Object>> getConfigList(int start, int end) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            result.add(createConfig(i));
        }
        return result;
    }

    /**
     * 构造单条配置数据
     *
     * @param index
     * @return
     */
    private Map<String, Object> createConfig(int index) {
        Map<String, Object> data = new HashedMap();
        data.put("app_name", "app" + index);
        data.put("service", "service" + index);
        data.put("method", "method" + index);
        data.put("rpc_type", "rpcType" + index);
        data.put("extend", "extend" + index);
        return data;
    }

}
