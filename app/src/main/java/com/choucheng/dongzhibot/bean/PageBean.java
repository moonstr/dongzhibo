package com.choucheng.dongzhibot.bean;

public class PageBean {
    private String page;
    private boolean isChecked;

    public PageBean(String page, boolean isChecked) {
        this.page = page;
        this.isChecked = isChecked;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
