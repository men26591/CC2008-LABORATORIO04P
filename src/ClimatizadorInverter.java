public class ClimatizadorInverter extends Climatizador {
    public ClimatizadorInverter(String codigo, Oficina oficina, double potenciaNominal) {
        super(codigo, oficina, potenciaNominal);
    }

    public int getNivelMinimo() {
        return 10;
    }

    protected double calcularConsumoHoraKwh() {
        return super.calcularConsumoHoraKwh() * 0.80;
    }
}
