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

package io.shulie.surge.data.deploy.pradar.link.processor;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.pamirs.pradar.log.parser.trace.RpcBased;
import io.shulie.surge.data.deploy.pradar.link.AbstractLinkCache;
import io.shulie.surge.data.deploy.pradar.link.TaskManager;
import io.shulie.surge.data.deploy.pradar.link.enums.TraceLogQueryScopeEnum;
import io.shulie.surge.data.deploy.pradar.link.model.LinkEdgeModel;
import io.shulie.surge.data.deploy.pradar.link.model.LinkNodeModel;
import io.shulie.surge.data.deploy.pradar.link.model.TTrackClickhouseModel;
import io.shulie.surge.data.deploy.pradar.link.util.PropertiesUtil;
import io.shulie.surge.data.deploy.pradar.link.util.StringUtil;
import io.shulie.surge.data.deploy.pradar.parser.MiddlewareType;
import io.shulie.surge.data.deploy.pradar.parser.PradarLogType;
import io.shulie.surge.data.deploy.pradar.parser.RpcBasedParser;
import io.shulie.surge.data.deploy.pradar.parser.RpcBasedParserFactory;
import io.shulie.surge.data.runtime.common.remote.DefaultValue;
import io.shulie.surge.data.runtime.common.remote.Remote;
import io.shulie.surge.data.sink.clickhouse.ClickHouseSupport;
import io.shulie.surge.data.sink.mysql.MysqlSupport;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

/**
 * ???????????????
 *
 * @author vincent
 */
public class LinkProcessor extends AbstractProcessor {
    private static final Logger logger = LoggerFactory.getLogger(LinkProcessor.class);

    private static final String LINKNODE_TABLENAME = "t_amdb_pradar_link_node";
    private static final String LINKEDGE_TABLENAME = "t_amdb_pradar_link_edge";

    private static final String NEW_LINE_MATCHER = "\r\n";

    private static final String LINK_TOPOLOGY_SQL
            = " appName,entranceNodeId,traceId,rpcId,logType,rpcType,upAppName,middlewareName,serviceName,"
            + "parsedServiceName,methodName,port,remoteIp ";

    @Inject
    private ClickHouseSupport clickHouseSupport;

    @Inject
    private MysqlSupport mysqlSupport;

    @Inject
    private TaskManager<String, String> taskManager;

    /**
     * ??????????????????????????????
     */
    @Inject
    @DefaultValue("true")
    @Named("/pradar/config/rt/linkProcessDisable")
    private Remote<Boolean> linkProcessDisable;

    /**
     * ????????????,??????2??????
     */
    @Inject
    @DefaultValue("120")
    @Named("/pradar/config/rt/linkProcess/delayTime")
    private Remote<Long> intervalTime;

    @Inject
    @Named("config.link.trace.query.limit")
    private String traceQuerylimit;

    public AbstractLinkCache linkCache = new AbstractLinkCache() {
        @Override
        public void save(String linkId, LinkedBlockingQueue<String> linkedBlockingQueue) {
            return;
        }
    };

    String linkNodeInsertSql = "";
    String linkEdgeInsertSql = "";

    /**
     * ???????????????????????????????????????
     *
     * @param currentTaskId
     * @throws IOException
     */
    @Override
    public void share(List<String> taskIds, String currentTaskId) {
        if (!linkProcessDisable.get()) {
            return;
        }
        if (!isHandler(intervalTime.get())) {
            return;
        }
        Map<String, Map<String, Object>> linkConfig = linkCache.getLinkConfig();
        if (linkConfig == null || linkConfig.isEmpty()) {
            return;
        }
        Set<String> linkIdSet = linkConfig.keySet();
        Map<String, List<String>> avgMap = taskManager.allotOfAverage(taskIds, new ArrayList<>(linkIdSet));
        List<String> avgList = avgMap.get(currentTaskId);
        if (CollectionUtils.isNotEmpty(avgList)) {
            for (int i = 0; i < avgList.size(); i++) {
                String linkId = avgList.get(i);
                Map<String, Object> link = linkConfig.get(avgList.get(i));
                saveLink(linkId, link);
            }
        }
    }

    /**
     * ???????????????????????????????????????
     *
     * @param taskId
     * @throws IOException
     */
    @Override
    public void share(int taskId) {
        if (!linkProcessDisable.get()) {
            return;
        }
        if (!isHandler(intervalTime.get())) {
            return;
        }
        if (taskId == -1) {
            return;
        }

        Map<String, Map<String, Object>> linkConfig = linkCache.getLinkConfig();
        List<Map.Entry<String, Map<String, Object>>> linkList = Lists.newArrayList(linkConfig.entrySet());
        for (int i = 0; i < linkList.size(); i++) {
            if (i % taskId == 0) {
                Map.Entry<String, Map<String, Object>> link = linkList.get(i);
                saveLink(link.getKey(), link.getValue());
            }
        }
    }

    /**
     * ??????????????????????????????????????????
     *
     * @throws IOException
     */
    @Override
    public void share() {
        if (!linkProcessDisable.get()) {
            return;
        }
        if (!isHandler(intervalTime.get())) {
            return;
        }
        Map<String, Map<String, Object>> linkConfig = linkCache.getLinkConfig();
        for (Map.Entry<String, Map<String, Object>> entry : linkConfig.entrySet()) {
            saveLink(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void init() {

    }

    /**
     * ??????????????????
     */
    public void saveLink(String linkId, Map<String, Object> linkConfig) {
        logger.info("LinkProcessor {},{}", linkId, linkConfig);
        try {
            //??????MySQL
            Pair<Set<LinkNodeModel>, Set<LinkEdgeModel>> linkPair = link(linkId, linkConfig,
                    TraceLogQueryScopeEnum.MINUTE);
            Set<LinkNodeModel> linkNodeModels = linkPair.getLeft();
            Set<LinkEdgeModel> linkEdgeModels = linkPair.getRight();
            if (CollectionUtils.isEmpty(linkNodeModels) || CollectionUtils.isEmpty(linkEdgeModels)) {
                logger.warn("LinkProcessor is empty  {}, {}", linkId, linkConfig);
                return;
            }
            mysqlSupport.batchUpdate(linkNodeInsertSql,
                    linkNodeModels.stream().map(LinkNodeModel::getValues).collect(Collectors.toList()));
            mysqlSupport.batchUpdate(linkEdgeInsertSql,
                    linkEdgeModels.stream().map(LinkEdgeModel::getValues).collect(Collectors.toList()));
            logger.info("LinkProcessor save is ok size {},{}", linkPair.getKey().size(), linkPair.getValue().size());
        } catch (Exception e) {
            logger.error("Save to pradar_link_info error!" + linkId, e);
            //ignore
        }
    }

    /**
     * ??????linkId?????????????????????
     *
     * @param linkId
     * @return
     * @throws IOException
     */
    public Pair<Set<LinkNodeModel>, Set<LinkEdgeModel>> link(String linkId, Map<String, Object> linkConfig,
                                                             TraceLogQueryScopeEnum queryScope) throws IOException {
        Set<LinkEdgeModel> edges = new HashSet<>();
        Set<LinkNodeModel> nodes = new HashSet<>();
        List<RpcBased> rpcBaseds = getTraceLog(linkConfig, queryScope);
        Pair<Set<LinkNodeModel>, Set<LinkEdgeModel>> linkRelationPair = linkAnalysis(linkId, rpcBaseds);
        nodes.addAll(linkRelationPair.getLeft());
        edges.addAll(linkRelationPair.getRight());
        return Pair.of(nodes, edges);
    }

    public List<RpcBased> getTraceLog(Map<String, Object> linkConfig, TraceLogQueryScopeEnum queryScope) {
        String method = String.valueOf(linkConfig.get("method"));
        String appName = String.valueOf(linkConfig.get("appName"));
        String rpcType = String.valueOf(linkConfig.get("rpcType"));
        String service = String.valueOf(linkConfig.get("service"));
        if (StringUtils.isBlank(rpcType) || "null".equals(rpcType)) {
            return Collections.EMPTY_LIST;
        }
        // k=traceId v=rpcId  ?????????????????????rpcId?????????????????????
        String simpleSql = "select traceId,rpcId,logType from t_trace_all where appName='" + appName +
                "' and parsedMethod = '" + method +
                "' and rpcType = '" + rpcType +
                "' and parsedServiceName = '" + service + "'";
        Calendar beginCalendar = Calendar.getInstance();
        switch (queryScope) {
            case WEEK:
                beginCalendar.add(Calendar.DATE, (int) (-1 * queryScope.getTime()));
                break;
            case DAY:
                beginCalendar.add(Calendar.DATE, (int) (-1 * queryScope.getTime()));
                break;
            case MINUTE:
            case MIN_CUS:
                beginCalendar.add(Calendar.MINUTE, (int) (-1 * queryScope.getTime()));
                break;
            default:
                //do nothing
        }
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.MINUTE, -1);
/*
        simpleSql += " and logType!='5'";
*/
        simpleSql += " and startDate>='" + DateFormatUtils.format(beginCalendar.getTime(), "yyyy-MM-dd HH:mm:ss")
                + "' and startDate <='" + DateFormatUtils.format(endCalendar, "yyyy-MM-dd HH:mm:ss")
                + "' order by startDate desc limit 2";

        List<Map<String, Object>> traceMaps = Lists.newArrayList();
        if (this.isUseCk()) {
            traceMaps = clickHouseSupport.queryForList(simpleSql);
        } else {
            traceMaps = mysqlSupport.queryForList(simpleSql);
        }

        StringBuilder sql = new StringBuilder();
        Map<String, String> traceFilter = new HashMap<>();
        for (Map<String, Object> traceIdMap : traceMaps) {
            if (traceIdMap.containsKey("logType") && "5".equals(traceIdMap.get("logType"))) {
                continue;
            }
            String traceId = Objects.toString(traceIdMap.get("traceId"));
            String rpcId = Objects.toString(traceIdMap.get("rpcId"));
            int logType = NumberUtils.toInt(Objects.toString(traceIdMap.get("logType")));

            //?????????mysql????????????,???union???limit???????????????,?????????????????????
            //clickhouse????????????????????????sql,???????????????????????????
            if (!this.isUseCk()) {
                sql.append("(");
            }
            sql.append(
                    "select " + LINK_TOPOLOGY_SQL + " from t_trace_all where traceId ='" + traceId + "'");
            //and logType!='5'");
            sql.append(" and startDate>='" + DateFormatUtils.format(beginCalendar.getTime(), "yyyy-MM-dd HH:mm:ss")
                    + "'");
            sql.append(" order by rpcId asc limit " + ("".equals(traceQuerylimit) ? "500" : traceQuerylimit));
            if (!this.isUseCk()) {
                sql.append(")");
            }
            sql.append(" union all ");
            traceFilter.put(traceId, rpcId + "#" + logType);
        }

        if (sql.length() <= 0) {
            return new ArrayList<>();
        }
        logger.info("LinkProcessor query traceIds:{}", traceFilter);
        //add trace log limit
        sql.delete(sql.length() - 11, sql.length());
        List<TTrackClickhouseModel> modelList = Lists.newArrayList();
        if (this.isUseCk()) {
            modelList = clickHouseSupport.queryForList(sql.toString(), TTrackClickhouseModel.class);
        } else {
            modelList = mysqlSupport.query(sql.toString(), new BeanPropertyRowMapper(TTrackClickhouseModel.class));
        }
        modelList = modelList.stream().
                filter(model -> {
                    if (model.getLogType() == 5) {
                        return false;
                    }
                    String ary[] = traceFilter.get(model.getTraceId()).split("#");
                    String filterRpcId = ary[0];
                    String filterLogType = ary[1];
                    if (model.getRpcId().equals(filterRpcId)) {
                        // ??????RpcID???????????????????????????????????????????????????????????????????????????????????????????????????
                        return "0".equals(filterRpcId) && appName.equals(model.getAppName()) && model.getParsedServiceName()
                                .contains(service) && method.equals(model.getMethodName()) && filterLogType.equals(
                                model.getLogType() + "");
                    }
                    // ???????????????????????????RpcId???????????????????????????????????????
                    return model.getRpcId().startsWith(filterRpcId) && model.getLogType() != 1;
                })
                .collect(Collectors.toList());

        return modelList.stream()
                .map(TTrackClickhouseModel::getRpcBased)
                .collect(Collectors.toList());
    }

    /**
     * ??????????????????
     *
     * @param rpcBaseds
     */
    public Pair<Set<LinkNodeModel>, Set<LinkEdgeModel>> linkAnalysis(String linkId, List<RpcBased> rpcBaseds) {
        Set<LinkEdgeModel> edges = new HashSet<>();
        Set<LinkNodeModel> nodes = new HashSet<>();
        for (RpcBased rpcBased : rpcBaseds) {
            if (rpcBased == null) {
                continue;
            }
            //????????????????????????????????????rpc??????????????????
            if (PradarLogType.LOG_TYPE_RPC_CLIENT == rpcBased.getLogType() && MiddlewareType.TYPE_RPC == rpcBased.getRpcType()) {
//                logger.warn("client rpc log ignored by system.");
                continue;
            }

            RpcBasedParser rpcBasedParser = RpcBasedParserFactory.getInstance(rpcBased.getLogType(),
                    rpcBased.getRpcType());
            if (rpcBasedParser == null) {
                continue;
            }
            String edgeId = rpcBasedParser.edgeId("", rpcBased);
            Map<String, Object> edgeTags = rpcBasedParser.edgeTags("", rpcBased);
            String fromAppId = rpcBasedParser.fromAppId(linkId, rpcBased);
            String toAppId = rpcBasedParser.toAppId(linkId, rpcBased);
            Map<String, Object> fromAppTags = rpcBasedParser.fromAppTags(linkId, rpcBased);
            Map<String, Object> toAppTags = rpcBasedParser.toAppTags(linkId, rpcBased);
            fromAppTags.put("appId", fromAppId);
            toAppTags.put("appId", toAppId);
            edgeTags.put("edgeId", edgeId);
            edgeTags.put("fromAppId", fromAppId);
            edgeTags.put("toAppId", toAppId);
            edgeTags.put("linkId", linkId);
            LinkNodeModel fromNodeModel = LinkNodeModel.parseFromDataMap(fromAppTags);
            if (StringUtils.isNotBlank((String) fromAppTags.get("middlewareName"))) {
                Map<String, Object> fromNodeExtendInfo = new HashMap<>();
                fromNodeExtendInfo.put("ip", rpcBased.getRemoteIp());
                fromNodeExtendInfo.put("port", rpcBased.getPort());
                fromNodeModel.setExtend(JSON.toJSONString(fromNodeExtendInfo));
            }
            nodes.add(fromNodeModel);
            LinkNodeModel toNodeModel = LinkNodeModel.parseFromDataMap(toAppTags);
            if (StringUtils.isNotBlank((String) toAppTags.get("middlewareName"))) {
                Map<String, Object> toNodeExtendInfo = new HashMap<>();
                toNodeExtendInfo.put("ip", rpcBased.getRemoteIp());
                toNodeExtendInfo.put("port", rpcBased.getPort());
                toNodeModel.setExtend(JSON.toJSONString(toNodeExtendInfo));
            }
            nodes.add(toNodeModel);
            edges.add(LinkEdgeModel.parseFromDataMap(edgeTags));
        }
        return Pair.of(nodes, edges);
    }

    /**
     * ??????????????????
     */
    public void init(String dataSourceType) {
        //???????????????
        this.setDataSourceType(dataSourceType);
        linkCache.autoRefresh(mysqlSupport);
        linkNodeInsertSql = "INSERT INTO " + LINKNODE_TABLENAME + LinkNodeModel.getCols() + " VALUES " + LinkNodeModel
                .getParamCols() + LinkNodeModel.getOnDuplicateCols();
        linkEdgeInsertSql = "INSERT INTO " + LINKEDGE_TABLENAME + LinkEdgeModel.getCols() + " VALUES " + LinkEdgeModel
                .getParamCols() + LinkEdgeModel.getOnDuplicateCols();
    }

    public AbstractLinkCache getLinkCache() {
        return linkCache;
    }


    public static void main(String[] args) throws Exception {
        Long userId = 100000237900002L;
        Long dbIndex = Long.valueOf(userId.longValue() % 64 % 8);
        System.out.println(dbIndex);
    }
}
