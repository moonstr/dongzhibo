package com.choucheng.dongzhibot.bean;

public class ImagePic {
    private String name;
    private int picNum;

    public ImagePic(String name, int picNum) {
        this.name = name;
        this.picNum = picNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPicNum() {
        return picNum;
    }

    public void setPicNum(int picNum) {
        this.picNum = picNum;
    }
}
