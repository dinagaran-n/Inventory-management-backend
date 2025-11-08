package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@Service
public class InventoryService {

    private final Path filePath;
    private final Map<String, GroceryItem> items = new LinkedHashMap<>();

    public InventoryService(@Value("${inventory.file}") String file) throws IOException {
        // Try to load file from resources
        InputStream in = getClass().getClassLoader().getResourceAsStream(file);

        if (in != null) {
            // Resources file is read-only, so copy it to a writable location
            Path temp = Paths.get(System.getProperty("user.dir"), file);
            Files.copy(in, temp, StandardCopyOption.REPLACE_EXISTING);
            this.filePath = temp.toAbsolutePath();
        } else {
            // Fallback: treat file as normal path
            this.filePath = Paths.get(file).toAbsolutePath();
        }

        loadFile();
    }


    private void loadFile() throws IOException {
        items.clear();
        if (!Files.exists(filePath)) return;

        List<String> lines = Files.readAllLines(filePath);
        for (String line : lines) {
            if (line.isBlank()) continue;
            String[] data = line.split(",");
            String name = data[0];
            String barcode = data[1];
            double price = Double.parseDouble(data[2]);
            int qty = Integer.parseInt(data[3]);
            items.put(barcode, new GroceryItem(name, barcode, price, qty));
        }
    }

    private void saveFile() throws IOException {
        List<String> lines = new ArrayList<>();
        for (GroceryItem item : items.values()) {
            lines.add(item.getName() + "," + item.getBarcode() + "," +
                      item.getPrice() + "," + item.getQuantity());
        }
        Files.write(filePath, lines);
    }

    public List<GroceryItem> getAll() {
        return new ArrayList<>(items.values());
    }

    public GroceryItem add(GroceryItem item) throws IOException {
        items.put(item.getBarcode(), item);
        saveFile();
        return item;
    }

    public GroceryItem update(String barcode, GroceryItem newItem) throws IOException {
        items.put(barcode, newItem);
        saveFile();
        return newItem;
    }

    public boolean delete(String barcode) throws IOException {
        GroceryItem removed = items.remove(barcode);
        saveFile();
        return removed != null;
    }
}
