package com.empresa.handler.producto.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.empresa.data.ProductoDAO;
import com.empresa.handler.response.ResponseProducto;
import com.empresa.model.Producto;
import com.empresa.model.UserSession;
import com.empresa.util.LocalDateAdapter;
import com.empresa.util.GlobalLambdaLogger;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public abstract class BusquedaProductoAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    static {
        HEADERS = new HashMap<>();
        HEADERS.put("Content-Type", "application/json");
        HEADERS.put("X-Custom-Header", "application/json");
        HEADERS.put("Access-Control-Allow-Origin", "*");
        HEADERS.put("Access-Control-Allow-Headers", "X-UserId, X-Roles, content-type, X-Custom-Header, X-Amz-Date, Authorization, X-Api-Key, X-Amz-Security-Token");
        HEADERS.put("Access-Control-Allow-Methods", "PUT, OPTIONS");
    }
    protected abstract String extractAuthToken(APIGatewayProxyRequestEvent request);
    protected abstract UserSession validateAuthToken(String token, Context context);
    protected abstract void addAuthorizationHeaders(UserSession session, APIGatewayProxyRequestEvent request);
    private static final LambdaLogger logger = new GlobalLambdaLogger();
    private static final Map<String, String> HEADERS;
    private final Moshi moshi;
    private final JsonAdapter<List<Producto>> listAdapter;
    private final JsonAdapter<ResponseProducto> responseAdapter;
    private final ProductoDAO dao;
    public BusquedaProductoAbstract() {
        this.moshi = new Moshi.Builder()
                .add(LocalDate.class, new LocalDateAdapter())
                .build();
        this.listAdapter = moshi.adapter(
                Types.newParameterizedType(List.class, Producto.class)
        );
        this.responseAdapter = moshi.adapter(ResponseProducto.class);
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(DynamoDbClient.create())
                .build();
        this.dao = new ProductoDAO(enhancedClient);
    }
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        GlobalLambdaLogger.logRequest(request);
        //Se comenta la autenticación y autorización
        /*String token = extractAuthToken(request);
        UserSession session = validateAuthToken(token, context);
        if (session == null) return error(401, "Token inválido");
        addAuthorizationHeaders(session, request);*/
        try {
            Map<String, String> queryParams = request.getQueryStringParameters();
            String nombre = queryParams != null ? queryParams.get("nombre") : null;
            String categoria = queryParams != null ? queryParams.get("categoria") : null;

            List<Producto> resultados = dao.buscar(nombre, categoria);
            String json = listAdapter.toJson(resultados);
            return success("Búsqueda correcta");
        } catch (Exception e) {
            logger.log("ERROR GENERAL: " + getStackTrace(e));
            return error(500, "Error interno del servidor");
        }
    }

    private APIGatewayProxyResponseEvent success(String message) {
        ResponseProducto response = new ResponseProducto();
        response.setStatus("ok");
        response.setMessage(message);
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withHeaders(HEADERS)
                .withBody(responseAdapter.toJson(response));
    }

    private APIGatewayProxyResponseEvent error(int status, String message) {
        ResponseProducto response = new ResponseProducto();
        response.setStatus("error");
        response.setMessage(message);
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(status)
                .withHeaders(Map.of("Content-Type", "application/json"))
                .withBody(responseAdapter.toJson(response));
    }
    private String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

}