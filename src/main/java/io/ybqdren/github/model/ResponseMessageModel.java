package io.ybqdren.github.model;

/**
 * Created by Zhao Wen on 2021/2/21
 */

public class ResponseMessageModel {
    private String recContent;
    private int sendStatus;

    public String getRecContent() {
        return recContent;
    }

    public void setRecContent(String recContent) {
        this.recContent = recContent;
    }

    public int getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(int sendStatus) {
        this.sendStatus = sendStatus;
    }
}
