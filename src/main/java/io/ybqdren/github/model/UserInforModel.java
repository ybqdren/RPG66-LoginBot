package io.ybqdren.github.model;

/**
 * Created by Zhao Wen on 2021/2/21
 */

public class UserInforModel {
    private String uid;
    private String username;
    private int flowers;
    private String totalTime;
    private String lastLogin;
    private String url;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getFlowers() {
        return flowers;
    }

    public void setFlowers(int flowers) {
        this.flowers = flowers;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "橙光每日自动登录"+"\n"+
                "\n"+
                "用户名："+username+"\n"+
               "用户ID："+uid+"\n"+
                "当前花数："+flowers+"\n"+
                lastLogin+"\n"+
                "\n"+
                "教程/问题集中反馈页："+url;
    }
}
