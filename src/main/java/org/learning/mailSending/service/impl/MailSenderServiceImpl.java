package org.learning.mailSending.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.learning.mailSending.dtos.GroupByKeysForTable;
import org.learning.mailSending.dtos.MailProps;
import org.learning.mailSending.dtos.TableContent;
import org.learning.mailSending.service.MailSenderService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        Set<LocalDate> pricingDates = getTableData().stream().map(TableContent::pricingDate).collect(Collectors.toSet());
        context.setVariable("pricingDates",pricingDates);
        Map<GroupByKeysForTable, Map<LocalDate, Integer>> tableContent =  getTableData().stream()
                .collect(Collectors.groupingBy(x -> new GroupByKeysForTable(x.calculator(), x.commandName()), Collectors.toMap(TableContent::pricingDate, TableContent::productCount)));
        context.setVariable("tableContent",tableContent.entrySet().stream().sorted(Map.Entry.comparingByKey(Comparator.comparing(GroupByKeysForTable::calculator).thenComparing(GroupByKeysForTable::commandName))));

        String htmlMessage = templateEngine.process("emailTemplate", context);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_RELATED, "UTF-8");
        mimeMessageHelper.setTo(mailProps.to());
        mimeMessageHelper.setCc(mailProps.cc());
        mimeMessageHelper.setSubject(mailProps.subject());
        mimeMessageHelper.setText(htmlMessage, true);/*setting html text*/
        log.info("sending a html template mail to {} and cc {}", mailProps.to(), mailProps.cc());
        /*u can add attachments to this*/
        /*Class path resource picks files from the src/main/resource/ as base directory and choose file from there*/
        ClassPathResource classPathResource = new ClassPathResource("classPathResourceFile.txt");
        /*Simple file picked from the spring-boot-concepts or ./ as base directory*/
        File simpleFile = new File("src/main/resources/simpleFile.txt");

        mimeMessageHelper.addAttachment("classPathResourceFile.txt", classPathResource);
        mimeMessageHelper.addAttachment("simpleFile.txt", simpleFile);
        javaMailSender.send(message);
    }

    public static List<TableContent> getTableData() {
        return List.of(
                new TableContent("HWFXO", "CommandA", 10, LocalDate.of(2024, 6, 1)),
                new TableContent("HIGHWAY", "CommandB", 15, LocalDate.of(2024, 6, 1)),
                new TableContent("RISKONE", "CommandC", 20, LocalDate.of(2024, 6, 1)),
                new TableContent("HWFXO", "CommandD", 10, LocalDate.of(2024, 6, 1)),
                new TableContent("HIGHWAY", "CommandE", 15, LocalDate.of(2024, 6, 1)),
                new TableContent("RISKONE", "CommandF", 20, LocalDate.of(2024, 6, 1)),

                new TableContent("HWFXO", "CommandA", 12, LocalDate.of(2024, 6, 2)),
                new TableContent("HIGHWAY", "CommandB", 18, LocalDate.of(2024, 6, 2)),
                new TableContent("RISKONE", "CommandC", 22, LocalDate.of(2024, 6, 2)),
                new TableContent("HWFXO", "CommandD", 12, LocalDate.of(2024, 6, 2)),
                new TableContent("HIGHWAY", "CommandE", 18, LocalDate.of(2024, 6, 2)),
                new TableContent("RISKONE", "CommandF", 22, LocalDate.of(2024, 6, 2)),

                new TableContent("HWFXO", "CommandA", 14, LocalDate.of(2024, 6, 3)),
                new TableContent("HIGHWAY", "CommandB", 16, LocalDate.of(2024, 6, 3)),
                new TableContent("RISKONE", "CommandC", 24, LocalDate.of(2024, 6, 3)),
                new TableContent("HWFXO", "CommandD", 14, LocalDate.of(2024, 6, 3)),
                new TableContent("HIGHWAY", "CommandE", 16, LocalDate.of(2024, 6, 3)),
                new TableContent("RISKONE", "CommandF", 24, LocalDate.of(2024, 6, 3)),


                new TableContent("HWFXO", "CommandA", 11, LocalDate.of(2024, 6, 4)),
                new TableContent("HIGHWAY", "CommandB", 19, LocalDate.of(2024, 6, 4)),
                new TableContent("RISKONE", "CommandC", 21, LocalDate.of(2024, 6, 4)),
                new TableContent("HWFXO", "CommandD", 11, LocalDate.of(2024, 6, 4)),
                new TableContent("HIGHWAY", "CommandE", 19, LocalDate.of(2024, 6, 4)),
                new TableContent("RISKONE", "CommandF", 21, LocalDate.of(2024, 6, 4))

        );
    }


}
