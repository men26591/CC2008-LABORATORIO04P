import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        SistemaTorre sistema = new SistemaTorre();
        int opcion = 0;

        while (opcion != 11) {
            System.out.println("\n--- Sistema TorreGT ---");
            System.out.println("1. Registrar oficina");
            System.out.println("2. Registrar dispositivo");
            System.out.println("3. Encender dispositivo");
            System.out.println("4. Apagar dispositivo");
            System.out.println("5. Ajustar nivel");
            System.out.println("6. Registrar lectura");
            System.out.println("7. Registrar movimiento de cámara");
            System.out.println("8. Avanzar una hora");
            System.out.println("9. Atender alerta más urgente");
            System.out.println("10. Mostrar reportes");
            System.out.println("11. Salir");
            opcion = leerEntero(teclado, "Elige una opción: ");

            if (opcion == 1) {
                String codigo = leerTexto(teclado, "Código de oficina: ");
                String inquilino = leerTexto(teclado, "Nombre del inquilino: ");
                if (sistema.registrarOficina(codigo, inquilino)) {
                    System.out.println("Oficina registrada.");
                } else {
                    System.out.println("No se registró. El código ya existe.");
                }
            } else if (opcion == 2) {
                registrarDispositivo(teclado, sistema);
            } else if (opcion == 3) {
                String codigo = leerTexto(teclado, "Código del dispositivo: ");
                mostrarResultado(sistema.encender(codigo), "Dispositivo encendido.",
                        "No se pudo encender. Revisa el código y el estado.");
            } else if (opcion == 4) {
                String codigo = leerTexto(teclado, "Código del dispositivo: ");
                mostrarResultado(sistema.apagar(codigo), "Dispositivo apagado.",
                        "No se pudo apagar. Revisa el código y el tipo de dispositivo.");
            } else if (opcion == 5) {
                String codigo = leerTexto(teclado, "Código del dispositivo: ");
                int nivel = leerEntero(teclado, "Nuevo nivel: ");
                mostrarResultado(sistema.ajustarNivel(codigo, nivel), "Nivel ajustado.",
                        "No se pudo ajustar. Verifica que esté encendido y el nivel sea válido.");
            } else if (opcion == 6) {
                String codigo = leerTexto(teclado, "Código del dispositivo medidor: ");
                double valor = leerDouble(teclado, "Valor de la lectura: ");
                mostrarResultado(sistema.registrarLectura(codigo, valor), "Lectura registrada.",
                        "Lectura rechazada. Revisa el código, el estado y el rango.");
            } else if (opcion == 7) {
                String codigo = leerTexto(teclado, "Código de la cámara: ");
                mostrarResultado(sistema.registrarMovimiento(codigo), "Movimiento registrado.",
                        "Movimiento rechazado. Verifica que sea una cámara encendida.");
            } else if (opcion == 8) {
                sistema.avanzarHora();
                System.out.println("Hora simulada: " + sistema.getHoraDelDia());
                System.out.println("Se avanzó una hora.");
            } else if (opcion == 9) {
                Alerta alerta = sistema.atenderAlerta();
                if (alerta == null) {
                    System.out.println("No hay alertas pendientes.");
                } else {
                    System.out.println("Atendida: " + alerta.toString());
                    if (alerta.tuvoRetraso()) {
                        System.out.println("Retraso: " + alerta.getHorasRetraso()
                                + " horas. Crédito: Q" + alerta.getCredito());
                    }
                }
            } else if (opcion == 10) {
                mostrarReportes(teclado, sistema);
            } else if (opcion != 11) {
                System.out.println("Opción no válida.");
            }
        }
        teclado.close();
        System.out.println("Programa finalizado.");
    }

    private static void registrarDispositivo(Scanner teclado, SistemaTorre sistema) {
        String codigoOficina = leerTexto(teclado, "Código de oficina: ");
        Oficina oficina = sistema.buscarOficina(codigoOficina);
        if (oficina == null) {
            System.out.println("No existe esa oficina.");
            return;
        }
        String codigo = leerTexto(teclado, "Código del dispositivo: ");
        double potencia = leerDouble(teclado, "Potencia nominal en watts: ");
        System.out.println("Tipo de dispositivo:");
        System.out.println("1. Luminaria");
        System.out.println("2. Climatizador");
        System.out.println("3. Climatizador inverter");
        System.out.println("4. Sensor de humo");
        System.out.println("5. Cámara de seguridad");
        int tipo = leerEntero(teclado, "Elige el tipo: ");

        Dispositivo dispositivo = null;
        if (tipo == 1) {
            dispositivo = new Luminaria(codigo, oficina, potencia);
        } else if (tipo == 2) {
            dispositivo = new Climatizador(codigo, oficina, potencia);
        } else if (tipo == 3) {
            dispositivo = new ClimatizadorInverter(codigo, oficina, potencia);
        } else if (tipo == 4) {
            dispositivo = new SensorHumo(codigo, oficina, potencia);
        } else if (tipo == 5) {
            dispositivo = new CamaraSeguridad(codigo, oficina, potencia);
        }

        if (dispositivo == null) {
            System.out.println("Tipo no válido.");
        } else if (sistema.registrarDispositivo(codigoOficina, dispositivo)) {
            System.out.println("Dispositivo registrado.");
        } else {
            System.out.println("No se registró. Revisa el código y la potencia.");
        }
    }

    private static void mostrarReportes(Scanner teclado, SistemaTorre sistema) {
        String codigoOficina = leerTexto(teclado, "Código de oficina para reporte de dispositivos: ");
        System.out.println(sistema.reporteDispositivos(codigoOficina));
        System.out.println(sistema.reporteBandeja());
        System.out.println(sistema.reporteAtendidasConRetraso());
        System.out.println(sistema.reporteFacturas());
        System.out.println(sistema.reporteTotalesPorFranja());
        System.out.println(sistema.reporteCreditos());
    }

    private static String leerTexto(Scanner teclado, String mensaje) {
        System.out.print(mensaje);
        return teclado.nextLine();
    }

    private static int leerEntero(Scanner teclado, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            if (teclado.hasNextInt()) {
                int valor = teclado.nextInt();
                teclado.nextLine();
                return valor;
            }
            System.out.println("Ingresa un número entero válido.");
            teclado.nextLine();
        }
    }

    private static double leerDouble(Scanner teclado, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            if (teclado.hasNextDouble()) {
                double valor = teclado.nextDouble();
                teclado.nextLine();
                return valor;
            }
            System.out.println("Ingresa un número válido.");
            teclado.nextLine();
        }
    }

    private static void mostrarResultado(boolean resultado, String correcto, String rechazado) {
        if (resultado) {
            System.out.println(correcto);
        } else {
            System.out.println(rechazado);
        }
    }
}
