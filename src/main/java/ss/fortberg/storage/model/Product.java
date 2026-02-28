package ss.fortberg.storage.model;

import java.util.List;

public record Product(
    String id,
    String name,
    List<String> barcodes,
    String code,
    List<ProductPrice> salePrices
) {
}
