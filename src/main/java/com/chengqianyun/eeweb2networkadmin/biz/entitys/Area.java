package com.chengqianyun.eeweb2networkadmin.biz.entitys;

import java.util.Date;
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

}