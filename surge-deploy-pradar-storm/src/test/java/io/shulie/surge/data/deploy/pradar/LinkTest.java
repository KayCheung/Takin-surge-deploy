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

import com.google.inject.Injector;
import io.shulie.surge.data.deploy.pradar.config.PradarSupplierConfiguration;
import io.shulie.surge.data.deploy.pradar.link.processor.EntranceProcessor;
import io.shulie.surge.data.deploy.pradar.link.processor.LinkProcessor;
import io.shulie.surge.deploy.pradar.common.CommonStat;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LinkTest {

    public static void main(String args[]) {
        Injector injector = new PradarSupplierConfiguration().initDataRuntime().getInstance(Injector.class);
        EntranceProcessor entranceProcessor = injector.getInstance(EntranceProcessor.class);
        try {
            entranceProcessor.init();
            ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    try {
                        entranceProcessor.share(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 10, 5, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main1(String args[]) {
        Injector injector = new PradarSupplierConfiguration().initDataRuntime().getInstance(Injector.class);
        LinkProcessor linkProcessor = injector.getInstance(LinkProcessor.class);
        try {
            linkProcessor.init(CommonStat.CLICKHOUSE);
            ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    try {
                        linkProcessor.share(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 10, 5, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO use mock
    /* *//**
     * 任意服务拓扑图
     *//*
    @Test
    public void linkTopologyForEveryService() throws IOException {
        Injector injector = new PradarSupplierConfiguration().initDataRuntime().getInstance(Injector.class);
        LinkProcessor linkProcessor = injector.getInstance(LinkProcessor.class);
        Pair<Set<LinkNodeModel>, Set<LinkEdgeModel>> pair = linkProcessor.link(null, getLinkConfig("wsdemo-soft-210104", "0", "/kafka/sendToKafka", "GET"), TraceLogQueryScopeEnum.DAY);
        System.out.println(JSONObject.toJSON(pair.getLeft()).toString());
        System.out.println(JSONObject.toJSON(pair.getRight()).toString());
    }

    @Test
    public void linkTopologyForTrace() {
        Injector injector = new PradarSupplierConfiguration().initDataRuntime().getInstance(Injector.class);
        LinkProcessor linkProcessor = injector.getInstance(LinkProcessor.class);
        ClickHouseSupport clickHouseSupport = injector.getInstance(ClickHouseSupport.class);
        String sql = "select * from t_trace_all where traceId = '807ece0a16147517917511083d0044'";
        List<TTrackClickhouseModel> modelList = clickHouseSupport.queryForList(sql, TTrackClickhouseModel.class);
        List<RpcBased> rpcBasedList = modelList.stream().map(model -> model.getRpcBased()).collect(Collectors.toList());
        linkProcessor.saveLink("df71b5babd00a2ad74e3734a969d8c20", linkProcessor.linkCache.getLinkConfig().get("df71b5babd00a2ad74e3734a969d8c20"));
    }

    @Test
    public void linkTopologyForTrace1() {
        Injector injector = new PradarSupplierConfiguration().initDataRuntime().getInstance(Injector.class);
        LinkProcessor linkProcessor = injector.getInstance(LinkProcessor.class);
        ClickHouseSupport clickHouseSupport = injector.getInstance(ClickHouseSupport.class);
        String sql = "select * from t_trace_all where traceId = '0100007f16161445233901023d7382' order by rpcId asc";
        List<TTrackClickhouseModel> modelList = clickHouseSupport.queryForList(sql, TTrackClickhouseModel.class);
        List<RpcBased> rpcBasedList = modelList.stream().map(model -> model.getRpcBased()).collect(Collectors.toList());
        RpcBased rpcBased = rpcBasedList.get(0);
        Pair<Set<LinkNodeModel>, Set<LinkEdgeModel>> json = linkProcessor.linkAnalysis("0100007f16161445233901023d7382", rpcBasedList);
    }

    @Test
    public void linkTopologyForTrace0() {
        Injector injector = new PradarSupplierConfiguration().initDataRuntime().getInstance(Injector.class);
        LinkProcessor linkProcessor = injector.getInstance(LinkProcessor.class);
        ClickHouseSupport clickHouseSupport = injector.getInstance(ClickHouseSupport.class);
        String sql = "select * from t_trace_all where traceId='0100007f16161445233901023d7382' order by rpcId asc limit 1";
        List<TTrackClickhouseModel> modelList = clickHouseSupport.queryForList(sql, TTrackClickhouseModel.class);
        List<RpcBased> rpcBasedList = modelList.stream().map(model -> model.getRpcBased()).collect(Collectors.toList());
        RpcBased rpcBased = rpcBasedList.get(0);
        DefaultRpcBasedParser parser = new DefaultRpcBasedParser();
        rpcBased.setServiceName(ApiProcessor.merge(rpcBased.getAppName(), rpcBased.getServiceName(), rpcBased.getMethodName()));
        String linkId = parser.linkId(rpcBased);
        //String appName, String rpcType, String service, String method
        linkProcessor.saveLink(linkId, getLinkConfig(rpcBased.getAppName(), String.valueOf(rpcBased.getRpcType()), ApiProcessor.merge(rpcBased.getAppName(), rpcBased.getServiceName(), rpcBased.getMethodName()), rpcBased.getMethodName()));
    }

    private Map<String, Object> getLinkConfig(String appName, String rpcType, String service, String method) {
        Map<String, Object> map = new HashMap<>();
        map.put("method", method);
        map.put("appName", appName);
        map.put("rpcType", rpcType);
        map.put("service", service);
        return map;
    }


    @Test
    public void testLinkUnKnowMQProcessor() {
        Injector injector = new PradarSupplierConfiguration().initDataRuntime().getInstance(Injector.class);
        LinkProcessor linkProcessor = injector.getInstance(LinkProcessor.class);
        linkProcessor.init();
        LinkUnKnowMQProcessor linkUnKnowMQProcessor = injector.getInstance(LinkUnKnowMQProcessor.class);
        linkUnKnowMQProcessor.processUnKnowNodeMQ("df71b5babd00a2ad74e3734a969d8c20", linkProcessor.linkCache.getLinkConfig().get("df71b5babd00a2ad74e3734a969d8c20"));
    }

    @Test
    public void testLinkProcessor() throws IOException {
        Injector injector = new PradarSupplierConfiguration().initDataRuntime().getInstance(Injector.class);
        LinkProcessor linkProcessor = injector.getInstance(LinkProcessor.class);
        linkProcessor.init();
        linkProcessor.saveLink("df71b5babd00a2ad74e3734a969d8c20", linkProcessor.linkCache.getLinkConfig().get("df71b5babd00a2ad74e3734a969d8c20"));
    }

    @Test
    public void testSaveLinkEntrance() throws IOException {
        Injector injector = new PradarSupplierConfiguration().initDataRuntime().getInstance(Injector.class);
        LinkProcessor linkProcessor = injector.getInstance(LinkProcessor.class);
        linkProcessor.init();
        EntranceProcessor entranceProcessor = injector.getInstance(EntranceProcessor.class);
        entranceProcessor.init();
        entranceProcessor.share();

        linkProcessor.saveLink("df71b5babd00a2ad74e3734a969d8c20", linkProcessor.linkCache.getLinkConfig().get("df71b5babd00a2ad74e3734a969d8c20"));
    }

    @Test
    public void testLinkUnKnowNodeProcessor() {
        Injector injector = new PradarSupplierConfiguration().initDataRuntime().getInstance(Injector.class);
        LinkProcessor linkProcessor = injector.getInstance(LinkProcessor.class);
        linkProcessor.init();
        LinkUnKnowNodeProcessor linkUnKnowNodeProcessor = injector.getInstance(LinkUnKnowNodeProcessor.class);
        linkUnKnowNodeProcessor.processUnKnowNodeCommon("df71b5babd00a2ad74e3734a969d8c20", linkProcessor.linkCache.getLinkConfig().get("df71b5babd00a2ad74e3734a969d8c20"));
    }

    @Test
    public void testLinkUnKnowNodeCleanProcessor() {
        Injector injector = new PradarSupplierConfiguration().initDataRuntime().getInstance(Injector.class);
        LinkProcessor linkProcessor = injector.getInstance(LinkProcessor.class);
        linkProcessor.init();
        LinkUnKnowNodeCleanProcessor linkUnKnowNodeCleanProcessor = injector.getInstance(LinkUnKnowNodeCleanProcessor.class);
        linkUnKnowNodeCleanProcessor.clearUnknownNode("df71b5babd00a2ad74e3734a969d8c20");
    }

    @Test
    public void testInsert() {
        Injector injector = new PradarSupplierConfiguration().initDataRuntime().getInstance(Injector.class);
        String log = "f277632f16167516753331081d0001|1616751675335|172.18.0.18-1|df70438e2b7d677970d4f29ef22cfd68|86d66acb59200a4854ab1f93556ecfdd|0.1|2|1|quick_dms_mall|quick_dms_mall|quick_dms_mall|171|httpclient4.x|/uic/login/login|POST|192.168.0.72||200|0|0|{}||1||@|@|172.70.235.220|1.2";
        ClickHouseSupport clickHouseSupport = injector.getInstance(ClickHouseSupport.class);
        RpcBased rpcBased = ProtocolParserFactory.getFactory().getTraceProtocolParser("1.5").parse(log);
        ClickhouseFacade clickhouseFacade = ClickhouseFacade.Factory.getInstace();
        clickhouseFacade.addCommond(new BaseCommand());
        clickhouseFacade.addCommond(new LinkCommand());
        String sql = "insert into t_trace_all (" + clickhouseFacade.getCols() + ") values (" + clickhouseFacade.getParam() + ")";
        clickhouseFacade.toObjects(clickhouseFacade.invoke(rpcBased));
        List<Object[]> list = new ArrayList();
        list.add(clickhouseFacade.toObjects(clickhouseFacade.invoke(rpcBased)));
        clickHouseSupport.update(sql, list);


    }*/
}
