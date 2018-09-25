package com.choucheng.dongzhibot.bean;

import java.util.ArrayList;

/**
 * Created by admin on 2018/6/13.
 */

public class InstallOrderBean extends BaseBean<InstallOrderBean.InstallOrder> {
    public static class InstallOrder {

        public ArrayList<InstallOrderItem> zhuangji_lists;
        public InstallOrderPaging paging;

        public static class InstallOrderItem {
            public String pos_no;//商户编码
            public String device_id;//装机id
            public String device_type;//设备型号
            public String bank;//终端类型
            public String mark;//备注
            public String status;// status 0未审核 1审核中 2审核成功 3审核失败
            public String is_over;//is_over 0未接受1已接受2拒绝接受3装机中4装机完成
            public String name;//商户名称
            public String address;//商户地址
            public String link_phone;//联系人电话
            public String link_name;//联系人姓名
            public String merchant_id;//用户id
//            "pos_no": "123123",
//                    "device_id": "1",
//                    "pos_fac": "123123",
//                    "bank": "对对对",
//                    "mark": "范围分为",
//                    "status": "0",
//                    "is_over": "0",
//                    "name": "123",
//                    "address": "成都市高新区高新管委会",
//                    "tel": "0"


//            "pos_no": "123123", //pos设备编号
//                    "device_id": "1",     // 设备id
//                    "pos_fac": "123123",  //pos厂家设备型号
//                    "bank": "对对对",   //归属支行
//                    "mark": "范围分为",  //备注
//                    "status": "1",  //0未审核1审核中2已审核3审核不通过
//                    "is_over": "1", //0未接受1已接受2拒绝接受3装机中4装机完成
//                    "name": "123",  //商户名称
//                    "address": "高新区管委会", //商户地址
//                    "tel": "18688888888",  //联系电话
//                    "link_name": "探戈",  //联系人
//                    "merchant_id": "1"   //商户id

        }

        public class InstallOrderPaging {
            public String page;//页码
            public String totalcount;//总数
            public String numberofpage;//每页数量
//             "page": "1",
//                     "totalcount": "1",
//                     "numberofpage": "10"
        }
    }
}
