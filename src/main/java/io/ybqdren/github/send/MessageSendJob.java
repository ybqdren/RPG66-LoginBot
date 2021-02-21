package io.ybqdren.github.send;

import com.alibaba.fastjson.JSONObject;
import io.ybqdren.github.common.Base;
import io.ybqdren.github.model.Message;
import io.ybqdren.github.model.StatusCodeModel;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by Zhao Wen on 2021/2/21
 */

public class MessageSendJob extends Base {
    public static void run(String statusCode, Message message) throws IOException {
        String sendAPI = "http://wxpusher.zjiecode.com/api/send/message/";
        if(StatusCodeModel.SUCCESS_CODE.equals(statusCode)){
            successMessage(sendAPI,message);
        }else if(StatusCodeModel.FAILD_CODE.equals(statusCode) || StatusCodeModel.NOCOOKIE_CODE.equals(statusCode)){
            failMessage(sendAPI,message);
        }
    }

    /**
     * get fail
     * @param url
     * @param message
     */
    private static void failMessage(String url,Message message) throws IOException{
        String content = "橙光自动登录失败";
        url=url+"?"+"appToken="+message.getAppToken()+"&content="+content+"&uid="+message.getUids()[0]+"&url="+message.getUrl();
        HttpGet httpGet = new HttpGet(url);
        HttpClient httpClient = HttpClients.custom().build();
        HttpResponse response = httpClient.execute(httpGet);
    }

    /**
     * get success
     * @param url
     * @param message
     * @throws IOException
     */
    private static void successMessage(String url,Message message) throws IOException {
        // send success
        if(pageRequest(url,message)){
            return;
        }

        // send fail
        failMessage(url,message);
    }

    private static Boolean pageRequest(String url,Message message) throws IOException {
        String content = URLEncoder.encode(message.getContent(),"UTF-8");
        url=url+"?"+"appToken="+message.getAppToken()+"&content="+content+"&uid="+message.getUids()[0];
        HttpGet httpGet = new HttpGet(url);
        HttpClient httpClient = HttpClients.custom().build();
        HttpResponse response = httpClient.execute(httpGet);
        return isSuccess(response);
    }

    private static boolean isSuccess(HttpResponse response) throws IOException {
        String content = EntityUtils.toString(response.getEntity(),"UTF-8");
        JSONObject jsonObject = JSONObject.parseObject(content);
        return Boolean.parseBoolean(jsonObject.getString("success"));
    }
}
