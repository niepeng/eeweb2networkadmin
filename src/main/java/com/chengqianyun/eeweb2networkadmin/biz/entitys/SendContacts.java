package com.chengqianyun.eeweb2networkadmin.biz.entitys;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
* Created by Mybatis Generator on 2018/08/21
*/
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendContacts {
    /**
     * 主键
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * type: phone==电话, sms == 短信
     */
    private String type;

    /**
     * 短信内容
     */
    private String smsContent;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 修改时间
     */
    private Date updatedAt;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 修改人
     */
    private String updatedBy;
}