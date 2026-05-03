package modelo;

// esta clase representa al administrador
public class Administrador extends Usuario
{
	// constructor
	public Administrador(String id, String nombre, String login, String password)
	{
		super(id, nombre, login, password);
	}

	// crea un torneo desde el rol administrador
	public Torneo crearTorneo(BoardgameCafe cafe, String diaSemana, JuegoDeMesa juego, int maxParticipantes,
			TipoTorneo tipo, double tarifaEntrada, double bonoDescuento)
	{
		return cafe.crearTorneo(diaSemana, juego, maxParticipantes, tipo, tarifaEntrada, bonoDescuento);
	}

	// registra un empleado desde el rol administrador
	public void registrarEmpleado(BoardgameCafe cafe, Empleado empleado)
	{
		cafe.agregarEmpleado(empleado);
	}

	// inscribe un empleado a un torneo desde el rol administrador
	public void inscribirEmpleadoATorneo(BoardgameCafe cafe, Empleado empleado, Torneo torneo, int cupos)
	{
		cafe.inscribirEmpleadoTorneo(empleado, torneo, cupos);
	}

	@Override
	public String toString()
	{
		return "Administrador [id=" + getId() + ", nombre=" + getNombre() + "]";
	}
}