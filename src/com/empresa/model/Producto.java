package com.empresa.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import java.time.LocalDate;

@DynamoDbBean
public class Producto {
    private String productoId;       // Clave primaria
    private String nombre;
    private String descripcion;
    private String categoria;
    private String nivel;          // Básico, Intermedio, Avanzado
    private String estado;         // Activo, Inactivo
    private LocalDate fechaCreacion;

    // Para frontend visual
    private String imagenUrl;
    private String dificultad;
    private String tiempo;

    // Para backend de carga dinámica
    private String rutaProducto;       // ruta de Backend
    private Integer productoInicial; // ej: 0
    private Integer productoFinal;   // ej: 15

    //
    private String visible;
    private String codigo;
    private String linea;
    private String codigoBarra;
    private String pdf;
    private String precio;
    private String precioEnLetras;
    private String rolename;
    private String tipo;


    @DynamoDbPartitionKey
    public String getProductoId() {
        return productoId;
    }

    public void setProductoId(String productoId) {
        this.productoId = productoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getRutaProducto() {
        return rutaProducto;
    }

    public void setRutaProducto(String rutaProducto) {
        this.rutaProducto = rutaProducto;
    }

    public Integer getProductoInicial() {
        return productoInicial;
    }

    public void setProductoInicial(Integer productoInicial) {
        this.productoInicial = productoInicial;
    }

    public Integer getProductoFinal() {
        return productoFinal;
    }

    public void setProductoFinal(Integer productoFinal) {
        this.productoFinal = productoFinal;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public String getCodigoBarra() {
        return codigoBarra;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getPrecioEnLetras() {
        return precioEnLetras;
    }

    public void setPrecioEnLetras(String precioEnLetras) {
        this.precioEnLetras = precioEnLetras;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}