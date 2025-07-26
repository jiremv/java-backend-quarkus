package com.empresa.handler.response;

import com.empresa.model.Producto;
import java.util.HashMap;
import java.util.Map;

public class ResponseProducto extends com.empresa.handler.Response {
    private com.empresa.handler.Response productoResponse; // Contendrá la lista de productos
    private Map<String, Object> data = new HashMap<>();
    private String message;
    private String status;

    public com.empresa.handler.Response getProductoResponse() {
        return productoResponse;
    }
    public void setProductoResponse(com.empresa.handler.Response productoResponse) {
        this.productoResponse = productoResponse;
    }
    // Método utilitario opcional para establecer ambos a la vez
    public void setResponseData(Producto producto) {
        data.put("producto", producto);
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Map<String, Object> getData() {
        return data;
    }

}