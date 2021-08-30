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

create_mapping() {
curl -XPUT -H 'Content-Type: application/json' $HOSTIP/$INDEXNAME/_mapping -d '
  {
    "properties": {
      "id": {
        "type": "keyword"
      },
      "appName": {
        "type": "keyword"
      },
      "traceId": {
        "type": "keyword"
      },
      "entranceNodeId": {
        "type": "keyword"
      }
      "entranceId": {
        "type": "keyword"
      },
      "level": {
        "type": "integer"
      },
      "parentIndex": {
        "type": "integer"
      },
      "index": {
        "type": "integer"
      },
      "rpcId": {
        "type": "keyword"
      },
      "rpcType": {
        "type": "keyword"
      },
      "logType": {
        "type": "keyword"
      },
      "traceAppName": {
        "type": "keyword"
      },
      "upAppName": {
        "type": "keyword"
      },
      "startTime": {
        "type": "long"
      },
      "cost": {
        "type": "long"
      },
      "middlewareName": {
        "type": "keyword"
      },
      "serviceName": {
        "type": "keyword"
      },
      "methodName": {
        "type": "keyword"
      },
      "remoteIp": {
        "type": "keyword"
      },
      "port": {
        "type": "keyword"
      },
      "resultCode": {
        "type": "keyword"
      },
      "requestSize": {
        "type": "long"
      },
      "responseSize": {
        "type": "long"
      },
      "request": {
        "type": "text"
      },
      "response": {
        "type": "text"
      },
      "clusterTest": {
        "type": "keyword"
      },
      "callbackMsg": {
        "type": "text"
      },
      "samplingInterval": {
        "type": "integer"
      },
      "localId": {
        "type": "keyword"
      },
      "attributes": {
        "type": "text"
      },
      "localAttributes": {
        "type": "text"
      },
      "async": {
        "type": "keyword"
      },
      "version": {
        "type": "keyword"
      },
      "hostIp": {
        "type": "keyword"
      },
      "agentId": {
        "type": "keyword"
      }
    }
  }'
}
create_mapping