package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modelo.BoardgameCafe;
import modelo.Cocinero;
import modelo.JuegoDeMesa;
import modelo.Mesero;
import modelo.ProductoMenu;
import modelo.SolicitudCambioTurno;
import modelo.SugerenciaPlatillo;
import modelo.Turno;
import modelo.Administrador;
import modelo.Torneo;
import modelo.TipoTorneo;

public class AdministradorTest {
	
	private BoardgameCafe cafe;
	private Administrador admin;
	
	@BeforeEach
	public void setUP()
	{
		cafe = new BoardgameCafe(10);
		admin = new Administrador("A1", "Admin", "admin", "123");
		cafe.setAdministrador(admin);
		
	}
	
	@Test
	public void testCrearTorneo()

	{
		JuegoDeMesa juego = new JuegoDeMesa("Monopoly", 2007, "Hasbro", 2, 4, 0, "familiar", false, 4,1, 50.0);
		cafe.agregarJuego(juego);
		
		Torneo torneo= admin.crearTorneo(cafe, "Lunes", juego, 6, TipoTorneo.AMISTOSO, 0.0, 10000.0);
		
		assertEquals("T-1", torneo.getCodigo());
		assertEquals("Lunes", torneo.getDiaSemana());
		assertEquals(6, torneo.getMaxParticipantes());
		assertEquals(1, cafe.getTorneos().size());
		assertTrue(cafe.getTorneos().contains(torneo));
		
	}
	
	@Test
	public void testCrearTorneoJuegoInvalido()
	{
		assertThrows(IllegalArgumentException.class, () -> {
			admin.crearTorneo(cafe, "Lunes", null, 6, TipoTorneo.AMISTOSO, 0.0, 10000.0);
		});
		
	}
	@Test
	public void testCrearTorneoNoSuficientesCopias()
	{
		JuegoDeMesa juego = new JuegoDeMesa("Monopoly", 2007, "Hasbro", 2, 4, 0, "familiar", false, 0,1, 50.0);
		cafe.agregarJuego(juego);
		
		assertThrows(IllegalStateException.class, () -> {
			admin.crearTorneo(cafe, "Lunes", juego, 6, TipoTorneo.AMISTOSO, 0.0, 10000.0);
		});
	}
	
	@Test
	public void testRegistrarEmpleado()
	{
		Mesero mesero = new Mesero("123","Carlos", "C123", "C123*" );
		admin.registrarEmpleado(cafe, mesero);
		
		assertTrue(cafe.getEmpleados().contains(mesero));
		
	}
	@Test
	public void testInscribirEmpleadoATorneo()
	{
		Mesero mesero = new Mesero("123","Carlos", "C123", "C123*" );
		JuegoDeMesa juego = new JuegoDeMesa("Monopoly", 2007, "Hasbro", 2, 4, 0, "familiar", false, 4,1, 50.0);
		cafe.agregarJuego(juego);
		
		Torneo torneo= admin.crearTorneo(cafe, "Lunes", juego, 6, TipoTorneo.AMISTOSO, 0.0, 10000.0);
		
		admin.inscribirEmpleadoATorneo(cafe, mesero, torneo, 3);
		
		assertTrue(torneo.estaInscrito(mesero));
	}
	
	@Test
	public void testInscribirEmpleadoATorneoDatosInvalidos()
	{
		Mesero mesero = new Mesero("123","Carlos", "C123", "C123*" );
		JuegoDeMesa juego = new JuegoDeMesa("Monopoly", 2007, "Hasbro", 2, 4, 0, "familiar", false, 4,1, 50.0);
		cafe.agregarJuego(juego);
		
		Torneo torneo= admin.crearTorneo(cafe, "Lunes", juego, 6, TipoTorneo.AMISTOSO, 0.0, 10000.0);
		
		
		assertThrows(IllegalArgumentException.class, () -> {
			admin.inscribirEmpleadoATorneo(cafe, null, torneo, 3);
		});
		
		
	}
	
	@Test
	public void testInscribirEmpleadoATorneoEmpleadoOcupado()
	{
		Mesero mesero = new Mesero("123","Carlos", "C123", "C123*" );
		JuegoDeMesa juego = new JuegoDeMesa("Monopoly", 2007, "Hasbro", 2, 4, 0, "familiar", false, 4,1, 50.0);
		Turno lunes = new Turno("Lunes");
		cafe.agregarJuego(juego);
		cafe.agregarEmpleado(mesero);
		cafe.agregarTurno(lunes);
		cafe.asignarTurno(mesero, lunes);
		
		Torneo torneo= admin.crearTorneo(cafe, "Lunes", juego, 6, TipoTorneo.AMISTOSO, 0.0, 10000.0);
		assertThrows(IllegalStateException.class, () -> {
			admin.inscribirEmpleadoATorneo(cafe, mesero, torneo, 1);
		});
		
	}
	
	
	@Test
	public void testMoverVentaAPrestamo()
	{
		JuegoDeMesa juego = new JuegoDeMesa("Monopoly", 2007, "Hasbro", 2, 4, 0, "familiar", false, 4,1, 50.0);
		cafe.agregarJuego(juego);
		int antesVenta = juego.getCopiasVenta();
		int antesPrestamo = juego.getTotalCopiasPrestamo();
		
		admin.moverVentaAPrestamo(juego);
		
		assertEquals(antesVenta -1, juego.getCopiasVenta());
		assertEquals(antesPrestamo +1, juego.getTotalCopiasPrestamo());
		
	}
	
	@Test
	public void testMoverVentaAPrestamoNoCopias()
	{
		JuegoDeMesa juego = new JuegoDeMesa("Monopoly", 2007, "Hasbro", 2, 4, 0, "familiar", false, 4,0, 50.0);
		cafe.agregarJuego(juego);
		
		assertThrows(IllegalStateException.class, () -> {
			admin.moverVentaAPrestamo(juego);
		});
	}
	
	
	@Test
	public void testAprobarCambioGeneral()
	{
		Mesero mesero = new Mesero("123","Carlos", "C123", "C123*" );
		Mesero apoyo1 = new Mesero("124","Laura", "L124", "L124*");
		Mesero apoyo2 = new Mesero("125","Mateo", "M125", "M125*");
		Cocinero cocinero = new Cocinero("200","Luis", "L200", "L200*");
		Turno lunes = new Turno("LUNES");
		Turno martes = new Turno("MARTES");
		
		cafe.agregarEmpleado(mesero);
		cafe.agregarEmpleado(apoyo1);
		cafe.agregarEmpleado(apoyo2);
		cafe.agregarEmpleado(cocinero);
		cafe.agregarTurno(lunes);
		cafe.agregarTurno(martes);
		
		cafe.asignarTurno(mesero, lunes);
		cafe.asignarTurno(apoyo1, lunes);
		cafe.asignarTurno(apoyo2, lunes);
		cafe.asignarTurno(cocinero, lunes);
		SolicitudCambioTurno solicitud = cafe.solicitarCambioGeneral(mesero, lunes, martes, "motivo");
		
		cafe.aprobarCambioGeneral(solicitud);
		
		assertEquals("Aprobada", solicitud.getEstado());
		assertFalse(mesero.getTurnos().contains(lunes));
		assertTrue(mesero.getTurnos().contains(martes));
		
	}
	
	@Test
	public void testAprobarCambioGeneralSolicitudYaProcesada()
	{
		Mesero mesero = new Mesero("123","Carlos", "C123", "C123*" );
		Mesero apoyo1 = new Mesero("124","Laura", "L124", "L124*");
		Mesero apoyo2 = new Mesero("125","Mateo", "M125", "M125*");
		Cocinero cocinero = new Cocinero("200","Luis", "L200", "L200*");
		Turno lunes = new Turno("LUNES");
		Turno martes = new Turno("MARTES");
		
		cafe.agregarEmpleado(mesero);
		cafe.agregarEmpleado(apoyo1);
		cafe.agregarEmpleado(apoyo2);
		cafe.agregarEmpleado(cocinero);
		cafe.agregarTurno(lunes);
		cafe.agregarTurno(martes);
		
		cafe.asignarTurno(mesero, lunes);
		cafe.asignarTurno(apoyo1, lunes);
		cafe.asignarTurno(apoyo2, lunes);
		cafe.asignarTurno(cocinero, lunes);
		SolicitudCambioTurno solicitud = cafe.solicitarCambioGeneral(mesero, lunes, martes, "motivo");
		
		cafe.aprobarCambioGeneral(solicitud);
		assertThrows(IllegalStateException.class, () -> {
			cafe.aprobarCambioGeneral(solicitud);;
	});
		
		
	}
	
	@Test
	public void testAprobarCambioGeneralPocoPersonal()
	{
		Mesero mesero = new Mesero("123","Carlos", "C123", "C123*" );
		Mesero apoyo1 = new Mesero("124","Laura", "L124", "L124*");
		Cocinero cocinero = new Cocinero("200","Luis", "L200", "L200*");
		Turno lunes = new Turno("LUNES");
		Turno martes = new Turno("MARTES");
		
		cafe.agregarEmpleado(mesero);
		cafe.agregarEmpleado(apoyo1);
		cafe.agregarEmpleado(cocinero);
		cafe.agregarTurno(lunes);
		cafe.agregarTurno(martes);
		
		cafe.asignarTurno(mesero, lunes);
		cafe.asignarTurno(apoyo1, lunes);
		cafe.asignarTurno(cocinero, lunes);
		SolicitudCambioTurno solicitud = cafe.solicitarCambioGeneral(mesero, lunes, martes, "motivo");
		
		assertThrows(IllegalStateException.class, () -> {
			cafe.aprobarCambioGeneral(solicitud);;
	});
		assertEquals("Pendiente", solicitud.getEstado());
		
	}
	@Test
	public void testAprobarIntercambioTurno() 
	
	{
		Mesero mesero1 = new Mesero("123","Carlos", "C123", "C123*" );
		Mesero mesero2 = new Mesero("234", "Ana", "C234", "C234*");
		Mesero lunesApoyo1 = new Mesero("125","Laura", "L125", "L125*");
		Mesero lunesApoyo2 = new Mesero("126","Mateo", "M126", "M126*");
		Cocinero cocineroLunes = new Cocinero("201","Luis", "L201", "L201*");
		Mesero martesApoyo1 = new Mesero("127","Sara", "S127", "S127*");
		Mesero martesApoyo2 = new Mesero("128","Juan", "J128", "J128*");
		Cocinero cocineroMartes = new Cocinero("202","Nora", "N202", "N202*");
		
		Turno lunes = new Turno("LUNES");
		Turno martes = new Turno("MARTES");
		
		cafe.agregarEmpleado(mesero1);
		cafe.agregarEmpleado(mesero2);
		cafe.agregarEmpleado(lunesApoyo1);
		cafe.agregarEmpleado(lunesApoyo2);
		cafe.agregarEmpleado(cocineroLunes);
		cafe.agregarEmpleado(martesApoyo1);
		cafe.agregarEmpleado(martesApoyo2);
		cafe.agregarEmpleado(cocineroMartes);
		cafe.agregarTurno(lunes);
		cafe.agregarTurno(martes);
		
		cafe.asignarTurno(mesero1, lunes);
		cafe.asignarTurno(lunesApoyo1, lunes);
		cafe.asignarTurno(lunesApoyo2, lunes);
		cafe.asignarTurno(cocineroLunes, lunes);
		cafe.asignarTurno(mesero2, martes);
		cafe.asignarTurno(martesApoyo1, martes);
		cafe.asignarTurno(martesApoyo2, martes);
		cafe.asignarTurno(cocineroMartes, martes);
		SolicitudCambioTurno solicitud = cafe.solicitarIntercambioTurno(mesero1,mesero2, lunes, martes, "Intercambio");
		
		cafe.aprobarIntercambioTurno(solicitud);
		assertEquals("Aprobada", solicitud.getEstado());
		assertFalse(mesero1.getTurnos().contains(lunes));
		assertTrue(mesero1.getTurnos().contains(martes));
		assertFalse(mesero2.getTurnos().contains(martes));
		assertTrue(mesero2.getTurnos().contains(lunes));
	}
	
	@Test
	public void testAprobarIntercambioTurnoSolicitudYaProcesada()
	{
		Mesero mesero1 = new Mesero("123","Carlos", "C123", "C123*" );
		Mesero mesero2 = new Mesero("234", "Ana", "C234", "C234*");
		Mesero lunesApoyo1 = new Mesero("125","Laura", "L125", "L125*");
		Mesero lunesApoyo2 = new Mesero("126","Mateo", "M126", "M126*");
		Cocinero cocineroLunes = new Cocinero("201","Luis", "L201", "L201*");
		Mesero martesApoyo1 = new Mesero("127","Sara", "S127", "S127*");
		Mesero martesApoyo2 = new Mesero("128","Juan", "J128", "J128*");
		Cocinero cocineroMartes = new Cocinero("202","Nora", "N202", "N202*");
		
		Turno lunes = new Turno("LUNES");
		Turno martes = new Turno("MARTES");
		
		cafe.agregarEmpleado(mesero1);
		cafe.agregarEmpleado(mesero2);
		cafe.agregarEmpleado(lunesApoyo1);
		cafe.agregarEmpleado(lunesApoyo2);
		cafe.agregarEmpleado(cocineroLunes);
		cafe.agregarEmpleado(martesApoyo1);
		cafe.agregarEmpleado(martesApoyo2);
		cafe.agregarEmpleado(cocineroMartes);
		cafe.agregarTurno(lunes);
		cafe.agregarTurno(martes);
		
		cafe.asignarTurno(mesero1, lunes);
		cafe.asignarTurno(lunesApoyo1, lunes);
		cafe.asignarTurno(lunesApoyo2, lunes);
		cafe.asignarTurno(cocineroLunes, lunes);
		cafe.asignarTurno(mesero2, martes);
		cafe.asignarTurno(martesApoyo1, martes);
		cafe.asignarTurno(martesApoyo2, martes);
		cafe.asignarTurno(cocineroMartes, martes);
		SolicitudCambioTurno solicitud = cafe.solicitarIntercambioTurno(mesero1,mesero2, lunes, martes, "Intercambio");
		
		cafe.aprobarIntercambioTurno(solicitud);
		assertThrows(IllegalStateException.class, () -> {
			cafe.aprobarIntercambioTurno(solicitud);
	});
		
	}
	
	@Test
	public void testAprobarIntercambioTurnoPocoPersonalTurnoOfrecido()
	{
		
		Mesero mesero1 = new Mesero("123","Carlos", "C123", "C123*" );
		Mesero mesero2 = new Mesero("234", "Ana", "C234", "C234*");
		Mesero lunesApoyo1 = new Mesero("125","Laura", "L125", "L125*");
		Cocinero cocineroLunes = new Cocinero("201","Luis", "L201", "L201*");
		Mesero martesApoyo1 = new Mesero("127","Sara", "S127", "S127*");
		Mesero martesApoyo2 = new Mesero("128","Juan", "J128", "J128*");
		Cocinero cocineroMartes = new Cocinero("202","Nora", "N202", "N202*");
		
		Turno lunes = new Turno("LUNES");
		Turno martes = new Turno("MARTES");
		
		cafe.agregarEmpleado(mesero1);
		cafe.agregarEmpleado(mesero2);
		cafe.agregarEmpleado(lunesApoyo1);
		cafe.agregarEmpleado(cocineroLunes);
		cafe.agregarEmpleado(martesApoyo1);
		cafe.agregarEmpleado(martesApoyo2);
		cafe.agregarEmpleado(cocineroMartes);
		cafe.agregarTurno(lunes);
		cafe.agregarTurno(martes);
		
		cafe.asignarTurno(mesero1, lunes);
		cafe.asignarTurno(lunesApoyo1, lunes);
		cafe.asignarTurno(cocineroLunes, lunes);
		cafe.asignarTurno(mesero2, martes);
		cafe.asignarTurno(martesApoyo1, martes);
		cafe.asignarTurno(martesApoyo2, martes);
		cafe.asignarTurno(cocineroMartes, martes);
		SolicitudCambioTurno solicitud = cafe.solicitarIntercambioTurno(mesero1,mesero2, lunes, martes, "Intercambio");
		
		assertThrows(IllegalStateException.class, () -> {
			cafe.aprobarIntercambioTurno(solicitud);
	});
	}
	
	@Test
	public void testRechazarCambioTurno() 
	
	{
		Mesero mesero1 = new Mesero("123","Carlos", "C123", "C123*" );
		Mesero mesero2 = new Mesero("234", "Ana", "C234", "C234*");
		Mesero lunesApoyo1 = new Mesero("125","Laura", "L125", "L125*");
		Mesero lunesApoyo2 = new Mesero("126","Mateo", "M126", "M126*");
		Cocinero cocineroLunes = new Cocinero("201","Luis", "L201", "L201*");
		Mesero martesApoyo1 = new Mesero("127","Sara", "S127", "S127*");
		Cocinero cocineroMartes = new Cocinero("202","Nora", "N202", "N202*");
		
		Turno lunes = new Turno("LUNES");
		Turno martes = new Turno("MARTES");
		
		cafe.agregarEmpleado(mesero1);
		cafe.agregarEmpleado(mesero2);
		cafe.agregarEmpleado(lunesApoyo1);
		cafe.agregarEmpleado(lunesApoyo2);
		cafe.agregarEmpleado(cocineroLunes);
		cafe.agregarEmpleado(martesApoyo1);
		cafe.agregarEmpleado(cocineroMartes);
		cafe.agregarTurno(lunes);
		cafe.agregarTurno(martes);
		
		cafe.asignarTurno(mesero1, lunes);
		cafe.asignarTurno(lunesApoyo1, lunes);
		cafe.asignarTurno(lunesApoyo2, lunes);
		cafe.asignarTurno(cocineroLunes, lunes);
		cafe.asignarTurno(mesero2, martes);
		cafe.asignarTurno(martesApoyo1, martes);
		cafe.asignarTurno(cocineroMartes, martes);
		SolicitudCambioTurno solicitud = cafe.solicitarIntercambioTurno(mesero1,mesero2, lunes, martes, "Intercambio");
		
		assertThrows(IllegalStateException.class, () -> {
			cafe.aprobarIntercambioTurno(solicitud);
	});
	}
	@Test
	public void testCrearSugerencia() 
	{
		Mesero mesero = new Mesero("123","Carlos", "C123", "C123*" );
		cafe.agregarEmpleado(mesero);
		
		SugerenciaPlatillo sugerencia =cafe.crearSugerencia(mesero, "Cheesecake", "Torta a base de queso");
		
		assertEquals("Pendiente", sugerencia.getEstado());
		assertEquals(mesero, sugerencia.getEmpleado());
		assertEquals("Cheesecake", sugerencia.getNombrePlatillo());
		assertTrue(cafe.getSugerenciasPlatillos().contains(sugerencia));
		
		
	}
	
	@Test
	public void testAprobarSugerencia()
	{

		Mesero mesero = new Mesero("123","Carlos", "C123", "C123*" );
		cafe.agregarEmpleado(mesero);
		
		SugerenciaPlatillo sugerencia =cafe.crearSugerencia(mesero, "Cheesecake", "Torta a base de queso");
		cafe.aprobarSugerencia(sugerencia,"bebida", 10.0);
		
		assertEquals("Aprobada", sugerencia.getEstado());
		assertEquals(1,cafe.getProductosMenu().size());
		ProductoMenu producto = cafe.getProductosMenu().get(0);
		assertEquals("Cheesecake", producto.getNombre());	
		
	}
	
	@Test
	public void testAprobarSugerenciaYaProcesada()
	{
		Mesero mesero = new Mesero("123","Carlos", "C123", "C123*" );
		cafe.agregarEmpleado(mesero);
		
		SugerenciaPlatillo sugerencia =cafe.crearSugerencia(mesero, "Cheesecake", "Torta a base de queso");
		cafe.aprobarSugerencia(sugerencia,"bebida", 10.0);
		
		assertThrows(IllegalStateException.class, () -> {
			cafe.aprobarSugerencia(sugerencia,"bebida", 10.0);
	});
		
	}
	@Test
	public void testRechazarSugerencia() 
	{
		Mesero mesero = new Mesero("123","Carlos", "C123", "C123*" );
		cafe.agregarEmpleado(mesero);
		
		SugerenciaPlatillo sugerencia =cafe.crearSugerencia(mesero, "Cheesecake", "Torta a base de queso");
		cafe.rechazarSugerencia(sugerencia);
		
		assertEquals("Rechazada", sugerencia.getEstado());
		assertEquals(0,cafe.getProductosMenu().size());	
	}
	
}
