package ss.fortberg.storage;

import ss.fortberg.server.model.Assortment;
import ss.fortberg.server.model.RetailInfo;
import ss.fortberg.storage.model.Cashier;
import ss.fortberg.storage.model.Product;
import ss.fortberg.util.FBLogger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static ss.fortberg.util.JsonUtils.objectMapper;

public class DataStorage implements FBLogger {

    private static final String PRODUCTS_FILE = "products.json";
    private static final String CASHIERS_FILE = "cashiers.json";

    private static DataStorage storage = null;

    private DataStorage() {}

    private Map<String, Cashier> cashiersMap = new ConcurrentHashMap<>();

    private Map<String, Product> productsMap = new ConcurrentHashMap<>();

    public static DataStorage getInstance() {
        if (storage == null) {
            storage = new DataStorage();
            final var cashiers = new File(CASHIERS_FILE);
            if (cashiers.exists()) {
                try {
                    final var retailInfo = objectMapper.readValue(cashiers, RetailInfo.class);
                    storage.setCashiers(retailInfo);
                    log.info("Cashiers [" + storage.cashiersMap.size() + "] loaded from disk...");
                } catch (Exception e) {
                    log.warning("Reading cashiers from file problem: " + e.getMessage());
                }
            }
            final var assortment = new File(PRODUCTS_FILE);
            if (assortment.exists()) {
                try {
                    final var info = objectMapper.readValue(assortment, Assortment.class);
                    storage.setProducts(info);
                    log.info("Products [" + storage.productsMap.size() + "] loaded from disk...");
                } catch (Exception e) {
                    log.warning("Reading products from file problem: " + e.getMessage());
                }
            }
        }
        return storage;
    }

    public Cashier getCashier(String id) {
        return cashiersMap.get(id);
    }

    public Product getProduct(String id) {
        return productsMap.get(id);
    }

    public void setCashiers(RetailInfo info) {
        final var newData = info.cashiers().rows().stream().map(raw ->
            new Cashier(raw.lastName(), raw.meta().id())
        ).toList();
        cashiersMap.clear();
        try {
            Files.write(new File(CASHIERS_FILE).toPath(), objectMapper.writeValueAsBytes(info), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            log.warning("Error of saving cashiers to disk: " + e.getMessage());
        }
        newData.forEach(item -> cashiersMap.put(item.id(), item));
        log.fine("Cashiers added: " + newData.toString());
    }

    public void setProducts(Assortment assortment) {
        try {
            Files.write(new File(PRODUCTS_FILE).toPath(), objectMapper.writeValueAsBytes(assortment), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            log.warning("Error of saving products to disk: " + e.getMessage());
        }
        productsMap.clear();
        assortment.rows().forEach(item -> {
            productsMap.put(item.id(), item);
        });
        log.fine("Products added: " + assortment.rows().toString());
    }
}
