package io.ybqdren.github;

import io.ybqdren.github.common.Base;
import io.ybqdren.github.model.Message;
import io.ybqdren.github.model.RequestHeaderModel;
import io.ybqdren.github.model.StatusCodeModel;
import io.ybqdren.github.send.LoginJob;
import io.ybqdren.github.send.MessageSendJob;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Zhao Wen on 2021/2/21
 * @Blog https://blog.wenzhao18.top/
 * @E-Mail：withzhaowen@126.com
 */

public class Main extends Base {
    static Logger logger = LogManager.getLogger(Main.class);

    public static RequestHeaderModel requestHeaderModel = new RequestHeaderModel();

    public static void main(String[] args) throws IOException, InterruptedException {
        String cookie = requestHeaderModel.getCookie();
        String userAgent = requestHeaderModel.getUserAgent();
        Message message = readConfig("userConfig.properties");

        // 从参数中提取Cookie
        if(args.length == 0){
            logger.warn("请在Secrets中填写COOKIE");
        }
        requestHeaderModel.setCookie(args[0]);


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
