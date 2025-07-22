package com.empresa.util;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

public class GlobalLambdaLogger implements LambdaLogger {
    @Override
    public void log(String message) {
        System.out.println(message);
    }

    @Override
    public void log(byte[] message) {
        System.out.println(new String(message));
    }

    public static void logRequest(APIGatewayProxyRequestEvent request) {
        if (request == null) {
            System.out.println("Request is null");
            return;
        }

        System.out.println("===== NUEVA SOLICITUD =====");
        System.out.println("HTTP Method: " + request.getHttpMethod());
        System.out.println("Path: " + request.getPath());
        System.out.println("Headers: " + request.getHeaders());
        System.out.println("Query String Parameters: " + request.getQueryStringParameters());
        System.out.println("Path Parameters: " + request.getPathParameters());
        System.out.println("Body: " + request.getBody());
        System.out.println("===========================");
    }
}