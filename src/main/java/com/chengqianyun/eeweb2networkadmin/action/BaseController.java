package com.chengqianyun.eeweb2networkadmin.action;

import com.alibaba.fastjson.JSON;
import com.chengqianyun.eeweb2networkadmin.biz.bean.ElementDataBean;
import com.chengqianyun.eeweb2networkadmin.biz.bean.ParseToken;
import com.chengqianyun.eeweb2networkadmin.biz.bean.Token;
import com.chengqianyun.eeweb2networkadmin.biz.bean.api.ApiResp;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.Area;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.ConsoleLoginAccount;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceDataIntime;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.enums.MenuEnum;
import com.chengqianyun.eeweb2networkadmin.biz.enums.StatusEnum;
import com.chengqianyun.eeweb2networkadmin.core.TokenHelper;
import com.chengqianyun.eeweb2networkadmin.core.utils.BizConstant;
import com.chengqianyun.eeweb2networkadmin.core.utils.BizConstant.ApiCode;
import com.chengqianyun.eeweb2networkadmin.core.utils.HttpSessionUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.PageUtilFactory;
import com.chengqianyun.eeweb2networkadmin.core.utils.StringUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.Tuple2;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


public abstract class BaseController {

	protected Logger log = Logger.getLogger(this.getClass());

//	@Autowired
//	protected SystemParamService systemParamService;
//	@Autowired
//	protected AppCodeService appCodeService;
//
//	@Autowired
//	protected OperationLogService operationLogService;
//
//
//	@Autowired
//	protected InvestService investService;


    final String MESSAGE ="msg";
    final String SUCCESS = "success";
    
    @SuppressWarnings("unused")
	  private String www;

	static Map<Long, List<MenuEnum>> roleMenuMap = new HashMap<Long, List<MenuEnum>>();

  
    @ModelAttribute()
    public void init(ModelMap model) throws Exception {
			model.addAttribute("www", getWww());
			model.addAttribute("applicationUtil", PageUtilFactory.applicationUtil);
			model.addAttribute("dateUtil", PageUtilFactory.dateUtil);
			model.addAttribute("stringUtil", PageUtilFactory.stringUtil);
			model.addAttribute("unitUtil", PageUtilFactory.unitUtil);
			model.addAttribute("platformName", PageUtilFactory.platformName);
//			model.addAttribute("escapeUtil", new BaseEscapeUtil());

			ConsoleLoginAccount account = getLoginAccount();
			if(account != null) {
				Long roleId = account.getRoleId();
				List<MenuEnum> menuList = roleMenuMap.get(getRoleId());
				if(menuList == null) {
					synchronized (this) {
						menuList = roleMenuMap.get(getRoleId());
						if (menuList == null) {
							menuList = new ArrayList<MenuEnum>();
							genMenuList(menuList, roleId);
							roleMenuMap.put(roleId, menuList);
						}
					}
				}
				model.addAttribute("menuList", menuList);
			}
		}

	public String getWww() {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			if (request.getServerName().contains("www")) {
				return "https://" + request.getServerName() + request.getContextPath();
			} else {
				int port = request.getServerPort();
				if ("https".equalsIgnoreCase(request.getScheme())) {
					port = 443;
				}
				return request.getScheme() + "://" + request.getServerName() + ":" + port + request.getContextPath();
			}

		}

	protected void genMenuList(List<MenuEnum> menuList, long roleId) {
		// 在结果集中添加一级类目
		for (MenuEnum menu : MenuEnum.values()) {
			if (menu.isFirstLevel() && menu.hasPermission(roleId)) {
				menu.clearChildren();
				menuList.add(menu);
			}
		}

		// 在结果集中添加二级类目
		for (MenuEnum menu : MenuEnum.values()) {
			if (menu.isSecondLevel() && menu.hasPermission(roleId)) {
				for (MenuEnum firstMenu : menuList) {
					if (firstMenu.getName().equalsIgnoreCase(menu.getParentName())) {
						firstMenu.addChild(menu);
						break;
					}
				}
			}
		}
	}



		public void setWww(String www) {
			this.www = www;
		}


	@InitBinder
	public void init(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));

	}


	protected void addColor(Model model) {
//		model.addAttribute("downLineColor", "#ffde33"); // 黄色
		model.addAttribute("downLineColor", "#039bff");  // 蓝色
		model.addAttribute("lineColor", "#096");					// 绿色
		model.addAttribute("upLineColor", "#cc0033");			// 红色
	}




	protected void addOptMenu(Model model, MenuEnum menuEnum) {
		model.addAttribute("openMenu",menuEnum.getName());
	}


	protected ConsoleLoginAccount getLoginAccount(){
		ConsoleLoginAccount loginSession = HttpSessionUtil.getLoginSession();
		return loginSession;
	}

	protected Long getRoleId() {
		ConsoleLoginAccount loginSession = getLoginAccount();
		return loginSession.getRoleId();
	}

	protected String getLoginName(){
		ConsoleLoginAccount loginSession = getLoginAccount();
		return loginSession.getVcLoginName();
	}

	protected Integer getLoginId(){
		ConsoleLoginAccount loginSession = getLoginAccount();
		return loginSession.getId();
	}


	protected DeviceInfo findOneDeviceId(List<Area> areaList, long deviceId) {
		DeviceInfo deviceInfo = null;
		if (areaList == null) {
			return null;
		}

		for (Area area : areaList) {
			if (area.getDeviceInfoList() == null || area.getDeviceInfoList().size() == 0) {
				continue;
			}
			if (deviceId < 1) {
				deviceInfo = area.getDeviceInfoList().get(0);
//				注意: 这里不能添加area对象 到 deviceInfo, velocity中输出会导致输出的时候 StackOverflowError问题,因为本身存在循环依赖
//				deviceInfo.setArea(area);
				break;
			}

			for (DeviceInfo tmpDeviceInfo : area.getDeviceInfoList()) {
				if (tmpDeviceInfo.getId().longValue() == deviceId) {
					deviceInfo = tmpDeviceInfo;
					//	注意:这里不能添加area对象 到 deviceInfo, velocity中输出会导致输出的时候 StackOverflowError问题,因为本身存在循环依赖
//					deviceInfo.setArea(area);
					return deviceInfo;
				}
			}

		}
		return deviceInfo;
	}

	protected ApiResp defaultApiError(String msg) {
		return defaultApiError(msg, ApiCode.fail);
	}


	protected ApiResp defaultApiError(String msg, int code) {
		ApiResp resp = new ApiResp();
		resp.setCode(code);
		resp.setMsg(StringUtil.isEmpty(msg) ? "系统异常" : msg);
		return resp;
	}

	protected Tuple2<Boolean, ApiResp> checkToken(String token) {
		ParseToken parseToken = TokenHelper.parseToken(token);
		if (parseToken == null) {
			return new Tuple2<Boolean, ApiResp>(false, defaultApiError("token校验失败", ApiCode.check_fail));
		}
		if (parseToken.isExpire()) {
			return new Tuple2<Boolean, ApiResp>(false, defaultApiError("token过期", ApiCode.token_expire));
		}
		return new Tuple2<Boolean, ApiResp>(true, null);
	}


//	protected OperationLog convertOperationLog(String name,HttpServletRequest request,Object object,OperationResultEnum resultEnum,OperationTypeEnum operationTypeEnum){
//		OperationLog operationLog = new OperationLog();
//		operationLog.setName(name);
//		operationLog.setOccurTime(DateUtil.getCurrentDate());
//		operationLog.setEventType(operationTypeEnum.getCode());
//		operationLog.setOpeResult(resultEnum.getCode());
//		operationLog.setReqParams(JSON.toJSONString(object));
//		operationLog.setDeviceInfo(request.getHeader("User-Agent"));
//		operationLog.setIp(getRemoteHost(request));
//		return operationLog;
//	}

	 private String getRemoteHost(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
	}
}
