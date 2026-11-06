public class Luminaria extends Dispositivo implements Regulable {
    private int nivel;

    public Luminaria(String codigo, Oficina oficina, double potenciaNominal) {
        super(codigo, oficina, potenciaNominal);
        nivel = 100;
    }

    public int getNivel() {
        return nivel;
    }

    public int getNivelMinimo() {
        return 10;
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

    protected double calcularConsumoHoraKwh() {
        return (getPotenciaNominal() * nivel / 100.0) / 1000.0;
    }

    public String toString() {
        return super.toString() + " | Nivel: " + nivel + "%";
    }
}
