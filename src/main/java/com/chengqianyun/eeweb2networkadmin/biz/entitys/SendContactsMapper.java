package com.chengqianyun.eeweb2networkadmin.biz.entitys;

import com.chengqianyun.eeweb2networkadmin.biz.entitys.SendContacts;
import java.util.List;
import java.util.Map;

/**
* Created by Mybatis Generator on 2018/08/21
*/
public interface SendContactsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SendContacts record);

    int insertForImport(SendContacts record);

    SendContacts selectByPrimaryKey(Long id);

    List<SendContacts> selectAll();

    int updateByPrimaryKey(SendContacts record);

    Integer findPageCount(Map<String,String> map);

    List<SendContacts> findPage(Map<String,String> map);

    int deleteForExport();
}