package ss.fortberg.server.model;

import java.util.List;

public record SaleRequest(
    Meta meta,
    String name,
    String moment,
    SaleOwner owner,
    List<Position> positions,
    Integer cashSum,
    Integer noCashSum
) {
}
