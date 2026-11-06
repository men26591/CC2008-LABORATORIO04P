public interface GeneradorAlertas {
    Severidad evaluarSeveridad(double dato);
    String getMensajeAlerta(double dato);
}
