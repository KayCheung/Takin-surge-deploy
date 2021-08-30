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

package io.shulie.surge.data.deploy.pradar.parser.rpc;

import com.pamirs.pradar.log.parser.trace.RpcBased;
import io.shulie.surge.data.common.utils.HttpUtil;
import io.shulie.surge.data.runtime.common.utils.ApiProcessor;
import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

//TODO P2 验证节点是否可以解析出来
@RunWith(PowerMockRunner.class)
@PrepareForTest({Integer.class, HttpUtil.class})
public class RpcServerRpcBasedParserTest extends TestCase {

    public void testServiceParse() throws IllegalAccessException, InterruptedException {
        //初始化parser
        RpcServerRpcBasedParser parser = new RpcServerRpcBasedParser();
        //System.out.println(parser);

        //初始化请求报文
        RpcBased rpcBased = new RpcBased();
        //设置应用名,URL,HTTP METHOD
        rpcBased.setAppName("zeus-web");
        rpcBased.setServiceName("http://pdasys.zt-express.com/zto/zeusApi/v1/pdaUpload");
        rpcBased.setMethodName("POST");

        String rules = "{\"error\":null,\"data\":{\"zeus-web\":[\"/checkSn#GET\",\"/foo#GET\",\"/hystrix/hystrixTest#GET\",\"/zto/api/addNoBill#POST\",\"/zto/api/checkRepeatBag#POST\",\"/zto/api/findByOrgCode#GET\",\"/zto/api/findWarnBillByBillCode#POST\",\"/zto/api/getGunDateList#POST\",\"/zto/api/getGunPost#POST\",\"/zto/api/getGunStaff#POST\",\"/zto/api/getNewsByParam#POST\",\"/zto/api/getNoBillTypes#GET\",\"/zto/api/getOaNews#POST\",\"/zto/api/getPackageCheckConfig#POST\",\"/zto/api/getPaperBillProvByBillCodes#POST\",\"/zto/api/hasContents#POST\",\"/zto/api/intelligentScanUpload#POST\",\"/zto/api/pdaBackPass#POST\",\"/zto/api/queryPackageError#POST || GET\",\"/zto/api/staffBind#POST\",\"/zto/api/upLoadFile#GET || POST\",\"/zto/api/upLoadFileBatch#GET || POST\",\"/zto/api/uploadConsumingTimeList#POST\",\"/zto/api/uploadExceptionInfo#POST\",\"/zto/api/uploadMismatchList#POST\",\"/zto/api/whiteBillCodeValidation#POST\",\"/zto/api/whiteListValidation#POST\",\"/zto/api_utf8/InnerService#POST || GET\",\"/zto/api_utf8/queryCarInfo#POST || GET\",\"/zto/external/packageCollect#POST\",\"/zto/log/logCollect#POST\",\"/zto/pdaApi/{version}/BatchUploadFile#POST\",\"/zto/pdaApi/{version}/checkDepart#POST\",\"/zto/pdaApi/{version}/checkLogisticsCar#POST\",\"/zto/pdaApi/{version}/checkRoute#POST\",\"/zto/pdaApi/{version}/checkSiteCarLoad#POST\",\"/zto/pdaApi/{version}/checkSiteOpenPromiseArriveByBillCode#POST\",\"/zto/pdaApi/{version}/completeLoading#POST\",\"/zto/pdaApi/{version}/conditionCheck#POST\",\"/zto/pdaApi/{version}/functionSwitchUpload#POST\",\"/zto/pdaApi/{version}/getEmployees#POST\",\"/zto/pdaApi/{version}/getIncrementLabel#POST\",\"/zto/pdaApi/{version}/getStationListByCode#POST\",\"/zto/pdaApi/{version}/getStrandedInventory#POST\",\"/zto/pdaApi/{version}/getTruckBaseCarSurveying#POST\",\"/zto/pdaApi/{version}/getTrucksBySiteId#POST\",\"/zto/pdaApi/{version}/judgeSameCity#POST\",\"/zto/pdaApi/{version}/oneKeyCollect#POST\",\"/zto/pdaApi/{version}/operationTimeCollection#POST\",\"/zto/pdaApi/{version}/orderPromiseArrive#POST\",\"/zto/pdaApi/{version}/queryCustomerInformation#POST\",\"/zto/pdaApi/{version}/queryPostByLeaderId#POST\",\"/zto/pdaApi/{version}/queryPostScanType#POST\",\"/zto/pdaApi/{version}/queryProvideRecord#POST\",\"/zto/pdaApi/{version}/queryTransferCompany#POST\",\"/zto/pdaApi/{version}/queryTransportAndGoods#POST\",\"/zto/pdaApi/{version}/questionsUpload#POST\",\"/zto/pdaApi/{version}/remindersLimited#POST\",\"/zto/pdaApi/{version}/repairComplaint#POST\",\"/zto/pdaApi/{version}/siteArrearsInquiry#POST\",\"/zto/pdaApi/{version}/transmitJobInformation#POST\",\"/zto/pdaApi/{version}/uploadConsumingTimeList#POST\",\"/zto/pdaApi/{version}/uploadLocalMismatchList#POST\",\"/zto/zeusApi/addNoBill#POST\",\"/zto/zeusApi/checkLogFile#POST\",\"/zto/zeusApi/checkRepeatBag#POST\",\"/zto/zeusApi/findBySiteCode#POST\",\"/zto/zeusApi/findWarnBillByBillCode#POST\",\"/zto/zeusApi/getNoBillTypes#GET\",\"/zto/zeusApi/getPackageCheckConfig#POST\",\"/zto/zeusApi/getPdaCommonInfo#POST\",\"/zto/zeusApi/getUserInfo#POST\",\"/zto/zeusApi/hasContents#POST\",\"/zto/zeusApi/intelligentScanUpload#POST\",\"/zto/zeusApi/queryCenterPackageError#POST\",\"/zto/zeusApi/queryInterceptorInfo#POST\",\"/zto/zeusApi/querySitePackageError#POST\",\"/zto/zeusApi/returnOrder#POST\",\"/zto/zeusApi/searchInterceptors#POST\",\"/zto/zeusApi/upLoadFile#POST\",\"/zto/zeusApi/upLoadFileBatch#POST\",\"/zto/zeusApi/uploadCenterMismatchList#POST\",\"/zto/zeusApi/uploadChunk#POST\",\"/zto/zeusApi/uploadConsumingTimeList#POST\",\"/zto/zeusApi/uploadExceptionInfo#POST\",\"/zto/zeusApi/uploadSiteMismatchList#POST\",\"/zto/zeusApi/whiteBillCodeValidation#POST\",\"/zto/zeusApi/whiteListValidation#POST\",\"/zto/zeusApi/{version}/checkLogin#POST\",\"/zto/zeusApi/{version}/getCustomParam#POST\",\"/zto/zeusApi/{version}/getLimitSite#POST\",\"/zto/zeusApi/{version}/getOaNews#POST\",\"/zto/zeusApi/{version}/getOaNewsDetail#POST\",\"/zto/zeusApi/{version}/getPackageCollects#POST\",\"/zto/zeusApi/{version}/pdaLogin#POST\",\"/zto/zeusApi/{version}/pdaUpload#POST\",\"/zto/zeusApi/{version}/pdaUploadPicture#POST\",\"/zto/zeusApi/{version}/pictureUpload#POST\",\"/zto/zeusApi/{version}/uploadEntryMismatchList#POST\",\"/zto/zeusApi/{version}/wayBillCodeUnobstructed#POST\"]},\"success\":true}";

        //初始化API处理器
        ApiProcessor apiProcessor = new ApiProcessor();
        //mock其中私有属性PORT为任意一个整数,这里为8080,无实际意义
        PowerMockito.field(ApiProcessor.class, "PORT").set(apiProcessor, "8080");

        //在调用匹配前需要调用refresh去获取规则,refresh方法是通过init方法以一个2分钟的定时调度线程池去更新的
        PowerMockito.mockStatic(HttpUtil.class);
        PowerMockito.when(HttpUtil.doGet(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString())).thenReturn(rules);
        //开始获取规则
        apiProcessor.init();
        //由于是线程池处理,需要等待3s将规则收集
        Thread.sleep(3000);

        //打印和规则中匹配的内容
        //System.out.println(parser.serviceParse(rpcBased));

        assertEquals("/zto/zeusApi/{version}/pdaUpload", parser.serviceParse(rpcBased));
    }

    public void testServiceParse2() throws IllegalAccessException, InterruptedException {
        //初始化parser
        RpcServerRpcBasedParser parser = new RpcServerRpcBasedParser();
        //System.out.println(parser);

        //初始化请求报文
        RpcBased rpcBased = new RpcBased();
        //设置应用名,URL,HTTP METHOD
        rpcBased.setAppName("zeus-web");
        rpcBased.setServiceName("/interface/bsn/gen_bsn/20210802/xxxxx");
        rpcBased.setMethodName("GET");

        String rules = "{\"error\":null,\"data\":{\"zeus-web\":[\"/checkSn#GET\",\"/interface/bsn/gen_bsn/{date}/{serialno}#GET\"]},\"success\":true}";

        //初始化API处理器
        ApiProcessor apiProcessor = new ApiProcessor();
        //mock其中私有属性PORT为任意一个整数,这里为8080,无实际意义
        PowerMockito.field(ApiProcessor.class, "PORT").set(apiProcessor, "8080");

        //在调用匹配前需要调用refresh去获取规则,refresh方法是通过init方法以一个2分钟的定时调度线程池去更新的
        PowerMockito.mockStatic(HttpUtil.class);
        PowerMockito.when(HttpUtil.doGet(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString())).thenReturn(rules);
        //开始获取规则
        apiProcessor.init();
        //由于是线程池处理,需要等待3s将规则收集
        Thread.sleep(3000);

        //打印和规则中匹配的内容
        //System.out.println(parser.serviceParse(rpcBased));

        assertEquals("/interface/bsn/gen_bsn/{date}/{serialno}", parser.serviceParse(rpcBased));
    }

    public void testServiceParse3() throws IllegalAccessException, InterruptedException {
        //初始化parser
        RpcServerRpcBasedParser parser = new RpcServerRpcBasedParser();
        //System.out.println(parser);

        //初始化请求报文
        RpcBased rpcBased = new RpcBased();
        //设置应用名,URL,HTTP METHOD
        rpcBased.setAppName("zeus-web");
        rpcBased.setServiceName("/interface/bsn/gen_bsn/20210802");
        rpcBased.setMethodName("GET");

        String rules = "{\"error\":null,\"data\":{\"zeus-web\":[\"/checkSn#GET\",\"/interface/bsn/gen_bsn/{date}#GET\"]},\"success\":true}";

        //初始化API处理器
        ApiProcessor apiProcessor = new ApiProcessor();
        //mock其中私有属性PORT为任意一个整数,这里为8080,无实际意义
        PowerMockito.field(ApiProcessor.class, "PORT").set(apiProcessor, "8080");

        //在调用匹配前需要调用refresh去获取规则,refresh方法是通过init方法以一个2分钟的定时调度线程池去更新的
        PowerMockito.mockStatic(HttpUtil.class);
        PowerMockito.when(HttpUtil.doGet(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString())).thenReturn(rules);
        //开始获取规则
        apiProcessor.init();
        //由于是线程池处理,需要等待3s将规则收集
        Thread.sleep(3000);

        //打印和规则中匹配的内容
        //System.out.println(parser.serviceParse(rpcBased));

        assertEquals("/interface/bsn/gen_bsn/{date}", parser.serviceParse(rpcBased));
    }
}