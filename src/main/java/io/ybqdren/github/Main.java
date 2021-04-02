package io.ybqdren.github;

import io.ybqdren.github.common.Base;
import io.ybqdren.github.model.Message;
import io.ybqdren.github.model.RequestHeaderModel;
import io.ybqdren.github.model.StatusCodeModel;
import io.ybqdren.github.send.LoginJob;
import io.ybqdren.github.send.MessageSendJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Zhao Wen on 2021/2/21
 *
 * @Blog https://blog.wenzhao18.top/
 * @GitHub https://github.com/ybqdren
 */

public class Main extends Base {

    private static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static RequestHeaderModel requestHeaderModel = new RequestHeaderModel();

    public static void main(String[] args) throws IOException, InterruptedException {
        if(args.length == 0){
            LOGGER.warn("请检查$COOKIE、REQUESTS_INFO、WXPUSHER_CONFIG是否有在Secrets中进行设置");
        }

        // 设置cookie
        requestHeaderModel.setCookie(args[0]);

        // 设置浏览器头
        requestHeaderModel.setUserAgent(args[1]);

        // 设置请求的其他信息
//        String[] wxpusherInfo = args[2].split(",");
        Message message = new Message();
//        message.setAppToken(wxpusherInfo[0]);
//        message.setUids(new String[]{wxpusherInfo[1]});
//        message.setUids(new String[]{"UID_Cm4JqDw8OYs7adsl1AwRkg6vG4DF"});
//        message.setAppToken("AT_R3oG2VwZ7vtaksqD6XEhXP5Fkgqwy6uK");
        message.setAppToken(args[2]);
        message.setUids(new String[]{args[3]});

        String cookie = requestHeaderModel.getCookie();
        String userAgent = requestHeaderModel.getUserAgent();
        if(!(cookieIsNull(cookie) && userAgentIsNull(userAgent))){
            LoginJob.run(requestHeaderModel,message);
        }else{
            MessageSendJob.run(StatusCodeModel.NOCOOKIE_CODE,message);
        }
    }

 /*   private static void run(String[] args) throws IOException, InterruptedException {


    }*/

    private static Boolean userAgentIsNull(String userAgent){
        return "".equals(userAgent);
    }

    private static Boolean cookieIsNull(String cookie){
        return "".equals(cookie);
    }
}
