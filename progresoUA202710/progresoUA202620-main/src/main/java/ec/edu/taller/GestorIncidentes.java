package ec.edu.taller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class GestorIncidentes {

    private final Queue<IncidenteSeguridad> colaIncidentes;
    private final int capacidadMaxima;

    public GestorIncidentes(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
        this.colaIncidentes = new PriorityQueue<>(Comparator.comparingInt(IncidenteSeguridad::getPrioridad));
    }

    public boolean registrarIncidente(IncidenteSeguridad incidente) {
        if (incidente == null) {
            return false;
        }

        String codigo = incidente.getCodigo();
        if (codigo == null || codigo.trim().isEmpty()) {
            return false;
        }

        if (colaIncidentes.size() >= capacidadMaxima) {
            return false;
        }

        if (existeIncidente(codigo)) {
            return false;
        }

        return colaIncidentes.offer(incidente);
    }

    public boolean existeIncidente(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return false;
        }
        for (IncidenteSeguridad inc : colaIncidentes) {
            if (inc != null && codigo.equals(inc.getCodigo())) {
                return true;
            }
        }
        return false;
    }

    public IncidenteSeguridad consultarSiguienteIncidente() {
        return colaIncidentes.peek();
    }

    public IncidenteSeguridad atenderSiguienteIncidente() {
        IncidenteSeguridad atendido = colaIncidentes.poll();
        if (atendido != null) {
            atendido.setEstado("ATENDIDO");
        }
        return atendido;
    }

    public int contarIncidentesPendientes() {
        return colaIncidentes.size();
    }

    public int consultarEspaciosDisponibles() {
        return Math.max(0, capacidadMaxima - colaIncidentes.size());
    }

    public int contarPendientes() {
        return contarIncidentesPendientes();
    }

    public int obtenerEspaciosDisponibles() {
        return consultarEspaciosDisponibles();
    }

    public List<IncidenteSeguridad> listarIncidentes() {
        List<IncidenteSeguridad> lista = new ArrayList<>(colaIncidentes);
        lista.sort(Comparator.comparingInt(IncidenteSeguridad::getPrioridad));
        return lista;
    }
}