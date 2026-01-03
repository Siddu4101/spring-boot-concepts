package org.learning.mailSending.dtos;

import java.time.LocalDate;

public record TableContent(String calculator, String commandName, int productCount, LocalDate pricingDate) {
}
