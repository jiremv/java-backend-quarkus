package com.empresa.handler.examen.response;

import com.empresa.model.Examen;
import com.empresa.model.ExamenSesion;
import com.empresa.model.PreguntaSesion;
import java.util.HashMap;
import java.util.Map;

public class ResponseRest extends com.empresa.handler.ResponseRest {
    private Response productoResponse; // Contendrá la lista de productos
    private String jwtToken; // Contendrá el JWT

    private Map<String, Object> data = new HashMap<>();
    private String message;
    private String status;

    public Response getProductoResponse() {
        return examenSolucionResponse;
    }

    public void setProductoResponse(Response productoResponse) {
        this.examenSolucionResponse = productoResponse;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    // Método utilitario opcional para establecer ambos a la vez
    public void setResponseDataWithToken(Response examenSolucionResponse, String jwtToken) {
        this.examenSolucionResponse = examenSolucionResponse;
        this.jwtToken = jwtToken;
    }

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
}