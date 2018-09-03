package com.choucheng.dongzhibot.bean;

/**
 * Created by admin on 2018/7/1.
 */

public class InstallOrderInfoBean extends BaseBean<InstallOrderInfoBean.InstallOrderInfoData> {
    public class InstallOrderInfoData {
        public String pos_no;
        public String device_id;

        public String device_type;
        public String bank;
        public String mark;
        public String status;
        public String is_over;
        public String name;
        public String address;
        public String link_phone;
        public String link_name;
        public String merchant_id;
        public String merchant_no;
        public String iem;

//         "pos_no": "123123",
//                 "device_id": "1",
//                 "pos_fac": "123123",
//                 "bank": "对对对",
//                 "mark": "范围分为",
//                 "status": "0",
//                 "is_over": "0",
//                 "name": "123",
//                 "address": "成都市高新区高新管委会",
//                 "tel": "0",
//                 "merchant_id": "1"
    }
}
