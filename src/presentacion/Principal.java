package presentacion;

import java.util.ArrayList;

import modelo.Administrador;
import modelo.Bebida;
import modelo.BoardgameCafe;
import modelo.Cliente;
import modelo.Cocinero;
import modelo.DetalleVentaCafe;
import modelo.DetalleVentaJuego;
import modelo.JuegoDeMesa;
import modelo.Mesa;
import modelo.Mesero;
import modelo.Pasteleria;
import modelo.Prestamo;
import modelo.SolicitudCambioTurno;
import modelo.SugerenciaPlatillo;
import modelo.VentaCafe;
import modelo.VentaJuego;
import modelo.Empleado;
import modelo.PuedePrestar;
import modelo.Usuario;
import modelo.Turno;
import modelo.ProductoMenu;
import modelo.Torneo;
import modelo.TipoTorneo;

import persistencia.CentralPersistencia;
import persistencia.PersistenciaBoardGameCafeJSON;

public class Principal
{
	public static void main(String[] args)
	{
		BoardgameCafe cafe = new BoardgameCafe(20);

		// =========================
		// ADMINISTRADOR
		// =========================
		Administrador admin = new Administrador("A1", "Andres", "admin", "admin123");
		cafe.setAdministrador(admin);
		System.out.println("Administrador del sistema: " + cafe.getAdministrador());

		// =========================
		// CLIENTES
		// =========================
		Cliente c1 = new Cliente("1", "Samuel", "samuel123", "1234");
		Cliente c2 = new Cliente("2", "Laura", "laura456", "abcd");
		Cliente c3 = new Cliente("3", "Sofia", "sof1", "hola");

		cafe.agregarCliente(c1);
		cafe.agregarCliente(c2);
		cafe.agregarCliente(c3);
		
		// =========================
		// EMPLEADOS
		// =========================
		Mesero m1 = new Mesero("10", "Carlos", "carlosm", "1111");
		Cocinero co1 = new Cocinero("11", "Pedro", "pedroc", "2222");
		Mesero m2 = new Mesero("12", "Ana", "anam", "2222");
		Mesero m3 = new Mesero("13", "Hector", "Hec555", "azul");
		Mesero m4 = new Mesero("14", "Andrea", "Andreag", "triangulo");
		Mesero m5 = new Mesero("15", "Juan", "Juan04", "queso");
		Mesero m6 = new Mesero("16", "Adrian", "Adrianp", "1234");
		Cocinero co2 = new Cocinero("17", "Nicolas", "nico123", "5678");
		cafe.agregarEmpleado(m1);
		cafe.agregarEmpleado(m2);
		cafe.agregarEmpleado(co1);
		cafe.agregarEmpleado(m3);
		cafe.agregarEmpleado(m4);
		cafe.agregarEmpleado(m5);
		cafe.agregarEmpleado(m6);
		cafe.agregarEmpleado(co2);
		m1.agregarJuegoQueExplica("Catan");
		
		

		// =========================
		// MESAS
		// =========================
		Mesa mesa1 = new Mesa(1, 4);
		Mesa mesa2 = new Mesa(2, 6);
		cafe.agregarMesa(mesa1);
		cafe.agregarMesa(mesa2);

		// =========================
		// JUEGOS
		// =========================
		JuegoDeMesa j1 = new JuegoDeMesa("Catan", 2000, "Hasbro", 3, 6, 10, "TABLERO", true, 1, 10, 50000);
		JuegoDeMesa j2 = new JuegoDeMesa("Uno",1980, "Mattel", 2, 10, 5, "CARTAS", false, 6, 5, 30000);
		JuegoDeMesa j3 = new JuegoDeMesa("Twister",1995, "Mattel", 2, 4, 5, "ACCION", false, 6, 5, 40000);
		cafe.agregarJuego(j1);
		cafe.agregarJuego(j2);
		cafe.agregarJuego(j3);

		
		
		
		// =========================
		// TURNOS
		// =========================
		Turno lunes = new Turno("LUNES");
		Turno martes = new Turno("MARTES");
		Turno miercoles = new Turno("MIERCOLES");
		Turno jueves = new Turno("JUEVES");
		Turno viernes = new Turno("VIERNES");
		Turno sabado = new Turno("SABADO");
		Turno domingo = new Turno("DOMINGO");
		
		cafe.agregarTurno(lunes);
		cafe.agregarTurno(martes);
		cafe.agregarTurno(miercoles);
		cafe.agregarTurno(jueves);
		cafe.agregarTurno(viernes);
		cafe.agregarTurno(sabado);
		cafe.agregarTurno(domingo);



		// =========================
		// TURNOS ASIGNAR E INICIAR
		// =========================
		cafe.asignarTurno(m1, lunes);
		cafe.asignarTurno(m2, lunes);
		cafe.asignarTurno(co1, lunes);
		cafe.asignarTurno(m3, lunes);
		cafe.asignarTurno(co2, martes); 
		
		m1.iniciarTurno();
		m2.iniciarTurno();
		co1.iniciarTurno();
		m3.iniciarTurno();
		co2.iniciarTurno();
		
		// =========================
		// PRODUCTOS MENÚ
		// =========================
		Bebida b1 = new Bebida("Cafe Americano", 7000, false, true);
		Bebida b2 = new Bebida("Cerveza", 9000, true, false);
		Pasteleria pa1 = new Pasteleria("Brownie", 8000);
		Pasteleria pa2 = new Pasteleria("Galleta", 5000);
		pa1.agregarAlergeno("gluten");

		cafe.agregarProductoMenu(b1);
		cafe.agregarProductoMenu(b2);
		cafe.agregarProductoMenu(pa1);
		cafe.agregarProductoMenu(pa2);

		System.out.println("Productos del menu:");
		for (ProductoMenu p : cafe.getProductosMenu()) {
		    System.out.println(p);
		}
		
		
		
		// =========================
		// FASE 1: PRÉSTAMOS
		// =========================
		System.out.println("\n===== FASE 1: PRÉSTAMOS =====");
		
		// Recibir clientes
		try {
		    cafe.recibirCliente(c1, 3, false, false);
		    System.out.println(c1.getNombre()+ "recibido en mesa: " + c1.getMesaActual().getNumero());
		} catch (IllegalStateException e) {
		    System.out.println("Error: " + e.getMessage());
		}

		try {
		    cafe.recibirCliente(c2, 2, true, false);
		    System.out.println(c2.getNombre()+"recibida en mesa: " + c2.getMesaActual().getNumero());
		} catch (IllegalStateException e) {
		    System.out.println("Error: " + e.getMessage());
		}
		
		System.out.println("\nCatálogo juegos disponibles para préstamo");
		for (JuegoDeMesa j : cafe.getJuegosDisponiblesPrestamo()) {
		    System.out.println(j);
		}
		
		// Realizar prestamos
		Prestamo pre1 = null;
		
		try {
		    pre1 = cafe.crearPrestamo(c1, j1);
		    System.out.println("Prestamo creado: " + pre1);
		    System.out.println("Copias disponibles Catan: " + j1.getCopiasDisponiblesPrestamo());
		} catch (IllegalStateException e) {
		    System.out.println("Error: " + e.getMessage());
		}
		
		try {
		    Prestamo pre2 = cafe.crearPrestamo(c1, j2);
		    System.out.println("Segundo prestamo creado: " + pre2);
		} catch (IllegalStateException e) {
		    System.out.println("Error: " + e.getMessage());
		}
		
		
		System.out.println("\n Prueba error: prestar un tercer juego");
		try {
		    Prestamo pre3 = cafe.crearPrestamo(c1, j3);
		    System.out.println("Debio haber fallado");
		} catch (IllegalStateException e) {
		    System.out.println("Error esperado: " + e.getMessage());
		}
		
		System.out.println("\n Prueba error: juego no disponible");
		try {
		    cafe.crearPrestamo(c2, j1);  // todas las copias de Catan prestadas
		    System.out.println("Debio haber fallado");
		} catch (IllegalStateException e) {
		    System.out.println("Error esperado: " + e.getMessage());
		}

		// ERROR ESPERADO: cliente sin mesa
		System.out.println("\n Prueba error: cliente sin mesa ");
		try {
		    Cliente c4 = new Cliente("4", "Pedro", "pedro123", "perro");
		    Prestamo pre4 = cafe.crearPrestamo(c4, j2);
		    System.out.println("Debio haber fallado");
		} catch (IllegalStateException e) {
		    System.out.println("Error esperado: " + e.getMessage());
		}
		
		
		

		System.out.println();
		System.out.println("Estado general del sistema:");
		System.out.println(cafe);

		System.out.println();
		System.out.println("Copias disponibles de Catan: " + j1.getCopiasDisponiblesPrestamo());
		System.out.println("Copias disponibles de Uno: " + j2.getCopiasDisponiblesPrestamo());

		System.out.println();
		
		// Ver préstamos activos
				System.out.println("\n-- Préstamos activos --");
				for (Prestamo p : cafe.getPrestamosActivos()) {
				    System.out.println(p);
				}
				
		// Retirar cliente
		cafe.retirarCliente(c1);
		System.out.println("Cliente retirado");
		System.out.println(" Prestamo activo: " + pre1.isActivo());  // false
		System.out.println("  Copias disponibles Catan después de devolver: " + j1.getTotalCopiasPrestamo()); 
		System.out.println();
		
		System.out.println("Estado final del sistema:");
		System.out.println(cafe);

		
		
		// =========================
		// FASE 2: MENÚ Y VENTAS CAFÉ
		// =========================
		
		System.out.println("\n==== FASE 2: VENTAS CAFETERÍA ====");
		
		
		System.out.println("Venta cafe --");
		VentaCafe venta1 = null;
		try {
		    venta1 = cafe.registrarVentaCafe(c2, true);
		    cafe.agregarProductoAVenta(venta1, b1, 2);  // 2 cafes
		    cafe.agregarProductoAVenta(venta1, pa1, 1);  // 1 brownie
		    cafe.cerrarVentaCafe(venta1);
		    System.out.println("Venta cerrada: " + venta1);
		    System.out.println("Puntos de Laura: " + c2.getPuntosFidelidad());
		} catch (IllegalStateException e) {
		    System.out.println("Error: " + e.getMessage());
		}

		System.out.println();
		System.out.println("Venta del café registrada:");
		System.out.println(venta1);

		// ERROR ESPERADO: alcohol con menores
		System.out.println("Prueba error: alcohol con menores --");
		try {
		    VentaCafe ventaMenores = cafe.registrarVentaCafe(c2, false);
		    cafe.agregarProductoAVenta(ventaMenores, b2, 1);  // cerveza
		    System.out.println("Debio haber fallado");
		} catch (IllegalStateException e) {
		    System.out.println("Error esperado: " + e.getMessage());
		}

		// ERROR ESPERADO: bebida caliente con juego de accion
		System.out.println("Prueba error: bebida caliente con juego accion --");
		try {
		    Prestamo pAccion = cafe.crearPrestamo(c2, j3);  // j3 es ACCION
		    VentaCafe ventaAccion = cafe.registrarVentaCafe(c2, false);
		    cafe.agregarProductoAVenta(ventaAccion, b1, 1);  // cafe caliente
		    System.out.println("Debio haber fallado");
		} catch (IllegalStateException e) {
		    System.out.println("Error esperado: " + e.getMessage());
		}

		// ERROR ESPERADO: cliente sin mesa
		System.out.println("Prueba error: cliente sin mesa --");
		try {
		    cafe.registrarVentaCafe(c3, true);
		    System.out.println("Debio haber fallado");
		} catch (IllegalStateException e) {
		    System.out.println("Error esperado: " + e.getMessage());
		}
		
		
		
		
		System.out.println();
		System.out.println("Productos del menú registrados:");
		for (int i = 0; i < cafe.getProductosMenu().size(); i++)
		{
			System.out.println(cafe.getProductosMenu().get(i));
		}

		System.out.println();
		System.out.println("Ventas del café registradas:");
		for (VentaCafe v : cafe.getVentasCafe()) {
		    System.out.println(v);
		}
		
		System.out.println("\n-- Informe ventas cafeteria --");
		System.out.println("Total ventas cafe: $" + cafe.getTotalVentasCafe());
		
		

		// =========================
		// FASE 3: TURNOS Y SUGERENCIAS
		// =========================
		
		System.out.println("\n ===== FASE 3: TURNOS Y SUGERENCIAS ===== ");
		
		System.out.println(" Turnos actuales ");
		System.out.println("Turnos de Carlos: " + m1.getTurnos());
		System.out.println("Turnos de Ana: " + m2.getTurnos());
		System.out.println("Turnos de Pedro: " + co1.getTurnos());
		
		
		System.out.println(" Solicitud cambio general ");
		try {
		    SolicitudCambioTurno sol1 = cafe.solicitarCambioGeneral(m1, lunes, martes, "Cita medica");
		    System.out.println("Solicitud creada: " + sol1);
		    cafe.aprobarCambioGeneral(sol1);
		    System.out.println("Solicitud aprobada");
		    System.out.println(" Turnos de Carlos ahora: " + m1.getTurnos());
		} catch (IllegalStateException e) {
		    System.out.println("Error: " + e.getMessage());
		}

		
		System.out.println(" Prueba error: turno sin empleados minimos");
		try {
		    // m2 quiere salir del lunes pero quedaria solo 1 mesero el lunes
		    SolicitudCambioTurno sol2 = cafe.solicitarCambioGeneral(m2, lunes, martes, "Graduacion");
		    cafe.aprobarCambioGeneral(sol2);
		    System.out.println("Debio haber fallado");
		} catch (IllegalStateException e) {
		    System.out.println("Error esperado: " + e.getMessage());
		}
		
		System.out.println("Se asigna turno martes a mesero 4 y 5 para hacer prueba de intercambio");
	
		cafe.asignarTurno(m4, martes);
		cafe.asignarTurno(m5, martes);
		cafe.asignarTurno(m6, lunes);
		
		// Solicitud intercambio exitosa
		System.out.println(" Solicitud intercambio de turno ");
		try {
		    SolicitudCambioTurno sol3 = cafe.solicitarIntercambioTurno(m2, m1, lunes, martes, "Prefiero el lunes");
		    System.out.println("Solicitud intercambio creada: " + sol3);
		    cafe.aprobarIntercambioTurno(sol3);
		    System.out.println("Intercambio aprobado");
		    System.out.println("  Turnos de Carlos: " + m1.getTurnos());
		    System.out.println("  Turnos de Ana: " + m2.getTurnos());
		} catch (IllegalStateException e) {
		    System.out.println("Error: " + e.getMessage());
		}
		
		
		// Rechazar solicitud
		System.out.println(" Rechazar solicitud ");
		try {
		    SolicitudCambioTurno sol5 = cafe.solicitarCambioGeneral(m1, lunes, martes, "Vacaciones");
		    cafe.rechazarCambioTurno(sol5);
		    System.out.println("Solicitud rechazada: " + sol5.getEstado());
		} catch (IllegalStateException e) {
		    System.out.println("Error: " + e.getMessage());
		}

		// Ver solicitudes pendientes
		System.out.println(" Solicitudes pendientes ");
		ArrayList<SolicitudCambioTurno> pendientes = cafe.getSolicitudesPendientes();
		if (pendientes.isEmpty()) {
		    System.out.println("No hay solicitudes pendientes");
		} else {
		    for (SolicitudCambioTurno s : pendientes) {
		        System.out.println(s);
		    }
		}
		
		
		// =========================
		// SUGERENCIAS DE PLATILLOS
		// =========================
		
		System.out.println("\n ===== SUGERENCIAS DE PLATILLOS ===== ");
		
		
		System.out.println(" Sugerencia aprobada ");
		try {
		    SugerenciaPlatillo sug1 = cafe.crearSugerencia(m1, "Cheesecake", "Tarta de queso con frutos rojos");
		    System.out.println("Sugerencia creada: " + sug1);
		    cafe.aprobarSugerencia(sug1, "PASTELERIA", 8000);
		    System.out.println("Sugerencia aprobada");
		    System.out.println("  Productos en menu: " + cafe.getProductosMenu().size());
		} catch (IllegalStateException e) {
		    System.out.println("Error: " + e.getMessage());
		}

		// Sugerencia rechazada
		System.out.println("\n-- Sugerencia rechazada --");
		try {
		    SugerenciaPlatillo sug2 = cafe.crearSugerencia(co1, "Pizza", "Pizza napolitana");
		    System.out.println("Sugerencia creada: " + sug2);
		    cafe.rechazarSugerencia(sug2);
		    System.out.println("Sugerencia rechazada: " + sug2.getEstado());
		} catch (IllegalStateException e) {
		    System.out.println("Error: " + e.getMessage());
		}
		
		
		// Ver todas las sugerencias
		System.out.println("\n-- Todas las sugerencias --");
		for (SugerenciaPlatillo s : cafe.getSugerenciasPlatillos()) {
		    System.out.println(s);
		}
		
		

		// =========================
		// FASE 4: VENTAS DE JUEGOS
		// =========================
		
		System.out.println("\n ===== FASE 4: VENTAS DE JUEGOS ===== ");
		
		System.out.println("Venta juego a cliente");
		try {VentaJuego vj1 = cafe.registrarVentaJuego(c2, j1, 1);
		    System.out.println(" Venta creada: " + vj1);
		    System.out.println("  Copias venta Catan: " + j1.getCopiasVenta());
		    System.out.println("  Puntos Laura: " + c2.getPuntosFidelidad());
		} catch (IllegalStateException e) {
		    System.out.println(" Error: " + e.getMessage());
		}
		
		System.out.println("Venta juego a cliente sin mesa");
		try {
		    VentaJuego vj2 = cafe.registrarVentaJuego(c1, j2, 1);
		    System.out.println("Venta creada: " + vj2);
		    System.out.println("  Copias venta Catan: " + j2.getCopiasVenta());
		    System.out.println("  Puntos Samuel: " + c1.getPuntosFidelidad());
		} catch (IllegalStateException e) {
		    System.out.println("Error: " + e.getMessage());
		}
		
		
		
		// Venta a empleado con descuento 20%
		System.out.println("Venta juego a empleado (20% descuento)");
		try {
		    VentaJuego vj3 = cafe.registrarVentaJuego(m1, j2, 1);
		    System.out.println("Venta creada: " + vj3);
		    System.out.println("  Descuento aplicado: $" + vj3.getDescuento());
		} catch (IllegalStateException e) {
		    System.out.println("Error: " + e.getMessage());
		}
		
		
		// Venta a cliente con codigo de descuento 10%
		System.out.println("Venta juego a cliente con descuento (10%) ");
		try {
		    cafe.aplicarCodigoDescuento(c2, m1.getCodigoDescuento());
		    VentaJuego vj4 = cafe.registrarVentaJuego(c2, j2, 1);
		    System.out.println(" Venta creada: " + vj4);
		    System.out.println("  Descuento aplicado: $" + vj4.getDescuento());
		} catch (IllegalStateException e) {
		    System.out.println(" Error: " + e.getMessage());
		}
		
		
		// ERROR ESPERADO: no hay copias en venta
		System.out.println(" Prueba error: sin copias en venta ");
		try {
		    JuegoDeMesa j4 = new JuegoDeMesa("Monopoly", 1935, "Hasbro", 2, 8, 8, "TABLERO", false, 3, 0, 60000);
		    cafe.agregarJuego(j4);
		    cafe.registrarVentaJuego(c1, j4, 1);  // 0 copias en venta
		    System.out.println(" Debio haber fallado");
		} catch (IllegalStateException e) {
		    System.out.println(" Error esperado: " + e.getMessage());
		}

		// ERROR ESPERADO: cantidad mayor a copias disponibles
		System.out.println(" Prueba error: cantidad mayor a copias ");
		try {
		    cafe.registrarVentaJuego(c1, j3, 100);
		    System.out.println(" Debio haber fallado");
		} catch (IllegalStateException e) {
		    System.out.println(" Error esperado: " + e.getMessage());
		}

		// Ver ventas de juegos
		System.out.println(" Ventas de juegos registradas: ");
		for (VentaJuego v : cafe.getVentasJuego()) {
		    System.out.println(v);
		}

		
		// =========================
		// FASE 5: INFORMES
		// =========================
		System.out.println("\n ===== FASE 5: INFORMES =====");
		System.out.println(cafe.getInformeVentas());

		System.out.println("-- Historial prestamos -- ");
		for (Prestamo p : cafe.getHistorialPrestamos()) {
		    System.out.println(p);
		}

		System.out.println("\n-- Estado final del sistema --");
		System.out.println(cafe);
		
		// =========================
		// FASE 6: TORNEOS
		// =========================
		
		System.out.println("\n ===== FASE 6: TORNEOS ===== ");

		Torneo torneoAmistoso = null;

		try {
		    torneoAmistoso = admin.crearTorneo(cafe, "VIERNES", j1, 6, TipoTorneo.AMISTOSO, 0, 5000);

		    c1.agregarJuegoFavorito(j1);

		    c1.inscribirseATorneo(cafe, torneoAmistoso, 2);

		    System.out.println("Torneo amistoso creado: " + torneoAmistoso);
		    System.out.println("Samuel inscrito como fanatico");
		    System.out.println("Cupos ocupados: " + torneoAmistoso.getCuposOcupados());
		    System.out.println("Cupos fanaticos: " + torneoAmistoso.getCuposFanaticos());

		    cafe.entregarPremioAmistoso(torneoAmistoso, c1);
		    System.out.println("Bono ganado por Samuel: $" + c1.getBonoTorneo());

		    VentaJuego ventaConBono = cafe.registrarVentaJuego(c1, j1, 1);
		    System.out.println("Venta con bono aplicada: " + ventaConBono);
		    System.out.println("Descuento aplicado por bono: $" + ventaConBono.getDescuento());
		    System.out.println("Bono restante Samuel: $" + c1.getBonoTorneo());

		} catch (IllegalStateException e) {
		    System.out.println("Error: " + e.getMessage());
		}

		try {
		    Torneo torneoCompetitivo = admin.crearTorneo(cafe, "MARTES", j2, 10, TipoTorneo.COMPETITIVO, 10000, 0);

		    admin.inscribirEmpleadoATorneo(cafe, m4, torneoCompetitivo, 1);
		    cafe.inscribirClienteTorneo(c2, torneoCompetitivo, 3);

		    System.out.println("Torneo competitivo creado: " + torneoCompetitivo);
		    System.out.println("Recaudado: $" + torneoCompetitivo.getRecaudado());
		    System.out.println("Premio metalico: $" + torneoCompetitivo.getPremioMetalico());

		} catch (IllegalStateException e) {
		    System.out.println("Error: " + e.getMessage());
		}

		try {
		    Torneo torneoError = admin.crearTorneo(cafe, "LUNES", j2, 6, TipoTorneo.COMPETITIVO, 8000, 0);

		    admin.inscribirEmpleadoATorneo(cafe, m2, torneoError, 1);
		    System.out.println("Debio haber fallado");

		} catch (IllegalStateException e) {
		    System.out.println("Error esperado empleado con turno: " + e.getMessage());
		}
		
		// =========================
		// PERSISTENCIA
		// =========================

		// crear persistencia (ruta del archivo)
		PersistenciaBoardGameCafeJSON persistenciaJSON =
		        new PersistenciaBoardGameCafeJSON("src/datos/boardgamecafe.txt"); //sabemos que se llama PersistenciaBoard...JSON pero prefirimos dejarlo en formato txt, igual si quieren que sea en formato JSON, solo cambien el "txt" despues del punto por json
                                                                                  //Ademas para encontrar que la persistencia esta funcionando vean que se crea el archivo txt en la carpeta datos que viene de src
		// crear central de persistencia
		CentralPersistencia central = new CentralPersistencia(persistenciaJSON);

		// guardar sistema
		central.guardar(cafe);

		System.out.println("\nSistema guardado correctamente");

		// cargar sistema
		BoardgameCafe cafeCargado = central.cargar();

		System.out.println("\nSistema cargado desde archivo:");
		System.out.println(cafeCargado);
		
		
}
	
}