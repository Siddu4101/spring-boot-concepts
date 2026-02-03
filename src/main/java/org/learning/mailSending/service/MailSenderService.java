package org.learning.mailSending.service;

import jakarta.mail.MessagingException;
import org.learning.mailSending.dtos.MailProps;

public interface MailSenderService {
    void sendMail(MailProps mailProps);
    void sendMailWithHtmlTemplate(MailProps mailProps) throws MessagingException;
}
