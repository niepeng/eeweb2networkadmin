package com.chengqianyun.eeweb2networkadmin.service;


import com.chengqianyun.eeweb2networkadmin.biz.entitys.Contacts;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.SendContacts;
import com.chengqianyun.eeweb2networkadmin.biz.page.PageResult;
import com.chengqianyun.eeweb2networkadmin.biz.page.PaginationQuery;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 联系分
 * @author 聂鹏
 * @version 1.0
 * @date 18/8/21
 */
@Service
@Slf4j
public class ContactService extends BaseService {


  public List<Contacts> getAll() {
    return contactsMapper.selectAll();
  }

  public PageResult<Contacts> getContactList(PaginationQuery query) {
    PageResult<Contacts> result = null;
    try {

      Integer count = contactsMapper.findPageCount(query.getQueryData());

      if (null != count && count.intValue() > 0) {
        int startRecord = (query.getPageIndex() - 1) * query.getRowsPerPage();
        query.addQueryData("startRecord", Integer.toString(startRecord));
        query.addQueryData("endRecord", Integer.toString(query.getRowsPerPage()));
        List<Contacts> list = contactsMapper.findPage(query.getQueryData());
        result = new PageResult<Contacts>(list, count, query);
      }
    } catch (Exception e) {
      log.error("ContactService.getContactList,Error", e);
    }
    return result;
  }

  public void addContacts(Contacts contacts) {
    Contacts fromDB = contactsMapper.selectByPhone(contacts.getPhone());
    if(fromDB != null) {
      throw new RuntimeException("当前手机号码已经存在,不能添加");
    }
    contactsMapper.insert(contacts);
  }

  public Contacts getContactsById(long id) {
    return contactsMapper.selectByPrimaryKey(id);
  }

  public void updateContacts(Contacts contacts) {
    Contacts fromDB = contactsMapper.selectByPhone(contacts.getPhone());
    if(fromDB != null && fromDB.getId().longValue() != contacts.getId().longValue()) {
      throw new RuntimeException("当前手机号码已经存在,不能添加");
    }

    Contacts f = contactsMapper.selectByPrimaryKey(contacts.getId());
    f.setPhone(contacts.getPhone());
    f.setName(contacts.getName());
    f.setNote(contacts.getNote());
    contactsMapper.updateByPrimaryKey(f);

  }

  public void deleteContacts(long id) {
    contactsMapper.deleteByPrimaryKey(id);
  }


  public PageResult<SendContacts> getSendContactsList(PaginationQuery query) {
    PageResult<SendContacts> result = null;
    try {

      Integer count = sendContactsMapper.findPageCount(query.getQueryData());

      if (null != count && count.intValue() > 0) {
        int startRecord = (query.getPageIndex() - 1) * query.getRowsPerPage();
        query.addQueryData("startRecord", Integer.toString(startRecord));
        query.addQueryData("endRecord", Integer.toString(query.getRowsPerPage()));
        List<SendContacts> list = sendContactsMapper.findPage(query.getQueryData());
        result = new PageResult<SendContacts>(list, count, query);
      }
    } catch (Exception e) {
      log.error("ContactService.getSendContactsList,Error", e);
    }
    return result;
  }

}