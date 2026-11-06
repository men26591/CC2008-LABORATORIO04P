public class Factura {
    private static final double TARIFA_VALLE = 0.85;
    private static final double TARIFA_NORMAL = 1.40;
    private static final double TARIFA_PUNTA = 2.10;

    private Oficina oficina;
    private int dia;
    private double kwhValle;
    private double kwhNormal;
    private double kwhPunta;
    private double creditos;
    private double total;

    public Factura(Oficina oficina, int dia, double kwhValle, double kwhNormal,
                   double kwhPunta, double creditos) {
        this.oficina = oficina;
        this.dia = dia;
        this.kwhValle = kwhValle;
        this.kwhNormal = kwhNormal;
        this.kwhPunta = kwhPunta;
        this.creditos = creditos;
        double subtotal = getSubtotal();
        total = subtotal - creditos;
        if (total < 0) {
            total = 0;
        }
        total = Math.round(total * 100.0) / 100.0;
    }

    public double getSubtotal() {
        return kwhValle * TARIFA_VALLE + kwhNormal * TARIFA_NORMAL
                + kwhPunta * TARIFA_PUNTA;
    }

    public double getTotal() {
        return total;
    }

    public double getCreditos() {
        return creditos;
    }

    public double getKwhValle() {
        return kwhValle;
    }

    public double getKwhNormal() {
        return kwhNormal;
    }

    public double getKwhPunta() {
        return kwhPunta;
    }

    public double getMontoValle() {
        return kwhValle * TARIFA_VALLE;
    }

    public double getMontoNormal() {
        return kwhNormal * TARIFA_NORMAL;
    }

    public double getMontoPunta() {
        return kwhPunta * TARIFA_PUNTA;
    }

    public Oficina getOficina() {
        return oficina;
    }

    public int getDia() {
        return dia;
    }

    public String toString() {
        return "Factura día " + dia + " | Oficina " + oficina.getCodigo()
                + " | Valle: " + kwhValle + " kWh, Q" + getMontoValle()
                + " | Normal: " + kwhNormal + " kWh, Q" + getMontoNormal()
                + " | Punta: " + kwhPunta + " kWh, Q" + getMontoPunta()
                + " | Créditos: Q" + creditos + " | Total: Q" + total;
    }
}
