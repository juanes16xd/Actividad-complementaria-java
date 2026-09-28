package Controlador;
import Modelo.EmpleadoAdministrativo;
import Modelo.EmpleadoBase;
import Modelo.RepositorioEmpleados;

import java.util.ArrayList;
public class EmpleadoControlador {

    public static final String[] TIPOS_EMPLEADO = {
            "Operativo",
            "Administrativo"
    };

    private RepositorioEmpleados repositorio;
    private ArrayList<String> historial;

    public EmpleadoControlador() {
        repositorio = new RepositorioEmpleados();
        historial = new ArrayList<>();

        cargarDatosPrueba();
    }

    private void cargarDatosPrueba() {

        String[] cedulas = {"1001", "1002", "1003", "1004"};
        String[] nombres = {"Juan", "Laura", "Marta", "Carlos"};
        double[] salarios = {2500000, 1800000, 2200000, 3350000};

        for (int i = 0; i < cedulas.length; i++) {

            EmpleadoBase empleado;

            if (i % 2 == 0) {
                empleado = new EmpleadoBase(
                        cedulas[i],
                        nombres[i],
                        salarios[i]
                );
            } else {
                empleado = new EmpleadoAdministrativo(
                        cedulas[i],
                        nombres[i],
                        salarios[i],
                        0
                );
            }

            repositorio.agregar(empleado);
        }
    }

    public boolean esNumeroValido(String texto) {

        if (texto == null || texto.isEmpty()) {
            return false;
        }

        int puntos = 0;

        for (int i = 0; i < texto.length(); i++) {

            char caracter = texto.charAt(i);

            if (caracter == '.') {
                puntos++;

                if (puntos > 1) {
                    return false;
                }

            } else if (!Character.isDigit(caracter)) {
                return false;
            }
        }

        return true;
    }

    private String validar(String cedula, String nombre, String salario, String tipo, String bonificacion) {

        if (cedula == null || cedula.trim().isEmpty()) {
            return "La cédula es obligatoria.";
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre es obligatorio.";
        }

        if (!esNumeroValido(salario)) {
            return "El salario debe ser un número válido.";
        }

        if (tipo.equals("Administrativo")
                && !esNumeroValido(bonificacion)) {

            return "La bonificación debe ser un número válido.";
        }

        return "";
    }

    private EmpleadoBase construirEmpleado(String cedula, String nombre, String salario, String tipo, String bonificacion) {

        double salarioBase = Double.parseDouble(salario);

        if (tipo.equals("Administrativo")) {

            double bono = Double.parseDouble(bonificacion);

            return new EmpleadoAdministrativo(
                    cedula,
                    nombre,
                    salarioBase,
                    bono
            );
        }

        return new EmpleadoBase(
                cedula,
                nombre,
                salarioBase
        );
    }

    public String agregarEmpleado(String cedula, String nombre,
                                  String salario, String tipo,
                                  String bonificacion) {

        String error = validar(
                cedula,
                nombre,
                salario,
                tipo,
                bonificacion
        );

        if (!error.isEmpty()) {
            return error;
        }

        if (repositorio.buscar(cedula) != null) {
            return "Ya existe un empleado con esa cédula.";
        }

        EmpleadoBase empleado = construirEmpleado(
                cedula,
                nombre,
                salario,
                tipo,
                bonificacion
        );

        repositorio.agregar(empleado);

        historial.add("Agregado: " + cedula + " - " + nombre);

        return "Empleado agregado correctamente.";
    }

    public EmpleadoBase buscarEmpleado(String cedula) {
        return repositorio.buscar(cedula);
    }

    public String actualizarEmpleado(String cedula, String nombre,
                                     String salario, String tipo,
                                     String bonificacion) {

        String error = validar(
                cedula,
                nombre,
                salario,
                tipo,
                bonificacion
        );

        if (!error.isEmpty()) {
            return error;
        }

        if (repositorio.buscar(cedula) == null) {
            return "No existe un empleado con esa cédula.";
        }

        EmpleadoBase empleado = construirEmpleado(
                cedula,
                nombre,
                salario,
                tipo,
                bonificacion
        );

        repositorio.actualizar(empleado);

        historial.add("Actualizado: " + cedula + " - " + nombre);

        return "Empleado actualizado correctamente.";
    }

    public String eliminarEmpleado(String cedula) {

        if (repositorio.buscar(cedula) == null) {
            return "No existe un empleado con esa cédula.";
        }

        repositorio.eliminar(cedula);

        historial.add("Eliminado: " + cedula);

        return "Empleado eliminado correctamente.";
    }

    public ArrayList<EmpleadoBase> obtenerEmpleados() {
        return repositorio.listarTodos();
    }

    public double calcularTotalNomina() {

        double total = 0;

        for (EmpleadoBase empleado : repositorio.listarTodos()) {
            total += empleado.calcularSalarioTotal();
        }

        return total;
    }

    public ArrayList<String> obtenerHistorial() {
        return historial;
    }
}
