package com.chengqianyun.eeweb2networkadmin.action;


import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 聂鹏
 * @version 1.0
 * @email lsb@51huadian.cn
 * @date 18/4/9
 */
@Controller
@RequestMapping("/intime")
public class IntimeController extends BaseController {


  public void dataList() {

  }

  @RequestMapping( value = "/dataList", method = RequestMethod.GET)
  public String list(
      @RequestParam(value = "queryData", required = false) Map<String, String> queryData,
      @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,
      @RequestParam(value = "pageSize", required = false, defaultValue = "15") int pageSize,
      Model model) {
    try {
//      PaginationQuery query = new PaginationQuery();
//      if (queryData == null) {
//        queryData = new HashMap<>();
//      }
//      ;
//      query.setPageIndex(pageIndex);
//      query.setRowsPerPage(pageSize);
//      query.setQueryData(queryData);
//      PageResult<UserApply> params = userApplyService.getParamsList(query);
//      model.addAttribute("result", params);
//      model.addAttribute("queryData", queryData);
      return "/intime/dataList";
    } catch (Exception ex) {
      model.addAttribute(SUCCESS, false);
      model.addAttribute(MESSAGE, ex.getMessage());
      log.error(ex);
      return "/intime/dataList";
    }
  }

}