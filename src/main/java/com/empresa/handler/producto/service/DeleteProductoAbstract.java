package com.empresa.handler.producto.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.empresa.data.ProductoDAO;
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
public abstract class DeleteProductoAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static final Map<String, String> HEADERS;
    static {
        HEADERS = new HashMap<>();
        HEADERS.put("Content-Type", "application/json");
        HEADERS.put("X-Custom-Header", "application/json");
        HEADERS.put("Access-Control-Allow-Origin", "*");
        HEADERS.put("Access-Control-Allow-Headers", "X-UserId, X-Roles, content-type, X-Custom-Header, X-Amz-Date, Authorization, X-Api-Key, X-Amz-Security-Token");
        HEADERS.put("Access-Control-Allow-Methods", "DELETE, OPTIONS");
    }
    private final Moshi moshi;
    private final JsonAdapter<ResponseProducto> responseAdapter;
    private final ProductoDAO dao;
    public DeleteProductoAbstract() {
        this.moshi = new Moshi.Builder()
                .add(LocalDate.class, new LocalDateAdapter())
                .build();
        this.responseAdapter = moshi.adapter(ResponseProducto.class);
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(DynamoDbClient.create())
                .build();
        this.dao = new ProductoDAO(enhancedClient);
    }
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        LambdaLogger logger = context.getLogger();
        GlobalLambdaLogger.logRequest(request, logger);
        try {
            String productoId = request.getPathParameters() != null ? request.getPathParameters().get("id") : null;
            if (productoId == null || productoId.isEmpty()) {
                return new APIGatewayProxyResponseEvent()
                        .withStatusCode(400)
                        .withBody("Falta el parámetro de ruta 'id'");
            }

            logger.log("Producto ID a eliminar: " + productoId);

            // Verifica si existe antes de eliminar
            if (dao.findById(productoId).isEmpty()) {
                return error(404, "Producto no encontrado: " + productoId);
            }
            logger.log("PathParameters: " + request.getPathParameters());
            logger.log("ID a eliminar: " + productoId);
            logger.log("PathParameters: " + request.getPathParameters());
            logger.log("ID a eliminar: " + productoId);
            dao.deleteById(productoId);
            logger.log("Eliminación exitosa de producto: " + productoId);
            return success("Producto eliminado correctamente");
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