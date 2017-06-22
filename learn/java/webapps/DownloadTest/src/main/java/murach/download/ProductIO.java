package murach.download;

import murach.beans.Product;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.TreeMap;

/**
 * Created by somebody on 21.06.2017.
 */
public class ProductIO {

    private static final TreeMap<String, Product> products = new TreeMap<>();

    @Contract("null -> null")
    public static Product search(Object id) throws IOException {
        return id != null ? products.get(id.toString()) : null;
    }

    public static void initProductList(String path) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(path))) {
            String buffer;

            while ((buffer = reader.readLine()) != null) {
                String split[] = buffer.split(";");

                if (split.length == 5) {
                    products.put(split[0],
                            new Product(
                                split[1], split[2], split[3], split[4]
                    ));
                }
            }
        }
    }
}
