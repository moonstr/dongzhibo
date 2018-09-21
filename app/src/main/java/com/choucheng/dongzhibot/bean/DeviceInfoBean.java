package com.choucheng.dongzhibot.bean;

/**
 * Created by admin on 2018/7/17.
 */

public class DeviceInfoBean extends BaseBean<DeviceInfoBean.DeviceInfoData> {

    public class DeviceInfoData {
        public String pos_no;
        public String device_id;
        public String device_type;
        public String pos_fac;
        public String bank;
        public String mark;
        public String status;
        public String is_over;
        public String name;
        public String address;
        public String link_phone;
        public String link_name;
        public String merchant_no;
        public String xunjian_time;


// "pos_no": "44444", //设备编号
//         "device_id": "2",  //设备id
//         "pos_fac": "5512312",  //设备型号
//         "bank": "比利时银行", //银行
//         "mark": "巴西", //备注
//         "status": "0",  //'0未审核1审核中2已审核3审核不通过',
//         "is_over": "4",    // 0未接受1已接受2拒绝接受3装机中4装机完成
//         "name": "李一",     //商户名字
//         "address": "武汉江夏", //地点
//         "link_phone": "13632720361", //电话
//         "link_name": "李一一", //联系人
//         "merchant_no": "123456" //商户注册编码
    }
}
