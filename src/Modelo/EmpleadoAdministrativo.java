package Modelo;

public class EmpleadoAdministrativo extends EmpleadoBase{

    private double bonificacion;

    public EmpleadoAdministrativo(String cedula, String nombre,
                                  double salarioBase, double bonificacion) {

        super(cedula, nombre, salarioBase);
        this.bonificacion = bonificacion;
    }

    public double getBonificacion() {
        return bonificacion;
    }

    public void setBonificacion(double bonificacion) {
        this.bonificacion = bonificacion;
    }

    @Override
    public double calcularSalarioTotal() {
        return getSalarioBase() + bonificacion;
    }

    @Override
    public String getTipo() {
        return "Administrativo";
    }
}
