public class SensorHumo extends Dispositivo implements Medidor, GeneradorAlertas {
    public SensorHumo(String codigo, Oficina oficina, double potenciaNominal) {
        super(codigo, oficina, potenciaNominal);
        fijarEncendido(true);
    }

    public boolean apagar() {
        return false;
    }

    public double getLecturaMinima() {
        return 0;
    }

    public double getLecturaMaxima() {
        return 1000;
    }

    public String getUnidad() {
        return "ppm";
    }

    public Severidad evaluarSeveridad(double humo) {
        if (humo >= 300) {
            return new Severidad("Crítica", 30, 1, 5.00);
        } else if (humo >= 150) {
            return new Severidad("Alta", 20, 4, 2.00);
        }
        return null;
    }

    public String getMensajeAlerta(double humo) {
        return "Concentración de humo registrada: " + humo + " ppm";
    }

    protected double calcularConsumoHoraKwh() {
        return getPotenciaNominal() / 1000.0;
    }
}
