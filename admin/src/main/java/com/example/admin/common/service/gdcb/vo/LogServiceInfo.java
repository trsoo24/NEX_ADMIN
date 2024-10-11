package com.example.admin.common.service.gdcb.vo;

public class LogServiceInfo {

    private String seq = "";
    private String url = "";
    private String method = "";
    private String description = "";

    // SERVICE 관련 로그 추가
    private String umk = "";
    private String handler = "";
    private String class_name = "";
    private String elapsed_time = "";

    public String getSeq() {
        return seq;
    }
    public void setSeq(String seq) {
        this.seq = seq;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getUmk() {
        return umk;
    }
    public void setUmk(String umk) {
        this.umk = umk;
    }
    public String getHandler() {
        return handler;
    }
    public void setHandler(String handler) {
        this.handler = handler;
    }
    public String getClass_name() {
        return class_name;
    }
    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }
    public String getElapsed_time() {
        return elapsed_time;
    }
    public void setElapsed_time(String elapsed_time) {
        this.elapsed_time = elapsed_time;
    }

}