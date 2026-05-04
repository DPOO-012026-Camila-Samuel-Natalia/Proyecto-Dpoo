package modelo;

public class JuegoDeMesa {

	private String nombre;
	private int anioPublicacion;
	private String empresa;
	private int minJugadores;
	private int maxJugadores;
	private int edadMinima; // 0 si no tiene
	private String categoria; //ACCION O CARTAS O TABLERO
	private boolean dificil;
	private int totalCopiasPrestamo;
	private int copiasEnUso; 
	private int copiasVenta; // disponibles para vender
	// La idea es luego (interface o donde se necesite) hacer un filtro de la lista de juegos mostrando solo los que esten disponibles para prestamo y los que esten disponibles para venta
	private double precioUnitario;
	
	
	// Prestamo
	public void setCopiasEnUso(int copiasEnUso) {
		this.copiasEnUso = copiasEnUso;
	}
	
	public JuegoDeMesa(String nombre, int anioPublicacion, String empresa, int minJugadores, int maxJugadores,
			int edadMinima, String categoria, boolean dificil, int totalCopiasPrestamo,
			int copiasVenta, double precioUnitario) {
		super();
		this.nombre = nombre;
		this.anioPublicacion = anioPublicacion;
		this.empresa = empresa;
		this.minJugadores = minJugadores;
		this.maxJugadores = maxJugadores;
		this.edadMinima = edadMinima;
		this.categoria = categoria;
		this.dificil = dificil;
		this.totalCopiasPrestamo = totalCopiasPrestamo;
		this.copiasEnUso = 0;
		this.copiasVenta = copiasVenta;
		this.precioUnitario = precioUnitario;
	}

	public boolean estaDisponiblePrestamo() {
		return copiasEnUso < totalCopiasPrestamo;
		
	}
	
	public int getCopiasDisponiblesPrestamo() {
		return totalCopiasPrestamo - copiasEnUso;
		
	}
	public void prestar() {
		if (!estaDisponiblePrestamo()) throw new IllegalStateException ("No hay copias disponibles");
		copiasEnUso++;
	}
	
	public void devolver() {
		if (copiasEnUso == 0) throw new IllegalStateException("No hay copias prestadas");
		copiasEnUso --;
		
	}
	
	// Venta
	public boolean estaDisponibleVenta() {
		return copiasVenta > 0;
	}
	
	public void vender() {copiasVenta --; }
	
	
	
	// administrador agrega inventario

	public void agregarCopiasPrestamo (int cantidad) {totalCopiasPrestamo += cantidad;}
	public void agregarCopiasVenta (int cantidad) {copiasVenta+= cantidad;}
	public void moverVentaAPrestamo() {
		if (copiasVenta == 0) throw new IllegalStateException("No hay copias en venta");
		copiasVenta --;
		totalCopiasPrestamo ++;
		
	}
	
	public boolean soloAdultos() {
		if (this.edadMinima >= 18) {return true;}
		return false;
		
	}
	

	public int getTotalCopiasPrestamo() {
		return totalCopiasPrestamo;
	}

	public void setTotalCopiasPrestamo(int totalCopiasPrestamo) {
		this.totalCopiasPrestamo = totalCopiasPrestamo;
	}

	public int getCopiasVenta() {
		return copiasVenta;
	}

	public void setCopiasVenta(int copiasVenta) {
		this.copiasVenta = copiasVenta;
	}

	public String getNombre() {
		return nombre;
	}

	public int getMinJugadores() {
		return minJugadores;
	}

	public int getMaxJugadores() {
		return maxJugadores;
	}

	public int getEdadMinima() {
		return edadMinima;
	}

	public String getCategoria() {
		return categoria;
	}

	public boolean isDificil() {
		return dificil;
	}
	
	public double getPrecioUnitario() {
		return this.precioUnitario;
	}
	

	// cambia el precio unitario
	public void setPrecioUnitario(double precioUnitario)
	{
	    this.precioUnitario = precioUnitario;
	}

	// =========================
	// MÉTODOS ADICIONALES
	// =========================

	// devuelve cuantas copias estan en uso
	public int getCopiasEnUso() {
	    return copiasEnUso;
	}

	// Representación del objeto en texto (útil para imprimir)
	@Override
	public String toString() {
	    return "JuegoDeMesa [nombre=" + nombre 
	        + ", categoria=" + categoria
	        + ", copiasPrestamo=" + totalCopiasPrestamo
	        + ", copiasVenta=" + copiasVenta + "]";
	}

	// comparacion entre juegos (se comparan por nombre)
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) return true;
	    if (!(obj instanceof JuegoDeMesa)) return false;
	    JuegoDeMesa otro = (JuegoDeMesa) obj;
	    return this.nombre.equals(otro.nombre);
	}
}
