package com.chengqianyun.eeweb2networkadmin.biz.entitys;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public interface ConsoleLoginAccountMapper {

    List<ConsoleLoginAccount> findAll();

    List<ConsoleLoginAccount> selectAll();

    ConsoleLoginAccount selectByName(String loginName);

    ConsoleLoginAccount findById(int id);

    int save(ConsoleLoginAccount account);

    int insertByImport(ConsoleLoginAccount account);

    int update(ConsoleLoginAccount account);

    int queryAccountsCount(Map<String, String> map);

    void delete(int id);

    int updatePassword(ConsoleLoginAccount account);

    int updateLockTime(ConsoleLoginAccount account);


    List<ConsoleLoginAccount> queryAccounts(Map<String, String> map);
    
    /**
     * 查询当前用户的审核权限
     */
    public List<Integer> queryCurrentUserAudit(Integer id);
    
    /**
     * 查询所有有效的管理端平台账户信息
     */
    public List<ConsoleLoginAccount> queryConsoleLoginAccountAll();

    int deleteForExport();
    
}