
# 📧 Quick Guide: Sending Emails with JavaMailSender in Spring Boot

> This guide uses real code examples from this project to show how to send emails, attach files, and use HTML templates in Spring Boot.
---
🔗Code Reference: [mail sending via spring-boot](../mailSending)

## ✉️ 1. Basic Email Sending (Plain Text)

```java
public void sendMail(MailProps mailProps){
    SimpleMailMessage message = new SimpleMailMessage(); // for simple text mail
    // message.setFrom(mailProps.from()) // Not required, uses authenticated user
    message.setTo(mailProps.to(), mailProps.cc()); // multiple to and cc if required
    message.setCc(mailProps.cc());
    message.setSubject(mailProps.subject());
    message.setText(mailProps.body());
    javaMailSender.send(message);
}
```
**Tip:** `MailProps` is a record holding from, to, cc, subject, and body.

---


---

## 📎 2. Sending HTML Emails with Attachments & Template

```java
public void sendMailWithHtmlTemplate(MailProps mailProps) throws MessagingException {
    // Dynamic variables for the template
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
    mimeMessageHelper.setText(htmlMessage, true); // true = isHtml

    // Attach files
    ClassPathResource classPathResource = new ClassPathResource("classPathResourceFile.txt");
    File simpleFile = new File("src/main/resources/simpleFile.txt");
    mimeMessageHelper.addAttachment("classPathResourceFile.txt", classPathResource);
    mimeMessageHelper.addAttachment("simpleFile.txt", simpleFile);

    javaMailSender.send(message);
}
```
**Tips:**
- Use `MimeMessageHelper` for HTML and attachments.
- Use `templateEngine.process("emailTemplate", context)` for HTML templates (Thymeleaf).
- Attach files from classpath or file system as shown above.

---




---

## ⚙️ 3. Configuration

Set your mail server properties in `application.yaml`:

```yaml
spring:
    mail:
        host: smtp.example.com
        port: 587
        username: your@email.com
        password: ${EMAIL_PASSWORD} # this password must be the password generated for the app from the App passwords not the gmail pass
        properties:
            mail:
                smtp:
                    auth: true
                    starttls:
                        enable: true
```

---


## 💡 Tips
- Place your HTML templates in `src/main/resources/templates/`.
- Use Thymeleaf for dynamic templates.
- Always handle `MessagingException`.
- Attachments can be added from classpath or file system.

---

