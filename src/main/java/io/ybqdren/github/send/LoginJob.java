package io.ybqdren.github.send;

import io.ybqdren.github.common.Base;
import io.ybqdren.github.model.*;
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
 *  Created by Zhao Wen on 2021/2/21
 *
 *  疑似领取鲜花的url        http://c2.cgyouxi.com/website/orange/js/login_sign/login_sign.js?v=20170621
 *  疑似检查用户登录信息      http://www.66rpg.com/ajax/LoginSign/user_login_set.json
 *  每日任务                http://www.66rpg.com/ActiveSystem/index/get_today_task_lists
 */

public class LoginJob extends Base {
    public static void run(RequestHeaderModel requestHeaderModel, Message message) throws IOException, InterruptedException {
        // 第一次请求url
        String loginUrl = "http://www.66rpg.com/home";

        // 第二次请求url 获取当前用户鲜花数目信息
        String userUrl = "http://www.66rpg.com/home";

        // 第三次请求url 获取当前用户登录信息
        String friendUrl = "http://www.66rpg.com/friend/";

        UserInforModel userInforModel = null;
        ResponseMessageModel responseMessageModelLogin = null;
        ResponseMessageModel requestHeaderModelUserInfo = null;
        ResponseMessageModel responseMessageModelFriend = null;

        // 进行登录
        responseMessageModelLogin = pageRequest(loginUrl,requestHeaderModel);

        // 第一次请求与第二次请求相隔 10s
        Thread.sleep(10000);

        // 获取用户信息
        requestHeaderModelUserInfo = pageRequest(userUrl,requestHeaderModel);
        System.out.println(requestHeaderModelUserInfo.getRecContent());
        userInforModel = getUserInfo(requestHeaderModelUserInfo);

        if(!("".equals(userInforModel.getUid())) && userInforModel.getUid()!=null){
            responseMessageModelFriend = pageRequest(friendUrl+userInforModel.getUid(),requestHeaderModel);
            if( responseMessageModelFriend.getSendStatus() == 200){
                userInforModel.setUrl(message.getUrl());
                userInforModel = getUerInforOther(userInforModel,responseMessageModelFriend.getRecContent());
            }
        }else {
            userInforModel = null;
        }

        sendMessage(userInforModel,message);
    }

    /**
     * 发送消息
     * @param userInforModel
     */
    private static void sendMessage(UserInforModel userInforModel, Message message) throws IOException {
        String statusCode= userInforModel==null? StatusCodeModel.FAILD_CODE : StatusCodeModel.SUCCESS_CODE;
        if(StatusCodeModel.SUCCESS_CODE.equals(statusCode)){
            message.setContent(userInforModel.toString());
        }
        MessageSendJob.run(statusCode,message);
    }

    /**
     * 获取用户上次登录信息
     * @param userInforModel
     * @param resContent
     * @return
     */
    private static UserInforModel getUerInforOther(UserInforModel userInforModel,String resContent){
        Document document = Jsoup.parse(resContent);
        System.out.println(document.html());
        Elements profileNode = document.select("div.left");

        // user lastlogin time
        String lastLogin = profileNode.select("div.profile").select("span:contains(上次登录时间)").text();
        userInforModel.setLastLogin(lastLogin);

        return userInforModel;
    }

    /**
     * 获取用户信息
     *
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
     *
     * @param url
     * @param requestHeaderModel
     * @return
     * @throws IOException
     */
    private static ResponseMessageModel pageRequest(String url,RequestHeaderModel requestHeaderModel) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Cookie",requestHeaderModel.getCookie());
        httpGet.addHeader("User-Agent",requestHeaderModel.getUserAgent());

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
