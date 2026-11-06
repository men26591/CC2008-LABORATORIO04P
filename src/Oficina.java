public class Oficina {
    private String codigo;
    private String inquilino;
    private Dispositivo[] dispositivos;
    private int cantidadDispositivos;
    private double kwhValle;
    private double kwhNormal;
    private double kwhPunta;
    private double creditosDia;

    public Oficina(String codigo, String inquilino) {
        this.codigo = codigo;
        this.inquilino = inquilino;
        dispositivos = new Dispositivo[5];
        cantidadDispositivos = 0;
        kwhValle = 0;
        kwhNormal = 0;
        kwhPunta = 0;
        creditosDia = 0;
    }

    public boolean agregarDispositivo(Dispositivo dispositivo) {
        for (int i = 0; i < cantidadDispositivos; i++) {
            if (dispositivos[i].getCodigo().equals(dispositivo.getCodigo())) {
                return false;
            }
        }
        if (cantidadDispositivos == dispositivos.length) {
            Dispositivo[] nuevo = new Dispositivo[dispositivos.length * 2];
            for (int i = 0; i < cantidadDispositivos; i++) {
                nuevo[i] = dispositivos[i];
            }
            dispositivos = nuevo;
        }

        int posicion = cantidadDispositivos;
        while (posicion > 0 && dispositivos[posicion - 1].compareTo(dispositivo) > 0) {
            dispositivos[posicion] = dispositivos[posicion - 1];
            posicion--;
        }
        dispositivos[posicion] = dispositivo;
        cantidadDispositivos++;
        return true;
    }

    public Dispositivo buscarDispositivo(String codigo) {
        for (int i = 0; i < cantidadDispositivos; i++) {
            if (dispositivos[i].getCodigo().equals(codigo)) {
                return dispositivos[i];
            }
        }
        return null;
    }

    public void acumularConsumo(int franja, double kwh) {
        if (franja == 0) {
            kwhValle = kwhValle + kwh;
        } else if (franja == 1) {
            kwhNormal = kwhNormal + kwh;
        } else {
            kwhPunta = kwhPunta + kwh;
        }
    }

    public void acreditar(double monto) {
        creditosDia = creditosDia + monto;
    }

    public Factura generarFactura(int dia) {
        return new Factura(this, dia, kwhValle, kwhNormal, kwhPunta, creditosDia);
    }

    public void reiniciarDia() {
        kwhValle = 0;
        kwhNormal = 0;
        kwhPunta = 0;
        creditosDia = 0;
        for (int i = 0; i < cantidadDispositivos; i++) {
            dispositivos[i].reiniciarConsumoDia();
        }
    }

    public String getCodigo() {
        return codigo;
    }

    public String getInquilino() {
        return inquilino;
    }

    public int getCantidadDispositivos() {
        return cantidadDispositivos;
    }

    public Dispositivo getDispositivo(int indice) {
        if (indice < 0 || indice >= cantidadDispositivos) {
            return null;
        }
        return dispositivos[indice];
    }

    public double getCreditosDia() {
        return creditosDia;
    }
}
