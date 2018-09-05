package com.choucheng.dongzhibot.bean;

import java.util.ArrayList;

/**
 * Created by admin on 2018/6/13.
 */

public class ProtectOrderBean extends BaseBean<ProtectOrderBean.ProtectOrder> {
    public class ProtectOrder {

        public ArrayList<ProtectOrderItem> yunwei_lists;
        public ProtectOrderPaging paging;

        public class ProtectOrderItem {
            public String xunjianid;//查询id1
            public String merchant_id;//查询id2
            public String yunwei_id;//
            public String id;//
            public String yunwei_status;//0未审核 1审核中 2审核成功 3审核失败
            public String yunwei_over;//0未接受1已接受2拒绝接受3装机中4装机完成
            public String odd_number;
            public String d_id;
            public String start_time;
            public String end_time;
            public String is_question;
            public String x_status;
            public String status;
            public MerchantInfo merchant_info;
            public String yunwei_info;

            public class MerchantInfo {
                public String name;
                public String address;
            }

            @Override
            public String toString() {
                return "ProtectOrderItem{" +
                        "xunjianid='" + xunjianid + '\'' +
                        ", merchant_id='" + merchant_id + '\'' +
                        ", yunwei_id='" + yunwei_id + '\'' +
                        ", id='" + id + '\'' +
                        ", yunwei_status='" + yunwei_status + '\'' +
                        ", yunwei_over='" + yunwei_over + '\'' +
                        ", odd_number='" + odd_number + '\'' +
                        ", d_id='" + d_id + '\'' +
                        ", start_time='" + start_time + '\'' +
                        ", end_time='" + end_time + '\'' +
                        ", is_question='" + is_question + '\'' +
                        ", x_status='" + x_status + '\'' +
                        ", status='" + status + '\'' +
                        ", merchant_info=" + merchant_info +
                        ", yunwei_info='" + yunwei_info + '\'' +
                        '}';
            }
            //  "xunjianid": "1",        巡检的id
//                "merchant_id": "1",   商户id
//                "yunwei_id": "114",     维护人员的id
//                "yunwei_status": "0",  0未审核1审核中 2审核通过 3审核未通过
//                "yunwei_over": "0",  0未接受1已接受2拒绝接受3维护中4完成
//                "odd_number": "15081923049696",   巡检单号
//                "d_id": "1",    设备id
//                "start_time": "1439999868",
//                        "end_time": "1440000463",
//                        "is_question": "1",  1有问题 2没问题
//                "x_status": "2",   1巡检中 2 巡检结束
//                "status": "1", 1有效2 无效3删除
//                "merchant_info": {
//                "address": "天府大道",  商户地址
//                "name": "家乐福超市"   商户名字
//            }
        }

        public class ProtectOrderPaging {
            public String page;
            public String totalcount;
            public String numberofpage;
//             "page": "1",
//                     "totalcount": "1",
//                     "numberofpage": "10"
        }
    }
}
