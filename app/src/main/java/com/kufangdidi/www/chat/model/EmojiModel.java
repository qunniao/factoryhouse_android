package com.kufangdidi.www.chat.model;

/**
 * Created by root on 2017/12/19.
 */

public class EmojiModel {
    private int mEventType;
    private String mIconUri;
    private String mContent;

    public int getEventType() {
        return mEventType;
    }

    public void setEventType(int eventType) {
        this.mEventType = eventType;
    }

    public String getIconUri() {
        return mIconUri;
    }

    public void setIconUri(String iconUri) {
        this.mIconUri = iconUri;
    }

    public void setIconUri(int iconUri) {
        this.mIconUri = "" + iconUri;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        this.mContent = content;
    }

    public EmojiModel(int eventType, String iconUri, String content) {
        this.mEventType = eventType;
        this.mIconUri = iconUri;
        this.mContent = content;
    }
    public EmojiModel(int eventType, String content) {
        this.mEventType = eventType;
        this.mContent = content;
    }
    public EmojiModel(String iconUri, String content) {
        this.mIconUri = iconUri;
        this.mContent = content;
    }

    public EmojiModel(String content) {
        this.mContent = content;
    }

    public EmojiModel() { }
}
