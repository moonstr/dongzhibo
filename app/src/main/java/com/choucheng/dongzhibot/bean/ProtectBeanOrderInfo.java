package com.choucheng.dongzhibot.bean;

/**
 * Created by admin on 2018/7/17.
 */

public class ProtectBeanOrderInfo extends BaseBean<ProtectBeanOrderInfo.ProtectBeanOrderInfoData> {

    public class ProtectBeanOrderInfoData {
        public String merchant_no;//商户编码
        public String link_name;//联系人
        public String link_phone;//联系电话
        public String address;//地址
        public String name;//name
        public String pos_no;//机具编号
        public String service_mark;//服务需求描述
        public String odd_number;//添加时间
        public String iem;//pos编码
        public String device_type;//终端类型

    }

//     "merchant_no": "423", //商户编号
//             "link_name": "探戈",
//             "link_phone": "13277777777",
//             "address": "高新区管委会",
//             "name": "123",
//             "pos_no": "123123",
//             "service_mark": "服务需求描述"  //服务描述
//             "odd_number": "15081923049696"   //巡检单号
}
