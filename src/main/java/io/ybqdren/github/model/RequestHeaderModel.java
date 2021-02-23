package io.ybqdren.github.model;

/**
 * Created by Zhao Wen on 2021/2/23
 */

public class RequestHeaderModel {
    private  String     cookie                   = null;
    private  String     userAgent                = null;
    private  String     cacheControl             = null;
    private  String     pragma                   = null;
    private  String     upgradeInsecureRequests  = null;
    private  String     Connection               = null;

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getCacheControl() {
        return cacheControl;
    }

    public void setCacheControl(String cacheControl) {
        this.cacheControl = cacheControl;
    }

    public String getPragma() {
        return pragma;
    }

    public void setPragma(String pragma) {
        this.pragma = pragma;
    }

    public String getUpgradeInsecureRequests() {
        return upgradeInsecureRequests;
    }

    public void setUpgradeInsecureRequests(String upgradeInsecureRequests) {
        this.upgradeInsecureRequests = upgradeInsecureRequests;
    }

    public String getConnection() {
        return Connection;
    }

    public void setConnection(String connection) {
        Connection = connection;
    }
}
