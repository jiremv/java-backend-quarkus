package com.empresa.handler.producto.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.empresa.data.ProductoDAO;
import com.empresa.handler.producto.response.ResponseRest;
import com.empresa.model.Producto;
import com.empresa.model.UserSession;
import com.empresa.util.LocalDateAdapter;
import com.empresa.util.MyLambdaLogger;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public abstract class CreateProductoAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static final Map<String, String> HEADERS;

    static {
        HEADERS = new HashMap<>();
        HEADERS.put("Content-Type", "application/json");
        HEADERS.put("X-Custom-Header", "application/json");
        HEADERS.put("Access-Control-Allow-Origin", "*");
        HEADERS.put("Access-Control-Allow-Headers", "X-UserId, X-Roles, content-type, X-Custom-Header, X-Amz-Date, Authorization, X-Api-Key, X-Amz-Security-Token");
        HEADERS.put("Access-Control-Allow-Methods", "POST, OPTIONS");
    }
    protected abstract String extractAuthToken(APIGatewayProxyRequestEvent request);
    protected abstract UserSession validateAuthToken(String token, Context context);
    protected abstract void addAuthorizationHeaders(UserSession session, APIGatewayProxyRequestEvent request);
    private final Moshi moshi;
    private final JsonAdapter<Producto> adapter;
    private final JsonAdapter<ResponseRest> responseAdapter;
    private final ProductoDAO dao;
    public CreateProductoAbstract() {
        this.moshi = new Moshi.Builder()
                .add(LocalDate.class, new LocalDateAdapter())
                .build();
        this.adapter = moshi.adapter(Producto.class);
        this.responseAdapter = moshi.adapter(ResponseRest.class);
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(DynamoDbClient.create())
                .build();
        this.dao = new ProductoDAO(enhancedClient);
    }
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        MyLambdaLogger.logRequest(request);
        //Se comenta la autenticación y autorización
        /*String token = extractAuthToken(request);
        UserSession session = validateAuthToken(token, context);
        if (session == null) return error(401, "Token inválido");
        addAuthorizationHeaders(session, request);*/
        try {
            Producto diccionario = adapter.fromJson(request.getBody());
            if (diccionario == null) {
                return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("JSON inválido");
            }
            dao.save(diccionario);
            return new APIGatewayProxyResponseEvent().withStatusCode(201).withHeaders(HEADERS).withBody("{\"mensaje\":\"Producto creado correctamente\"}");
        } catch (Exception e) {
            return new APIGatewayProxyResponseEvent().withStatusCode(500).withBody("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
    private APIGatewayProxyResponseEvent success(String message) {
        ResponseRest response = new ResponseRest();
        response.setStatus("ok");
        response.setMessage(message);
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withHeaders(HEADERS)
                .withBody(responseAdapter.toJson(response));
    }
    private APIGatewayProxyResponseEvent error(int status, String message) {
        ResponseRest response = new ResponseRest();
        response.setStatus("error");
        response.setMessage(message);
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(status)
                .withHeaders(Map.of("Content-Type", "application/json"))
                .withBody(responseAdapter.toJson(response));
    }

}