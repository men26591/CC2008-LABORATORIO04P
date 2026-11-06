public class Climatizador extends Dispositivo implements Regulable, Medidor, GeneradorAlertas {
    private int nivel;

    public Climatizador(String codigo, Oficina oficina, double potenciaNominal) {
        super(codigo, oficina, potenciaNominal);
        nivel = 50;
    }

    public int getNivel() {
        return nivel;
    }

    public int getNivelMinimo() {
        return 20;
    }

    public int getNivelMaximo() {
        return 100;
    }

    public boolean ajustarNivel(int nivel) {
        if (!isEncendido() || nivel < getNivelMinimo() || nivel > getNivelMaximo()) {
            return false;
        }
        this.nivel = nivel;
        return true;
    }

    public double getLecturaMinima() {
        return -10;
    }

    public double getLecturaMaxima() {
        return 60;
    }

    public String getUnidad() {
        return "°C";
    }

    public Severidad evaluarSeveridad(double temperatura) {
        if (temperatura >= 40) {
            return new Severidad("Crítica", 30, 1, 5.00);
        } else if (temperatura >= 32) {
            return new Severidad("Alta", 20, 4, 2.00);
        } else if (temperatura >= 28) {
            return new Severidad("Baja", 10, 24, 0.50);
        }
        return null;
    }

    public String getMensajeAlerta(double temperatura) {
        return "Temperatura alta registrada: " + temperatura + " °C";
    }

    protected double calcularConsumoHoraKwh() {
        double watts = getPotenciaNominal() * (0.30 + 0.70 * nivel / 100.0);
        return watts / 1000.0;
    }

    public String toString() {
        return super.toString() + " | Nivel: " + nivel + "%";
    }
}
