package io.ybqdren.github.send;

import io.ybqdren.github.common.Base;
import io.ybqdren.github.model.Message;
import io.ybqdren.github.model.ResponseMessageModel;
import io.ybqdren.github.model.StatusCodeModel;
import io.ybqdren.github.model.UserInforModel;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Zhao Wen on 2021/2/21
 */

public class LoginJob extends Base {
    public static void run(String cookie, String userAgent, Message message) throws IOException {
        String userurl = "http://www.66rpg.com/home";
        String friendUrl = "http://www.66rpg.com/friend/";
        UserInforModel userInforModel = null;
        ResponseMessageModel responseMessageModel_01 = null;
        ResponseMessageModel responseMessageModel_02 = null;
        // get tow page info
        responseMessageModel_01 = pageRequest(userurl,cookie,userAgent);
        userInforModel = getUserInfo(responseMessageModel_01);

        if(!("".equals(userInforModel.getUid())) && userInforModel.getUid()!=null){
            responseMessageModel_02 = pageRequest(friendUrl+userInforModel.getUid(),cookie,userAgent);
            if( responseMessageModel_02.getSendStatus() == 200){
                userInforModel.setUrl(message.getUrl());
                userInforModel = getUerInforOther(userInforModel,responseMessageModel_02.getRecContent());
            }
        }else {
            userInforModel = null;
        }
        sendMessage(userInforModel,message);
    }

    /**
     *
     * @param userInforModel
     */
    private static void sendMessage(UserInforModel userInforModel, Message message) throws IOException {
        String statusCode= userInforModel==null? StatusCodeModel.FAILD_CODE : StatusCodeModel.SUCCESS_CODE;
        if(StatusCodeModel.SUCCESS_CODE.equals(statusCode)){
            message.setContent(userInforModel.toString());
        }
        MessageSendJob.run(statusCode,message);
    }

    private static UserInforModel getUerInforOther(UserInforModel userInforModel,String resContent){
        Document document = Jsoup.parse(resContent);
        System.out.println(document.html());
        Elements profileNode = document.select("div.left");

        // user lastlogin time
        String lastLogin = profileNode.select("div.profile").select("span:contains(上次登录时间)").text();
        userInforModel.setLastLogin(lastLogin);

        // user total play-game time
//        String totalTime = profileNode.select("div.profile").select("span#js_runtime_sum").text();
//        userInforModel.setTotalTime(totalTime);

        return userInforModel;
    }

    /**
     * 获取用户信息
     * @param responseMessageModel
     * @return
     */
    private static UserInforModel getUserInfo(ResponseMessageModel responseMessageModel){
        UserInforModel userInforModel = new UserInforModel();
        Document document = Jsoup.parse(responseMessageModel.getRecContent());
        Elements profileNode = document.select("div.usr-profile").select("dl");
        // username /friend/84155247
        String username = document.select("span.username_box").select("a.user-name").text();

        // userid
        String uid = profileNode.select("span.profile-txt").select("a[href]").attr("href").replaceAll("/friend/","");

        // flowers
        String flowers = profileNode.select("span.fc-orange").select("span.va-m").text();

        if(username!="" && uid!="" && flowers!=""){
            userInforModel.setUsername(username);
            userInforModel.setUid(uid);
            userInforModel.setFlowers(Integer.parseInt(flowers.split(" ")[1]));
        }
        return userInforModel;
    }

    /**
     * get response
     * @param url
     * @param cookie
     * @param userAgent
     * @return
     * @throws IOException
     */
    private static ResponseMessageModel pageRequest(String url, String cookie, String userAgent) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Cookie",cookie);
        httpGet.addHeader("User-Agent",userAgent);
        HttpClient httpClient = HttpClients.custom().build();
        HttpResponse response = httpClient.execute(httpGet);
        String recContent = null;

        recContent = EntityUtils.toString(response.getEntity(),"UTF-8");
        ResponseMessageModel responseMessageModel = new ResponseMessageModel();
        responseMessageModel.setRecContent(recContent);
        responseMessageModel.setSendStatus(response.getStatusLine().getStatusCode());
        return responseMessageModel;
    }
}
