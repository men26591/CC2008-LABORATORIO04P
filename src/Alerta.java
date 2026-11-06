public class Alerta implements Comparable<Alerta> {
    private int codigo;
    private Dispositivo dispositivo;
    private int horaGeneracion;
    private Severidad severidad;
    private String mensaje;
    private boolean atendida;
    private int horaAtencion;
    private int horasRetraso;
    private double credito;

    public Alerta(int codigo, Dispositivo dispositivo, int horaGeneracion,
                  Severidad severidad, String mensaje) {
        this.codigo = codigo;
        this.dispositivo = dispositivo;
        this.horaGeneracion = horaGeneracion;
        this.severidad = severidad;
        this.mensaje = mensaje;
        atendida = false;
        horaAtencion = -1;
        horasRetraso = 0;
        credito = 0;
    }

    public double atender(int horaAtencion) {
        if (atendida) {
            return 0;
        }
        this.atendida = true;
        this.horaAtencion = horaAtencion;
        int horasTranscurridas = horaAtencion - horaGeneracion;
        horasRetraso = horasTranscurridas - severidad.getPlazoHoras();
        if (horasRetraso < 0) {
            horasRetraso = 0;
        }
        credito = horasRetraso * severidad.getCreditoPorHora();
        return credito;
    }

    public boolean isAtendida() {
        return atendida;
    }

    public boolean tuvoRetraso() {
        return horasRetraso > 0;
    }

    public int getCodigo() {
        return codigo;
    }

    public Dispositivo getDispositivo() {
        return dispositivo;
    }

    public int getHoraGeneracion() {
        return horaGeneracion;
    }

    public int getHoraAtencion() {
        return horaAtencion;
    }

    public Severidad getSeveridad() {
        return severidad;
    }

    public String getMensaje() {
        return mensaje;
    }

    public int getHorasRetraso() {
        return horasRetraso;
    }

    public double getCredito() {
        return credito;
    }

    public int compareTo(Alerta otra) {
        if (severidad.getPrioridad() > otra.severidad.getPrioridad()) {
            return -1;
        } else if (severidad.getPrioridad() < otra.severidad.getPrioridad()) {
            return 1;
        }
        if (horaGeneracion < otra.horaGeneracion) {
            return -1;
        } else if (horaGeneracion > otra.horaGeneracion) {
            return 1;
        }
        if (codigo < otra.codigo) {
            return -1;
        } else if (codigo > otra.codigo) {
            return 1;
        }
        return 0;
    }

    public String toString() {
        String estado = "Pendiente";
        if (atendida) {
            estado = "Atendida";
        }
        return "Alerta " + codigo + " | " + severidad.getNombre() + " | " + mensaje
                + " | Hora: " + horaGeneracion + " | Estado: " + estado;
    }
}
