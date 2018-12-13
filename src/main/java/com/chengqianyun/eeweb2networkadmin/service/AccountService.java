package com.chengqianyun.eeweb2networkadmin.service;

import com.chengqianyun.eeweb2networkadmin.biz.entitys.ConsoleLoginAccount;
import com.chengqianyun.eeweb2networkadmin.biz.enums.RoleEnum;
import com.chengqianyun.eeweb2networkadmin.core.utils.SHAUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.StringUtil;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/12/11
 */
@Service
@Slf4j
public class AccountService extends BaseService {

  public List<ConsoleLoginAccount> getAccountList(String userName, String type) {
    List<ConsoleLoginAccount> list = consoleLoginAccountMapper.findAll();
    if (!StringUtil.isEmpty(userName)) {
      for (int i = 0; i < list.size(); ) {
        if (list.get(i).getVcLoginName().indexOf(userName) >= 0) {
          i++;
          continue;
        }
        list.remove(i);
      }
    }

    if (!StringUtil.isEmpty(type)) {
      int roleId = Integer.parseInt(type);
      for (int i = 0; i < list.size(); ) {
        if (list.get(i).getRoleId() == roleId) {
          i++;
          continue;
        }
        list.remove(i);
      }
    }

    return list;
  }

  public synchronized void addAccount(ConsoleLoginAccount account) {
    ConsoleLoginAccount fromDB = consoleLoginAccountMapper.selectByName(account.getVcLoginName());
    if (fromDB != null) {
      throw new RuntimeException("当前账号已经存在了");
    }

    account.setVcLoginPassword(SHAUtil.encode("cql123456"));
    if (account.getRoleId() != RoleEnum.NORMAL.getRoleId() && account.getRoleId() != RoleEnum.MANAGER.getRoleId()) {
      throw new RuntimeException("当前参数错误");
    }
    consoleLoginAccountMapper.save(account);
  }

  public ConsoleLoginAccount getAccount(Integer id) {
    ConsoleLoginAccount account = consoleLoginAccountMapper.findById(id);
    if(account == null) {
      throw new RuntimeException("当前账号已经不存在");
    }
    return  account;
  }

  public void updateAccount(ConsoleLoginAccount account) {
    ConsoleLoginAccount fromDB = consoleLoginAccountMapper.findById(account.getId());
    if (fromDB == null) {
      throw new RuntimeException("当前账户信息不存在了");
    }
    if (account.getRoleId() != RoleEnum.NORMAL.getRoleId() && account.getRoleId() != RoleEnum.MANAGER.getRoleId()) {
      throw new RuntimeException("当前参数错误");
    }
    fromDB.setIValid(account.getIValid());
    fromDB.setVcRealName(account.getVcRealName());
    fromDB.setRoleId(account.getRoleId());
    consoleLoginAccountMapper.update(fromDB);
  }

  public void initPsw(Integer id) {
    ConsoleLoginAccount fromDB = consoleLoginAccountMapper.findById(id);
    if (fromDB == null) {
      throw new RuntimeException("当前账户信息不存在了");
    }
    fromDB.setVcLoginPassword(SHAUtil.encode("cql123456"));
    consoleLoginAccountMapper.update(fromDB);
  }

  public void deleteAccount(Integer id) {
    consoleLoginAccountMapper.delete(id);
  }
}