public class Severidad {
    private String nombre;
    private int prioridad;
    private int plazoHoras;
    private double creditoPorHora;

    public Severidad(String nombre, int prioridad, int plazoHoras, double creditoPorHora) {
        this.nombre = nombre;
        this.prioridad = prioridad;
        this.plazoHoras = plazoHoras;
        this.creditoPorHora = creditoPorHora;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public int getPlazoHoras() {
        return plazoHoras;
    }

    public double getCreditoPorHora() {
        return creditoPorHora;
    }

    public String toString() {
        return nombre;
    }
}
