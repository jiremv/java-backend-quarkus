package com.empresa.data;

import com.empresa.model.Producto;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
public class ProductoDAO {
    private final DynamoDbTable<Producto> productosTable;
    public ProductoDAO(DynamoDbEnhancedClient enhancedClient) {
        this.productosTable = enhancedClient.table("Producto-dev", TableSchema.fromBean(Producto.class));
    }
    public void save(Producto diccionario) {
        productosTable.putItem(diccionario);
    }
    public Optional<Producto> findById(String productoId) {
        Producto diccionario = productosTable.getItem(r -> r.key(k -> k.partitionValue(productoId)));
        return Optional.ofNullable(diccionario);
    }
    
    public List<Producto> findAll() {
        List<Producto> result = new ArrayList<>();
        productosTable.scan().items().forEach(result::add);
        return result;
    }
    public void deleteById(String productoId) {
        productosTable.deleteItem(r -> r.key(k -> k.partitionValue(productoId)));
    }
    public void update(Producto diccionario) {
        productosTable.updateItem(diccionario);
    }
    public List<Producto> buscar(String nombre, String categoria) {
        List<Producto> todos = this.findAll();
        return todos.stream()
                .filter(d -> (nombre == null || d.getNombre().toLowerCase().contains(nombre.toLowerCase())) &&
                        (categoria == null || d.getCategoria().equalsIgnoreCase(categoria)))
                .collect(Collectors.toList());
    }
}