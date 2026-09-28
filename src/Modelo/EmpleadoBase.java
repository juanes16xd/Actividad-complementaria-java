package Modelo;

public class EmpleadoBase {

    private final String cedula;
    private String nombre;
    private double salarioBase;

    public EmpleadoBase(String cedula, String nombre, double salarioBase) {
        this.cedula = cedula;
        this.nombre = nombre;
        setSalarioBase(salarioBase);
    }

    public String getCedula() {
        return cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public double getSalarioBase() {
        return salarioBase;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setSalarioBase(double salarioBase) {
        if (salarioBase < 0) {
            throw new IllegalArgumentException(
                    "El salario no puede ser negativo."
            );
        }

        this.salarioBase = salarioBase;
    }

    public double calcularSalarioTotal() {
        return salarioBase;
    }

    public String getTipo() {
        return "Operativo";
    }
}
