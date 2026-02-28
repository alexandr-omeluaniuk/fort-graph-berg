package ss.fortberg.terminal.model;

import java.math.BigDecimal;

public record SaleItem(
    String nameTitle,
    String codeNumber,
    BigDecimal price,
    BigDecimal amount,
    Boolean isPercentDiscount,
    BigDecimal discount
) {
}
