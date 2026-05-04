package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modelo.BoardgameCafe;
import modelo.Cliente;
import modelo.Mesa;
import modelo.Prestamo;
import modelo.JuegoDeMesa;
import modelo.Mesero;
import modelo.VentaCafe;
import modelo.SolicitudCambioTurno;
import modelo.Bebida;
import modelo.VentaJuego;
import modelo.Turno;


public class BoardgameCafeTest {
	
	private BoardgameCafe cafe;
	
	@BeforeEach
	public void setUP()
	{
		cafe = new BoardgameCafe(10);
	}
	
	@Test 
	public void testRecibirCliente()
	{
		Cliente cliente = new Cliente("123","Sofia", "S123", "S123*");
		Mesa mesa = new Mesa(1,4);
		cafe.agregarMesa(mesa);
		cafe.recibirCliente(cliente, 3, false, false);
		
		assertTrue(cliente.estaEnCafe());
		assertEquals(3,cafe.getClientesActuales());
		assertEquals(mesa, cliente.getMesaActual());
		assertTrue(cliente.getMesaActual().ocupada());
		
	}
	
	@Test
	public void testRecibirClienteLleno()
	{
		Cliente cliente = new Cliente("123","Sofia", "S123", "S123*");
		Mesa mesa = new Mesa(1,10);
		cafe.agregarMesa(mesa);
		
		assertThrows(IllegalStateException.class, () -> {
			cafe.recibirCliente(cliente, 11, false, false);
		});
	}
	
	@Test
	public void testRecibirClienteSinDisponibilidad()
	{
		Cliente cliente = new Cliente("123","Sofia", "S123", "S123*");
		
		assertThrows(IllegalStateException.class, () -> {
			cafe.recibirCliente(cliente, 3, false, false);
		});
	}
	
	@Test
	public void testRecibirClienteSuperaCapacidad()
	{
		Cliente cliente = new Cliente("123","Sofia", "S123", "S123*");
		Mesa mesa = new Mesa(1,2);
		cafe.agregarMesa(mesa);
		
		assertThrows(IllegalStateException.class, () -> {
			cafe.recibirCliente(cliente, 4, false, false);
		});
		
	}
	
	@Test
	public void testRetirarCliente()
	{
		Cliente cliente = new Cliente("123","Sofia", "S123", "S123*");
		Mesa mesa = new Mesa(1,2);
		cafe.agregarMesa(mesa);
		cafe.recibirCliente(cliente, 2, false, false);
		
		cafe.retirarCliente(cliente);
		
		assertFalse(cliente.estaEnCafe());
		assertEquals(0,cafe.getClientesActuales());
		assertFalse(mesa.ocupada());
		
	}
	
	@Test
	public void testRetirarClienteCapacidad()
	{
		Cliente cliente = new Cliente("123","Sofia", "S123", "S123*");
		Mesa mesa = new Mesa(1,2);
		cafe.agregarMesa(mesa);
		
		assertThrows(IllegalStateException.class, () -> {
			cafe.recibirCliente(cliente, 4, false, false);
		});
	}
	
	@Test 
	public void testRetirarClientePrestamosActivos()
	{
		
		Cliente cliente = new Cliente("123","Sofia", "S123", "S123*");
		Mesa mesa = new Mesa(1,4);
	
		JuegoDeMesa juego = new JuegoDeMesa("Monopoly Electronic Banking", 2007, "Hasbro", 
				2, 4, 0, "familiar", false, 4,1, 50.0);
		
		cafe.agregarMesa(mesa);
		cafe.agregarJuego(juego);
		cafe.recibirCliente(cliente, 3, false, false);
		
		Mesa mesaAsignada = cliente.getMesaActual();
		Prestamo prestamo = cafe.crearPrestamo(cliente, juego);
		
		cafe.retirarCliente(cliente);
		
		assertFalse(prestamo.isActivo());
	    assertFalse(cliente.estaEnCafe());
	    assertEquals(0, cafe.getClientesActuales());
	    assertNull(cliente.getMesaActual());
	    assertFalse(mesaAsignada.ocupada());
			
	}
	
	@Test
	
	public void testCrearPrestamos()
	{
		Cliente cliente = new Cliente("123","Sofia", "S123", "S123*");
		Mesa mesa = new Mesa(1,4);
	
		JuegoDeMesa juego = new JuegoDeMesa("Monopoly Electronic Banking", 2007, "Hasbro", 
				2, 4, 0, "familiar", false, 4,1, 50.0);
		
		cafe.agregarMesa(mesa);
		cafe.recibirCliente(cliente, 3, false, false);
		
		
		Prestamo prestamo = cafe.crearPrestamo(cliente, juego);
		
		assertTrue(prestamo.isActivo());
		assertEquals(juego,prestamo.getJuego());
		assertEquals(cliente, prestamo.getSolicitante());
		assertEquals(1, cafe.getHistorialPrestamos().size());
		assertEquals(1, juego.getCopiasEnUso());
		assertTrue(cliente.getJuegosPrestados().contains(juego));
			
	}
	
	@Test
	public void testCrearPrestamoNoPermitido() 
	{
		Cliente cliente = new Cliente("123","Sofia", "S123", "S123*");
		Mesa mesa = new Mesa(1,4);
	
		JuegoDeMesa juego1 = new JuegoDeMesa("Monopoly Electronic Banking", 2007, "Hasbro", 
				2, 4, 0, "familiar", false, 4,1, 50.0);
		JuegoDeMesa juego2 = new JuegoDeMesa("Twister", 2000, "Mattel", 
				2, 4, 0, "familiar", false, 4,1, 50.0);
		JuegoDeMesa juego3 = new JuegoDeMesa("Uno", 2000, "Mattel", 
				2, 4, 0, "familiar", false, 4,1, 50.0);
		
		cafe.agregarMesa(mesa);
		cafe.recibirCliente(cliente, 3, false, false);
		cafe.agregarJuego(juego1);
		cafe.agregarJuego(juego2);
		cafe.agregarJuego(juego3);
		cafe.crearPrestamo(cliente, juego1);
		cafe.crearPrestamo(cliente, juego2);
		
		
		assertThrows(IllegalStateException.class, () -> {
			cafe.crearPrestamo(cliente, juego3);
		});	
		
	}
	
	@Test
	public void testCrearPrestamoJuegoNoDisponible()
	{
		Cliente cliente1 = new Cliente("123","Sofia", "S123", "S123*");
		Mesa mesa1 = new Mesa(1,4);
	
		JuegoDeMesa juego1 = new JuegoDeMesa("Monopoly Electronic Banking", 2007, "Hasbro", 
				2, 4, 0, "familiar", false, 1,1, 50.0);
		cafe.agregarMesa(mesa1);
		cafe.recibirCliente(cliente1, 3, false, false);
		cafe.agregarJuego(juego1);
		cafe.crearPrestamo(cliente1, juego1);
		
		Cliente cliente2 = new Cliente("234","Mario", "M234", "M234*");
		Mesa mesa2 = new Mesa(1,4);
		
		cafe.agregarMesa(mesa2);
		cafe.recibirCliente(cliente2, 3, false, false);
		
		assertThrows(IllegalStateException.class, () -> {
			cafe.crearPrestamo(cliente2, juego1);
		});
	}
	
	@Test 
	public void testCrearPrestamoNoMenores()
	{
		Cliente cliente = new Cliente("123","Sofia", "S123", "S123*");
		Mesa mesa = new Mesa(1,4);
	
		JuegoDeMesa juego1 = new JuegoDeMesa("Monopoly Electronic Banking", 2007, "Hasbro", 
				2, 4, 19, "familiar", false, 4,1, 50.0);
		
		cafe.agregarMesa(mesa);
		cafe.recibirCliente(cliente, 3, false, true);
		
		assertThrows(IllegalStateException.class, () -> {
			cafe.crearPrestamo(cliente, juego1);
		});
	}
	
	@Test
	public void testCrearPrestamoEdadMinima()
	{	
		Cliente cliente = new Cliente("123","Sofia", "S123", "S123*");
		Mesa mesa = new Mesa(1,4);
	
		JuegoDeMesa juego1 = new JuegoDeMesa("Monopoly Electronic Banking", 2007, "Hasbro", 
				2, 4, 19, "familiar", false, 4,1, 50.0);
		
		cafe.agregarMesa(mesa);
		cafe.agregarJuego(juego1);
		cafe.recibirCliente(cliente, 3, true, false);
		
		assertThrows(IllegalStateException.class, () -> {
			cafe.crearPrestamo(cliente, juego1);
		});
	}
	
	
	@Test
	public void testCrearPrestamoNoPersonasSuficientes()
	{
		Cliente cliente = new Cliente("123","Sofia", "S123", "S123*");
		Mesa mesa = new Mesa(1,4);
	
		JuegoDeMesa juego1 = new JuegoDeMesa("Monopoly Electronic Banking", 2007, "Hasbro", 
				4, 7 , 19, "familiar", false, 4,1, 50.0);
		
		cafe.agregarMesa(mesa);
		cafe.agregarJuego(juego1);
		cafe.recibirCliente(cliente, 3, false, true);
		
		assertThrows(IllegalStateException.class, () -> {
			cafe.crearPrestamo(cliente, juego1);
		});
	}
	
	@Test
	public void testCrearPrestamoMaximoPersonasSuperado()
	{
		Cliente cliente = new Cliente("123","Sofia", "S123", "S123*");
		Mesa mesa = new Mesa(1,4);
	
		JuegoDeMesa juego1 = new JuegoDeMesa("Monopoly Electronic Banking", 2007, "Hasbro", 
				2, 2, 19, "familiar", false, 4,1, 50.0);
		
		cafe.agregarMesa(mesa);
		cafe.agregarJuego(juego1);
		cafe.recibirCliente(cliente, 3, false, true);
		
		assertThrows(IllegalStateException.class, () -> {
			cafe.crearPrestamo(cliente, juego1);
		});
	}
	
	@Test 
	public void testCrearPrestamoJuegoDificilSinMesero() 
	{
		Cliente cliente = new Cliente("123","Sofia", "S123", "S123*");
		Mesa mesa = new Mesa(1,4);
	
		JuegoDeMesa juego1 = new JuegoDeMesa("Monopoly Electronic Banking", 2007, "Hasbro", 
				2, 4, 0, "familiar", true, 4,1, 50.0);
		
		cafe.agregarMesa(mesa);
		cafe.agregarJuego(juego1);
		cafe.recibirCliente(cliente, 3, false, false);
		
		assertThrows(IllegalStateException.class, () -> {
			cafe.crearPrestamo(cliente, juego1);
		});
		
	}
	
	@Test
	public void testCrearPrestamoJuegoDificilConMesero() 
	{
		Cliente cliente = new Cliente("123","Sofia", "S123", "S123*");
		Mesa mesa = new Mesa(1,4);
	
		JuegoDeMesa juego1 = new JuegoDeMesa("Monopoly", 2007, "Hasbro", 
				2, 4, 0, "familiar", true, 4,1, 50.0);
		Mesero mesero = new Mesero("123","Carlos", "C123", "C123*" );
		
		cafe.agregarMesa(mesa);
		cafe.agregarJuego(juego1);
		cafe.agregarEmpleado(mesero);
		
		mesero.agregarJuegoQueExplica("Monopoly");
		mesero.iniciarTurno();
		
		cafe.recibirCliente(cliente, 3, false, false);
		Prestamo prestamo = cafe.crearPrestamo(cliente, juego1);
		
		assertTrue (prestamo.isActivo());
	}
	
	@Test
	public void testDevolverPrestamo()
	{
		Cliente cliente = new Cliente("123","Sofia", "S123", "S123*");
		Mesa mesa = new Mesa(1,4);
	
		JuegoDeMesa juego1 = new JuegoDeMesa("Monopoly Electronic Banking", 2007, "Hasbro", 
				2, 4, 0, "familiar", false, 4,1, 50.0);
		
		cafe.agregarMesa(mesa);
		cafe.agregarJuego(juego1);
		cafe.recibirCliente(cliente, 3, false, false);
		Prestamo prestamo =cafe.crearPrestamo(cliente, juego1);
		cafe.devolverPrestamo(prestamo);
		
		assertFalse(prestamo.isActivo());
		assertEquals(0, juego1.getCopiasEnUso());
		assertFalse(cliente.getJuegosPrestados().contains(juego1));
	}
	
	@Test
	public void testDevolverPrestamoDevuelto()
	{
		Cliente cliente = new Cliente("123","Sofia", "S123", "S123*");
		Mesa mesa = new Mesa(1,4);
	
		JuegoDeMesa juego1 = new JuegoDeMesa("Monopoly Electronic Banking", 2007, "Hasbro", 
				2, 4, 0, "familiar", false, 4,1, 50.0);
		
		cafe.agregarMesa(mesa);
		cafe.agregarJuego(juego1);
		cafe.recibirCliente(cliente, 3, false, false);
		Prestamo prestamo =cafe.crearPrestamo(cliente, juego1);
		cafe.devolverPrestamo(prestamo);
		
		assertThrows(IllegalStateException.class, () -> {
			cafe.devolverPrestamo(prestamo);
		});
		
	}
	
	@Test
	public void testRegistrarVentaCafe()
	{
		Cliente cliente = new Cliente("123","Sofia", "S123", "S123*");
		Mesa mesa = new Mesa(1,4);
		
		cafe.agregarMesa(mesa);
		cafe.recibirCliente(cliente, 3, false, false);
		VentaCafe ventaCafe = cafe.registrarVentaCafe(cliente, true);
		
		assertEquals(cliente, ventaCafe.getCliente());
		assertTrue(ventaCafe.isCobraPropina());
		assertFalse(ventaCafe.isCerrada());
		assertEquals(mesa, ventaCafe.getMesa());	
	}
	
	@Test
	public void testRegistrarVentaCafeNoMesaAsignada()
	{
		Cliente cliente = new Cliente("123","Sofia", "S123", "S123*");

		assertThrows(IllegalStateException.class, () -> {
			cafe.registrarVentaCafe(cliente, true);
		});
	}

	
	//agregar producto a venta
	@Test
	public void testAgregarProductoAVenta()
	{
		Cliente cliente = new Cliente("123","Sofia", "S123", "S123*");
		Mesa mesa = new Mesa(1,4);
		Bebida producto= new Bebida("latte", 500.0, false, true);
		
		cafe.agregarMesa(mesa);
		cafe.recibirCliente(cliente, 3, false, false);
		
		VentaCafe ventaCafe = cafe.registrarVentaCafe(cliente, true);
		cafe.agregarProductoAVenta(ventaCafe, producto, 2);
		
		assertEquals(1, ventaCafe.getDetalles().size());
		assertEquals(1000.0, ventaCafe.calcularSubtotal());
	}
	
	@Test
	public void testAgregarProductoAVentaYaCerrada()
	{
		Cliente cliente = new Cliente("123","Sofia", "S123", "S123*");
		Mesa mesa = new Mesa(1,4);
		Bebida producto= new Bebida("latte", 500.0, false, true);
		
		cafe.agregarMesa(mesa);
		cafe.recibirCliente(cliente, 3, false, false);
		
		VentaCafe ventaCafe = cafe.registrarVentaCafe(cliente, true);
		cafe.agregarProductoAVenta(ventaCafe, producto, 2);
		cafe.cerrarVentaCafe(ventaCafe);
		
		assertTrue(ventaCafe.isCerrada());
		
		assertThrows(IllegalStateException.class, () -> {
			cafe.agregarProductoAVenta(ventaCafe, producto, 2);
		});
		
	}
	
	@Test
	public void testAgregarProductoAVentaMenores()
	{
		Cliente cliente = new Cliente("123","Sofia", "S123", "S123*");
		Mesa mesa = new Mesa(1,4);
		Bebida producto= new Bebida("cerveza", 500.0, true, false);
		
		
		cafe.agregarMesa(mesa);
		cafe.recibirCliente(cliente, 3, false, true);
		
		VentaCafe ventaCafe = cafe.registrarVentaCafe(cliente, true);
		
		assertThrows(IllegalStateException.class, () -> {
			cafe.agregarProductoAVenta(ventaCafe, producto, 2);
		});
		
	}
	
	@Test
	public void testAgregarProductoAVentaJuegoAccion()
	{
		Cliente cliente = new Cliente("123","Sofia", "S123", "S123*");
		Mesa mesa = new Mesa(1,4);
		Bebida producto= new Bebida("Capuccino", 500.0, false, true);
		
		JuegoDeMesa juego = new JuegoDeMesa("Monopoly Electronic Banking", 2007, "Hasbro", 
				2, 4, 0, "ACCION", false, 4,1, 50.0);
		cafe.agregarMesa(mesa);
		cafe.recibirCliente(cliente, 3, false, false);
		
		cafe.crearPrestamo(cliente, juego);
		
		VentaCafe ventaCafe = cafe.registrarVentaCafe(cliente, true);
		
		assertThrows(IllegalStateException.class, () -> {
			cafe.agregarProductoAVenta(ventaCafe, producto, 2);
		});
	}
	
	//cerrar venta cafe 
	
	@Test
	public void testCerrarVentacafe()
	{

		Cliente cliente = new Cliente("123","Sofia", "S123", "S123*");
		Mesa mesa = new Mesa(1,4);
		Bebida producto= new Bebida("latte", 500.0, false, true);
		
		cafe.agregarMesa(mesa);
		cafe.recibirCliente(cliente, 3, false, false);
		
		VentaCafe ventaCafe = cafe.registrarVentaCafe(cliente, true);
		cafe.agregarProductoAVenta(ventaCafe, producto, 2);
		cafe.cerrarVentaCafe(ventaCafe);
		
		assertTrue(ventaCafe.isCerrada());
		assertTrue(cafe.getVentasCafe().contains(ventaCafe));
		assertTrue(cafe.getTotalVentasCafe()>0);

	}
	
	@Test
	public void testCerrarVentacafeYaCerrada()
	{
		Cliente cliente = new Cliente("123","Sofia", "S123", "S123*");
		Mesa mesa = new Mesa(1,4);
		Bebida producto= new Bebida("latte", 500.0, false, true);
		
		cafe.agregarMesa(mesa);
		cafe.recibirCliente(cliente, 3, false, false);
		
		VentaCafe ventaCafe = cafe.registrarVentaCafe(cliente, true);
		cafe.agregarProductoAVenta(ventaCafe, producto, 2);
		cafe.cerrarVentaCafe(ventaCafe);
		
		assertTrue(ventaCafe.isCerrada());
		
		assertThrows(IllegalStateException.class, () -> {
			cafe.cerrarVentaCafe(ventaCafe);
		});
		
		assertEquals(1, cafe.getVentasCafe().size());
		
	}
	
	@Test
	public void testCerrarVentacafeNoProductos()
	{
		Cliente cliente = new Cliente("123","Sofia", "S123", "S123*");
		Mesa mesa = new Mesa(1,4);
		
		cafe.agregarMesa(mesa);
		cafe.recibirCliente(cliente, 3, false, false);
		
		VentaCafe ventaCafe = cafe.registrarVentaCafe(cliente, true);
		
		assertThrows(IllegalStateException.class, () -> {
			cafe.cerrarVentaCafe(ventaCafe);
		});
		
		assertFalse(ventaCafe.isCerrada());
		assertFalse(cafe.getVentasCafe().contains(ventaCafe));
		
	}
	
	@Test
	public void testRegistrarVentaJuego()
	{
		Cliente cliente = new Cliente("123","Sofia", "S123", "S123*");
		Mesa mesa = new Mesa(1,4);
		JuegoDeMesa juego = new JuegoDeMesa("Monopoly Electronic Banking", 2007, "Hasbro", 
				2, 4, 0, "ACCION", false, 4,1, 50.0);
		cafe.agregarMesa(mesa);
		cafe.recibirCliente(cliente, 3, false, false);
		
		VentaJuego ventaJuego = cafe.registrarVentaJuego(cliente, juego, 1);
		
		assertEquals(cliente, ventaJuego.getComprador());
		assertTrue(ventaJuego.isCerrada());
		assertEquals(1,cafe.getVentasJuego().size());
		assertEquals(0,juego.getCopiasVenta());
		assertEquals(59.5, ventaJuego.calcularTotal(), 0.01);
	
	}
	
	@Test
	public void testRegistrarVentaJuegoNoCopiasDisponibles()
	{
		Cliente cliente = new Cliente("123","Sofia", "S123", "S123*");
		Mesa mesa = new Mesa(1,4);
		JuegoDeMesa juego = new JuegoDeMesa("Monopoly Electronic Banking", 2007, "Hasbro", 
				2, 4, 0, "ACCION", false, 4,1, 50.0);
		cafe.agregarMesa(mesa);
		cafe.agregarJuego(juego);
		cafe.recibirCliente(cliente, 3, false, false);
		
		cafe.registrarVentaJuego(cliente, juego, 1);
		
		assertThrows(IllegalStateException.class, () -> {
			cafe.registrarVentaJuego(cliente, juego, 1);
		});
		
	}
	
	
	@Test
	public void testRegistrarVentaJuegoNoCopiasSuficientes()
	{
		Cliente cliente = new Cliente("123","Sofia", "S123", "S123*");
		Mesa mesa = new Mesa(1,4);
		JuegoDeMesa juego = new JuegoDeMesa("Monopoly Electronic Banking", 2007, "Hasbro", 
				2, 4, 0, "ACCION", false, 4,1, 50.0);
		cafe.agregarMesa(mesa);
		cafe.agregarJuego(juego);
		cafe.recibirCliente(cliente, 3, false, false);
		
		
		assertThrows(IllegalStateException.class, () -> {
			cafe.registrarVentaJuego(cliente, juego, 2);
		});
	}

	@Test
	public void testAplicarCodigoDescuento()
	{
		Cliente cliente = new Cliente("123","Sofia", "S123", "S123*");
		Mesero mesero = new Mesero("123","Carlos", "C123", "C123*" );
		
		cafe.agregarEmpleado(mesero);
		String codigo=mesero.getCodigoDescuento();
		cafe.aplicarCodigoDescuento(cliente, codigo);
		
		assertTrue(cliente.tieneDescuento());
		assertEquals(codigo, cliente.getCodigoDescuento());
	}
	
	@Test
	public void testAplicarCodigoDescuentoInvalido()
	{
		Cliente cliente = new Cliente("123","Sofia", "S123", "S123*");
		assertThrows(IllegalStateException.class, () -> {
			cafe.aplicarCodigoDescuento(cliente, "CODIGO-FALSO");
		});
		
		assertFalse(cliente.tieneDescuento());
		
	}
	@Test
	public void testSolicitarCambioGeneral()
	{
		Mesero mesero = new Mesero("123","Carlos", "C123", "C123*" );
		Turno lunes = new Turno("LUNES");
		Turno martes = new Turno("MARTES");
		
		cafe.agregarEmpleado(mesero);
		cafe.agregarTurno(lunes);
		cafe.agregarTurno(martes);
		
		cafe.asignarTurno(mesero, lunes);
		SolicitudCambioTurno solicitud = cafe.solicitarCambioGeneral(mesero, lunes, martes, "motivo");
		
		assertEquals("Pendiente", solicitud.getEstado());
		assertEquals(mesero, solicitud.getEmpleadoSolicitante());
		assertEquals(martes, solicitud.getTurnoSolicitado());
	}
	
	@Test
	public void testSolicitarCambioGeneralSinTurno()
	{
		Mesero mesero = new Mesero("123","Carlos", "C123", "C123*" );
		Turno lunes = new Turno("LUNES");
		Turno martes = new Turno("MARTES");
		
		cafe.agregarEmpleado(mesero);
		cafe.agregarTurno(lunes);
		cafe.agregarTurno(martes);
		
		assertThrows(IllegalStateException.class, () -> {
			cafe.solicitarCambioGeneral(mesero, lunes, martes, "Motivo");
	});
		
	}
	
	
	@Test
	public void testSolicitarIntercambioturno() 
	{
		Mesero mesero1 = new Mesero("123","Carlos", "C123", "C123*" );
		Mesero mesero2 = new Mesero("234", "Ana", "C234", "C234*");
		
		Turno lunes = new Turno("LUNES");
		Turno martes = new Turno("MARTES");
		
		cafe.agregarEmpleado(mesero1);
		cafe.agregarEmpleado(mesero2);
		cafe.agregarTurno(lunes);
		cafe.agregarTurno(martes);
		
		cafe.asignarTurno(mesero1, lunes);
		cafe.asignarTurno(mesero2, martes);
		SolicitudCambioTurno solicitud = cafe.solicitarIntercambioTurno(mesero1,mesero2, lunes, martes, "Intercambio");
		
		assertEquals("Pendiente", solicitud.getEstado());
		assertEquals(mesero1, solicitud.getEmpleadoSolicitante());
		assertEquals(mesero2, solicitud.getEmpleadoIntercambio());
		
	}
	
	@Test
	public void testSolicitarIntercambioturnoSinTurno() 
	{
		Mesero mesero1 = new Mesero("123","Carlos", "C123", "C123*" );
		Mesero mesero2 = new Mesero("234", "Ana", "C234", "C234*");
		
		Turno lunes = new Turno("LUNES");
		Turno martes = new Turno("MARTES");
		
		cafe.agregarEmpleado(mesero1);
		cafe.agregarEmpleado(mesero2);
		cafe.agregarTurno(lunes);
		cafe.agregarTurno(martes);
		
		cafe.asignarTurno(mesero1, lunes);
		assertThrows(IllegalStateException.class, () -> {
			cafe.solicitarIntercambioTurno(mesero1, mesero2, lunes, martes, "Intercambio");
		});
		
	}
	
}
	