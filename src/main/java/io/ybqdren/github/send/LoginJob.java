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
 * Created by Zhao Wen on 2021/2/21
 *
 * 找到鲜花信息不更新的缘故了：
 * 获取的鲜花信息是登录时抓的 肯定有延时性
 *
 * 应这样修改：
 * 登录 -> 判断是否登录成功 记录当前获得的鲜花数 -> 在此请求用户信息页 获取鲜花数 -> 将两个进行比较 -> 如果上次登录时间非今天
 * 且鲜花数 新请求大于原来数目 —> 登录成功
 */

public class LoginJob extends Base {
    public static void run(RequestHeaderModel requestHeaderModel, Message message) throws IOException {
        String userurl = "http://www.66rpg.com/home";
        String friendUrl = "http://www.66rpg.com/friend/";
        UserInforModel userInforModel = null;
        ResponseMessageModel responseMessageModelLogin = null;
        ResponseMessageModel requestHeaderModelUserInfo = null;
        ResponseMessageModel responseMessageModel_02 = null;

        // get tow page info
        // login
        responseMessageModelLogin = pageRequest(userurl,requestHeaderModel);
        // get user information
        requestHeaderModelUserInfo = pageRequest(userurl,requestHeaderModel);
        userInforModel = getUserInfo(requestHeaderModelUserInfo);

        if(!("".equals(userInforModel.getUid())) && userInforModel.getUid()!=null){
            responseMessageModel_02 = pageRequest(friendUrl+userInforModel.getUid(),requestHeaderModel);
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

    /**
     * get user lastime
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
        httpGet.addHeader("Cache-Control",requestHeaderModel.getCacheControl());
        httpGet.addHeader("Pragma",requestHeaderModel.getPragma());
        httpGet.addHeader("Upgrade-Insecure-Requests",requestHeaderModel.getUpgradeInsecureRequests());
        httpGet.addHeader("Connection",requestHeaderModel.getConnection());

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
