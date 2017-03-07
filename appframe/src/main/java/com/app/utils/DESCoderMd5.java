package com.app.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;

public class DESCoderMd5
{
    // 密钥
    private static String YAN = "TomatoStore!@#";

    public static String desCode(List<String> strings)
    {
        Collections.sort(strings);
        StringBuilder stringBuffer = new StringBuilder();
        for (String s : strings)
        {
            stringBuffer.append("&").append(s);
        }

        String string = stringBuffer.substring(1, stringBuffer.length());
        LogWriter.d("加密之前字符串：" + string);
        String deSString = md5Encrypt(string);
        LogWriter.d("第一次加密：" + deSString);
        deSString = md5Encrypt(deSString + YAN);
        LogWriter.d("第二次加密：" + deSString);
        return deSString;
    }

    //YAN是随意设置的一串字母符号数字,跟密码原文组合到一起进行MD5算法加密,以起到混淆作用,可以有效干扰对密码的暴力破解​
    private static String md5Encrypt(String string)
    {

        try
        {
            StringBuffer sb = new StringBuffer();

            MessageDigest digest = MessageDigest.getInstance("md5");// algorithm

            byte[] bytes = digest.digest(string.getBytes()); // 参数是，明文字节数组，返回的就是加密后的结果，字节数组

            for (byte b : bytes)
            { // 数byte 类型转换为无符号的整数

                int n = b & 0XFF; // 将整数转换为16进制

                String s = Integer.toHexString(n); // 如果16进制字符串是一位，那么前面补0

                if (s.length() == 1)
                {

                    sb.append("0" + s);

                } else
                {

                    sb.append(s);
                }

            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e)
        {

            e.printStackTrace();
        }
        return null;
    }


}