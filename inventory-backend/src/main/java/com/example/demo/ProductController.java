package com.example.demo;

import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final InventoryService service;

    public ProductController(InventoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<GroceryItem> all() {
        return service.getAll();
    }

    @PostMapping
    public GroceryItem add(@RequestBody GroceryItem item) throws IOException {
        return service.add(item);
    }

    @PutMapping("/{barcode}")
    public GroceryItem update(@PathVariable String barcode, @RequestBody GroceryItem item) throws IOException {
        return service.update(barcode, item);
    }

    @DeleteMapping("/{barcode}")
    public boolean delete(@PathVariable String barcode) throws IOException {
        return service.delete(barcode);
    }
}
