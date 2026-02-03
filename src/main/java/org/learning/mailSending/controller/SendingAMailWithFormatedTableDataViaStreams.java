package org.learning.mailSending.controller;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.learning.mailSending.dtos.MailProps;
import org.learning.mailSending.service.MailSenderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import  org.learning.helper.ResponseDto;

@RestController
@RequestMapping("api/v1/mail")
@AllArgsConstructor
@Slf4j
public class SendingAMailWithFormatedTableDataViaStreams {
    /*
    * Q. given data of 4 pricingDate having commandName, productCount, calculator
    *     represent the data as
    *     calculator, commandName, productCount, pricingDate1, pricingDate2, pricingDate3, pricingDate4
    *     table attached to a mail body.
    * */

    private final MailSenderService mailSender;

    @PostMapping("/sendMail")
    public ResponseEntity<@NonNull ResponseDto> sendMail(@RequestBody MailProps mailProps){
        mailSender.sendMail(mailProps);
        log.info("Mail sent successfully...");
        return ResponseEntity.accepted().body(new ResponseDto(HttpStatus.OK, "Success", "Mail sent successfully"));
    }

    @PostMapping("/sendHtmlMail")
    public ResponseEntity<@NonNull ResponseDto> sendHtmlMail(@RequestBody MailProps mailProps) throws MessagingException {
        mailSender.sendMailWithHtmlTemplate(mailProps);
        log.info("Html mail sent successfully...");
        return ResponseEntity.accepted().body(new ResponseDto(HttpStatus.OK, "Success", "Html mail sent successfully"));
    }

}
