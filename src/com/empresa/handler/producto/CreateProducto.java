package com.empresa.handler.producto;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.empresa.handler.diccionario.service.CreateProductoAbstract;
import com.empresa.model.UserSession;
import com.empresa.util.MyLambdaLogger;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.*;
import static com.empresa.util.Avatar.generarColorDesdeNombre;
import static com.empresa.util.Avatar.obtenerIniciales;
public class CreateProducto extends CreateProductoAbstract {
    private static final LambdaLogger logger = new MyLambdaLogger();
    @Override
    protected String extractAuthToken(APIGatewayProxyRequestEvent input) {
        Map<String, String> headers = input.getHeaders();
        logger.log("headers = "+headers);
        if (headers != null) {
            String authHeader = headers.get("Authorization");
            logger.log("authHeader = "+authHeader);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                logger.log("token: "+authHeader.substring(7));
                return authHeader.substring(7);
            }
        }
        return null;
    }
    @Override
    protected UserSession validateAuthToken(String authToken, Context context) {
        if (authToken != null) {
            try {
                String jwkJson = "{\n" +
                        "  \"e\": \"AQAB\",\n" +
                        "  \"kty\": \"RSA\",\n" +
                        "  \"n\": \"tf1f25bAtZMGbwXkD_UNZ1z9eH91rNeZ-MIWeiGpE-g0u1Y6lBKi-vI6O0nFGTcilCTR6tcqXu9Ah6cwxPC6n0ORCUFd_VXRzjGHzrU5_Kofhb8_yPYFaAp3JAuvrj7PJNnY7RUZZibBuBpGIesrdwuDdBDprR4VzKuSKl7HZCMcAkhqNQjaSNt1UhwDb1mV22uu4NfjqaGSfp2LnRWnpUYTGZobTRE2S5kAw73kefewPCHiooryCZK_69NK5TAZWXWf-YPpFtdwmf7mFggonpWWrCuTWikEKicwdL6xn6oWYeuVlM80M2hUhNJNUSLB7fDHYli5NRqky315bsjvhw\"\n" +
                        "}";

                try {
                    PublicKey publicKey = getPublicKeyFromJWK(jwkJson);
                    Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) publicKey, null);
                    JWTVerifier verifier = JWT.require(algorithm)
                            .withIssuer("https://empresa-u8698.vm.elestio.app/realms/Empresa")
                            .build();
                    DecodedJWT token = verifier.verify(authToken);
                    // Extraer y utilizar reclamaciones
                    String subject = token.getSubject();
                    String issuer = token.getIssuer();
                    Map<String, Claim> claims = token.getClaims();
                    Claim claim_realm_access = claims.get("realm_access");
                    UserSession estudianteSession =new UserSession(context);
                    logger.log("claim_realm_access = " + claim_realm_access.asMap());
                    Map<String, Object> realmAccess = claim_realm_access != null ? claim_realm_access.asMap() : null;
                    /////Map<String, Object> realmAccess = claim_realm_access.asMap();
                    List<String> roles = null;
                    if (realmAccess != null) {
                        Object claim_realm_roles =  realmAccess.get("roles");
                        logger.log("roles = " + claim_realm_roles);
                        if (claim_realm_roles instanceof List) {
                            ///roles.addAll((List<String>) claimRealmRoles);
                            roles = new ArrayList<>((List<String>) claim_realm_roles);
                        } else if (claim_realm_roles instanceof String) {
                            ///roles.add((String) claimRealmRoles);
                            roles = Collections.singletonList((String) claim_realm_roles);
                        } else {
                            System.out.println("Invalid realm_access roles format.");
                        }
                        System.out.println("Subject: " + subject);
                        System.out.println("Issuer: " + issuer);
                        // Mostrar roles si están presentes
                        if (roles != null) {
                            System.out.println("Roles: ");
                            for (String role : roles) {
                                System.out.println(role);
                                // Ejemplo de verificación de rol específico
                                if ("user".equals(role)) {
                                    System.out.println("El usuario tiene el rol 'user'.");
                                    // Aquí puedes agregar lógica adicional relacionada con el rol 'user'
                                }
                            }
                            userSession.setRoles(roles);
                        } else {
                            System.out.println("No roles found.");
                        }
                    }
                    userSession.setUserId(token.getClaim("sid").asString());
                    userSession.setEmail(token.getClaim("email").asString());
                    userSession.setName(token.getClaim("name").asString());
                    userSession.setNombrePerfil(token.getClaim("name").asString());
                    userSession.setNombres(token.getClaim("given_name").asString());
                    userSession.setApellidos(token.getClaim("name").asString());
                    String picture = token.getClaim("picture").asString();
                    userSession.setFotoPerfil((picture != null && !picture.isBlank()) ? picture : "nullo");
                    // Obtener las iniciales a partir del nombre completo
                    String initials = obtenerIniciales(token.getClaim("name").asString());
                    userSession.setInitials(initials);

                    // Generar un color (aleatorio o basado en el nombre)
                    String color = generarColorDesdeNombre(token.getClaim("name").asString());
                    userSession.setColor(color);

                    userSession.setGivenName(token.getClaim("given_name").asString());
                    userSession.setFamilyName(token.getClaim("family_name").asString());
                    //userSession.setAttribute("sesione", token.getClaim("sid").asString());
                    userSession.setNivel("NIVEL basic");

                    // Añadir atributos personalizados
                    //userSession.setAttribute("sesione", token.getClaim("sid").asString());
                    userSession.setAttribute("sesione", token.getClaim("sub").asString());//sub=id_usuario
                    userSession.setAttribute("token_id", token.getClaim("jti").asString());//id del token
                    userSession.setAttribute("token_session", token.getClaim("session_state").asString());//sid del token

                    userSession.setAttribute("custom_attribute", "Custom value");
                    // Verificar datos adicionales para lógica personalizada
                    if ("admin".equals(token.getClaim("preferred_username").asString())) {
                        userSession.setNivel("NIVEL admin");
                    }

                    ////viene desde json de front userSession.setIdPregunta("1 one");
                    return estudianteSession;
                } catch (JWTVerificationException exception) {
                    //System.out.println("Token verification failed: " + exception.getMessage());
                } catch (Exception e) {
                    //System.out.println("Exception occurred: " + e.getMessage());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    private static PublicKey getPublicKeyFromJWK(String jwkJson) throws Exception {
        Moshi moshi = new Moshi.Builder().build();
        Type type = Types.newParameterizedType(Map.class, String.class, String.class);
        JsonAdapter<Map<String, String>> adapter = moshi.adapter(type);
        Map<String, String> jwk = adapter.fromJson(jwkJson);

        BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(jwk.get("n")));
        BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(jwk.get("e")));
        RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(spec);
    }
    @Override
    protected void addAuthorizationHeaders(UserSession authInfo, APIGatewayProxyRequestEvent request) {
        if (authInfo != null) {
            logger.log("authInfo = "+authInfo);
            request.getHeaders().put("X-UserId", authInfo.getUserId());
            request.getHeaders().put("X-Roles", String.join(",", authInfo.getRoles()));
            logger.log("request.getHeaders() X-UserId = "+request.getHeaders().get("X-UserId"));
            logger.log("request.getHeaders() X-Roles = "+request.getHeaders().get("X-Roles"));
        } else {
            logger.log("authInfo is null, cannot add authorization headers");
        }
    }
}