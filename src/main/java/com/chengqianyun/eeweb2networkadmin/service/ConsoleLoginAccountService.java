package com.chengqianyun.eeweb2networkadmin.service;


import com.chengqianyun.eeweb2networkadmin.biz.entitys.ConsoleLoginAccount;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.ConsoleLoginAccountMapper;
import com.chengqianyun.eeweb2networkadmin.core.utils.AdminConstant;
import com.chengqianyun.eeweb2networkadmin.core.utils.SHAUtil;
import java.util.Date;
import java.util.List;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/3/13
 */
@Service
public class ConsoleLoginAccountService extends BaseService {

  @Autowired
  private ConsoleLoginAccountMapper consoleLoginAccountMapper;

  public ConsoleLoginAccount getLoginAccount(String loginName) {
    return consoleLoginAccountMapper.selectByName(loginName);
  }


  public int save(ConsoleLoginAccount account) throws Exception {
    if (StringUtils.isBlank(account.getVcLoginName())) {
      throw new Exception("登录账号不能为空");
    }
    if (StringUtils.isBlank(account.getVcPhone())) {
      throw new Exception("手机号不能为空");
    }
    ConsoleLoginAccount loginAccount = consoleLoginAccountMapper
        .selectByName(account.getVcLoginName());
    if (loginAccount != null) {
      throw new Exception("登录账号已经存在");
    }
    account.setVcLoginPassword(SHAUtil.encode(account.getVcLoginPassword()));
//    account.setiValid(1);
    account.setDtCreate(new Date());
    int id = consoleLoginAccountMapper.save(account);
    return id;
  }


  public int update(ConsoleLoginAccount account) throws Exception {
    if (StringUtils.isBlank(account.getVcLoginName())) {
      throw new Exception("账号不能为空！");
    }
    if (StringUtils.isBlank(account.getVcPhone())) {
      throw new Exception("手机号不能为空！");
    }

    // 根据账号查询
    ConsoleLoginAccount loginAccount = consoleLoginAccountMapper.selectByName(account.getVcLoginName());
    if (loginAccount != null && loginAccount.getId().intValue() != account.getId()) {
      throw new Exception("账号已经存在！");
    }
    account.setDtModify(new Date());
    int id = consoleLoginAccountMapper.update(account);
    return id;
  }

  public void updatepassword(ConsoleLoginAccount account) throws Exception {
    consoleLoginAccountMapper.updatePassword(account);
  }

  public void updateLockTime(ConsoleLoginAccount account)  {
    consoleLoginAccountMapper.updateLockTime(account);
  }


  public void delete(int id) {
    consoleLoginAccountMapper.delete(id);
  }

  public ConsoleLoginAccount findById(int id) throws Exception {
    ConsoleLoginAccount consoleLoginAccount = consoleLoginAccountMapper
        .findById(id);
    if (consoleLoginAccount == null) {
      throw new Exception(AdminConstant.MESSAGE_RECORD_NOT_EXIST + " id ="+ id);
    }
    return consoleLoginAccount;
  }



//  public PageResult<ConsoleLoginAccount> getAccountList(PaginationQuery query) {
//    PageResult<ConsoleLoginAccount> result = null;
//    try {
//      Integer count = consoleLoginAccountMapper.queryAccountsCount(query
//          .getQueryData());
//      if (null != count && count.intValue() > 0) {
//        int startRecord = (query.getPageIndex() - 1)
//            * query.getRowsPerPage();
//        query.addQueryData("startRecord", Integer.toString(startRecord));
//        query.addQueryData("endRecord",
//            Integer.toString(query.getRowsPerPage()));
//        List<ConsoleLoginAccount> list = consoleLoginAccountMapper
//            .queryAccounts(query.getQueryData());
//
//        if(list != null) {
//          for(ConsoleLoginAccount tmp : list) {
//            tmp.setAdminRole(adminRoleMapper.selectByPrimaryKey(tmp.getRoleId() != null ? tmp.getRoleId() : 0));
//          }
//        }
//
//        result = new PageResult<ConsoleLoginAccount>(list, count, query);
//      }
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//    return result;
//  }

  /**
   * 查询当前用户的审核权限
   *
   * @return
   */
  public List<Integer> queryCurrentUserAudit(Integer id) {
    List<Integer> list = null;
    list = consoleLoginAccountMapper.queryCurrentUserAudit(id);
    return list;
  }

  /**
   * 查询所有有效的管理端平台账户信息
   *
   * @return
   */
  public List<ConsoleLoginAccount> queryConsoleLoginAccountAll() {
    List<ConsoleLoginAccount> list = null;
    list = consoleLoginAccountMapper.queryConsoleLoginAccountAll();
    return list;
  }

  public int resetPwd(String id) throws Exception{
//    ConsoleLoginAccount account = this.findById(Integer.parseInt(id));
//    String pwd = RandomUtil.produceStringAndNumber(HdConstant.MIN_PASSWORD_LENGTH);
//    account.setVcLoginPassword(SHAUtil.encode(pwd));
//    int flag = consoleLoginAccountMapper.updatePassword(account);
    int flag = 1;
    return flag;
  }

  // 初始化用户角色 权限
  public int initAuth(ConsoleLoginAccount consoleLoginAccount) {
    //角色
//    Long roleId = consoleLoginAccount.getRoleId();
//    AdminRole adminRole = adminRoleMapper.selectByPrimaryKey(roleId);
//    consoleLoginAccount.setAdminRole(adminRole);
//    //获取所有权限连接
//    String allAuthUrls = adminAuthMapper.selectAllAuthUrls();
//    consoleLoginAccount.setAllAuthUrls(allAuthUrls);
//    //权限代码和权限链接
//    if(adminRole!=null){
//      String authCodes = adminRoleAuthMapper.selectAuthCodesbyRoleId(consoleLoginAccount.getRoleId());
//      consoleLoginAccount.setAuthCodes(authCodes);
//      String authUrls = adminRoleAuthMapper.selectAuthUrlsbyRoleId(consoleLoginAccount.getRoleId());
//      consoleLoginAccount.setAuthUrls(authUrls);
//    }
    return 0;
  }
}
