package modelo;
import java.util.Date;

public class Prestamo {
	private Usuario solicitante;
    private JuegoDeMesa juego;
    private boolean activo;
    private Date fecha;

    public Prestamo(Usuario solicitante, JuegoDeMesa juego) {
        this.solicitante = solicitante;
        this.juego = juego;
        this.activo = true;
        this.fecha = new Date();
    }

    public void cerrar() {
        this.activo = false;
    }

	public Usuario getSolicitante() {
		return solicitante;
	}

	public JuegoDeMesa getJuego() {
		return juego;
	}

	public boolean isActivo() {
		return activo;
	}
	
	public Date getFecha()
	{
		return fecha;
	}
    
	@Override
	public String toString() {
	    return "Prestamo [solicitante=" + solicitante.getNombre()
	        + ", juego=" + juego.getNombre()
	        + ", activo=" + activo + "]"
	    	+ ", fecha=" + fecha + "]";
	}
    
}
