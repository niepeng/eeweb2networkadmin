package com.chengqianyun.eeweb2networkadmin.core.interceptor;

import com.chengqianyun.eeweb2networkadmin.biz.entitys.ConsoleLoginAccount;
import com.chengqianyun.eeweb2networkadmin.core.utils.AuthUtil;
import com.chengqianyun.eeweb2networkadmin.core.utils.HttpSessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 拦截器，拦截一些想要的数据，例如：用户访问路径等。
 */
@Slf4j
public class AuthorityAnnotationInterceptor extends HandlerInterceptorAdapter {

	private List<String> uncheckUrls = new ArrayList<String>(); // 不被拦截的地址

	private static final String URL_CODE = "upload_file_path";

//	@Autowired
//	private SystemParamService systemParamService;

	/**
	 * 重写 preHandle()方法，在业务处理器处理请求之前对该请求进行拦截处理
	 * 
	 * @param request
	 * @param response
	 * @param handler
	 * @throws Exception
	 * 
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		return preparedSessionUser(request, response);
	}

	/**
	 * 查看用户是否登录和权限验证
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * 
	 */
	private boolean preparedSessionUser(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String currentURL = request.getRequestURI(); // 取得根目录所对应的绝对路径:
		log.info("请求地址：{}",currentURL);
		String path = request.getContextPath();
		String basePath = null;
		if(request.getServerName().contains("www")){
			basePath = request.getScheme() + "://" + request.getServerName()
					 + path + "/";
		}else{
			basePath = request.getScheme() + "://" + request.getServerName()
					+ ":" + request.getServerPort() + path + "/";
		}

		String paramId = request.getParameter("id");
		if (currentURL.substring(path.length()).equals("/")
				|| currentURL.indexOf("/avatars/") != -1
				|| currentURL.indexOf("/permission/") != -1
				|| currentURL.indexOf("/css/") != -1
				|| currentURL.indexOf("/js/") != -1
				|| currentURL.indexOf("/ueditor/") != -1
				|| currentURL.indexOf("/images/") != -1
				|| currentURL.indexOf("/fonts/") != -1
				|| currentURL.matches(".*/login")
				|| currentURL.indexOf("/logout") != -1
				) {
			return true;
		}
		ConsoleLoginAccount loginAccount = HttpSessionUtil.getLoginSession();
		if (loginAccount == null) {
			response.sendRedirect(basePath);
			return false;
		}
		if (loginAccount.getVcLoginName().equals("root")) {
			return true;
		}

		// 进入更改密码页面和执行修改密码动作。
		if ((currentURL.indexOf("/changepassword") != -1 || currentURL
				.indexOf("/updatepassword") != -1)

		&& paramId.equals(Long.toString(loginAccount.getId()))) {
			return true;
		}
		if (currentURL.indexOf("/main") != -1
				|| currentURL.indexOf("/nopermission") != -1) {
			return true;
		}
//		if(!hasAuth(currentURL, loginAccount)){
//			response.sendRedirect(basePath);
//			return false;
//		}
		return true;
	}

	private boolean hasAuth(String currentURL, ConsoleLoginAccount loginAccount) {
		String authUrls = loginAccount.getAuthUrls();

		if(loginAccount.getAllAuthUrls()!=null&&loginAccount.getAllAuthUrls().contains(AuthUtil.wrapComma(currentURL))){
			if(authUrls==null||!loginAccount.getAuthUrls().contains(AuthUtil.wrapComma(currentURL))){
				return false;
			}
		}
		return true;
	}


	/**
	 * 完成对页面的render以后调用
	 * 
	 * @param request
	 * @param response
	 * @param ex
	 * 
	 */
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		logUerOperation(request, response, ex);
	}

	/**
	 * 用户操作记载日志
	 * 
	 * @param request
	 * @param response
	 * @param ex
	 * 
	 */
	private void logUerOperation(HttpServletRequest request,
			HttpServletResponse response, Exception ex) {
	}

}
