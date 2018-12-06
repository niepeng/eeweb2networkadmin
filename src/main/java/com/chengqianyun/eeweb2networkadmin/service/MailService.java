package com.chengqianyun.eeweb2networkadmin.service;


import com.chengqianyun.eeweb2networkadmin.biz.enums.SettingEnum;
import com.chengqianyun.eeweb2networkadmin.core.utils.ThreeDes;
import com.sun.mail.util.MailSSLSocketFactory;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/10/31
 */
@Slf4j
@Component
public class MailService extends BaseService {

  @Autowired
  private Environment env;

  public static String auth;
  public static String host;
  public static String protocol;
  public static int port;
  public static String authName;
  public static String password;
  public static boolean isSSL;
  public static String charset ;
  public static String timeout;

  @PostConstruct
  public void initParam() {
    MailService.auth =  getData(SettingEnum.mail_smtp_auth);
    MailService.host = getData(SettingEnum.mail_host);
    MailService.protocol = getData(SettingEnum.mail_transport_protocol);
    MailService.port = Integer.parseInt(getData(SettingEnum.mail_smtp_port));
    MailService.authName = getData(SettingEnum.mail_auth_name);
    MailService.password = ThreeDes.decrypt(getData(SettingEnum.mail_auth_password));
    MailService.charset = getData(SettingEnum.mail_send_charset);
    MailService.isSSL = Boolean.valueOf(getData(SettingEnum.mail_is_ssl));
    MailService.timeout = getData(SettingEnum.mail_smtp_timeout);
  }

  /**
   * 发送邮件
   * @param subject 主题
   * @param toUsers 收件人
   * @param ccUsers 抄送
   * @param content 邮件内容
   * @param attachfiles 附件列表  List<Map<String, String>> key: name && file
   */
  public boolean sendEmail(String subject, String[] toUsers, String[] ccUsers, String content, List<Map<String, String>> attachfiles) {
    boolean flag = true;
    try {
      JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
      javaMailSender.setHost(host);
      javaMailSender.setUsername(authName);
      javaMailSender.setPassword(password);
      javaMailSender.setDefaultEncoding(charset);
      javaMailSender.setProtocol(protocol);
      javaMailSender.setPort(port);

      Properties properties = new Properties();
      properties.setProperty("mail.smtp.auth", auth);
      properties.setProperty("mail.smtp.timeout", timeout);
      if(isSSL){
        MailSSLSocketFactory sf = null;
        try {
          sf = new MailSSLSocketFactory();
          sf.setTrustAllHosts(true);
          properties.put("mail.smtp.ssl.enable", "true");
          properties.put("mail.smtp.ssl.socketFactory", sf);
        } catch (GeneralSecurityException e) {
          e.printStackTrace();
        }
      }
      javaMailSender.setJavaMailProperties(properties);

      MimeMessage mailMessage = javaMailSender.createMimeMessage();
      MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true);
      messageHelper.setTo(toUsers);
      if (ccUsers != null && ccUsers.length > 0) {
        messageHelper.setCc(ccUsers);
      }
      messageHelper.setFrom(authName);
      messageHelper.setSubject(subject);
      messageHelper.setText(content, true);

      if (attachfiles != null && attachfiles.size() > 0) {
        for (Map<String, String> attachfile : attachfiles) {
          String attachfileName = attachfile.get("name");
          File file = new File(attachfile.get("file"));
          messageHelper.addAttachment(attachfileName, file);
        }
      }
      javaMailSender.send(mailMessage);

    } catch (Exception e) {
      log.error("发送邮件失败!", e);
      flag = false;
    }
    return flag;
  }
}