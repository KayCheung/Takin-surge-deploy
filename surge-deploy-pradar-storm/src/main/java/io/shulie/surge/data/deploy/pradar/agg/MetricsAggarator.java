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

package io.shulie.surge.data.deploy.pradar.agg;

import com.google.inject.Singleton;
import io.shulie.surge.data.common.aggregation.*;
import io.shulie.surge.data.common.aggregation.metrics.CallStat;
import io.shulie.surge.data.common.aggregation.metrics.Metric;
import io.shulie.surge.data.common.utils.FormatUtils;
import io.shulie.surge.data.common.utils.Pair;
import io.shulie.surge.data.deploy.pradar.PradarReduceBolt;
import io.shulie.surge.data.deploy.pradar.common.PradarRtConstant;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.shulie.surge.data.common.utils.CommonUtils.divide;
import static io.shulie.surge.data.deploy.pradar.common.PradarRtConstant.AGG_TRACE_SECONDS_INTERVAL;
import static io.shulie.surge.data.deploy.pradar.common.PradarRtConstant.AGG_TRACE_SECONDS_LOWER_LIMIT;

/**
 * @Author: xingchen
 * @ClassName: MetricsAggarator
 * @Package: io.shulie.surge.data.runtime.agg
 * @Date: 2020/11/3010:18
 * @Description:
 */
@Singleton
public class MetricsAggarator implements Aggregator {
    private static Logger logger = LoggerFactory.getLogger(MetricsAggarator.class);
    private final Aggregation<Metric, CallStat> aggregation = new Aggregation<>(
            AGG_TRACE_SECONDS_INTERVAL, AGG_TRACE_SECONDS_LOWER_LIMIT);

    /**
     * init
     *
     * @param scheduler
     * @param collector
     * @param topologyContext
     */
    public void init(Scheduler scheduler,
                     final SpoutOutputCollector collector,
                     final TopologyContext topologyContext) {
        aggregation.start(scheduler, new Aggregation.CommitAction<Metric, CallStat>() {
            @Override
            public void commit(long slotKey, AggregateSlot<Metric, CallStat> slot) {
                int size = slot.size();
                if (size > 0) {
                    Map<Metric, CallStat> map = slot.toMap();
                    // ?????? reducer ?????????????????? Pair<Metric, CallStat> ??????????????? Job
                    // ?????? emit ????????? Storm ?????????????????????????????????????????????????????????????????????
                    List<Integer> reducerIds = topologyContext.getComponentTasks(
                            PradarReduceBolt.class.getSimpleName());
                    final int reducerCount = reducerIds.size();
                    List<Pair<Metric, CallStat>>[] jobs = new List[reducerCount];
                    int jobSize = (int) divide(size, reducerCount - 1); // ??????
                    for (int i = 0; i < reducerCount; ++i) {
                        jobs[i] = new ArrayList<>(jobSize);
                    }

                    // ????????????????????? reducer ?????????????????????
                    // ????????????????????????????????????????????? metric ??????????????? reducer ??????
                    for (Map.Entry<Metric, CallStat> entry : map.entrySet()) {
                        int jobId = Math.abs(entry.getKey().hashCode()) % reducerCount;
                        List<Pair<Metric, CallStat>> job = jobs[jobId];
                        job.add(new Pair<>(entry.getKey(), entry.getValue()));
                    }
                    // ??? Job ??????????????? reducer
                    final String slotKeyTime = FormatUtils.toSecondTimeString(slotKey * 1000);
                    for (int i = 0; i < reducerCount; ++i) {
                        int reducerId = reducerIds.get(i).intValue();
                        List<Pair<Metric, CallStat>> job = jobs[i];
                        if (!job.isEmpty()) {
                            collector.emitDirect(reducerId, PradarRtConstant.REDUCE_METRICS_STREAM_ID,
                                    new Values(slotKey * 1000, job));
                        }
                    }
                    //logger.info("emit " + slotKeyTime + " to " + reducerIds.size() + " reducers, size=" + size);
                }
            }
        });
    }


    @Override
    public int size() {
        return 0;
    }

    @Override
    public int getInterval() {
        return 0;
    }

    @Override
    public int getLowerLimit() {
        return 0;
    }

    @Override
    public long getLowerBound() {
        return 0;
    }

    /**
     * ?????????????????????slotKey ??? timestamp ?????????????????????????????????
     *
     * @param key
     * @return
     */
    @Override
    public long slotKeyToTimestamp(long key) {
        return aggregation.slotKeyToTimestamp(key);
    }

    /**
     * ??????????????????
     *
     * @param timestamp
     * @return
     */
    @Override
    public long timestampToSlotKey(long timestamp) {
        return aggregation.timestampToSlotKey(timestamp);
    }

    /**
     * ????????????????????????
     *
     * @param timestamp
     * @return
     */
    @Override
    public AggregateSlot<Metric, CallStat> getSlotByTimestamp(long timestamp) {
        return aggregation.getSlotByTimestamp(timestamp);
    }

    /**
     * ????????????????????????
     *
     * @param timestamp
     * @return
     */
    @Override
    public AggregateSlot<Metric, CallStat> getSlotByTimestamp(String timestamp) {
        return aggregation.getSlotByTimestamp(NumberUtils.toLong(timestamp));
    }

    @Override
    public void start() throws Exception {
    }

    @Override
    public void stop() throws Exception {

    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
