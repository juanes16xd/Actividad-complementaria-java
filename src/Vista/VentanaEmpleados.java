package Vista;
import Controlador.EmpleadoControlador;
import Modelo.EmpleadoAdministrativo;
import Modelo.EmpleadoBase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class VentanaEmpleados extends JFrame{

    private EmpleadoControlador controlador;

    private JTextField txtCedula;
    private JTextField txtNombre;
    private JTextField txtSalario;
    private JTextField txtBonificacion;

    private JComboBox<String> cmbTipo;

    private JButton btnAgregar;
    private JButton btnBuscar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnHistorial;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JLabel lblResumen;

    public VentanaEmpleados(EmpleadoControlador controlador) {

        this.controlador = controlador;

        setTitle("Sistema de Talento Humano");
        setSize(780, 540);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        construirVentana();
        conectarEventos();
        refrescarTabla();
    }

    private void construirVentana() {

        setLayout(new BorderLayout());

        // FORMULARIO

        JPanel formulario = new JPanel(new GridLayout(5, 2, 5, 5));

        txtCedula = new JTextField();
        txtNombre = new JTextField();
        txtSalario = new JTextField();
        txtBonificacion = new JTextField();

        cmbTipo = new JComboBox<>(EmpleadoControlador.TIPOS_EMPLEADO);

        formulario.add(new JLabel("Cédula:"));
        formulario.add(txtCedula);

        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);

        formulario.add(new JLabel("Salario base:"));
        formulario.add(txtSalario);

        formulario.add(new JLabel("Tipo:"));
        formulario.add(cmbTipo);

        formulario.add(new JLabel("Bonificación:"));
        formulario.add(txtBonificacion);

        // BOTONES

        JPanel panelBotones = new JPanel(new FlowLayout());

        btnAgregar = new JButton("Agregar");
        btnBuscar = new JButton("Buscar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");
        btnHistorial = new JButton("Historial");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnHistorial);

        JPanel parteSuperior = new JPanel(new BorderLayout());

        parteSuperior.add(formulario, BorderLayout.CENTER);
        parteSuperior.add(panelBotones, BorderLayout.SOUTH);

        add(parteSuperior, BorderLayout.NORTH);

        // TABLA

        String[] columnas = {
                "Cédula",
                "Nombre",
                "Tipo",
                "Salario base",
                "Salario total"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);

        JScrollPane scroll = new JScrollPane(tabla);

        add(scroll, BorderLayout.CENTER);

        // RESUMEN

        lblResumen = new JLabel("Empleados: 0 | Nómina: $0");

        add(lblResumen, BorderLayout.SOUTH);

        txtBonificacion.setEnabled(false);
    }

    // EVENTOS

    private void conectarEventos() {

        cmbTipo.addActionListener(e -> {

            boolean administrativo = cmbTipo.getSelectedItem().toString().equals("Administrativo");
            txtBonificacion.setEnabled(administrativo);

            if (!administrativo) {
                txtBonificacion.setText("");
            }
        });

        btnAgregar.addActionListener(e -> agregarEmpleado());

        btnBuscar.addActionListener(e -> buscarEmpleado());

        btnActualizar.addActionListener(e -> actualizarEmpleado());

        btnEliminar.addActionListener(e -> eliminarEmpleado());

        btnLimpiar.addActionListener(e -> limpiarFormulario());

        btnHistorial.addActionListener(e -> mostrarHistorial());
    }

    // AGREGAR

    private void agregarEmpleado() {

        String cedula = txtCedula.getText();
        String nombre = txtNombre.getText();
        String salario = txtSalario.getText();
        String tipo = cmbTipo.getSelectedItem().toString();
        String bonificacion = txtBonificacion.getText();

        String resultado = controlador.agregarEmpleado(
                cedula,
                nombre,
                salario,
                tipo,
                bonificacion
        );

        mostrarResultado(resultado);
    }

    // BUSCAR

    private void buscarEmpleado() {

        String cedula = txtCedula.getText().trim();

        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribe una cédula para buscar.");
            return;
        }

        EmpleadoBase empleado = controlador.buscarEmpleado(cedula);

        if (empleado == null) {

            JOptionPane.showMessageDialog(this, "No se encontró el empleado.");

            return;
        }

        txtCedula.setText(empleado.getCedula());
        txtNombre.setText(empleado.getNombre());
        txtSalario.setText(String.valueOf(empleado.getSalarioBase()));

        cmbTipo.setSelectedItem(empleado.getTipo());

        if (empleado instanceof EmpleadoAdministrativo) {

            EmpleadoAdministrativo administrativo = (EmpleadoAdministrativo) empleado;

            txtBonificacion.setEnabled(true);

            txtBonificacion.setText(String.valueOf(administrativo.getBonificacion()));

        } else {

            txtBonificacion.setText("");
            txtBonificacion.setEnabled(false);
        }
    }

    // ACTUALIZAR

    private void actualizarEmpleado() {

        String cedula = txtCedula.getText();
        String nombre = txtNombre.getText();
        String salario = txtSalario.getText();
        String tipo = cmbTipo.getSelectedItem().toString();
        String bonificacion = txtBonificacion.getText();

        String resultado =
                controlador.actualizarEmpleado(
                        cedula,
                        nombre,
                        salario,
                        tipo,
                        bonificacion
                );

        mostrarResultado(resultado);
    }

    // ELIMINAR

    private void eliminarEmpleado() {

        String cedula = txtCedula.getText().trim();

        if (cedula.isEmpty()) {

            JOptionPane.showMessageDialog(this, "Escribe una cédula.");

            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que quieres eliminar este empleado?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION
        );

        if (respuesta == JOptionPane.YES_OPTION) {

            String resultado = controlador.eliminarEmpleado(cedula);

            mostrarResultado(resultado);
        }
    }

    // LIMPIAR

    private void limpiarFormulario() {

        txtCedula.setText("");
        txtNombre.setText("");
        txtSalario.setText("");
        txtBonificacion.setText("");

        cmbTipo.setSelectedIndex(0);

        txtBonificacion.setEnabled(false);

        tabla.clearSelection();
    }

    // HISTORIAL

    private void mostrarHistorial() {

        ArrayList<String> historial = controlador.obtenerHistorial();

        if (historial.isEmpty()) {

            JOptionPane.showMessageDialog(this, "No hay operaciones registradas.");

            return;
        }

        StringBuilder texto = new StringBuilder();

        for (int i = 0; i < historial.size(); i++) {

            texto.append(i + 1)
                    .append(". ")
                    .append(historial.get(i))
                    .append("\n");
        }

        JTextArea area = new JTextArea(texto.toString());

        area.setEditable(false);

        JScrollPane scroll = new JScrollPane(area);

        scroll.setPreferredSize(new Dimension(450, 250));

        JOptionPane.showMessageDialog(this, scroll, "Historial", JOptionPane.INFORMATION_MESSAGE);
    }

    // TABLA

    private void refrescarTabla() {

        modeloTabla.setRowCount(0);

        ArrayList<EmpleadoBase> empleados = controlador.obtenerEmpleados();

        for (EmpleadoBase empleado : empleados) {

            Object[] fila = {

                    empleado.getCedula(),

                    empleado.getNombre(),

                    empleado.getTipo(),

                    formatoPesos(empleado.getSalarioBase()),

                    formatoPesos(empleado.calcularSalarioTotal())
            };

            modeloTabla.addRow(fila);
        }

        double totalNomina =
                controlador.calcularTotalNomina();

        lblResumen.setText(
                "Empleados: " + empleados.size() + " | Nómina: " + formatoPesos(totalNomina)
        );
    }

    // MENSAJES

    private void mostrarResultado(String resultado) {

        JOptionPane.showMessageDialog(this, resultado);

        refrescarTabla();
    }

    // FORMATO DINERO

    private String formatoPesos(double valor) {

        return String.format("$%,.0f", valor);
    }
}
