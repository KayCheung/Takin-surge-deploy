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

package io.shulie.surge.data.deploy.pradar.parser.utils;

import io.shulie.surge.data.common.utils.Bytes;
import junit.framework.TestCase;
import org.junit.Assert;

import java.security.MessageDigest;

/**
 * @author sunshiyu
 * @description md5测试用例
 * @datetime 2021-05-19 10:24 上午
 */
public class Md5UtilsTest extends TestCase {

    public void testMd5() {
        String sourceStr = "1234567asdasfd|ag@12|32}1rnvknadnvasdv";
        Assert.assertEquals("md5 check failed.", md5IntegerToHexFormat(sourceStr), Md5Utils.md5(sourceStr));

    }


    public String md5IntegerToHexFormat(String text) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return text;
        }

        byte[] byteArray = Bytes.toBytes(text);
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}