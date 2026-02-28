package ss.fortberg.server.model;

import ss.fortberg.storage.model.Product;

import java.util.List;

public record Assortment(
    List<Product> rows
) {
}
