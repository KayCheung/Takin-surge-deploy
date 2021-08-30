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


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pamirs.pradar.log.parser.ProtocolParserFactory;
import io.shulie.surge.data.deploy.pradar.link.model.LinkEdgeModel;
import io.shulie.surge.data.deploy.pradar.link.model.LinkNodeModel;
import io.shulie.surge.data.deploy.pradar.link.processor.LinkProcessor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LinkProcessorTest {

    private static final String filePath = System.getProperty("user.dir") + "/src/test/java/cartlist.txt";

    private LinkProcessor linkProcessor;
    private String content;

    @Before
    public void init() throws IOException {
        linkProcessor = new LinkProcessor();
        content = FileUtils.readFileToString(new File(filePath));

    }

//    private static class MyTestModule extends AbstractModule {
//        @Override protected void configure() {
//            bind(LinkProcessor.class).to(LinkProcessor.class);
//        }
//    }

    @Test
    public void testLinkAnalysisToGraph() {
        String linkId = "bb245ef7e29cee7418d66c2c0aed0227";
        Iterable<String> splitter = Splitter.on("\n\n\n\n").omitEmptyStrings().split(content);

        Pair<Set<LinkNodeModel>, Set<LinkEdgeModel>> pair = linkProcessor.linkAnalysis(linkId, Lists.newArrayList(splitter).stream().map(log -> ProtocolParserFactory.getFactory().parseTraceLog(log)).collect(Collectors.toList()));
        //Map<String, String> nodes = pair.getLeft();
        //Map<String, String> edges = pair.getRight();
        Set<LinkNodeModel> nodeModelList = pair.getLeft();
        Set<LinkEdgeModel> edgeModelList = pair.getRight();

        Set<JSONObject> nodeSet = nodeModelList.stream().map(entry -> {
            String nodeId = entry.getLinkId();
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(entry);
            jsonObject.put("id", nodeId);
            jsonObject.put("label", jsonObject.get("appName"));
            return jsonObject;
        }).collect(Collectors.toSet());

        Set<JSONObject> edgeSet = edgeModelList.stream().map(entry -> {
            String edgeId = entry.getEdgeId();
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(entry);
            jsonObject.put("edgeId", edgeId);
            jsonObject.put("label", jsonObject.get("service") + "|" + jsonObject.get("method"));
            jsonObject.put("from", jsonObject.get("fromAppId"));
            jsonObject.put("to", jsonObject.get("toAppId"));
            return jsonObject;
        }).collect(Collectors.toSet());

        Map<String, Set<JSONObject>> map = Maps.newHashMap();
        map.put("nodes", nodeSet);
        map.put("edges", edgeSet);
        System.out.println(JSON.toJSON(map));
        /*for (Map.Entry<String, String> entry : nodes.entrySet()) {
            System.out.println(" put 'pradar-link-config','" + linkId + "','node:" + entry.getKey() + "','" + entry.getValue() + "'");
        }
        for (Map.Entry<String, String> entry : edges.entrySet()) {
            System.out.println(" put 'pradar-link-config','" + linkId + "','eagle:" + entry.getKey() + "','" + entry.getValue() + "'");

        }*/
    }


    @Test
    public void testTraceToGraph() throws IOException {
        String linkId = "bb245ef7e29cee7418d66c2c0aed0227";
        content = FileUtils.readFileToString(new File(System.getProperty("user.dir") + "/src/test/java/orderConfirm.txt"));

        Iterable<String> splitter = Splitter.on("\n").omitEmptyStrings().split(content);
        Pair<Set<LinkNodeModel>, Set<LinkEdgeModel>> pair = linkProcessor.linkAnalysis(linkId, Lists.newArrayList(splitter).stream().map(log -> ProtocolParserFactory.getFactory().parseTraceLog(log)).collect(Collectors.toList()));
        //Map<String, String> nodes = pair.getLeft();
        //Map<String, String> edges = pair.getRight();
        Set<LinkNodeModel> nodeModelList = pair.getLeft();
        Set<LinkEdgeModel> edgeModelList = pair.getRight();

        Set<JSONObject> nodeSet = nodeModelList.stream().map(entry -> {
            String nodeId = entry.getLinkId();
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(entry);
            jsonObject.put("id", nodeId);
            jsonObject.put("label", jsonObject.get("appName"));
            return jsonObject;
        }).collect(Collectors.toSet());

        Set<JSONObject> edgeSet = edgeModelList.stream().map(entry -> {
            String edgeId = entry.getEdgeId();
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(entry);
            jsonObject.put("edgeId", edgeId);
            jsonObject.put("label", jsonObject.get("service") + "|" + jsonObject.get("method"));
            jsonObject.put("from", jsonObject.get("fromAppId"));
            jsonObject.put("to", jsonObject.get("toAppId"));
            return jsonObject;
        }).collect(Collectors.toSet());

        Map<String, Set<JSONObject>> map = Maps.newHashMap();
        map.put("nodes", nodeSet);
        map.put("edges", edgeSet);
        System.out.println(JSON.toJSON(map));
        Assert.assertTrue(!map.isEmpty());
    }

    @Test
    public void testLinkAnalysis() {
        String linkId = "bb245ef7e29cee7418d66c2c0aed0227";
        Iterable<String> splitter = Splitter.on("\n\n\n\n").omitEmptyStrings().split(content);
        Pair<Set<LinkNodeModel>, Set<LinkEdgeModel>> pair = linkProcessor.linkAnalysis(linkId, Lists.newArrayList(splitter).stream().map(log -> ProtocolParserFactory.getFactory().parseTraceLog(log)).collect(Collectors.toList()));
        //Map<String, String> nodes = pair.getLeft();
        //Map<String, String> edges = pair.getRight();
        Set<LinkNodeModel> nodeModelList = pair.getLeft();
        Set<LinkEdgeModel> edgeModelList = pair.getRight();


        for (LinkNodeModel linkNodeModel : nodeModelList) {
            System.out.println(" put 'pradar-link-config','" + linkId + "','node:" + linkNodeModel.getLinkId() + "','" + JSON.toJSONString(linkNodeModel) + "'");
        }
        for (LinkEdgeModel linkEdgeModel : edgeModelList) {
            System.out.println(" put 'pradar-link-config','" + linkId + "','eagle:" + linkEdgeModel.getEdgeId() + "','" + JSON.toJSONString(linkEdgeModel) + "'");
        }

        Assert.assertTrue(!nodeModelList.isEmpty());
        Assert.assertTrue(!edgeModelList.isEmpty());
    }


    @Test
    public void testStoLinkAnalysis() throws IOException {
        String linkId = "69321a1b176bbb212c73add560a0e36e";
        content = FileUtils.readFileToString(new File(System.getProperty("user.dir") + "/src/test/java/sto.txt"));

        Iterable<String> splitter = Splitter.on("\n\n").omitEmptyStrings().split(content);
        Pair<Set<LinkNodeModel>, Set<LinkEdgeModel>> pair = linkProcessor.linkAnalysis(linkId, Lists.newArrayList(splitter).stream().map(log -> ProtocolParserFactory.getFactory().parseTraceLog(log)).collect(Collectors.toList()));
        //Map<String, String> nodes = pair.getLeft();
        //Map<String, String> edges = pair.getRight();
        Set<LinkNodeModel> nodeModelList = pair.getLeft();
        Set<LinkEdgeModel> edgeModelList = pair.getRight();

        Set<JSONObject> nodeSet = nodeModelList.stream().map(entry -> {
            String nodeId = entry.getLinkId();
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(entry);
            jsonObject.put("id", nodeId);
            jsonObject.put("label", jsonObject.get("appName"));
            return jsonObject;
        }).collect(Collectors.toSet());

        Set<JSONObject> edgeSet = edgeModelList.stream().map(entry -> {
            String edgeId = entry.getEdgeId();
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(entry);
            jsonObject.put("edgeId", edgeId);
            jsonObject.put("label", jsonObject.get("service") + "|" + jsonObject.get("method"));
            jsonObject.put("from", jsonObject.get("fromAppId"));
            jsonObject.put("to", jsonObject.get("toAppId"));
            return jsonObject;
        }).collect(Collectors.toSet());

        Map<String, Set<JSONObject>> map = Maps.newHashMap();
        map.put("nodes", nodeSet);
        map.put("edges", edgeSet);
        System.out.println(JSON.toJSON(map));
        Assert.assertTrue(!map.isEmpty());
    }

    @Test
    public void testStoCanaceLinkAnalysis() throws IOException {
        String linkId = "bb245ef7e29cee7418d66c2c0aed0227";
        content = "010011ac16116525620771002d4362|1611652562077|wsdemohttp|0|7|wsdemo-http-210113|7|elastic-job|com.example.testhttpdemo.ElasticJob.SchduerService|execute|00|||false~null~true~false||#2|@wsdemo-http-210113~com.example.testhttpdemo.ElasticJob.SchduerService~execute|@wsdemo-http-210113~~~0~0|192.189.1.1|1.6";
        /*content = "fb504b6a16063785677872122d001f|1606378567780|0.3.1|2|5|member-center|api-rest|api-rest|1|redis|0:get|SuperMemberCache193475745970819829|10.19.65.68|7000|00|24|176|{SuperMemberCache193475745970819829}||0||!0.4|@s@eb49ae5@|@|172.70.242.203|1.2\n" +
                "fb504b6a16063785677872122d001f|1606378567780|0.3.1|2|5|member-center|api-rest|api-rest|1|redis|0:get|uperMemberCache193475745970819829|10.19.65.68|7000|00|24|176|{SuperMemberCache193475745970819829}||0||!0.4|@s@eb49ae5@|@|172.70.242.203|1.2\n";*/
        //FileUtils.readFileToString(new File("/Users/vincent/Documents/work/shulie/workspace/surge/surge-deploy/surge-deploy-pradar-link/src/test/java/sto.txt"));

        Iterable<String> splitter = Splitter.on("\n").omitEmptyStrings().split(content);
        Pair<Set<LinkNodeModel>, Set<LinkEdgeModel>> pair = linkProcessor.linkAnalysis(linkId, Lists.newArrayList(splitter).stream().map(log -> ProtocolParserFactory.getFactory().parseTraceLog(log)).collect(Collectors.toList()));
        //Map<String, String> nodes = pair.getLeft();
        //Map<String, String> edges = pair.getRight();
        Set<LinkNodeModel> nodeModelList = pair.getLeft();
        Set<LinkEdgeModel> edgeModelList = pair.getRight();

        Set<JSONObject> nodeSet = nodeModelList.stream().map(entry -> {
            String nodeId = entry.getLinkId();
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(entry);
            jsonObject.put("id", nodeId);
            jsonObject.put("label", jsonObject.get("appName"));
            return jsonObject;
        }).collect(Collectors.toSet());

        Set<JSONObject> edgeSet = edgeModelList.stream().map(entry -> {
            String edgeId = entry.getEdgeId();
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(entry);
            jsonObject.put("edgeId", edgeId);
            jsonObject.put("label", jsonObject.get("service") + "|" + jsonObject.get("method"));
            jsonObject.put("from", jsonObject.get("fromAppId"));
            jsonObject.put("to", jsonObject.get("toAppId"));
            return jsonObject;
        }).collect(Collectors.toSet());

        Map<String, Set<JSONObject>> map = Maps.newHashMap();
        map.put("nodes", nodeSet);
        map.put("edges", edgeSet);
        System.out.println(JSON.toJSON(map));
        Assert.assertTrue(!map.isEmpty());
    }
}