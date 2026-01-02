package org.learning.mailSending.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.learning.mailSending.dtos.MailProps;
import org.learning.mailSending.service.MailSenderService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@AllArgsConstructor
@Service
@Slf4j
public class MailSenderServiceImpl implements MailSenderService {

    private final JavaMailSender javaMailSender;

    private final TemplateEngine templateEngine;

    @Override
    public void sendMail(MailProps mailProps){
        SimpleMailMessage message = new SimpleMailMessage();/*for simple text mail*/
        /*message.setFrom(mailProps.from()) This is not required as it always takes from the authenticated user email id*/;
        message.setTo(mailProps.to(),mailProps.cc());/* u can multiple to anc cc if required*/
        message.setCc(mailProps.cc());
        message.setSubject(mailProps.subject());
        message.setText(mailProps.body());
        log.info("sending mail to {} and cc {}", mailProps.to() + mailProps.cc(), mailProps.cc());
        javaMailSender.send(message);
    }

    @Override
    public void sendMailWithHtmlTemplate(MailProps mailProps) throws MessagingException {
        /*dynamic var in template*/
        Context context = new Context();
        context.setVariable("body", mailProps.body());

        String htmlMessage = templateEngine.process("emailTemplate", context);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_RELATED, "UTF-8");
        mimeMessageHelper.setTo(mailProps.to());
        mimeMessageHelper.setCc(mailProps.cc());
        mimeMessageHelper.setSubject(mailProps.subject());
        mimeMessageHelper.setText(htmlMessage, true);/*setting html text*/
        log.info("sending a html template mail to {} and cc {}", mailProps.to(), mailProps.cc());
        /*u can add attachments to this*/
        javaMailSender.send(message);
    }

}
