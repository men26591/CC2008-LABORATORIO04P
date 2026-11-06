import java.util.Iterator;
import java.util.NoSuchElementException;

public class BandejaAlertas implements Iterable<Alerta> {
    private Alerta[] alertas;
    private int cantidad;

    public BandejaAlertas() {
        alertas = new Alerta[5];
        cantidad = 0;
    }

    public void agregar(Alerta alerta) {
        if (cantidad == alertas.length) {
            Alerta[] nuevo = new Alerta[alertas.length * 2];
            for (int i = 0; i < cantidad; i++) {
                nuevo[i] = alertas[i];
            }
            alertas = nuevo;
        }

        int posicion = cantidad;
        while (posicion > 0 && alertas[posicion - 1].compareTo(alerta) > 0) {
            alertas[posicion] = alertas[posicion - 1];
            posicion--;
        }
        alertas[posicion] = alerta;
        cantidad++;
    }

    public Alerta extraerMasUrgente() {
        if (cantidad == 0) {
            return null;
        }
        Alerta urgente = alertas[0];
        for (int i = 0; i < cantidad - 1; i++) {
            alertas[i] = alertas[i + 1];
        }
        cantidad--;
        alertas[cantidad] = null;
        return urgente;
    }

    public boolean estaVacia() {
        return cantidad == 0;
    }

    public int getCantidad() {
        return cantidad;
    }

    public Iterator<Alerta> iterator() {
        return new IteradorBandeja();
    }

    private class IteradorBandeja implements Iterator<Alerta> {
        private int posicion;

        public IteradorBandeja() {
            posicion = 0;
        }

        public boolean hasNext() {
            return posicion < cantidad;
        }

        public Alerta next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Alerta alerta = alertas[posicion];
            posicion++;
            return alerta;
        }
    }
}
