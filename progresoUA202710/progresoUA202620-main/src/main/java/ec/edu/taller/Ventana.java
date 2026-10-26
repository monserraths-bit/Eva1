package ec.edu.taller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Ventana {

    // Componentes del árbol de Ventana.form (Pestaña 1: Ingreso de Datos)
    private JPanel Ventana;
    private JTabbedPane tabbedPane1;
    private JLabel lblincidente;
    private JLabel lblcodigo;
    private JTextField txtcodigo;
    private JLabel lbl;
    private JComboBox<String> cbotipo;
    private JLabel lbldescripcion;
    private JTextArea txttarea;
    private JLabel txtprioridad;
    private JSpinner spinner1;
    private JLabel lblestado;
    private JButton btnregistrar;

    // Componentes de la Pestaña 2: Gestionar Incidentes
    private JTabbedPane tabbedPane2;
    private JTextArea txtarea1; // Pendientes
    private JTextArea txtarea2; // Disponibles
    private JTextArea txtarea3; // Capacidad
    private JTextArea txtinf1;  // Código, Tipo, Prioridad
    private JTextArea txtinf2;  // Estado, Descripción
    private JButton btnsig;     // Siguiente
    private JButton btnate;     // Atender
    private JButton btnlis;     // Listar
    private JTextArea txtlista; // Área para listado de incidentes

    private GestorIncidentes gestor;

    public Ventana() {
        // Capacidad inicial definida en 10
        this.gestor = new GestorIncidentes(10);

        // Predefinir los 8 registros iniciales
        cargarRegistrosPredefinidos();

        if (cbotipo != null && cbotipo.getItemCount() == 0) {
            cbotipo.addItem("Phishing");
            cbotipo.addItem("Malware");
            cbotipo.addItem("Acceso No Autorizado");
            cbotipo.addItem("Fuga de Datos");
            cbotipo.addItem("Otro");
        }

        // Prioridad limitada de 1 a 5
        if (spinner1 != null) {
            spinner1.setModel(new SpinnerNumberModel(1, 1, 5, 1));
        }

        if (txtarea1 != null) txtarea1.setEditable(false);
        if (txtarea2 != null) txtarea2.setEditable(false);
        if (txtarea3 != null) txtarea3.setEditable(false);
        if (txtinf1 != null) txtinf1.setEditable(false);
        if (txtinf2 != null) txtinf2.setEditable(false);
        if (txtlista != null) txtlista.setEditable(false);

        // Actualizar contadores (Pendientes: 8, Disponibles: 2, Capacidad: 10)
        actualizarContadores();

        // 1. Botón REGISTRAR
        if (btnregistrar != null) {
            btnregistrar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    registrarIncidente();
                }
            });
        }

        // 2. Botón SIGUIENTE (Consultar sin extraer)
        if (btnsig != null) {
            btnsig.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    consultarSiguiente();
                }
            });
        }

        // 3. Botón ATENDER (Retirar de la cola y cambiar a ATENDIDO)
        if (btnate != null) {
            btnate.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    atenderIncidente();
                }
            });
        }

        // 4. Botón LISTAR (Listar en txtlista ordenado de 1 a 5)
        if (btnlis != null) {
            btnlis.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    listarIncidentesEnTexto();
                }
            });
        }
    }

    private void cargarRegistrosPredefinidos() {
        gestor.registrarIncidente(new IncidenteSeguridad("INC-001", "Malware", "Ransomware detectado en servidor central", 1, "PENDIENTE"));
        gestor.registrarIncidente(new IncidenteSeguridad("INC-002", "Phishing", "Campaña masiva de correos fraudulentos a empleados", 2, "PENDIENTE"));
        gestor.registrarIncidente(new IncidenteSeguridad("INC-003", "Fuga de Datos", "Extracción no autorizada de base de clientes", 1, "PENDIENTE"));
        gestor.registrarIncidente(new IncidenteSeguridad("INC-004", "Acceso No Autorizado", "Múltiples intentos fallidos de login SSH desde IP externa", 3, "PENDIENTE"));
        gestor.registrarIncidente(new IncidenteSeguridad("INC-005", "Malware", "Troyano detectado en equipo de finanzas", 2, "PENDIENTE"));
        gestor.registrarIncidente(new IncidenteSeguridad("INC-006", "Otro", "Comportamiento anómalo en el tráfico de red interno", 4, "PENDIENTE"));
        gestor.registrarIncidente(new IncidenteSeguridad("INC-007", "Phishing", "Suplantación de identidad del portal bancario", 3, "PENDIENTE"));
        gestor.registrarIncidente(new IncidenteSeguridad("INC-008", "Otro", "Revisión de certificados SSL próximos a caducar", 5, "PENDIENTE"));
    }

    private void registrarIncidente() {
        if (txtcodigo == null || spinner1 == null) return;

        String codigo = txtcodigo.getText();
        String tipo = cbotipo != null ? String.valueOf(cbotipo.getSelectedItem()) : "General";
        String descripcion = txttarea != null ? txttarea.getText() : "";
        int prioridad = (Integer) spinner1.getValue();

        if (codigo == null || codigo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(Ventana, "El código no puede estar vacío ni contener solo espacios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (prioridad < 1 || prioridad > 5) {
            JOptionPane.showMessageDialog(Ventana, "La prioridad debe estar entre 1 (más alta) y 5 (más baja).", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (gestor.existeIncidente(codigo)) {
            JOptionPane.showMessageDialog(Ventana, "El código ya se encuentra registrado.", "Incidente duplicado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (gestor.consultarEspaciosDisponibles() <= 0) {
            JOptionPane.showMessageDialog(Ventana, "Capacidad máxima alcanzada (10 incidentes).", "Límite", JOptionPane.ERROR_MESSAGE);
            return;
        }

        IncidenteSeguridad nuevo = new IncidenteSeguridad(codigo, tipo, descripcion, prioridad, "PENDIENTE");
        boolean exito = gestor.registrarIncidente(nuevo);

        if (exito) {
            JOptionPane.showMessageDialog(Ventana, "Incidente registrado exitosamente con prioridad " + prioridad + ".", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            txtcodigo.setText("");
            if (txttarea != null) txttarea.setText("");
            spinner1.setValue(1);
            actualizarContadores();
        } else {
            JOptionPane.showMessageDialog(Ventana, "No se pudo registrar el incidente.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void consultarSiguiente() {
        IncidenteSeguridad siguiente = gestor.consultarSiguienteIncidente();
        if (siguiente == null) {
            limpiarDetalles();
            if (txtinf1 != null) txtinf1.setText("No hay incidentes pendientes.");
        } else {
            mostrarEnDetalles(siguiente);
        }
    }

    private void atenderIncidente() {
        IncidenteSeguridad atendido = gestor.atenderSiguienteIncidente();
        if (atendido == null) {
            JOptionPane.showMessageDialog(Ventana, "No hay incidentes pendientes para atender.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            mostrarEnDetalles(atendido);
            actualizarContadores();
            if (txtlista != null && !txtlista.getText().isEmpty()) {
                listarIncidentesEnTexto();
            }
        }
    }

    private void listarIncidentesEnTexto() {
        List<IncidenteSeguridad> lista = gestor.listarIncidentes();
        if (txtlista == null) return;

        if (lista.isEmpty()) {
            txtlista.setText("No hay incidentes pendientes en la cola.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-10s | %-6s | %-12s | %-18s | %s\n", "CÓDIGO", "PRIOR.", "ESTADO", "TIPO", "DESCRIPCIÓN"));
        sb.append("--------------------------------------------------------------------------------------------------------\n");

        for (IncidenteSeguridad inc : lista) {
            sb.append(String.format("%-10s | %-6d | %-12s | %-18s | %s\n",
                    inc.getCodigo(),
                    inc.getPrioridad(),
                    inc.getEstado(),
                    inc.getTipo(),
                    inc.getDescripcion()));
        }

        txtlista.setText(sb.toString());
    }

    private void mostrarEnDetalles(IncidenteSeguridad inc) {
        if (txtinf1 != null) {
            txtinf1.setText("Codigo: " + inc.getCodigo() + "\n" +
                    "Tipo: " + inc.getTipo() + "\n" +
                    "Prioridad: " + inc.getPrioridad() + (inc.getPrioridad() == 1 ? " (Máxima)" : ""));
        }
        if (txtinf2 != null) {
            txtinf2.setText("Estado: " + inc.getEstado() + "\n" +
                    "Descripcion: " + inc.getDescripcion());
        }
    }

    private void limpiarDetalles() {
        if (txtinf1 != null) txtinf1.setText("");
        if (txtinf2 != null) txtinf2.setText("");
    }

    private void actualizarContadores() {
        if (txtarea1 != null) txtarea1.setText(String.valueOf(gestor.contarIncidentesPendientes()));
        if (txtarea2 != null) txtarea2.setText(String.valueOf(gestor.consultarEspaciosDisponibles()));
        if (txtarea3 != null) txtarea3.setText("10");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Gestor de Incidentes de Seguridad");
            Ventana ventanaUI = new Ventana();
            frame.setContentPane(ventanaUI.Ventana);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}