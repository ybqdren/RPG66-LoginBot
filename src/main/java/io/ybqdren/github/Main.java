package io.ybqdren.github;

import io.ybqdren.github.common.Base;
import io.ybqdren.github.model.Message;
import io.ybqdren.github.model.RequestHeaderModel;
import io.ybqdren.github.model.StatusCodeModel;
import io.ybqdren.github.send.LoginJob;
import io.ybqdren.github.send.MessageSendJob;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Zhao Wen on 2021/2/21
 * @Blog https://blog.wenzhao18.top/
 * @E-Mail：withzhaowen@126.com
 */

public class Main extends Base {
    public static RequestHeaderModel requestHeaderModel = new RequestHeaderModel();

    public static void main(String[] args) throws IOException {
        String cookie = requestHeaderModel.getCookie();
        String userAgent = requestHeaderModel.getUserAgent();
        Message message = readConfig("userConfig.properties");
        if(!(cookieIsNull(cookie) && userAgentIsNull(userAgent))){
            LoginJob.run(requestHeaderModel,message);
        }else{
            MessageSendJob.run(StatusCodeModel.NOCOOKIE_CODE,message);
        }
    }

    private static Boolean userAgentIsNull(String userAgent){
        return "".equals(userAgent);
    }

    private static Boolean cookieIsNull(String cookie){
        return "".equals(cookie);
    }

    private static Message readConfig(String configFileName) throws IOException{
        Properties properties = getFileContent(configFileName);
        Message message = new Message();
        String[] uids = {properties.getProperty("uids")};

        requestHeaderModel.setCookie(properties.getProperty("Cookie"));
        requestHeaderModel.setUserAgent(properties.getProperty("User-Agent"));
        requestHeaderModel.setCacheControl(properties.getProperty("Cache-Control"));
        requestHeaderModel.setPragma(properties.getProperty("Pragma"));
        requestHeaderModel.setUpgradeInsecureRequests(properties.getProperty("Upgrade-Insecure-Requests"));
        requestHeaderModel.setConnection(properties.getProperty("Connection"));
        message.setAppToken(properties.getProperty("appToken"));
        message.setUrl(properties.getProperty("url"));
        message.setUids(uids);

        return message;
    }

    public static Properties getFileContent(String fileName) throws IOException{
        Properties properties = new Properties();
        BufferedInputStream inputStream = (BufferedInputStream) ClassLoader.getSystemResourceAsStream("userConfig.properties");
        properties.load(inputStream);
        inputStream.close();
        return properties;
    }
}
