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

package io.shulie.surge.data.deploy.pradar;

import com.google.common.collect.Maps;
import io.shulie.surge.data.deploy.pradar.common.ParamUtil;
import io.shulie.surge.data.deploy.pradar.common.StormConfig;
import io.shulie.surge.deploy.pradar.common.CommonStat;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PradarLinkTopologyTest {
    public static TopologyBuilder createLogBuilder(int workers) {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout(PradarLinkSpout.class.getSimpleName(), new PradarLinkSpout(), workers * 2);
        return builder;
    }

    /**
     * 启动link测试,默认使用ck
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Map<String, String> inputMap = Maps.newHashMap();
        ParamUtil.parseInputParam(inputMap, args);
        inputMap.put(ParamUtil.DATA_SOURCE_TYPE, CommonStat.MYSQL);
        Integer workers = Integer.valueOf(inputMap.get(ParamUtil.WORKERS));
        Config config = StormConfig.createConfig(workers);
        config.putAll(inputMap);

        TopologyBuilder topologyBuilder = createLogBuilder(Integer.valueOf(workers));
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology(PradarLinkTopologyTest.class.getSimpleName(), config, topologyBuilder.createTopology());
        TimeUnit.MINUTES.sleep(30);
    }
}

