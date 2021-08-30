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

import com.pamirs.pradar.log.parser.ProtocolParserFactory;
import com.pamirs.pradar.log.parser.monitor.MonitorBased;
import com.pamirs.pradar.log.parser.trace.RpcBased;
import com.pamirs.pradar.log.parser.trace.TraceProtocolParser;
import io.shulie.surge.data.deploy.pradar.parser.RpcBasedParser;
import io.shulie.surge.data.deploy.pradar.parser.RpcBasedParserFactory;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @Author: xingchen
 * @ClassName: LogTest
 * @Package: io.shulie.surge.data.deploy.pradar
 * @Date: 2020/12/221:32
 * @Description:
 */
public class LogTest {


    //TODO use mock
    /*@Test
    public void logDigester_clickhouseDisable(){
        Injector injector = new PradarSupplierConfiguration().initDataRuntime().getInstance(Injector.class);
        LogDigester linkProcessor = injector.getInstance(LogDigester.class);
        System.out.println(linkProcessor.getClickhouseDisable().get());
    }

    @Test
    public void logDigester_clickhouseSampling(){
        Injector injector = new PradarSupplierConfiguration().initDataRuntime().getInstance(Injector.class);
        LogDigester linkProcessor = injector.getInstance(LogDigester.class);
        System.out.println(linkProcessor.getClickhouseSampling().get());
    }

    @Test
    public void logDigester_digest(){
        Injector injector = new PradarSupplierConfiguration().initDataRuntime().getInstance(Injector.class);
        LogDigester linkProcessor = injector.getInstance(LogDigester.class);
        linkProcessor.init();
        DigestContext<RpcBased> context = new DigestContext<>();
        context.setContent(JSONObject.parseObject("{\"log\":null,\"version\":\"1.5\",\"hostIp\":\"172.18.0.10\",\"agentId\":\"dms-mall-1111\",\"appName\":\"dms_mall\",\"traceId\":\"1d56256516158755518975920d0001\",\"entranceNodeId\":\"43dffa1af9fab84372114ffe0dc9e881\",\"entranceId\":\"43dffa1af9fab84372114ffe0dc9e881\",\"level\":1,\"parentIndex\":0,\"index\":0,\"rpcId\":\"0\",\"rpcType\":0,\"logType\":1,\"traceAppName\":\"dms_mall\",\"upAppName\":\"dms_mall\",\"startTime\":1615875551897,\"cost\":720,\"middlewareName\":\"tomcat\",\"serviceName\":\"http://47.98.139.245:8010/mall/orders/order/render\",\"methodName\":\"POST\",\"remoteIp\":\"101.37.86.29\",\"port\":\"8010\",\"resultCode\":\"200\",\"requestSize\":38,\"responseSize\":0,\"request\":\"{{\\\"addressId\\\":\\\"-10\\\",\\\"isPickUp\\\":\\\"false\\\"}}\",\"response\":\"\",\"clusterTest\":true,\"callbackMsg\":\"\",\"samplingInterval\":1,\"localId\":null,\"attributes\":{},\"localAttributes\":{},\"async\":false,\"invokeId\":null,\"invokeType\":null,\"flags\":null,\"attributesBased\":null,\"localAttributesBased\":null,\"logTime\":1615875551897,\"ok\":false,\"clientAppName\":\"dms_mall\",\"serviceId\":\"http://47.98.139.245:8010/mall/orders/order/render.POST\",\"dataType\":1,\"serverAppName\":\"dms_mall\"}",RpcBased.class));
        linkProcessor.digest(context);
    }*/

    @Test
    public void parse_15() {
        String log = "e364a8c016117386935441001d56ec|1611738693974|172.17.0.1-22252|eb353ad8c406776cb8b0ee5bce5458c1|4453abbb9aec98ace119bf1a1b521128|0.1|2|1|wsdemoold-soft-210127|wsdemoold-soft-210127|wsdemoold-soft-210127|97|apache-dubbo|com.example.dubboproviderdemo.service.UserService|addUser(userModel)|192.168.1.118||00|0|0|{userModel[key=9,content=dubbo_dubbo_692]}||0||@|@|192.168.1.118|1.5";
        TraceProtocolParser traceLogParser = ProtocolParserFactory.getFactory().getTraceProtocolParser("1.5");
        RpcBased rpcBased = traceLogParser.parse(log);
        System.out.println(rpcBased.getAppName());
    }


    @Test
    public void parse_16() {
        String log = "010011ac16160461137318974da09e|1616046113732|172.17.0.1-41118|0.1|5|wsdemoold-soft-dubbo-provider-210222|0|redis|-1:set|e3952|00|{e3952,e3952}||true~null~false~false|redis-lettuce|#1|@null~~|@wsdemoold-soft-dubbo-provider-210222~~~0~0|@|@@nid7704ff52edd554f9064b3317367f66e3|1.6";
        TraceProtocolParser traceLogParser = ProtocolParserFactory.getFactory().getTraceProtocolParser("1.6");
        RpcBased rpcBased = traceLogParser.parse(log);
        System.out.println(rpcBased.getAppName());
    }

    @Test
    public void parseMonitor_11() {
        String log = "verify-local-tank-demo|1615278146|192.168.100.234-23767|0.00|0.12|0.10|0.07|52.16|3823181824|1828868096|0.00|0|0|1|94846595072|81315614720|5648814080|158224013824|1.5";
        String dataVersion = "1.1";
        String hostIp = "localhost";
        log += '|' + hostIp + '|' + dataVersion;
        MonitorBased monitorBased = ProtocolParserFactory.getFactory().getMonitorProtocolParser(dataVersion).parse(hostIp, dataVersion, log);
        if (monitorBased == null) {
            System.out.println("未解析到日志");
        }
    }

    /**
     * RocketMQ入口-ons
     */
    @Test
    public void testParse_01() {
        String log = "ffffffff1615430180404000000000|1615430180404|entrance_case_RocketMQ-3|cccccccccccccccccccccccccccccccc|null|0|3|3|entrance_case_RocketMQ-3|entrance_case_RocketMQ-3|entrance_case_RocketMQ-3|20|ons|order_create-RocketMQ-topic|RocketMQ-consumer|10.230.10.10|888|00|0|0|||1||!0.2|@s@1127585c@|@|10.230.10.10|1.5";
        TraceProtocolParser traceLogParser = ProtocolParserFactory.getFactory().getTraceProtocolParser("1.5");
        RpcBased rpcBased = traceLogParser.parse(log);
        System.out.println(rpcBased.getMethodName());
        RpcBasedParser rpcBasedParser = RpcBasedParserFactory.getInstance(rpcBased.getLogType(), rpcBased.getRpcType());
        System.out.println(StringUtils.defaultString(rpcBasedParser.methodParse(rpcBased), ""));
        Assert.assertEquals("RocketMQ-consumer", StringUtils.defaultString(rpcBasedParser.methodParse(rpcBased), ""));
    }

    /**
     * RocketMQ入口-ons method包含'%'
     */
    @Test
    public void testParse_02() {
        String log = "ffffffff1615430180441000000000|1615430180441|entrance_case_RocketMQ-4|cccccccccccccccccccccccccccccccc|null|0|3|3|entrance_case_RocketMQ-4|entrance_case_RocketMQ-4|entrance_case_RocketMQ-4|20|ons|order_create-RocketMQ-topic|order_create-RocketMQ-topic%RocketMQ-consumer|10.230.10.10|888|00|0|0|||1||!0.2|@s@1127585c@|@|10.230.10.10|1.5";
        TraceProtocolParser traceLogParser = ProtocolParserFactory.getFactory().getTraceProtocolParser("1.5");
        RpcBased rpcBased = traceLogParser.parse(log);
        System.out.println(rpcBased.getMethodName());
        RpcBasedParser rpcBasedParser = RpcBasedParserFactory.getInstance(rpcBased.getLogType(), rpcBased.getRpcType());
        System.out.println(StringUtils.defaultString(rpcBasedParser.methodParse(rpcBased), ""));
        Assert.assertEquals("RocketMQ-consumer", StringUtils.defaultString(rpcBasedParser.methodParse(rpcBased), ""));
    }

    /**
     * RocketMQ入口-ons method包含':'
     */
    @Test
    public void testParse_03() {
        String log = "ffffffff1615430180442000000000|1615430180442|entrance_case_RocketMQ-5|cccccccccccccccccccccccccccccccc|null|0|3|3|entrance_case_RocketMQ-5|entrance_case_RocketMQ-5|entrance_case_RocketMQ-5|20|ons|order_create-RocketMQ-topic|RocketMQ-consumer:abcd|10.230.10.10|888|00|0|0|||1||!0.2|@s@1127585c@|@|10.230.10.10|1.5";
        TraceProtocolParser traceLogParser = ProtocolParserFactory.getFactory().getTraceProtocolParser("1.5");
        RpcBased rpcBased = traceLogParser.parse(log);
        System.out.println(rpcBased.getMethodName());
        RpcBasedParser rpcBasedParser = RpcBasedParserFactory.getInstance(rpcBased.getLogType(), rpcBased.getRpcType());
        System.out.println(StringUtils.defaultString(rpcBasedParser.methodParse(rpcBased), ""));
        Assert.assertEquals("RocketMQ-consumer", StringUtils.defaultString(rpcBasedParser.methodParse(rpcBased), ""));
    }

    /**
     * RocketMQ入口-ons method包含多个':'
     */
    @Test
    public void testParse_04() {
        String log = "ffffffff1615430180423000000000|1615430180423|entrance_case_RocketMQ-7|cccccccccccccccccccccccccccccccc|null|0|3|3|entrance_case_RocketMQ-7|entrance_case_RocketMQ-7|entrance_case_RocketMQ-7|20|ons|order_create-RocketMQ-topic|RocketMQ-consumer:*:canace_9|10.230.10.10|888|00|0|0|||1||!0.2|@s@1127585c@|@|10.230.10.10|1.5";
        TraceProtocolParser traceLogParser = ProtocolParserFactory.getFactory().getTraceProtocolParser("1.5");
        RpcBased rpcBased = traceLogParser.parse(log);
        System.out.println(rpcBased.getMethodName());
        RpcBasedParser rpcBasedParser = RpcBasedParserFactory.getInstance(rpcBased.getLogType(), rpcBased.getRpcType());
        System.out.println(StringUtils.defaultString(rpcBasedParser.methodParse(rpcBased), ""));
        Assert.assertEquals("RocketMQ-consumer", StringUtils.defaultString(rpcBasedParser.methodParse(rpcBased), ""));
    }

}
