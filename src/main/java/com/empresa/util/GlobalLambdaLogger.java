package com.empresa.util;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

public class GlobalLambdaLogger implements LambdaLogger {

    @Override
    public void log(String message) {
        System.out.println(message); // Local log
    }

    @Override
    public void log(byte[] message) {
        System.out.println(new String(message)); // Local log
    }

    /**
     * Log básico del request usando solo System.out (útil en tests locales).
     */
    public static void logRequest(APIGatewayProxyRequestEvent request) {
        logRequest(request, null);
    }

    /**
     * Log completo del request con soporte para LambdaLogger (para CloudWatch).
     */
    public static void logRequest(APIGatewayProxyRequestEvent request, LambdaLogger logger) {
        if (request == null) {
            log("🛑 Request is null", logger);
            return;
        }

        log("===== 🌐 NUEVA SOLICITUD =====", logger);
        log("📎 HTTP Method: " + request.getHttpMethod(), logger);
        log("📍 Path: " + request.getPath(), logger);
        log("📫 Headers: " + request.getHeaders(), logger);
        log("🔍 Query Parameters: " + request.getQueryStringParameters(), logger);
        log("🧩 Path Parameters: " + request.getPathParameters(), logger);
        log("📦 Body: " + request.getBody(), logger);
        log("==============================", logger);
    }

    /**
     * Utilidad interna para loguear con o sin LambdaLogger.
     */
    private static void log(String message, LambdaLogger logger) {
        if (logger != null) {
            logger.log(message + "\n");
        } else {
            System.out.println(message);
        }
    }
}
