package com.empresa.model;

import com.amazonaws.services.lambda.runtime.Context;
import com.squareup.moshi.Json;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class UserSession implements Serializable {
    @Json(ignore = true)
    private final transient Context awsContext;
    private Map<String, Object> attributes;

    private String fbGet;
    private String userId;
    private String emailVerified;
    private String name;
    private String preferredUsername;
    private String givenName;
    private String familyName;
    private String email;
    private List<String> roles;
    private String password;
    private String fotoPerfil;
    private String nombrePerfil;
    private String nombres;
    private String apellidos;
    private String fecNacimiento;
    private String genero;
    private String nivel;
    private String sesionConectado;
    private String timeAge;
    private List<String> categorias;
    private String rol;
    private String urlmenuexams;
    private String atributo;
    private String initials;
    private String color;
    private String sessionId;

    public UserSession(Context awsContext) {
        this.awsContext = awsContext;
        this.attributes = new HashMap<>();
    }
    
    public Context getAwsContext() {
        return awsContext;
    }
    public void setAttribute(String key, Object value) {
        this.attributes.put(key, value);
    }
    public Object getAttribute(String key) {
        return this.attributes.get(key);
    }

    public String getFbGet() {
        return fbGet;
    }

    public void setFbGet(String fbGet) {
        this.fbGet = fbGet;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(String emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPreferredUsername() {
        return preferredUsername;
    }

    public void setPreferredUsername(String preferredUsername) {
        this.preferredUsername = preferredUsername;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getNombrePerfil() {
        return nombrePerfil;
    }

    public void setNombrePerfil(String nombrePerfil) {
        this.nombrePerfil = nombrePerfil;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getFecNacimiento() {
        return fecNacimiento;
    }

    public void setFecNacimiento(String fecNacimiento) {
        this.fecNacimiento = fecNacimiento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getSesionConectado() {
        return sesionConectado;
    }

    public void setSesionConectado(String sesionConectado) {
        this.sesionConectado = sesionConectado;
    }

    public String getTimeAge() {
        return timeAge;
    }

    public void setTimeAge(String timeAge) {
        this.timeAge = timeAge;
    }

    public List<String> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<String> categorias) {
        this.categorias = categorias;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getUrlmenuexams() {
        return urlmenuexams;
    }

    public void setUrlmenuexams(String urlmenuexams) {
        this.urlmenuexams = urlmenuexams;
    }

    public String getAtributo() {
        return atributo;
    }

    public void setAtributo(String atributo) {
        this.atributo = atributo;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}