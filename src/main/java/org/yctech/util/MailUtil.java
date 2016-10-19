package org.yctech.util;

import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.yctech.common.GlobalConstant;

/**
 * 发送邮件
 * @author shiyong.li
 *
 */
public class MailUtil {

	static ResourceUtil resourceUtil = new ResourceUtil("mail");
	
	/**
	 * 邮件发送
	 * @param ToUsers 发送地址
	 * @param CcUsers 抄送地址
	 * @param BccUsers 秘送地址
	 * @param title	邮件主题
	 * @param content	邮件内容
	 */
	public static void sendMail(String[] ToUsers, String[] CcUsers, String[] BccUsers, String title, String content) throws Exception {
		boolean hasFile = false;
		if (ToUsers == null || ToUsers.length == 0 || StringUtil.isEmpty(content)) {
			return;
		}
		
		try {
			JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
			senderImpl.setHost(resourceUtil.getResource("MAIL_HOST")); //resourceUtil.MAIL_HOST
			senderImpl.setUsername(resourceUtil.getResource("MAIL_USERNAME"));  // MAIL_USERNAME
			senderImpl.setPassword(resourceUtil.getResource("MAIL_PASSWORD"));   // MAIL_PASSWORD
			Properties prop = new Properties();
			prop.put("mail.smtp.auth", resourceUtil.getResource("MAIL_SMTP_AUTH"));
			prop.put("mail.smtp.timeout", resourceUtil.getResource("MAIL_TIMEOUT"));  // MAIL_TIMEOUT
			senderImpl.setJavaMailProperties(prop); 
			
			MimeMessage mailMessage = senderImpl.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true, "UTF-8"); 
			messageHelper.setTo(ToUsers);
			if (CcUsers != null && CcUsers.length > 0)
				messageHelper.setCc(CcUsers);
			if (BccUsers != null && BccUsers.length > 0)
				messageHelper.setBcc(BccUsers);
			messageHelper.setFrom(resourceUtil.getResource("MAIL_FROM"), resourceUtil.getResource("MAIL_FROM_NAME"));   //MAIL_FROM   MAIL_FROM_NAME
			messageHelper.setSubject(title);
			messageHelper.setText(content, true);
			if (hasFile) {
				//FileSystemResource file = new FileSystemResource(new File(resourceUtil.MAIL_ATTACH)); 
				//messageHelper.addAttachment(MimeUtility.encodeWord(resourceUtil.MAIL_ATTACH_NAME), file);
			}
			senderImpl.send(mailMessage);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());  
		}
	}

	public static void main(String args[]) {
		System.out.println("send mail Start.....");
		System.out.println("send mail End.....");
	}
}
