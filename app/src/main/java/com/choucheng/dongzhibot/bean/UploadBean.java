package com.choucheng.dongzhibot.bean;

import java.util.ArrayList;

/**
 * Created by admin on 2018/7/18.
 */

public class UploadBean extends BaseBean<ArrayList<UploadBean.UploadData>>{

    public class UploadData{
        public String addtime;
        public String path;
        public String newname;
        public String oldname;
        public String ext;
        public String size;
        public String filetype;
    }
}
