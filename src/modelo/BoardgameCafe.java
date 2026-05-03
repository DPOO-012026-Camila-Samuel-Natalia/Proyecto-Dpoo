package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Collection;

public class BoardgameCafe
{
	private Administrador administrador;

	private HashMap<String, Cliente> clientes;
	private HashMap<String, Empleado> empleados;
	private ArrayList<Mesa> mesas;
	private ArrayList<JuegoDeMesa> juegos;
	private ArrayList<Prestamo> historialPrestamos;
	private ArrayList<ProductoMenu> productosMenu;
	private ArrayList<VentaCafe> ventasCafe;
	private ArrayList<VentaJuego> ventasJuego;
	private ArrayList<SolicitudCambioTurno> solicitudesCambioTurno;
	private ArrayList<SugerenciaPlatillo> sugerenciasPlatillos;
	private int capacidadMaxima;
	private int clientesActuales;
	private ArrayList<Turno> turnos;
	
	// lista de torneos del cafe
	private ArrayList<Torneo> torneos;

	public BoardgameCafe(int capacidadMaxima)
	{
		this.capacidadMaxima = capacidadMaxima;
		this.clientesActuales = 0;
		administrador = null;
		clientes = new HashMap<String, Cliente>();
		empleados = new HashMap<String, Empleado>();
		mesas = new ArrayList<Mesa>();
		juegos = new ArrayList<JuegoDeMesa>();
		historialPrestamos = new ArrayList<Prestamo>();
		productosMenu = new ArrayList<ProductoMenu>();
		ventasCafe = new ArrayList<VentaCafe>();
		ventasJuego = new ArrayList<VentaJuego>();
		solicitudesCambioTurno = new ArrayList<SolicitudCambioTurno>();
		sugerenciasPlatillos = new ArrayList<SugerenciaPlatillo>();
		turnos = new ArrayList<Turno>();
		torneos = new ArrayList<Torneo>();
	}

	public Administrador getAdministrador()
	{
		return administrador;
	}

	public int getCapacidadMaxima() {
		return capacidadMaxima;
	}

	public int getClientesActuales() {
		return clientesActuales;
	}

	public Collection<Cliente> getClientes() {
	    return clientes.values();
	}

	public Collection<Empleado> getEmpleados() {
	    return empleados.values();
	}

	public ArrayList<Mesa> getMesas()
	{
		return mesas;
	}

	public ArrayList<JuegoDeMesa> getCatalogoJuegos()
	{
		return juegos;
	}

	public ArrayList<Prestamo> getHistorialPrestamos()
	{
		return historialPrestamos;
	}

	public ArrayList<ProductoMenu> getProductosMenu()
	{
		return productosMenu;
	}

	public ArrayList<VentaCafe> getVentasCafe()
	{
		return ventasCafe;
	}

	public ArrayList<VentaJuego> getVentasJuego()
	{
		return ventasJuego;
	}

	public ArrayList<SolicitudCambioTurno> getSolicitudesCambioTurno()
	{
		return solicitudesCambioTurno;
	}

	public ArrayList<SugerenciaPlatillo> getSugerenciasPlatillos()
	{
		return sugerenciasPlatillos;
	}

	public ArrayList<Turno> getTurnos() { 
		return turnos; 
	}
	
	// devuelve los torneos
	public ArrayList<Torneo> getTorneos() {
		return torneos;
	}

	public void setAdministrador(Administrador administrador)
	{
		this.administrador = administrador;
	}

	public void agregarCliente(Cliente c) {
	    clientes.put(c.getLogin(), c);
	}

	public void agregarEmpleado(Empleado e) {
	    empleados.put(e.getLogin(), e);
	}

	public void agregarMesa(Mesa m)
	{
		mesas.add(m);
	}

	public void agregarJuego(JuegoDeMesa j)
	{
		juegos.add(j);
	}

	public void agregarPrestamoDirecto(Prestamo p)
	{
		historialPrestamos.add(p);
	}

	public void agregarProductoMenu(ProductoMenu p)
	{
		productosMenu.add(p);
	}

	public void agregarVentaCafe(VentaCafe v)
	{
		ventasCafe.add(v);
	}

	public void agregarVentaJuego(VentaJuego v)
	{
		ventasJuego.add(v);
	}

	public void agregarSolicitudCambioTurno(SolicitudCambioTurno s)
	{
		solicitudesCambioTurno.add(s);
	}

	public void agregarSugerenciaPlatillo(SugerenciaPlatillo s)
	{
		sugerenciasPlatillos.add(s);
	}


	public Cliente buscarCliente(String login) {
	    return clientes.get(login);
	}

	public Empleado buscarEmpleado(String login) {
	    return empleados.get(login);
	}

	public Mesa buscarMesa(int numero)
	{
		for (int i = 0; i < mesas.size(); i++)
		{
			if (mesas.get(i).getNumero() == numero)
			{
				return mesas.get(i);
			}
		}
		return null;
	}

	public JuegoDeMesa buscarJuego(String nombre)
	{
		for (int i = 0; i < juegos.size(); i++)
		{
			if (juegos.get(i).getNombre().equals(nombre))
			{
				return juegos.get(i);
			}
		}
		return null;
	}

	public ProductoMenu buscarProductoMenu(String nombre)
	{
		for (int i = 0; i < productosMenu.size(); i++)
		{
			if (productosMenu.get(i).getNombre().equals(nombre))
			{
				return productosMenu.get(i);
			}
		}
		return null;
	}
	
	// agrega un torneo
	public void agregarTorneo(Torneo torneo)
	{
		torneos.add(torneo);
	}
	
	// busca un torneo por codigo
	public Torneo buscarTorneo(String codigo)
	{
		Torneo encontrado = null;

		for (Torneo t : torneos)
		{
			if (t.getCodigo().equals(codigo))
			{
				encontrado = t;
			}
		}

		return encontrado;
	}
	
	public ArrayList<Prestamo> getPrestamosActivos() {
	    ArrayList<Prestamo> activos = new ArrayList<Prestamo>();
	    for (Prestamo p : historialPrestamos) {
	        if (p.isActivo()) activos.add(p);
	    }
	    return activos;
	}

	public Prestamo crearPrestamo(PuedePrestar solicitante, JuegoDeMesa juego) {

	    if (!solicitante.puedePedirPrestamo())
	        throw new IllegalStateException("No puede pedir prestamo");

	    if (!juego.estaDisponiblePrestamo())
	        throw new IllegalStateException("Juego no disponible");

	    if (solicitante instanceof Cliente) {
	        Mesa mesa = ((Cliente) solicitante).getMesaActual();
	        
	        if (juego.soloAdultos() && mesa.hayMenoresDeEdad())
	            throw new IllegalStateException("Juego no apto para menores");
	        
	        if (juego.getEdadMinima() > 0 && mesa.isNinos() && !juego.soloAdultos())
	            throw new IllegalStateException("Juego no apto por edad minima");

	        if (mesa.getNumPersonas() < juego.getMinJugadores())
	            throw new IllegalStateException("Pocas personas para este juego");
	        
	        if (mesa.getNumPersonas() > juego.getMaxJugadores())	
	            throw new IllegalStateException("Demasiadas personas para este juego");
	    }
	    
	    if (juego.isDificil()) {
	        boolean hayMesero = false;
	        for (Empleado e : empleados.values()) {
	            if (e instanceof Mesero && e.isEnTurno()) {
	                if (((Mesero) e).dominaJuego(juego.getNombre())) {
	                    hayMesero = true;
	                }
	            }
	        }
	        if (!hayMesero)
	            throw new IllegalStateException("No hay mesero que domine este juego dificil");
	    }
	    Prestamo p = new Prestamo((Usuario) solicitante, juego);
	    juego.prestar();
	    solicitante.agregarJuegoPrestado(juego);
	    historialPrestamos.add(p);
	    return p;
	}

	public void devolverPrestamo(Prestamo prestamo) {
	    
	    if (!prestamo.isActivo())
	        throw new IllegalStateException("Prestamo ya fue devuelto");
	    prestamo.getJuego().devolver();
	    ((PuedePrestar) prestamo.getSolicitante()).quitarJuegoPrestado(prestamo.getJuego());
	    prestamo.cerrar();
	}

	public List<JuegoDeMesa> getJuegosDisponiblesPrestamo() {
	    List<JuegoDeMesa> disponibles = new ArrayList<JuegoDeMesa>();
	    for (JuegoDeMesa j : juegos) {
	        if (j.estaDisponiblePrestamo()) disponibles.add(j);
	    }
	    return disponibles;
	}
	
	// Buscar cliente en una mesa para que el mesero pueda gestionar el prestamo pero unirlo a un cliente
	public Cliente buscarClientePorMesa(int numeroMesa) {
	    for (Cliente c : clientes.values()) {
	        if (c.estaEnCafe() && c.getMesaActual().getNumero() == numeroMesa) {
	            return c;
	        }
	    }
	    return null;
	}

	public List<JuegoDeMesa> getJuegosDisponiblesVenta() {
	    List<JuegoDeMesa> disponibles = new ArrayList<JuegoDeMesa>();
	    for (JuegoDeMesa j : juegos) {
	        if (j.estaDisponibleVenta()) disponibles.add(j);
	    }
	    return disponibles;
	}
	
    public Mesa getMesaDisponible() {
        for (Mesa m : mesas) {
            if (!m.ocupada()) return m;
        }
        return null;
    }

    public void recibirCliente(Cliente cliente, int numPersonas,
                               boolean ninos, boolean menoresDeEdad) {
        if (clientesActuales + numPersonas > capacidadMaxima)
            throw new IllegalStateException("Cafe lleno");

        Mesa mesa = getMesaDisponible();
        if (mesa == null)
            throw new IllegalStateException("No hay mesas disponibles");

        if (numPersonas > mesa.getCapacidad())
            throw new IllegalStateException("Supera capacidad de la mesa");

        mesa.ocupar(numPersonas, ninos, menoresDeEdad);
        cliente.asignarMesa(mesa);
        clientesActuales += numPersonas;
    }

   

    public void retirarCliente(Cliente cliente) {
        if (!cliente.estaEnCafe())
            throw new IllegalStateException("Cliente no esta en el cafe");

        
        ArrayList<Prestamo> aDevolver = new ArrayList<>();
        for (Prestamo p : historialPrestamos) {
            if (p.isActivo() && p.getSolicitante().equals(cliente)) {
                aDevolver.add(p);
            }
        }
        
        for (Prestamo p : aDevolver) {
            devolverPrestamo(p);
        }

        clientesActuales -= cliente.getMesaActual().getNumPersonas();
        cliente.getMesaActual().liberar();
        cliente.liberarMesa();
    }
	
	// Ventas cafeteria
    public VentaCafe registrarVentaCafe(Cliente cliente, boolean cobraPropina) {
        if (!cliente.estaEnCafe())
            throw new IllegalStateException("Cliente no tiene mesa asignada");

        int numeroMesa = cliente.getMesaActual().getNumero();
        String codigo = "VC-" + numeroMesa + "-" + (ventasCafe.size() + 1);
        VentaCafe venta = new VentaCafe(codigo, cliente, cobraPropina);
        return venta;
    }

    public void agregarProductoAVenta(VentaCafe venta, ProductoMenu producto, int cantidad) {
        if (venta.isCerrada())
            throw new IllegalStateException("Venta ya cerrada");

        Mesa mesa = venta.getMesa();

        if (producto instanceof Bebida) {
            Bebida bebida = (Bebida) producto;
            if (bebida.isAlcoholica() && mesa.hayMenoresDeEdad())
                throw new IllegalStateException("No se puede servir alcohol con menores");
            if (bebida.isCaliente() && venta.getCliente().tieneJuegoAccion())
                throw new IllegalStateException("No se puede servir bebida caliente con juego de accion");
        }

        venta.agregarDetalle(new DetalleVentaCafe(producto, cantidad));
    }

    public void cerrarVentaCafe(VentaCafe venta) {
        if (venta.isCerrada())
            throw new IllegalStateException("Venta ya fue cerrada");
        if (venta.getDetalles().isEmpty())
            throw new IllegalStateException("La venta no tiene productos");

        venta.cerrar();
        ventasCafe.add(venta);
        venta.getCliente().agregarPuntos((int)(venta.calcularTotal() * 0.01));
    }

    public VentaCafe getVentaActivaCliente(Cliente cliente) {
        for (VentaCafe v : ventasCafe) {
            if (v.getComprador().equals(cliente) && !v.isCerrada())
                return v;
        }
        return null;
    }

    public void aplicarCodigoDescuento(Cliente cliente, String codigo) {
        for (Empleado e : empleados.values()) {
            if (e.getCodigoDescuento().equals(codigo)) {
                cliente.setCodigoDescuento(codigo);
                return;
            }
        }
        throw new IllegalStateException("Codigo de descuento invalido");
    }
	
    public VentaJuego registrarVentaJuego(Usuario comprador, JuegoDeMesa juego, int cantidad) {
        if (!juego.estaDisponibleVenta())
            throw new IllegalStateException("No hay copias disponibles para venta");
        if (cantidad > juego.getCopiasVenta())
            throw new IllegalStateException("No hay suficientes copias");

        String codigo = "VJ-" + (ventasJuego.size() + 1);
        VentaJuego venta = new VentaJuego(codigo, comprador);
        venta.agregarDetalle(new DetalleVentaJuego(juego, cantidad));
        
        if (comprador instanceof Empleado) {
            venta.setDescuento(venta.calcularSubtotal() * 0.20);
        } else if (comprador instanceof Cliente) {
            Cliente c = (Cliente) comprador;

            // primero intenta usar bono de torneo
            if (c.tieneBonoTorneo()) {

                double bono = c.usarBonoTorneo();

                if (bono > venta.calcularSubtotal()) {
                    bono = venta.calcularSubtotal();
                }

                venta.setDescuento(bono);
            }
            // si no tiene bono usa descuento normal
            else if (c.tieneDescuento()) {
                venta.setDescuento(venta.calcularSubtotal() * 0.10);
            }
        }

        for (int i = 0; i < cantidad; i++) juego.vender();

        ventasJuego.add(venta);
        venta.cerrar();

        if (comprador instanceof Cliente)
            ((Cliente) comprador).agregarPuntos((int)(venta.calcularTotal() * 0.01));

        return venta;
    }

    // crea un torneo nuevo
	public Torneo crearTorneo(String diaSemana, JuegoDeMesa juego, int maxParticipantes,
	                          TipoTorneo tipo, double tarifaEntrada, double bonoDescuento) {

	    if (juego == null) {
	        throw new IllegalArgumentException("juego invalido");
	    }

	    // valida que haya suficientes copias para soportar el torneo
	    if (maxParticipantes > juego.getTotalCopiasPrestamo() * juego.getMaxJugadores()) {
	        throw new IllegalStateException("no hay suficientes copias");
	    }

	    String codigo = "T-" + (torneos.size() + 1);

	    Torneo torneo = new Torneo(codigo, diaSemana, juego, maxParticipantes, tipo, tarifaEntrada, bonoDescuento);

	    torneos.add(torneo);

	    return torneo;
	}

	// inscribe cliente a torneo
	public void inscribirClienteTorneo(Cliente cliente, Torneo torneo, int cupos) {

	    if (cliente == null || torneo == null) {
	        throw new IllegalArgumentException("datos invalidos");
	    }

	    boolean esFanatico = cliente.esFanaticoDe(torneo.getJuego());

	    torneo.inscribir(cliente, cupos, esFanatico);
	}

	// inscribe empleado a torneo
	public void inscribirEmpleadoTorneo(Empleado empleado, Torneo torneo, int cupos) {

	    if (empleado == null || torneo == null) {
	        throw new IllegalArgumentException("datos invalidos");
	    }

	    if (empleadoTrabajaEseDia(empleado, torneo.getDiaSemana())) {
	        throw new IllegalStateException("empleado tiene turno ese dia");
	    }

	    torneo.inscribir(empleado, cupos, false);
	}

	// verifica si el empleado trabaja ese dia
	public boolean empleadoTrabajaEseDia(Empleado empleado, String diaSemana) {

	    boolean trabaja = false;

	    for (Turno t : empleado.getTurnos()) {
	        if (t.getDiaSemana().equals(diaSemana)) {
	            trabaja = true;
	        }
	    }

	    return trabaja;
	}

	// desinscribe usuario de torneo
	public void desinscribirUsuarioTorneo(Usuario usuario, Torneo torneo) {

	    if (usuario == null || torneo == null) {
	        throw new IllegalArgumentException("datos invalidos");
	    }

	    torneo.desinscribir(usuario);
	}

	// entrega bono a ganador de torneo amistoso
	public void entregarPremioAmistoso(Torneo torneo, Cliente ganador) {

	    if (torneo.getTipo() != TipoTorneo.AMISTOSO) {
	        throw new IllegalStateException("no es torneo amistoso");
	    }

	    if (!torneo.estaInscrito(ganador)) {
	        throw new IllegalStateException("cliente no inscrito");
	    }

	    ganador.guardarBonoTorneo(torneo.getBonoDescuento());
	}

    public void agregarTurno(Turno turno) {
        turnos.add(turno);
    }

    public void asignarTurno(Empleado empleado, Turno turno) {
        turno.agregarEmpleado(empleado);
        empleado.agregarTurno(turno);
    }

    public void quitarTurno(Empleado empleado, Turno turno) {
        turno.quitarEmpleado(empleado);
        empleado.quitarTurno(turno);
    }

    public SolicitudCambioTurno solicitarCambioGeneral(Empleado empleado,Turno turnoQueQuiereQuitar,Turno turnoQueDesea,String motivo) {
		if (!empleado.getTurnos().contains(turnoQueQuiereQuitar)) throw new IllegalStateException("El empleado no tiene ese turno");
		
		String codigo = "SCT-" + (solicitudesCambioTurno.size() + 1);
		SolicitudCambioTurno solicitud = new SolicitudCambioTurno(codigo, empleado,turnoQueQuiereQuitar,turnoQueDesea, motivo);
		solicitudesCambioTurno.add(solicitud);
		return solicitud;
	}

	public SolicitudCambioTurno solicitarIntercambioTurno(Empleado solicitante,Empleado otroEmpleado,Turno turnoQueOfrece,Turno turnoQueDesea,String motivo) {
		if (!solicitante.getTurnos().contains(turnoQueOfrece))throw new IllegalStateException("El empleado no tiene ese turno");
		
		if (!otroEmpleado.getTurnos().contains(turnoQueDesea))throw new IllegalStateException("El otro empleado no tiene ese turno");
		
		String codigo = "SCT-" + (solicitudesCambioTurno.size() + 1);
		SolicitudCambioTurno solicitud = new SolicitudCambioTurno(codigo, solicitante,otroEmpleado,turnoQueOfrece,turnoQueDesea, motivo);
		solicitudesCambioTurno.add(solicitud);
		return solicitud;
    }

	public void aprobarCambioGeneral(SolicitudCambioTurno solicitud) {
	    if (!solicitud.getEstado().equals("Pendiente"))
	        throw new IllegalStateException("Solicitud ya fue procesada");

	    if (!solicitud.getTurnoQueOfrece().tienePersonalMinimoSin(solicitud.getEmpleadoSolicitante())) {
	        throw new IllegalStateException("No se puede aprobar, quedaria poco personal");}

	    solicitud.aprobar();
	}

	public void aprobarIntercambioTurno(SolicitudCambioTurno solicitud) {
	    if (!solicitud.getEstado().equals("Pendiente"))
	        throw new IllegalStateException("Solicitud ya fue procesada");

	    if (!solicitud.getTurnoQueOfrece().tienePersonalMinimoSin(solicitud.getEmpleadoSolicitante())) {
	    	solicitud.rechazar();
	        throw new IllegalStateException("No se puede aprobar, quedaria poco personal en turno ofrecido");}

	    if (!solicitud.getTurnoSolicitado().tienePersonalMinimoSin(solicitud.getEmpleadoIntercambio())) {
	    	solicitud.rechazar();
	        throw new IllegalStateException("No se puede aprobar, quedaria poco personal en turno deseado");}

	    solicitud.aprobar();
	}

    public void rechazarCambioTurno(SolicitudCambioTurno solicitud) {
        if (!solicitud.getEstado().equals("Pendiente"))
            throw new IllegalStateException("Solicitud ya fue procesada");
        solicitud.rechazar();
    }

    public ArrayList<SolicitudCambioTurno> getSolicitudesPendientes() {
        ArrayList<SolicitudCambioTurno> pendientes = new ArrayList<SolicitudCambioTurno>();
        for (SolicitudCambioTurno s : solicitudesCambioTurno) {
            if (s.getEstado().equals("Pendiente")) pendientes.add(s);
        }
        return pendientes;
    }
	
	public void moverVentaAPrestamo(JuegoDeMesa juego) {
	    if (juego.getCopiasVenta() == 0)
	        throw new IllegalStateException("No hay copias en venta");
	    juego.moverVentaAPrestamo();
	}

	public void aprobarSugerencia(SugerenciaPlatillo sugerencia, String tipo, double precio) {
		if (!sugerencia.getEstado().equals("Pendiente"))throw new IllegalStateException("Sugerencia ya fue procesada");
		
		ProductoMenu nuevo = sugerencia.crearProducto(tipo, precio);
		productosMenu.add(nuevo);
		sugerencia.aprobar();
	}
	
	public void rechazarSugerencia(SugerenciaPlatillo sugerencia) {
		if (!sugerencia.getEstado().equals("Pendiente"))
		throw new IllegalStateException("Sugerencia ya fue procesada");
		sugerencia.rechazar();
	}

	public SugerenciaPlatillo crearSugerencia(Empleado empleado, String nombrePlatillo, String descripcion) {
		String codigo = "SP-" + (sugerenciasPlatillos.size() + 1);
		SugerenciaPlatillo s = new SugerenciaPlatillo(codigo, empleado, nombrePlatillo, descripcion);
		sugerenciasPlatillos.add(s);
		return s;
	}

	public double getTotalVentasCafe() {
	    double total = 0;
	    for (VentaCafe v : ventasCafe) {
	        if (v.isCerrada()) total += v.calcularTotal();
	    }
	    return total;
	}

	public double getTotalVentasJuego() {
	    double total = 0;
	    for (VentaJuego v : ventasJuego) {
	        total += v.calcularTotal();
	    }
	    return total;
	}

	public double getTotalVentas() {
	    return getTotalVentasCafe() + getTotalVentasJuego();
	}

	public String getInformeVentas() {
	    return "=== INFORME DE VENTAS ===" 
	        + "\nVentas cafeteria:  $" + getTotalVentasCafe()
	        + "\nVentas juegos:     $" + getTotalVentasJuego()
	        + "\nTotal:             $" + getTotalVentas()
	        + "\nNum ventas cafe:   " + ventasCafe.size()
	        + "\nNum ventas juegos: " + ventasJuego.size();
	}

	public ArrayList<Prestamo> getHistorialPrestamosJuego(JuegoDeMesa juego) {
	    ArrayList<Prestamo> resultado = new ArrayList<Prestamo>();
	    for (Prestamo p : historialPrestamos) {
	        if (p.getJuego().equals(juego)) resultado.add(p);
	    }
	    return resultado;
	}

	@Override
	public String toString()
	{
		String texto = "BoardgameCafe [";

		if (administrador != null)
		{
			texto += "administrador=" + administrador.getNombre();
		}
		else
		{
			texto += "administrador=Sin administrador";
		}

		texto += ", clientes=" + clientes.size();
		texto += ", empleados=" + empleados.size();
		texto += ", mesas=" + mesas.size();
		texto += ", juegos=" + juegos.size();
		texto += ", torneos=" + torneos.size();

		if (historialPrestamos.size() > 0)
		{
			texto += ", prestamosTotales=" + historialPrestamos.size();
			texto += ", prestamosActivos=" + getPrestamosActivos().size();
		}

		if (productosMenu.size() > 0)
		{
			texto += ", productosMenu=" + productosMenu.size();
		}

		if (ventasCafe.size() > 0)
		{
			texto += ", ventasCafe=" + ventasCafe.size();
		}

		if (ventasJuego.size() > 0)
		{
			texto += ", ventasJuego=" + ventasJuego.size();
		}

		if (solicitudesCambioTurno.size() > 0)
		{
			texto += ", solicitudesCambioTurno=" + solicitudesCambioTurno.size();
		}

		if (sugerenciasPlatillos.size() > 0)
		{
			texto += ", sugerenciasPlatillos=" + sugerenciasPlatillos.size();
		}

		texto += "]";

		return texto;
	}
}