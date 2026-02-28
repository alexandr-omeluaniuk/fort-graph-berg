package ss.fortberg.storage;

import ss.fortberg.server.model.Assortment;
import ss.fortberg.server.model.RetailInfo;
import ss.fortberg.storage.model.Cashier;
import ss.fortberg.storage.model.Product;
import ss.fortberg.util.FBLogger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataStorage implements FBLogger {

    private static DataStorage storage = null;

    private DataStorage() {}

    private Map<String, Cashier> cashiersMap = new ConcurrentHashMap<>();

    private Map<String, Product> productsMap = new ConcurrentHashMap<>();

    public static DataStorage getInstance() {
        if (storage == null) {
            storage = new DataStorage();
        }
        return storage;
    }

    public Cashier getCashier(String id) {
        return cashiersMap.get(id);
    }

    public void setCashiers(RetailInfo info) {
        final var newData = info.cashiers().rows().stream().map(raw ->
            new Cashier(raw.lastName(), raw.meta().id())
        ).toList();
        cashiersMap.clear();
        newData.forEach(item -> cashiersMap.put(item.id(), item));
        log.info("Cashiers added: " + newData.toString());
    }

    public void setProducts(Assortment assortment) {
        productsMap.clear();
        assortment.rows().forEach(item -> {
            productsMap.put(item.id(), item);
        });
        log.info("Products added: " + assortment.rows().toString());
    }
}
