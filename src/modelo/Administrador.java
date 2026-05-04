package modelo;
import java.util.ArrayList;
// esta clase representa al administrador
public class Administrador extends Usuario
{
	// constructor
	public Administrador(String id, String nombre, String login, String password)
	{
		super(id, nombre, login, password);
	}

	// crea un torneo desde el rol administrador
	public Torneo crearTorneo(BoardgameCafe cafe, String diaSemana, JuegoDeMesa juego, int maxParticipantes, TipoTorneo tipo, double tarifaEntrada, double bonoDescuento) {
		if (juego == null)
		throw new IllegalArgumentException("juego invalido");
		
		if (maxParticipantes > juego.getTotalCopiasPrestamo() * juego.getMaxJugadores())
		throw new IllegalStateException("no hay suficientes copias");
		
		String codigo = "T-" + (cafe.getTorneos().size() + 1);
		Torneo torneo = new Torneo(codigo, diaSemana, juego, maxParticipantes, tipo, tarifaEntrada, bonoDescuento);
		cafe.agregarTorneo(torneo);
		return torneo;
	}
	
	// registra un empleado desde el rol administrador
	public void registrarEmpleado(BoardgameCafe cafe, Empleado empleado)
	{
		cafe.agregarEmpleado(empleado);
	}

	// inscribe un empleado a un torneo desde el rol administrador
	public void inscribirEmpleadoATorneo(BoardgameCafe cafe, Empleado empleado, Torneo torneo, int cupos) {
	    if (empleado == null || torneo == null)
	        throw new IllegalArgumentException("datos invalidos");

	    if (cafe.empleadoTrabajaEseDia(empleado, torneo.getDiaSemana()))
	        throw new IllegalStateException("empleado tiene turno ese dia");

	    torneo.inscribir(empleado, cupos, false);
	}
	
	// sugerencias de platillo
	
	public void aprobarSugerencia(SugerenciaPlatillo sugerencia, String tipo, double precio, ArrayList<ProductoMenu> menu){
		if (!sugerencia.getEstado().equals("Pendiente"))
		throw new IllegalStateException("Sugerencia ya fue procesada");
		
		menu.add(sugerencia.crearProducto(tipo, precio));
		sugerencia.aprobar();
	}

	
    public void rechazarSugerencia(SugerenciaPlatillo sugerencia)
    {
        if (!sugerencia.getEstado().equals("Pendiente"))
            throw new IllegalStateException("Sugerencia ya fue procesada");
 
        sugerencia.rechazar();
    }
    
    
    public void aprobarCambioGeneral(SolicitudCambioTurno solicitud){
        if (!solicitud.getEstado().equals("Pendiente"))
            throw new IllegalStateException("Solicitud ya fue procesada");
 
        if (!solicitud.getTurnoQueOfrece()
                .tienePersonalMinimoSin(solicitud.getEmpleadoSolicitante()))
            throw new IllegalStateException("No se puede aprobar, quedaría poco personal");
 
        solicitud.aprobar();
    }
    
    
 // aprueba un intercambio de turno 
    public void aprobarIntercambioTurno(SolicitudCambioTurno solicitud)
    {
        if (!solicitud.getEstado().equals("Pendiente"))
            throw new IllegalStateException("Solicitud ya fue procesada");
 
        if (!solicitud.getTurnoQueOfrece().tienePersonalMinimoSin(solicitud.getEmpleadoSolicitante())) {
            solicitud.rechazar();
            throw new IllegalStateException("No se puede aprobar, quedaría poco personal en turno ofrecido");
        }
 
        if (!solicitud.getTurnoSolicitado().tienePersonalMinimoSin(solicitud.getEmpleadoIntercambio())) {
            solicitud.rechazar();
            throw new IllegalStateException("No se puede aprobar, quedaría poco personal en turno deseado");
        }
 
        solicitud.aprobar();
    }
    
    public void rechazarCambioTurno(SolicitudCambioTurno solicitud)
    {
        if (!solicitud.getEstado().equals("Pendiente"))
            throw new IllegalStateException("Solicitud ya fue procesada");
 
        solicitud.rechazar();
    }
    
    // Inventario 
    public void moverVentaAPrestamo(JuegoDeMesa juego)
    {
        if (juego.getCopiasVenta() == 0)
            throw new IllegalStateException("No hay copias en venta");
 
        juego.moverVentaAPrestamo();
    }
    
    
    
    
	@Override
	public String toString()
	{
		return "Administrador [id=" + getId() + ", nombre=" + getNombre() + "]";
	}
}