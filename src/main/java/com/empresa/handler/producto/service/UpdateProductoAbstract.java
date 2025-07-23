package com.empresa.handler.producto.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.empresa.data.ProductoDAO;
import com.empresa.model.Producto;
import com.empresa.model.UserSession;
import com.empresa.handler.response.ResponseProducto;
import com.empresa.util.LocalDateAdapter;
import com.empresa.util.GlobalLambdaLogger;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public  abstract class UpdateProductoAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    protected abstract String extractAuthToken(APIGatewayProxyRequestEvent request);
    protected abstract UserSession validateAuthToken(String token, Context context);
    protected abstract void addAuthorizationHeaders(UserSession session, APIGatewayProxyRequestEvent request);
    
    private static final Map<String, String> HEADERS;
    static {
        HEADERS = new HashMap<>();
        HEADERS.put("Content-Type", "application/json");
        HEADERS.put("X-Custom-Header", "application/json");
        HEADERS.put("Access-Control-Allow-Origin", "*");
        HEADERS.put("Access-Control-Allow-Headers", "X-UserId, X-Roles, content-type, X-Custom-Header, X-Amz-Date, Authorization, X-Api-Key, X-Amz-Security-Token");
        HEADERS.put("Access-Control-Allow-Methods", "PUT, OPTIONS");
    }    
    private final Moshi moshi;
    private final JsonAdapter<ResponseProducto> responseAdapter;
    private final JsonAdapter<Producto> jsonAdapter;
    private final ProductoDAO dao;
    public UpdateProductoAbstract() {
        this.moshi = new Moshi.Builder()
                .add(LocalDate.class, new LocalDateAdapter())
                .build();
        this.responseAdapter = moshi.adapter(ResponseProducto.class);
        this.jsonAdapter = moshi.adapter(Producto.class);
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(DynamoDbClient.create())
                .build();
        this.dao = new ProductoDAO(enhancedClient);
    }
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        LambdaLogger logger = context.getLogger();
        GlobalLambdaLogger.logRequest(request, logger); 
        //Se comenta la autenticación y autorización
        /*String token = extractAuthToken(request);
        UserSession session = validateAuthToken(token, context);
        if (session == null) return error(401, "Token inválido");
        addAuthorizationHeaders(session, request);*/
        try {
            // ✅ Obtener el productoId desde el path
            Map<String, String> pathParams = request.getPathParameters();
            if (pathParams == null || !pathParams.containsKey("id")) {
                return error(400, "Falta el parámetro de ruta 'id'");
            }
            String pathId = pathParams.get("id");

            // ✅ Parsear el JSON del body
            String body = request.getBody();
            Producto producto = jsonAdapter.fromJson(body);
            if (producto == null) {
                return error(400, "JSON inválido");
            }
            // ✅ Verificar consistencia entre path y body
            if (!pathId.equals(producto.getProductoId())) {
                return error(400, "El ID de la ruta no coincide con el ID en el cuerpo");
            }
            dao.update(producto);
            return success("Actualizado correctamente");
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