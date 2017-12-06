/***************************************
 * Copyright (c) 2014, 2017 Dtds.Inc
 * 
 * @date 2017年9月15日 上午9:51:58
 ***************************************/
package com.momo.util;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import com.google.common.base.Charsets;

/**
 * 邮件工具类<br>
 * 
 * @ClassName MailUtils
 * @author penghp <br>
 * @version V1.0.0<br>
 * @Date 2017年9月15日<br>
 */
public final class MailUtils {
	// 日志打印
	private Logger logger = LoggerFactory.getLogger(MailUtils.class);

	private static volatile MailUtils mailUtils = null;

	private static JavaMailSenderImpl mailSender;// 邮件发送器

	/**
	 * @Title MailUtils
	 * @Description 构造方法
	 */
	private MailUtils() {
		// 初始化变量
		/* mailSender = SpringContextUtil.getBean("mailSender"); */
	}

	/**
	 * 单例获取实例对象<br>
	 * 
	 * @author penghp <br>
	 * @Date 2017年9月15日<br>
	 * @return
	 */
	public static MailUtils getMailUtils() {
		if (mailUtils == null) {
			synchronized (MailUtils.class) {
				if (mailUtils == null) {
					mailUtils = new MailUtils();
				}
			}
		}
		return mailUtils;
	}

	/**
	 * 发送邮件<br>
	 * 
	 * @author penghp <br>
	 * @Date 2017年9月15日<br>
	 * @param to
	 *            收件人
	 * @param subject
	 *            邮件主题
	 * @param content
	 *            邮件内容
	 * @return
	 */
	public boolean sendEmail(String[] to, String subject, String content) {
		return sendEmail(to, null, subject, content);
	}

	/**
	 * 发送邮件<br>
	 * 
	 * @author penghp <br>
	 * @Date 2017年9月15日<br>
	 * @param to
	 *            收件人
	 * @param cc
	 *            抄送人
	 * @param subject
	 *            邮件主题
	 * @param content
	 *            邮件内容
	 * @return
	 */
	public boolean sendEmail(String[] to, String[] cc, String subject, String content) {
		return sendEmail(to, cc, subject, content, true, Charsets.UTF_8.name());
	}

	/**
	 * 发送邮件<br>
	 * 
	 * @author penghp <br>
	 * @Date 2017年9月15日<br>
	 * @param to
	 *            收件人
	 * @param cc
	 *            抄送人
	 * @param subject
	 *            邮件主题
	 * @param content
	 *            邮件内容
	 * @param html
	 *            邮件内容是否为html格式，true为html格式，否则为文本格式
	 * @param encoding
	 *            编码格式
	 */
	public boolean sendEmail(final String[] to, final String[] cc, final String subject, final String content, final boolean html, final String encoding) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, encoding);
				helper.setSubject(subject);// 邮件主题
				helper.setFrom(mailSender.getUsername());// 发送人
				helper.setTo(to);// 收件人
				if (ArrayUtils.isNotEmpty(cc)) {
					helper.setCc(cc);// 抄送人
				}
				helper.setText(content, html);// 邮件内容
			}
		};
		try {
			mailSender.send(preparator);
			logger.info("\r\n邮件发送成功");
			return true;
		} catch (MailException e) {
			logger.error("\r\n邮件发送失败", e);
			return false;
		}
	}
}
