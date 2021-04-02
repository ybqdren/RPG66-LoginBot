package io.ybqdren.github.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Zhao Wen on 2021/2/21
 */

public class Message implements Serializable {
    /**
     * app token
     */
    private String appToken;

    /**
     * message content
     */
    private String content;

    /**
     * message summary
     */
    private String summary;

    /**
     * message type
     */
    private int contentType = 1;

    /**
     * message topicIds
     */
    private int[] topicIds;

    /**
     * user ids
     */
    private String[] uids;

    /**
     * messagge url
     */
    private String url = "https://github.com/ybqdren/RPG66-LoginBot";

    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String[] getUids() {
        return uids;
    }

    public void setUids(String[] uids) {
        this.uids = uids;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int[] getTopicIds() {
        return topicIds;
    }

    public void setTopicIds(int[] topicIds) {
        this.topicIds = topicIds;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Message{" +
                "appToken='" + appToken + '\'' +
                ", content='" + content + '\'' +
                ", summary='" + summary + '\'' +
                ", contentType=" + contentType +
                ", topicIds=" + Arrays.toString(topicIds) +
                ", uids=" + Arrays.toString(uids) +
                ", url='" + url + '\'' +
                '}';
    }
}
