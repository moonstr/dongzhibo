package com.choucheng.dongzhibot.bean;

import java.util.ArrayList;
import java.util.List;

public class HistoryListBean extends BaseBean<HistoryListBean.HistoryListItem> {
    public class HistoryListItem {
        public List<HistoryList> list;
        public Paging paging;

        public class HistoryList {
            public String id;
            public String start_time;
            public String name;
            public String address;
            public String link_phone;
            public String link_name;
            public String merchant_id;
            public String status;
            public String is_over;
            public String add_time;
            public String is_set;
            public String xunjianid;
            public String d_id;
            public String device_id;

        }

        public class Paging {
            public String page;//当前页码
            public String totalcount;//总共数据条数
            public String numberofpage;//每个页面最多有好多条数
        }
    }
}
