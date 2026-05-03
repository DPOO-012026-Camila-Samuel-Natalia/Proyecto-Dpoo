package modelo;

import java.util.List;
import java.util.ArrayList;

public class Cliente extends Usuario implements PuedePrestar {

	private int puntosFidelidad = 0;
	private Mesa mesaActual = null;
	private List<JuegoDeMesa> juegosPrestados = new ArrayList<JuegoDeMesa>();
	private String codigoDescuento;

	// lista de juegos favoritos del cliente
	private ArrayList<JuegoDeMesa> juegosFavoritos = new ArrayList<JuegoDeMesa>();

	// bono ganado en torneo amistoso
	private double bonoTorneo = 0;

	public Cliente(String id, String nombre, String login, String password) {
		super(id, nombre, login, password);
	}

	public void setCodigoDescuento(String codigo) {
		this.codigoDescuento = codigo;
	}

	public String getCodigoDescuento() {
		return codigoDescuento;
	}

	public boolean tieneDescuento() {
		return codigoDescuento != null;
	}

	public int getPuntosFidelidad() {
		return puntosFidelidad;
	}

	public Mesa getMesaActual() {
		return mesaActual;
	}

	public boolean tieneJuegoAccion() {
		for (JuegoDeMesa j : juegosPrestados) {
			if (j.getCategoria().equals("ACCION")) {
				return true;
			}
		}
		return false;
	}

	public boolean estaEnCafe() {
		if (mesaActual == null) {
			return false;
		}
		return true;
	}

	public void asignarMesa(Mesa mesa) {
		this.mesaActual = mesa;
	}

	public void liberarMesa() {
		this.mesaActual = null;
	}

	public void agregarPuntos(int puntos) {
		this.puntosFidelidad += puntos;
	}

	public void usarPuntos(int puntos) {
		if (puntos > this.puntosFidelidad) {
			throw new IllegalStateException("Puntos insuficientes");
		}
		this.puntosFidelidad -= puntos;
	}

	// agrega un juego favorito
	public void agregarJuegoFavorito(JuegoDeMesa juego) {
		if (juego != null && !juegosFavoritos.contains(juego)) {
			juegosFavoritos.add(juego);
		}
	}

	// indica si el cliente es fanatico de un juego
	public boolean esFanaticoDe(JuegoDeMesa juego) {
		return juegosFavoritos.contains(juego);
	}

	// devuelve los juegos favoritos
	public ArrayList<JuegoDeMesa> getJuegosFavoritos() {
		return juegosFavoritos;
	}

	// guarda un bono ganado en torneo amistoso
	public void guardarBonoTorneo(double bono) {
		bonoTorneo = bono;
	}

	// indica si tiene bono de torneo
	public boolean tieneBonoTorneo() {
		return bonoTorneo > 0;
	}

	// devuelve el bono de torneo
	public double getBonoTorneo() {
		return bonoTorneo;
	}

	// usa el bono y lo deja en cero
	public double usarBonoTorneo() {
		double bono = bonoTorneo;
		bonoTorneo = 0;
		return bono;
	}

	// inscribe al cliente en un torneo
	public void inscribirseATorneo(BoardgameCafe cafe, Torneo torneo, int cupos) {
		cafe.inscribirClienteTorneo(this, torneo, cupos);
	}

	// desinscribe al cliente de un torneo
	public void desinscribirseDeTorneo(BoardgameCafe cafe, Torneo torneo) {
		cafe.desinscribirUsuarioTorneo(this, torneo);
	}

	@Override
	public boolean puedePedirPrestamo() {
		return (juegosPrestados.size() < 2) && this.estaEnCafe();
	}

	@Override
	public void agregarJuegoPrestado(JuegoDeMesa juego) {
		juegosPrestados.add(juego);
	}

	@Override
	public void quitarJuegoPrestado(JuegoDeMesa juego) {
		juegosPrestados.remove(juego);
	}

	@Override
	public List<JuegoDeMesa> getJuegosPrestados() {
		return juegosPrestados;
	}
}