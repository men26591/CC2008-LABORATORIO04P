public class CamaraSeguridad extends Dispositivo implements GeneradorAlertas {
    public CamaraSeguridad(String codigo, Oficina oficina, double potenciaNominal) {
        super(codigo, oficina, potenciaNominal);
    }

    public Severidad evaluarSeveridad(double horaDelDia) {
        if (horaDelDia < 7 || horaDelDia >= 19) {
            return new Severidad("Alta", 20, 4, 2.00);
        }
        return null;
    }

    public String getMensajeAlerta(double horaDelDia) {
        return "Movimiento detectado fuera del horario laboral";
    }

    protected double calcularConsumoHoraKwh() {
        return getPotenciaNominal() / 1000.0;
    }
}
