package com.chengqianyun.eeweb2networkadmin.biz.entitys;

import com.chengqianyun.eeweb2networkadmin.core.utils.StringUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Area {

    private Long id;

    private String name;

    private String note;

    private String smsPhones;

    private Date createdAt;

    private Date updatedAt;

    private String createdBy;

    private String updatedBy;


    // ==============  扩展属性  =================

    private List<DeviceInfo> deviceInfoList;


    // ==============  扩展方法  =================

    public void optSmsPhones() {
        if (smsPhones == null) {
            return;
        }
        smsPhones = smsPhones.trim().replaceAll("，", ",");
        String[] smsPhoneArray = smsPhones.split(",");
        for (int i = 0; i < smsPhoneArray.length; i++) {
            smsPhoneArray[i] = smsPhoneArray[i].trim();
        }

        smsPhones = StringUtil.assemble(smsPhoneArray, ",");
    }

    public void addDeviceInfo(DeviceInfo deviceInfo) {
        if (deviceInfoList == null) {
            deviceInfoList = new ArrayList<DeviceInfo>();
        }
        deviceInfoList.add(deviceInfo);
    }


}