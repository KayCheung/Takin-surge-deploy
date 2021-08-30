#!/bin/sh

#
# Copyright 2021 Shulie Technology, Co.Ltd
# Email: shulie@shulie.io
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# See the License for the specific language governing permissions and
# limitations under the License.
#

#搜索引擎相关操作
HOSTIP=http://192.168.1.111:9200
INDEXNAME="t_trace_v3"
# 索引别名
INDEXNAME_ALIAS="t_trace"
create_index(){
  curl -XPUT $HOSTIP/$INDEXNAME '
    {
      "settings": {
        "number_of_shards": 5,
        "number_of_replicas": 1
    }
  }'
}

#添加别名
create_index_alias(){
  curl -XPUT $HOSTIP/$INDEXNAME/_alias/$INDEXNAME_ALIAS
}

create_index

sleep 2

create_index_alias


