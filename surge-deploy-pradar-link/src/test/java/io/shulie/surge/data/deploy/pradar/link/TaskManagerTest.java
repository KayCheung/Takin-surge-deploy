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

import com.google.common.collect.Lists;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * 任务分配
 * @author vincent
 */
public class TaskManagerTest extends TestCase {

    @Test
    public void testAllotOfAverage() {
        TaskManager taskManager = new TaskManager();
        Map<String, List<String>> map = taskManager.allotOfAverage(Lists.newArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"), Lists.newArrayList("1", "2", "3", "4", "5", "6"));
        assertEquals(map.get("1"), Lists.newArrayList("1"));
        assertNull(map.get("7"));
        map = taskManager.allotOfAverage(Lists.newArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"), Lists.newArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "11"));
        assertEquals(map.get("1"), Lists.newArrayList("1"));
    }
}