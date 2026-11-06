public abstract class Dispositivo implements Comparable<Dispositivo> {
    private String codigo;
    private Oficina oficina;
    private double potenciaNominal;
    private boolean encendido;
    private double consumoDiaKwh;

    protected Dispositivo(String codigo, Oficina oficina, double potenciaNominal) {
        this.codigo = codigo;
        this.oficina = oficina;
        this.potenciaNominal = potenciaNominal;
        encendido = false;
        consumoDiaKwh = 0;
    }

    public String getCodigo() {
        return codigo;
    }

    public Oficina getOficina() {
        return oficina;
    }

    public double getPotenciaNominal() {
        return potenciaNominal;
    }

    public boolean isEncendido() {
        return encendido;
    }

    public double getConsumoDiaKwh() {
        return consumoDiaKwh;
    }

    public boolean encender() {
        if (encendido) {
            return false;
        }
        encendido = true;
        return true;
    }

    public boolean apagar() {
        if (!encendido) {
            return false;
        }
        encendido = false;
        return true;
    }

    protected void fijarEncendido(boolean estado) {
        encendido = estado;
    }

    protected abstract double calcularConsumoHoraKwh();

    public double consumirHora() {
        if (!encendido) {
            return 0;
        }
        double consumo = calcularConsumoHoraKwh();
        consumoDiaKwh = consumoDiaKwh + consumo;
        return consumo;
    }

    public void reiniciarConsumoDia() {
        consumoDiaKwh = 0;
    }

    public int compareTo(Dispositivo otro) {
        return codigo.compareTo(otro.codigo);
    }

    public String toString() {
        String estado = "Apagado";
        if (encendido) {
            estado = "Encendido";
        }
        return "Código: " + codigo + " | Estado: " + estado
                + " | Consumo del día: " + consumoDiaKwh + " kWh";
    }
}
