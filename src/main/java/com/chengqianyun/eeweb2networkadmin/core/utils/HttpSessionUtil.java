package com.chengqianyun.eeweb2networkadmin.core.utils;

import com.chengqianyun.eeweb2networkadmin.biz.entitys.ConsoleLoginAccount;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * 把用户对象放入session和从session获取用户对象
 */
public class HttpSessionUtil
{

	/**
	* 把用户对象放入session
	*/
	public static void setLoginSession(ConsoleLoginAccount account)
	{
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session = request.getSession(true);
		session.setAttribute("loginSessionInfo", account);
	}
	
	/**
	 * 从session中获取用户对象
	 * @return ConsoleLoginAccount
	 */
	public static ConsoleLoginAccount getLoginSession()
	{
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session = request.getSession();
        if(session == null) {
        	return null;	
        }
        ConsoleLoginAccount account = (ConsoleLoginAccount) session.getAttribute("loginSessionInfo");
		return account;
	}
	
	/**
     * 清除当前用户
     */
    public static void removeLoginSession() {
    	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		  HttpSession session = request.getSession();
			session.removeAttribute("loginSessionInfo");
			session.invalidate();
    }
    
   
	public static void setResourceSession(List<String> resources)
	{
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session = request.getSession(true);
		session.setAttribute("resourceSessionInfo", resources);
	}
	
	
	@SuppressWarnings("unchecked")
	public static List<String> getResourceSession()
	{
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session = request.getSession();
        if(session == null) {
        	return null;	
        }
        List<String> resources = (List<String>) session.getAttribute("resourceSessionInfo");
		return resources;
	}
	
	
    public static void removeResourceSession() {
    	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session = request.getSession();
        session.removeAttribute("resourceSessionInfo");
    }
    
    
    public static void setDynamicValidateCodeSession(String code)
   	{
   		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
   		HttpSession session = request.getSession(true);
   		session = request.getSession();
   		session.setAttribute("dynamicValidateCode", code);
   	}
   	
   	
   	public static String getDynamicValidateCodeSession()
   	{
   		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
   		HttpSession session = request.getSession();
           if(session == null) {
           	return null;	
           }
           String code = (String) session.getAttribute("dynamicValidateCode");
   		return code;
   	}
   	public static void removeDynamicValidateCodeSession() {
       	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
   		HttpSession session = request.getSession();
           session.removeAttribute("dynamicValidateCode");
       }

}
