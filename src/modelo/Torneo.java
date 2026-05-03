package modelo;

import java.util.ArrayList;

// esta clase representa un torneo del cafe
public class Torneo {

    // codigo del torneo
    private String codigo;

    // dia del torneo
    private String diaSemana;

    // juego del torneo
    private JuegoDeMesa juego;

    // maximo de participantes
    private int maxParticipantes;

    // tipo de torneo
    private TipoTorneo tipo;

    // tarifa de entrada
    private double tarifaEntrada;

    // premio en dinero
    private double premioMetalico;

    // bono de descuento
    private double bonoDescuento;

    // lista de inscripciones
    private ArrayList<InscripcionTorneo> inscripciones;

    // constructor
    public Torneo(String codigo, String diaSemana, JuegoDeMesa juego, int maxParticipantes,
                  TipoTorneo tipo, double tarifaEntrada, double bonoDescuento) {

        if (maxParticipantes <= 0) {
            throw new IllegalArgumentException("debe haber participantes");
        }

        this.codigo = codigo;
        this.diaSemana = diaSemana;
        this.juego = juego;
        this.maxParticipantes = maxParticipantes;
        this.tipo = tipo;
        this.inscripciones = new ArrayList<InscripcionTorneo>();

        // si es amistoso
        if (tipo == TipoTorneo.AMISTOSO) {
            this.tarifaEntrada = 0;
            this.bonoDescuento = bonoDescuento;
            this.premioMetalico = 0;
        } 
        // si es competitivo
        else {
            this.tarifaEntrada = tarifaEntrada;
            this.bonoDescuento = 0;
            this.premioMetalico = 0;
        }
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public JuegoDeMesa getJuego() {
        return juego;
    }

    public int getMaxParticipantes() {
        return maxParticipantes;
    }

    public TipoTorneo getTipo() {
        return tipo;
    }

    public double getTarifaEntrada() {
        return tarifaEntrada;
    }

    public double getPremioMetalico() {
        return premioMetalico;
    }

    public double getBonoDescuento() {
        return bonoDescuento;
    }

    public ArrayList<InscripcionTorneo> getInscripciones() {
        return inscripciones;
    }

    // calcula cupos fanaticos
    public int getCuposFanaticos() {
        return (int) Math.ceil(maxParticipantes * 0.20);
    }

    // calcula cupos ocupados
    public int getCuposOcupados() {
        int total = 0;

        for (InscripcionTorneo i : inscripciones) {
            total += i.getCuposTomados();
        }

        return total;
    }

    // cupos disponibles
    public int getCuposDisponibles() {
        return maxParticipantes - getCuposOcupados();
    }

    // verifica si ya esta inscrito
    public boolean estaInscrito(Usuario u) {
        boolean encontrado = false;

        for (InscripcionTorneo i : inscripciones) {
            if (i.getUsuario().equals(u)) {
                encontrado = true;
            }
        }

        return encontrado;
    }

    // inscribe un usuario
    public void inscribir(Usuario u, int cupos, boolean esFanatico) {

        if (cupos < 1 || cupos > 3) {
            throw new IllegalArgumentException("maximo 3 cupos");
        }

        if (estaInscrito(u)) {
            throw new IllegalStateException("ya inscrito");
        }

        if (cupos > getCuposDisponibles()) {
            throw new IllegalStateException("sin cupos");
        }

        boolean usaFan = false;

        // manejo de cupos fanaticos
        if (esFanatico) {
            int usados = 0;

            for (InscripcionTorneo i : inscripciones) {
                if (i.isUsoCuposFanaticos()) {
                    usados += i.getCuposTomados();
                }
            }

            int disponiblesFan = getCuposFanaticos() - usados;

            if (disponiblesFan >= cupos) {
                usaFan = true;
            }
        }

        inscripciones.add(new InscripcionTorneo(u, cupos, usaFan));

        recalcularPremio();
    }

    // desinscribe usuario
    public void desinscribir(Usuario u) {

        InscripcionTorneo encontrada = null;

        for (InscripcionTorneo i : inscripciones) {
            if (i.getUsuario().equals(u)) {
                encontrada = i;
            }
        }

        if (encontrada == null) {
            throw new IllegalStateException("no estaba inscrito");
        }

        inscripciones.remove(encontrada);

        recalcularPremio();
    }

    // dinero recaudado
    public double getRecaudado() {
        double total = 0;

        if (tipo == TipoTorneo.COMPETITIVO) {
            for (InscripcionTorneo i : inscripciones) {
                if (i.getUsuario() instanceof Cliente) {
                    total += tarifaEntrada * i.getCuposTomados();
                }
            }
        }

        return total;
    }

    // recalcula premio
    public void recalcularPremio() {
        if (tipo == TipoTorneo.COMPETITIVO) {
            premioMetalico = getRecaudado() * 0.7;
        }
    }

    @Override
    public String toString() {
        return "Torneo [codigo=" + codigo
                + ", juego=" + juego.getNombre()
                + ", tipo=" + tipo
                + ", cupos=" + getCuposOcupados() + "/" + maxParticipantes
                + "]";
    }
}