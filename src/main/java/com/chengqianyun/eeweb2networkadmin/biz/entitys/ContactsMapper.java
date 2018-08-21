package com.chengqianyun.eeweb2networkadmin.biz.entitys;

import com.chengqianyun.eeweb2networkadmin.biz.entitys.Contacts;
import java.util.List;
import java.util.Map;

/**
* Created by Mybatis Generator on 2018/08/21
*/
public interface ContactsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Contacts record);

    Contacts selectByPrimaryKey(Long id);

    List<Contacts> selectAll();

    int updateByPrimaryKey(Contacts record);

    Integer findPageCount(Map<String,String> map);

    List<Contacts> findPage(Map<String,String> map);

    Contacts selectByPhone(String phone);

}