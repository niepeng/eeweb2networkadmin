package com.chengqianyun.eeweb2networkadmin.biz.entitys;

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

  /**
   * t_send_contacts表的id,用来关联联系人,逗号分隔(报警的使用)
   */
  private String contactsIds;

    private Date createdAt;

    private Date updatedAt;

    private String createdBy;

    private String updatedBy;


    // ==============  扩展属性  =================

    private List<DeviceInfo> deviceInfoList;

    private List<Contacts> contactsList;

    private int normalNum;
    private int alarmNum;
    private int offlineNum;

    // ==============  扩展方法  =================

    public void addDeviceInfo(DeviceInfo deviceInfo) {
        if (deviceInfoList == null) {
            deviceInfoList = new ArrayList<DeviceInfo>();
        }
        deviceInfoList.add(deviceInfo);
    }

    public boolean hasAlarm() {
      return alarmNum > 0;
    }

    public boolean hasOffline() {
      return offlineNum > 0;
    }

    public boolean hasNormal() {
      return normalNum > 0;
    }

    public boolean hasNum() {
      return normalNum + alarmNum + offlineNum > 0;
    }

}