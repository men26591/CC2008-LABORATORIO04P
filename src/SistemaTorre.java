public class SistemaTorre {
    private int horaSimulacion;
    private Oficina[] oficinas;
    private int cantidadOficinas;
    private BandejaAlertas bandeja;
    private Alerta[] alertasAtendidas;
    private int cantidadAtendidas;
    private Factura[] facturas;
    private int cantidadFacturas;
    private int contadorAlertas;

    public SistemaTorre() {
        horaSimulacion = 0;
        oficinas = new Oficina[5];
        cantidadOficinas = 0;
        bandeja = new BandejaAlertas();
        alertasAtendidas = new Alerta[10];
        cantidadAtendidas = 0;
        facturas = new Factura[10];
        cantidadFacturas = 0;
        contadorAlertas = 0;
    }

    public boolean registrarOficina(String codigo, String inquilino) {
        if (buscarOficina(codigo) != null) {
            return false;
        }
        if (cantidadOficinas == oficinas.length) {
            Oficina[] nuevo = new Oficina[oficinas.length * 2];
            for (int i = 0; i < cantidadOficinas; i++) {
                nuevo[i] = oficinas[i];
            }
            oficinas = nuevo;
        }
        oficinas[cantidadOficinas] = new Oficina(codigo, inquilino);
        cantidadOficinas++;
        return true;
    }

    public Oficina buscarOficina(String codigo) {
        for (int i = 0; i < cantidadOficinas; i++) {
            if (oficinas[i].getCodigo().equals(codigo)) {
                return oficinas[i];
            }
        }
        return null;
    }

    public boolean registrarDispositivo(String codigoOficina, Dispositivo dispositivo) {
        Oficina oficina = buscarOficina(codigoOficina);
        if (oficina == null || dispositivo == null || dispositivo.getPotenciaNominal() <= 0) {
            return false;
        }
        if (dispositivo.getOficina() != oficina || buscarDispositivo(dispositivo.getCodigo()) != null) {
            return false;
        }
        return oficina.agregarDispositivo(dispositivo);
    }

    public boolean encender(String codigo) {
        Dispositivo dispositivo = buscarDispositivo(codigo);
        if (dispositivo == null) {
            return false;
        }
        return dispositivo.encender();
    }

    public boolean apagar(String codigo) {
        Dispositivo dispositivo = buscarDispositivo(codigo);
        if (dispositivo == null) {
            return false;
        }
        return dispositivo.apagar();
    }

    public boolean ajustarNivel(String codigo, int nivel) {
        Dispositivo dispositivo = buscarDispositivo(codigo);
        if (dispositivo == null || !(dispositivo instanceof Regulable)) {
            return false;
        }
        Regulable regulable = (Regulable) dispositivo;
        return regulable.ajustarNivel(nivel);
    }

    public boolean registrarLectura(String codigo, double valor) {
        Dispositivo dispositivo = buscarDispositivo(codigo);
        if (dispositivo == null || !dispositivo.isEncendido()
                || !(dispositivo instanceof Medidor)
                || !(dispositivo instanceof GeneradorAlertas)) {
            return false;
        }
        Medidor medidor = (Medidor) dispositivo;
        if (valor < medidor.getLecturaMinima() || valor > medidor.getLecturaMaxima()) {
            return false;
        }
        generarAlerta(dispositivo, valor);
        return true;
    }

    public boolean registrarMovimiento(String codigo) {
        Dispositivo dispositivo = buscarDispositivo(codigo);
        if (!(dispositivo instanceof CamaraSeguridad) || !dispositivo.isEncendido()) {
            return false;
        }
        generarAlerta(dispositivo, getHoraDelDia());
        return true;
    }

    public void avanzarHora() {
        int horaDelDia = getHoraDelDia();
        int franja = franjaDe(horaDelDia);
        for (int i = 0; i < cantidadOficinas; i++) {
            Oficina oficina = oficinas[i];
            for (int j = 0; j < oficina.getCantidadDispositivos(); j++) {
                Dispositivo dispositivo = oficina.getDispositivo(j);
                double kwh = dispositivo.consumirHora();
                oficina.acumularConsumo(franja, kwh);
            }
        }
        horaSimulacion++;
        if (horaSimulacion % 24 == 0) {
            cerrarDia();
        }
    }

    public Alerta atenderAlerta() {
        Alerta alerta = bandeja.extraerMasUrgente();
        if (alerta == null) {
            return null;
        }
        double credito = alerta.atender(horaSimulacion);
        if (credito > 0) {
            alerta.getDispositivo().getOficina().acreditar(credito);
        }
        if (cantidadAtendidas == alertasAtendidas.length) {
            Alerta[] nuevo = new Alerta[alertasAtendidas.length * 2];
            for (int i = 0; i < cantidadAtendidas; i++) {
                nuevo[i] = alertasAtendidas[i];
            }
            alertasAtendidas = nuevo;
        }
        alertasAtendidas[cantidadAtendidas] = alerta;
        cantidadAtendidas++;
        return alerta;
    }

    public int getHoraDelDia() {
        return horaSimulacion % 24;
    }

    private void generarAlerta(Dispositivo dispositivo, double dato) {
        GeneradorAlertas generador = (GeneradorAlertas) dispositivo;
        Severidad severidad = generador.evaluarSeveridad(dato);
        if (severidad != null) {
            contadorAlertas++;
            Alerta alerta = new Alerta(contadorAlertas, dispositivo, horaSimulacion,
                    severidad, generador.getMensajeAlerta(dato));
            bandeja.agregar(alerta);
        }
    }

    private int franjaDe(int hora) {
        if (hora >= 22 || hora < 6) {
            return 0;
        } else if (hora < 18) {
            return 1;
        }
        return 2;
    }

    private void cerrarDia() {
        int dia = horaSimulacion / 24;
        for (int i = 0; i < cantidadOficinas; i++) {
            if (cantidadFacturas == facturas.length) {
                Factura[] nuevo = new Factura[facturas.length * 2];
                for (int j = 0; j < cantidadFacturas; j++) {
                    nuevo[j] = facturas[j];
                }
                facturas = nuevo;
            }
            facturas[cantidadFacturas] = oficinas[i].generarFactura(dia);
            cantidadFacturas++;
            oficinas[i].reiniciarDia();
        }
    }

    private Dispositivo buscarDispositivo(String codigo) {
        for (int i = 0; i < cantidadOficinas; i++) {
            Dispositivo dispositivo = oficinas[i].buscarDispositivo(codigo);
            if (dispositivo != null) {
                return dispositivo;
            }
        }
        return null;
    }

    public String reporteDispositivos(String codigoOficina) {
        Oficina oficina = buscarOficina(codigoOficina);
        if (oficina == null) {
            return "No existe esa oficina.";
        }
        String resultado = "Dispositivos de la oficina " + codigoOficina + ":\n";
        for (int i = 0; i < oficina.getCantidadDispositivos(); i++) {
            resultado = resultado + oficina.getDispositivo(i).toString() + "\n";
        }
        return resultado;
    }

    public String reporteBandeja() {
        String resultado = "Alertas pendientes por urgencia:\n";
        if (bandeja.estaVacia()) {
            return resultado + "No hay alertas pendientes.\n";
        }
        for (Alerta alerta : bandeja) {
            resultado = resultado + alerta.toString() + "\n";
        }
        return resultado;
    }

    public String reporteAtendidasConRetraso() {
        String resultado = "Alertas atendidas con retraso:\n";
        boolean encontro = false;
        for (int i = 0; i < cantidadAtendidas; i++) {
            if (alertasAtendidas[i].tuvoRetraso()) {
                resultado = resultado + alertasAtendidas[i].toString()
                        + " | Retraso: " + alertasAtendidas[i].getHorasRetraso()
                        + " horas | Crédito: Q" + alertasAtendidas[i].getCredito() + "\n";
                encontro = true;
            }
        }
        if (!encontro) {
            resultado = resultado + "No hay alertas atendidas con retraso.\n";
        }
        return resultado;
    }

    public String reporteFacturas() {
        String resultado = "Facturas generadas:\n";
        if (cantidadFacturas == 0) {
            return resultado + "Todavía no hay facturas.\n";
        }
        for (int i = 0; i < cantidadFacturas; i++) {
            resultado = resultado + facturas[i].toString() + "\n";
        }
        return resultado;
    }

    public String reporteTotalesPorFranja() {
        double valle = 0;
        double normal = 0;
        double punta = 0;
        for (int i = 0; i < cantidadFacturas; i++) {
            valle = valle + facturas[i].getMontoValle();
            normal = normal + facturas[i].getMontoNormal();
            punta = punta + facturas[i].getMontoPunta();
        }
        return "Montos facturados por franja:\nValle: Q" + valle
                + "\nNormal: Q" + normal + "\nPunta: Q" + punta + "\n";
    }

    public String reporteCreditos() {
        String resultado = "Créditos aplicados en facturas:\n";
        double total = 0;
        for (int i = 0; i < cantidadFacturas; i++) {
            resultado = resultado + "Oficina " + facturas[i].getOficina().getCodigo()
                    + ", día " + facturas[i].getDia() + ": Q"
                    + facturas[i].getCreditos() + "\n";
            total = total + facturas[i].getCreditos();
        }
        resultado = resultado + "Total aplicado en facturas: Q" + total + "\n";
        resultado = resultado + "Créditos acumulados del día actual:\n";
        for (int i = 0; i < cantidadOficinas; i++) {
            resultado = resultado + "Oficina " + oficinas[i].getCodigo() + ": Q"
                    + oficinas[i].getCreditosDia() + "\n";
        }
        return resultado;
    }
}
