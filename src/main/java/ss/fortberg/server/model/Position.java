package ss.fortberg.server.model;

public record Position(
    PositionItem assortment,
    Integer quantity,
    Integer price,
    Integer discount
) {
}
