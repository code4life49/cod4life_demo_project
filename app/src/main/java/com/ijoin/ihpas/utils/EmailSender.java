package com.ijoin.ihpas.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailSender {

    public interface EmailSendListener {
        void onEmailSendStart();
        void onEmailSendSuccess();
        void onEmailSendFailed(String error);
    }

    // 邮件服务器配置
    private static class EmailConfig {
        static final String SMTP_HOST_GMAIL = "smtp.gmail.com";
        static final String SMTP_HOST_QQ = "smtp.qq.com";
        static final String SMTP_HOST_163 = "smtp.163.com";
        static final String SMTP_HOST_OUTLOOK = "smtp.live.com";

        static final int SMTP_PORT = 587; // TLS端口
        static final int SMTP_PORT_SSL = 465; // SSL端口
    }

    public static void sendEmail(String senderEmail, String senderPassword,
                                 String recipientEmail, String subject, String messageBody,
                                 List<File> attachments, EmailSendListener listener) {

        new SendEmailTask(senderEmail, senderPassword, recipientEmail,
                subject, messageBody, attachments, listener).execute();
    }

    private static class SendEmailTask extends AsyncTask<Void, Void, String> {
        private String senderEmail;
        private String senderPassword;
        private String recipientEmail;
        private String subject;
        private String messageBody;
        private List<File> attachments;
        private EmailSendListener listener;

        public SendEmailTask(String senderEmail, String senderPassword,
                             String recipientEmail, String subject, String messageBody,
                             List<File> attachments, EmailSendListener listener) {
            this.senderEmail = senderEmail;
            this.senderPassword = senderPassword;
            this.recipientEmail = recipientEmail;
            this.subject = subject;
            this.messageBody = messageBody;
            this.attachments = attachments;
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (listener != null) {
                listener.onEmailSendStart();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                // 获取邮件服务器配置
                String smtpHost = getSmtpHost(senderEmail);

                // 设置邮件属性
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", smtpHost);
                props.put("mail.smtp.port", EmailConfig.SMTP_PORT);
                props.put("mail.smtp.ssl.trust", smtpHost);

                // 创建认证
                Authenticator auth = new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(senderEmail, senderPassword);
                    }
                };

                // 创建会话
                Session session = Session.getInstance(props, auth);

                // 创建消息
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(senderEmail));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(recipientEmail));
                message.setSubject(subject);

                // 创建邮件内容
                Multipart multipart = new MimeMultipart();

                // 添加文本内容
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(messageBody);
                multipart.addBodyPart(messageBodyPart);

                // 添加附件
                if (attachments != null && !attachments.isEmpty()) {
                    for (File file : attachments) {
                        if (file.exists()) {
                            MimeBodyPart attachmentPart = new MimeBodyPart();
                            DataSource source = new FileDataSource(file);
                            attachmentPart.setDataHandler(new DataHandler(source));
                            attachmentPart.setFileName(file.getName());
                            multipart.addBodyPart(attachmentPart);
                        }
                    }
                }

                // 设置邮件内容
                message.setContent(multipart);

                // 发送邮件
                Transport.send(message);

                return "success";

            } catch (MessagingException e) {
                Log.e("EmailSender", "邮件发送失败", e);
                return "邮件发送失败: " + e.getMessage();
            } catch (Exception e) {
                Log.e("EmailSender", "发送过程中出错", e);
                return "发送过程中出错: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (listener != null) {
                if ("success".equals(result)) {
                    listener.onEmailSendSuccess();
                } else {
                    listener.onEmailSendFailed(result);
                }
            }
        }

        private String getSmtpHost(String email) {
            String domain = email.substring(email.indexOf("@") + 1).toLowerCase();

            switch (domain) {
                case "gmail.com":
                    return EmailConfig.SMTP_HOST_GMAIL;
                case "qq.com":
                    return EmailConfig.SMTP_HOST_QQ;
                case "163.com":
                    return EmailConfig.SMTP_HOST_163;
                case "outlook.com":
                case "hotmail.com":
                case "live.com":
                    return EmailConfig.SMTP_HOST_OUTLOOK;
                default:
                    // 默认尝试通用格式
                    return "smtp." + domain;
            }
        }
    }
}
