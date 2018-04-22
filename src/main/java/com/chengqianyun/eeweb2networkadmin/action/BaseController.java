package com.chengqianyun.eeweb2networkadmin.action;

import com.alibaba.fastjson.JSON;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.Area;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.ConsoleLoginAccount;
import com.chengqianyun.eeweb2networkadmin.biz.entitys.DeviceInfo;
import com.chengqianyun.eeweb2networkadmin.biz.enums.MenuEnum;
import com.chengqianyun.eeweb2networkadmin.core.utils.HttpSessionUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.PageUtilFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
    
  
    @ModelAttribute()
    public void init(ModelMap model) throws Exception {
			model.addAttribute("www",getWww());
			model.addAttribute("applicationUtil", PageUtilFactory.applicationUtil);
			model.addAttribute("dateUtil", PageUtilFactory.dateUtil);
			model.addAttribute("stringUtil", PageUtilFactory.stringUtil);
			model.addAttribute("unitUtil", PageUtilFactory.unitUtil);
//			model.addAttribute("escapeUtil", new BaseEscapeUtil());
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

		public void setWww(String www) {
			this.www = www;
		}


	@InitBinder
	public void init(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));

	}

	protected void addOptMenu(Model model, MenuEnum menuEnum) {
		model.addAttribute("openMenu",menuEnum.getName());
	}


	protected ConsoleLoginAccount getLoginAccount(){
		ConsoleLoginAccount loginSession = HttpSessionUtil.getLoginSession();
		return loginSession;
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
