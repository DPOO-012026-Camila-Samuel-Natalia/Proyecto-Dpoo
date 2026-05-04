package test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modelo.Administrador;
import modelo.Bebida;
import modelo.BoardgameCafe;
import modelo.Cliente;
import modelo.JuegoDeMesa;
import modelo.Mesa;
import modelo.Mesero;
import modelo.Prestamo;
import modelo.SolicitudCambioTurno;
import modelo.TipoTorneo;
import modelo.Torneo;
import modelo.Turno;
import modelo.VentaCafe;
import modelo.VentaJuego;

public class HistoriasUsuarioTest {
	
	private BoardgameCafe cafe;
	private Administrador admin;
	
	@BeforeEach
	public void setUp()
	{
		cafe = new BoardgameCafe(20);
		admin = new Administrador("A1", "Admin", "admin", "123");
		cafe.setAdministrador(admin);
	}
	
	// Historia 1: crear torneos
	@Test
	public void testHistoria1CrearTorneo()
	{
		JuegoDeMesa juego = new JuegoDeMesa("Monopoly", 2007, "Hasbro", 2, 4, 0, "familiar", false, 4,1, 50.0);
		Torneo torneo= new Torneo("T-01", "Viernes", juego, 10, TipoTorneo.AMISTOSO, 0, 20);

	    assertEquals("T-01", torneo.getCodigo());
	    assertEquals("Viernes", torneo.getDiaSemana());
	    assertEquals(juego, torneo.getJuego());
	    assertEquals(10, torneo.getMaxParticipantes());
	    assertEquals(TipoTorneo.AMISTOSO, torneo.getTipo());
	    assertEquals(0, torneo.getTarifaEntrada());
	    assertEquals(20, torneo.getBonoDescuento());
	    assertEquals(0, torneo.getPremioMetalico());
	    assertEquals(10, torneo.getCuposDisponibles());
	}

	// Historia 2: registrar empleados
	@Test
	public void testHistoria2RegistrarEmpleados()
	{
		Mesero empleado = new Mesero("101", "Carlos", "carlos", "1234");
		
		admin.registrarEmpleado(cafe, empleado);
		
		assertEquals(1, cafe.getEmpleados().size());
		assertTrue(cafe.getEmpleados().contains(empleado));
		assertEquals(empleado, cafe.buscarEmpleado("carlos"));
	}

	// Historia 3: asignar turnos
	@Test
	public void testHistoria3AsignarTurnos()
	{
		Mesero empleado = new Mesero("101", "Carlos", "carlos", "1234");
		Turno lunes = new Turno("LUNES");
		
		cafe.agregarEmpleado(empleado);
		cafe.agregarTurno(lunes);
		cafe.asignarTurno(empleado, lunes);
		
		assertTrue(empleado.getTurnos().contains(lunes));
		assertTrue(lunes.getEmpleados().contains(empleado));
	}

	// Historia 4: inscribir empleados en torneos
	@Test
	public void testHistoria4InscribirEmpleadosEnTorneos()
	{
		Mesero empleado = new Mesero("101", "Carlos", "carlos", "1234");
		JuegoDeMesa juego = new JuegoDeMesa("Monopoly", 2007, "Hasbro", 2, 4, 10, "familiar", false, 3, 1, 50.0);
		
		cafe.agregarEmpleado(empleado);
		cafe.agregarJuego(juego);
		
		Torneo torneo = admin.crearTorneo(cafe, "LUNES", juego, 3, TipoTorneo.AMISTOSO, 0.0, 10000.0);
		admin.inscribirEmpleadoATorneo(cafe, empleado, torneo, 1);
		
		assertTrue(torneo.estaInscrito(empleado));
		assertEquals(1, torneo.getCuposOcupados());
	}

	// Historia 5: registrarse en el sistema
	@Test
	public void testHistoria5RegistrarCliente()
	{
		Cliente cliente = new Cliente("201", "Sofia", "sofia", "clave");
		
		cafe.agregarCliente(cliente);
		
		assertEquals(1, cafe.getClientes().size());
		assertTrue(cafe.getClientes().contains(cliente));
		assertEquals(cliente, cafe.buscarCliente("sofia"));
	}

	// Historia 6: ocupar una mesa
	@Test
	public void testHistoria6OcuparMesa()
	{
		Cliente cliente = new Cliente("201", "Sofia", "sofia", "clave");
		Mesa mesa = new Mesa(1, 4);
		
		cafe.agregarMesa(mesa);
		cafe.recibirCliente(cliente, 3, false, false);
		
		assertTrue(cliente.estaEnCafe());
		assertEquals(mesa, cliente.getMesaActual());
		assertTrue(mesa.ocupada());
	}

	// Historia 7: pedir un prestamo de juego
	@Test
	public void testHistoria7PedirPrestamoDeJuego()
	{
		Cliente cliente = new Cliente("201", "Sofia", "sofia", "clave");
		Mesa mesa = new Mesa(1, 4);
		JuegoDeMesa juego = new JuegoDeMesa("Monopoly", 2007, "Hasbro", 2, 4, 10, "familiar", false, 3, 1, 50.0);
		
		cafe.agregarMesa(mesa);
		cafe.agregarJuego(juego);
		cafe.recibirCliente(cliente, 3, false, false);
		Prestamo prestamo = cafe.crearPrestamo(cliente, juego);
		
		assertTrue(prestamo.isActivo());
		assertEquals(cliente, prestamo.getSolicitante());
		assertEquals(juego, prestamo.getJuego());
	}

	// Historia 8: realizar compras
	@Test
	public void testHistoria8RealizarCompras()
	{
		Cliente cliente = new Cliente("201", "Sofia", "sofia", "clave");
		Mesa mesa = new Mesa(1, 4);
		Bebida bebida = new Bebida("Latte", 8000.0, false, false);
		
		cafe.agregarMesa(mesa);
		cafe.agregarProductoMenu(bebida);
		cafe.recibirCliente(cliente, 2, false, false);
		
		VentaCafe venta = cafe.registrarVentaCafe(cliente, true);
		cafe.agregarProductoAVenta(venta, bebida, 2);
		cafe.cerrarVentaCafe(venta);
		
		assertTrue(venta.isCerrada());
		assertEquals(1, cafe.getVentasCafe().size());
		assertTrue(venta.calcularTotal() > 0);
	}

	// Historia 9: inscribirse a torneos
	@Test
	public void testHistoria9InscribirseATorneos()
	{
		Cliente cliente = new Cliente("201", "Sofia", "sofia", "clave");
		JuegoDeMesa juego = new JuegoDeMesa("Monopoly", 2007, "Hasbro", 2, 4, 10, "familiar", false, 4, 1, 50.0);
		
		cafe.agregarJuego(juego);
		cliente.agregarJuegoFavorito(juego);
		
		Torneo torneo = admin.crearTorneo(cafe, "SABADO", juego, 10, TipoTorneo.AMISTOSO, 0.0, 12000.0);
		cliente.inscribirseATorneo(cafe, torneo, 2);
		
		assertTrue(torneo.estaInscrito(cliente));
		assertEquals(2, torneo.getCuposOcupados());
		assertTrue(torneo.getInscripciones().get(0).isUsoCuposFanaticos());
	}

	// Historia 10: desinscribirse de torneos
	@Test
	public void testHistoria10DesinscribirseDeTorneos()
	{
		Cliente cliente = new Cliente("201", "Sofia", "sofia", "clave");
		JuegoDeMesa juego = new JuegoDeMesa("Monopoly", 2007, "Hasbro", 2, 4, 10, "familiar", false, 4, 1, 50.0);
		
		cafe.agregarJuego(juego);
		Torneo torneo = admin.crearTorneo(cafe, "SABADO", juego, 10, TipoTorneo.AMISTOSO, 0.0, 12000.0);
		cliente.inscribirseATorneo(cafe, torneo, 2);
		cliente.desinscribirseDeTorneo(cafe, torneo);
		
		assertFalse(torneo.estaInscrito(cliente));
		assertEquals(0, torneo.getCuposOcupados());
	}

	// Historia 11: usar bono de torneo amistoso
	@Test
	public void testHistoria11UsarBonoDeTorneoAmistoso()
	{
		Cliente cliente = new Cliente("201", "Sofia", "sofia", "clave");
		JuegoDeMesa juegoTorneo = new JuegoDeMesa("Monopoly", 2007, "Hasbro", 2, 4, 10, "familiar", false, 4, 1, 50.0);
		JuegoDeMesa juegoVenta = new JuegoDeMesa("Twister", 2000, "Hasbro", 2, 4, 8, "familiar", false, 2, 2, 20000.0);
		
		cafe.agregarJuego(juegoTorneo);
		cafe.agregarJuego(juegoVenta);
		Torneo torneo = admin.crearTorneo(cafe, "DOMINGO", juegoTorneo, 8, TipoTorneo.AMISTOSO, 0.0, 5000.0);
		cliente.inscribirseATorneo(cafe, torneo, 1);
		cafe.entregarPremioAmistoso(torneo, cliente);
		
		VentaJuego venta = cafe.registrarVentaJuego(cliente, juegoVenta, 1);
		
		assertEquals(5000.0, venta.getDescuento());
		assertEquals(0.0, cliente.getBonoTorneo());
		assertFalse(cliente.tieneBonoTorneo());
	}

	// Historia 12: cumplir turno
	@Test
	public void testHistoria12CumplirTurno()
	{
		Mesero empleado = new Mesero("101", "Carlos", "carlos", "1234");
		Turno lunes = new Turno("LUNES");
		
		cafe.agregarEmpleado(empleado);
		cafe.agregarTurno(lunes);
		cafe.asignarTurno(empleado, lunes);
		empleado.iniciarTurno();
		
		assertTrue(empleado.isEnTurno());
		assertTrue(cafe.empleadoTrabajaEseDia(empleado, "LUNES"));
	}

	// Historia 13: solicitar cambio de turno
	@Test
	public void testHistoria13SolicitarCambioDeTurno()
	{
		Mesero empleado = new Mesero("101", "Carlos", "carlos", "1234");
		Turno lunes = new Turno("LUNES");
		Turno martes = new Turno("MARTES");
		
		cafe.agregarEmpleado(empleado);
		cafe.agregarTurno(lunes);
		cafe.agregarTurno(martes);
		cafe.asignarTurno(empleado, lunes);
		
		SolicitudCambioTurno solicitud = cafe.solicitarCambioGeneral(empleado, lunes, martes, "Cruce de horario");
		
		assertNotNull(solicitud);
		assertEquals("Pendiente", solicitud.getEstado());
		assertEquals(1, cafe.getSolicitudesCambioTurno().size());
	}

	// Historia 14: participar en torneos
	@Test
	public void testHistoria14ParticiparEnTorneos()
	{
		Mesero empleado = new Mesero("101", "Carlos", "carlos", "1234");
		JuegoDeMesa juego = new JuegoDeMesa("Monopoly", 2007, "Hasbro", 2, 4, 10, "familiar", false, 3, 1, 50.0);
		
		cafe.agregarEmpleado(empleado);
		cafe.agregarJuego(juego);
		Torneo torneo = admin.crearTorneo(cafe, "VIERNES", juego, 3, TipoTorneo.AMISTOSO, 0.0, 10000.0);
		
		cafe.inscribirEmpleadoTorneo(empleado, torneo, 1);
		
		assertTrue(torneo.estaInscrito(empleado));
		assertEquals(1, torneo.getCuposOcupados());
	}
}
