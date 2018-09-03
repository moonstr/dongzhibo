package com.choucheng.dongzhibot.bean;

/**
 * Created by admin on 2018/6/12.
 */

public class BaseBean<T> {
    public BeanStatus status;
    public T data;
    public class BeanStatus{
        public String msg;
        public String code;
    }

}
