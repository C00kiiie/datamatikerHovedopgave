package com.example.hovedopgave.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Offer implements Serializable {

    private String picture;
    private String buttonUrl;
    private String textNew;
    private String textInfo;

    public Offer(String picture, String buttonUrl, String textNew, String textInfo) {
        this.picture = picture;
        this.buttonUrl = buttonUrl;
        this.textNew = textNew;
        this.textInfo = textInfo;
    }

    public String getTextInfo(){
        return textInfo;
    }

    public String getTextNew() {
        return textNew;
    }

    public void setTextNew(String textNew) {
        this.textNew = textNew;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getButtonUrl() {
        return buttonUrl;
    }

    public void setButtonUrl(String buttonUrl) {
        this.buttonUrl = buttonUrl;
    }

}
