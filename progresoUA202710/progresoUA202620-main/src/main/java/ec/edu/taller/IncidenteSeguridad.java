package ec.edu.taller;

import java.util.Objects;

public class IncidenteSeguridad implements Comparable<IncidenteSeguridad> {

    private String codigo;
    private String tipo;
    private String descripcion;
    private int prioridad;
    private String estado;

    public IncidenteSeguridad() {
    }

    public IncidenteSeguridad(String codigo, String tipo, String descripcion, int prioridad) {
        this(codigo, tipo, descripcion, prioridad, "PENDIENTE");
    }

    public IncidenteSeguridad(String codigo, String tipo, String descripcion, int prioridad, String estado) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public int compareTo(IncidenteSeguridad otro) {
        if (otro == null) {
            return -1;
        }
        return Integer.compare(this.prioridad, otro.prioridad);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IncidenteSeguridad that = (IncidenteSeguridad) o;
        return Objects.equals(codigo, that.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return codigo + " | " + tipo + " | " + prioridad + " | " + estado;
    }
}