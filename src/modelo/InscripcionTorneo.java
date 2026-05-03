package modelo;

// esta clase representa la inscripcion de un usuario a un torneo
// un usuario puede tomar entre 1 y 3 cupos
public class InscripcionTorneo {

    // usuario que se inscribe
    private Usuario usuario;

    // numero de cupos tomados
    private int cuposTomados;

    // indica si uso cupos de fanaticos
    private boolean usoCuposFanaticos;

    // el constructor
    public InscripcionTorneo(Usuario usuario, int cuposTomados, boolean usoCuposFanaticos) {
        this.usuario = usuario;
        this.cuposTomados = cuposTomados;
        this.usoCuposFanaticos = usoCuposFanaticos;
    }

    // devuelve el usuario
    public Usuario getUsuario() {
        return usuario;
    }

    // devuelve los cupos tomados
    public int getCuposTomados() {
        return cuposTomados;
    }

    // indica si uso cupos de fanaticos
    public boolean isUsoCuposFanaticos() {
        return usoCuposFanaticos;
    }

    @Override
    public String toString() {
        return "InscripcionTorneo [usuario=" + usuario.getNombre()
                + ", cuposTomados=" + cuposTomados
                + ", usoCuposFanaticos=" + usoCuposFanaticos + "]";
    }
}