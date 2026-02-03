package org.learning.mailSending.dtos;

public record MailProps(String from, String to, String cc, String subject, String body) {
}
