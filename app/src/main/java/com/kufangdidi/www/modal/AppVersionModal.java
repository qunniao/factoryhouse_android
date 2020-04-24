package com.kufangdidi.www.modal;

public class AppVersionModal {
    private int id;
    private String name;//名称，
    private String zh_name;//中文名
    private String type;//类型
    private String uri;//二维码跳转的页面路径
    private String android_uri;//安卓下载地址
    private String ios_uri;//ios下载地址
    private String intro;//项目介绍
    private int status;//状态
    private String android_version;//安卓版本
    private String ios_version;//ios版本
    private String content;//更新内容

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getZh_name() {
        return zh_name;
    }
    public void setZh_name(String zh_name) {
        this.zh_name = zh_name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getUri() {
        return uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }
    public String getIntro() {
        return intro;
    }
    public void setIntro(String intro) {
        this.intro = intro;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getAndroid_uri() {
        return android_uri;
    }
    public void setAndroid_uri(String android_uri) {
        this.android_uri = android_uri;
    }
    public String getIos_uri() {
        return ios_uri;
    }
    public void setIos_uri(String ios_uri) {
        this.ios_uri = ios_uri;
    }
    public String getAndroid_version() {
        return android_version;
    }
    public void setAndroid_version(String android_version) {
        this.android_version = android_version;
    }
    public String getIos_version() {
        return ios_version;
    }
    public void setIos_version(String ios_version) {
        this.ios_version = ios_version;
    }


}
