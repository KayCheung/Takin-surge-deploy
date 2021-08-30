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
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;

import java.util.Map;
import java.util.concurrent.TimeUnit;

class PradarLogTopologyTest {
    /**
     * storm启动测试类
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //args = new String[]{"1"};
        //args = new String[]{"-DSourceType=MYSQL"};
        Map<String, String> inputMap = Maps.newHashMap();
        ParamUtil.parseInputParam(inputMap, args);

        LocalCluster cluster = new LocalCluster();
        Integer workers = Integer.valueOf(inputMap.get(ParamUtil.WORKERS));
        Config config = StormConfig.createConfig(workers);

        config.putAll(inputMap);
        TopologyBuilder topologyBuilder = PradarLogTopology.createLogBuilder(Integer.valueOf(workers), false);

        cluster.submitTopology(PradarLogTopology.class.getSimpleName(), config, topologyBuilder.createTopology());
        TimeUnit.MINUTES.sleep(30l);
    }
}
