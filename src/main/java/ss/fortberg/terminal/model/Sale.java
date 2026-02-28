package ss.fortberg.terminal.model;

import java.math.BigDecimal;
import java.util.List;

public record Sale(
    String cashier,
    List<SaleItem> itemList,

    BigDecimal cash,
    BigDecimal cashless,
    BigDecimal certificate,
    BigDecimal oplati,

    String prefix,
    String suffix
) {
}
