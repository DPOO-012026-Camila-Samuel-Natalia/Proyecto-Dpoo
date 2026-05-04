package presentacion;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
 
import modelo.*;
import persistencia.CentralPersistencia;
import persistencia.PersistenciaBoardGameCafeJSON;



public class ConsolaEmpleado {
	    private BoardgameCafe cafe;
	    private BufferedReader br;
	    private CentralPersistencia central;
	 
	    public ConsolaEmpleado() {
	        central = new CentralPersistencia(new PersistenciaBoardGameCafeJSON("src/datos/boardgamecafe.txt"));
	        this.cafe = central.cargar();
	        if (this.cafe == null) {
	            System.out.println("No se encontró archivo. Iniciando sistema vacío.");
	            this.cafe = new BoardgameCafe(20);
	        }
	 
	        this.br = new BufferedReader(new InputStreamReader(System.in));
	        
	       
	        Empleado empleado = autenticar();
	 
	        if (empleado != null) {
	            mostrarMenu(empleado);
	        }
	 
	        central.guardar(cafe);
	        System.out.println("Datos guardados. ¡Hasta luego!");
	 
	        try {
	            br.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    
	    // AUTENTICACION
	    
	 
	    private Empleado autenticar() {
	        System.out.println("=== ACCESO EMPLEADOS - BOARDGAME CAFÉ ===");
	        try {
	            System.out.println("Login:");
	            String login = br.readLine();
	            System.out.println("Contraseña:");
	            String password = br.readLine();
	 
	            Empleado empleado = cafe.buscarEmpleado(login);
	            if (empleado == null || !empleado.getPassword().equals(password)) {
	                System.out.println("Login o contraseña incorrectos.");
	                return null;
	            }
	 
	            System.out.println("¡Bienvenido, " + empleado.getNombre() + "!");
	            return empleado;
	 
	        } catch (IOException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
	    
	   // MENU PRINCIPAL
	 
	 
	    private void mostrarMenu(Empleado empleado) {
	        int op = 0;
	        do {
	            System.out.println("\n=== MENÚ EMPLEADO - " + empleado.getNombre()+ " (" + (empleado instanceof Mesero ? "Mesero" : "Cocinero") + ") ===");
	            System.out.println("Estado: " + (empleado.isEnTurno() ? "En turno" : "Fuera de turno"));
	            System.out.println("\nDigite:");
	            System.out.println("1. Ver mis turnos");
	            System.out.println("2. Solicitar cambio de turno");
	            System.out.println("3. Sugerir platillo");
	            System.out.println("4. Ver torneos disponibles");
	            System.out.println("5. Comprar un juego");
	 
	            if (empleado instanceof Mesero) {
	            	System.out.println("6. Aplicar descuento a cliente");
	                System.out.println("7. Registrar préstamo para una mesa");
	                System.out.println("8. Registrar préstamo para mí mismo");
	                System.out.println("9. Recibir cliente");
	                System.out.println("10. Retirar cliente");
	            }
	 
	            System.out.println("0. Cerrar sesión");
	 
	            op = leerEntero();
	 
	            if (op == 1)      verTurnos(empleado);
	            else if (op == 2) solicitarCambioTurno(empleado);
	            else if (op == 3) sugerirPlatillo(empleado);
	            else if (op == 4) verTorneos();
	            else if (op == 5) comprarJuego(empleado);
	            else if (op == 6 && empleado instanceof Mesero) aplicarDescuento((Mesero) empleado);
	            else if (op == 7 && empleado instanceof Mesero) prestamoParaMesa((Mesero) empleado);
	            else if (op == 8 && empleado instanceof Mesero) prestamoParaSiMismo((Mesero) empleado);
	            else if (op == 9 && empleado instanceof Mesero) recibirCliente();
	            else if (op == 10 && empleado instanceof Mesero) retirarCliente();
	            else if (op != 0) System.out.println("Opción inválida.");
	 
	        } while (op != 0);
	        System.out.println("Sesión cerrada.");
	    }    
	    
	
	    // TURNOS
	    
	 
	    private void verTurnos(Empleado empleado) {
	        System.out.println("\n--- MIS TURNOS ---");
	        if (empleado.getTurnos().isEmpty()) {
	            System.out.println("No tienes turnos asignados.");
	            return;
	        }
	        for (Turno t : empleado.getTurnos()) {
	            System.out.println("- " + t.getDiaSemana());
	        }
	    }
	 
	    private void solicitarCambioTurno(Empleado empleado) {
	        System.out.println("\n--- SOLICITAR CAMBIO DE TURNO ---");
	        if (empleado.getTurnos().isEmpty()) {
	            System.out.println("No tienes turnos asignados.");
	            return;
	        }
	 
	        try {
	            System.out.println("Tipo de cambio:");
	            System.out.println("1. Cambio general");
	            System.out.println("2. Intercambio con otro empleado");
	            int tipo = leerEntero();
	 
	            if (tipo != 1 && tipo != 2) {
	                System.out.println("Opción inválida.");
	                return;
	            }
	 
	            System.out.println("Tus turnos:");
	            for (Turno t : empleado.getTurnos()) {
	                System.out.println("- " + t.getDiaSemana());
	            }
	            System.out.println("Día del turno que quieres cambiar:");
	            String diaOfrece = br.readLine().trim();
	            Turno turnoOfrece = buscarTurnoPorDia(diaOfrece);
	            if (turnoOfrece == null) {
	                System.out.println("Turno no encontrado.");
	                return;
	            }
	            if (!empleado.getTurnos().contains(turnoOfrece)) {
	                System.out.println("No tienes ese turno.");
	                return;
	            }
	 
	            System.out.println("Turnos disponibles:");
	            for (Turno t : cafe.getTurnos()) {
	                System.out.println("- " + t.getDiaSemana());
	            }
	            System.out.println("Día del turno que deseas:");
	            String diaDesea = br.readLine().trim();
	            Turno turnoDesea = buscarTurnoPorDia(diaDesea);
	            if (turnoDesea == null) {
	                System.out.println("Turno no encontrado.");
	                return;
	            }
	 
	            System.out.println("Motivo:");
	            String motivo = br.readLine();
	 
	            if (tipo == 1) {
	                cafe.solicitarCambioGeneral(empleado, turnoOfrece, turnoDesea, motivo);
	                System.out.println("Solicitud enviada. Queda pendiente de aprobación.");
	            } else {
	                System.out.println("Login del empleado con quien intercambiar:");
	                String loginOtro = br.readLine().trim();
	                Empleado otro = cafe.buscarEmpleado(loginOtro);
	                if (otro == null) {
	                    System.out.println("Empleado no encontrado.");
	                    return;
	                }
	                cafe.solicitarIntercambioTurno(empleado, otro, turnoOfrece, turnoDesea, motivo);
	                System.out.println("Solicitud enviada. Queda pendiente de aprobación.");
	            }
	 
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (IllegalStateException e) {
	            System.out.println("Error: " + e.getMessage());
	        }
	    }
	 

	   
	    // DESCUENTO
	  
	 
	    private void aplicarDescuento(Mesero mesero) {
	        System.out.println("\n--- APLICAR DESCUENTO A CLIENTE ---");
	        try {
	            System.out.println("Login del cliente:");
	            String login = br.readLine().trim();
	            Cliente cliente = cafe.buscarCliente(login);
	 
	            if (cliente == null) {
	                System.out.println("Cliente no encontrado.");
	                return;
	            }
	            if (cliente.tieneDescuento()) {
	                System.out.println("El cliente ya tiene un descuento activo.");
	                return;
	            }
	 
	            System.out.println("Tu código de descuento es: " + mesero.getCodigoDescuento());
	            System.out.println("¿Confirmas aplicar tu descuento a " + cliente.getNombre() + "? (1=Sí / 0=No):");
	            int confirma = leerEntero();
	            if (confirma != 1) {
	                System.out.println("Operación cancelada.");
	                return;
	            }
	 
	            cafe.aplicarCodigoDescuento(cliente, mesero.getCodigoDescuento());
	            System.out.println("Descuento aplicado a " + cliente.getNombre() + " correctamente.");
	 
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (IllegalStateException e) {
	            System.out.println("Error: " + e.getMessage());
	        }
	    }
	    
	    // SUGERENCIA DE PLATILLO
	    
	 
	    private void sugerirPlatillo(Empleado empleado) {
	        System.out.println("\n--- SUGERIR PLATILLO ---");
	        try {
	            System.out.println("Nombre del platillo:");
	            String nombre = br.readLine();
	            System.out.println("Descripción:");
	            String descripcion = br.readLine();
	 
	            SugerenciaPlatillo s = cafe.crearSugerencia(empleado, nombre, descripcion);
	            System.out.println("Sugerencia enviada con código: " + s.getCodigo());
	 
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	 
	    
	    // TORNEOS (solo ver)
	 
	 
	    private void verTorneos() {
	        System.out.println("\n--- TORNEOS DISPONIBLES ---");
	        if (cafe.getTorneos().isEmpty()) {
	            System.out.println("No hay torneos disponibles.");
	            return;
	        }
	        for (Torneo t : cafe.getTorneos()) {
	            System.out.println("\nCódigo: " + t.getCodigo());
	            System.out.println("  Juego: " + t.getJuego().getNombre());
	            System.out.println("  Día: " + t.getDiaSemana());
	            System.out.println("  Tipo: " + t.getTipo());
	            System.out.println("  Cupos: " + t.getCuposOcupados() + "/" + t.getMaxParticipantes()+ " (disponibles: " + t.getCuposDisponibles() + ")");
	            if (t.getTipo() == TipoTorneo.COMPETITIVO) {
	                System.out.println("  Tarifa: $" + t.getTarifaEntrada() + " (gratis para empleados)");
	            } else {
	                System.out.println("  Bono al ganar: $" + t.getBonoDescuento());
	            }
	        }
	    }
	 
	    
	    // COMPRA DE JUEGO
	   
	 
	    private void comprarJuego(Empleado empleado) {
	        System.out.println("\n--- COMPRAR JUEGO ---");
	        if (cafe.getJuegosDisponiblesVenta().isEmpty()) {
	            System.out.println("No hay juegos disponibles para venta.");
	            return;
	        }
	        for (JuegoDeMesa j : cafe.getJuegosDisponiblesVenta()) {
	            System.out.println(j.getNombre()+ " - Precio: $" + j.getPrecioUnitario()+ " - Copias: " + j.getCopiasVenta());
	        }
	 
	        try {
	            System.out.println("Nombre del juego:");
	            String nombre = br.readLine().trim();
	            JuegoDeMesa juego = cafe.buscarJuego(nombre);
	 
	            if (juego == null) {
	                System.out.println("Juego no encontrado.");
	                return;
	            }
	            if (!juego.estaDisponibleVenta()) {
	                System.out.println("No hay copias disponibles.");
	                return;
	            }
	 
	            System.out.println("Cantidad:");
	            int cantidad = leerEntero();
	            if (cantidad <= 0) {
	                System.out.println("La cantidad debe ser mayor a 0.");
	                return;
	            }
	 
	            VentaJuego venta = cafe.registrarVentaJuego(empleado, juego, cantidad);
	            System.out.println("¡Compra exitosa! Descuento de empleado aplicado (20%).");
	            System.out.println(venta);
	 
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (IllegalStateException e) {
	            System.out.println("Error: " + e.getMessage());
	        }
	    }
	 
	  
	    // PRESTAMOS
	   
	 
	    private void prestamoParaMesa(Mesero mesero) {
	        System.out.println("\n--- PRÉSTAMO PARA UNA MESA ---");
	        try {
	            System.out.println("Número de mesa:");
	            int numMesa = leerEntero();
	            Cliente cliente = cafe.buscarClientePorMesa(numMesa);
	 
	            if (cliente == null) {
	                System.out.println("No hay cliente en esa mesa.");
	                return;
	            }
	            System.out.println("Cliente encontrado: " + cliente.getNombre());
	 
	            mostrarJuegosPrestamo();
	            System.out.println("Nombre del juego:");
	            String nombre = br.readLine().trim();
	            JuegoDeMesa juego = cafe.buscarJuego(nombre);
	 
	            if (juego == null) {
	                System.out.println("Juego no encontrado.");
	                return;
	            }
	 
	            Prestamo p = cafe.crearPrestamo(cliente, juego);
	            System.out.println("¡Préstamo registrado!");
	            System.out.println(p);
	 
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (IllegalStateException e) {
	            System.out.println("Error: " + e.getMessage());
	        }
	    }
	 
	    private void prestamoParaSiMismo(Mesero mesero) {
	        System.out.println("\n--- PRÉSTAMO PARA MÍ MISMO ---");
	        if (mesero.isEnTurno()) {
	            System.out.println("No puedes pedir préstamo mientras estás en turno.");
	            return;
	        }
	 
	        try {
	            mostrarJuegosPrestamo();
	            System.out.println("Nombre del juego:");
	            String nombre = br.readLine().trim();
	            JuegoDeMesa juego = cafe.buscarJuego(nombre);
	 
	            if (juego == null) {
	                System.out.println("Juego no encontrado.");
	                return;
	            }
	 
	            Prestamo p = cafe.crearPrestamo(mesero, juego);
	            System.out.println("¡Préstamo registrado!");
	            System.out.println(p);
	 
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (IllegalStateException e) {
	            System.out.println("Error: " + e.getMessage());
	        }
	    }
	 
	    private void mostrarJuegosPrestamo() {
	        System.out.println("Juegos disponibles para préstamo:");
	        if (cafe.getJuegosDisponiblesPrestamo().isEmpty()) {
	            System.out.println("No hay juegos disponibles.");
	            return;
	        }
	        for (JuegoDeMesa j : cafe.getJuegosDisponiblesPrestamo()) {
	            System.out.println("- " + j.getNombre()+ " | Categoría: " + j.getCategoria()+ " | Jugadores: " + j.getMinJugadores() + "-" + j.getMaxJugadores()+ " | Copias disponibles: " + j.getCopiasDisponiblesPrestamo());
	        }
	    }
	 
	  
	    // RECIBIR Y RETIRAR CLIENTE
	 
	 
	    private void recibirCliente() {
	        System.out.println("\n--- RECIBIR CLIENTE ---");
	        try {
	            System.out.println("Login del cliente:");
	            String login = br.readLine().trim();
	            Cliente cliente = cafe.buscarCliente(login);
	 
	            if (cliente == null) {
	                System.out.println("Cliente no encontrado.");
	                return;
	            }
	            if (cliente.estaEnCafe()) {
	                System.out.println("Ese cliente ya está en el café.");
	                return;
	            }
	 
	            System.out.println("Número de personas en el grupo:");
	            int numPersonas = leerEntero();
	            if (numPersonas <= 0) {
	                System.out.println("Debe ser mayor a 0.");
	                return;
	            }
	 
	            System.out.println("¿Hay niños menores de 5 años? (1=Sí / 0=No):");
	            boolean ninos = leerEntero() == 1;
	 
	            System.out.println("¿Hay menores de 18 años? (1=Sí / 0=No):");
	            boolean menores = leerEntero() == 1;
	 
	            cafe.recibirCliente(cliente, numPersonas, ninos, menores);
	            System.out.println("Cliente " + cliente.getNombre() + " asignado a mesa " + cliente.getMesaActual().getNumero());
	 
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (IllegalStateException e) {
	            System.out.println("Error: " + e.getMessage());
	        }
	    }
	 
	    private void retirarCliente() {
	        System.out.println("\n--- RETIRAR CLIENTE ---");
	        try {
	            System.out.println("Login del cliente:");
	            String login = br.readLine().trim();
	            Cliente cliente = cafe.buscarCliente(login);
	 
	            if (cliente == null) {
	                System.out.println("No hay cliente con ese login.");
	                return;
	            }
	            if (!cliente.estaEnCafe()) {
	                System.out.println("Ese cliente no está en el café.");
	                return;
	            }
	 
	            cafe.retirarCliente(cliente);
	            System.out.println("Cliente " + cliente.getNombre() + " retirado correctamente.");
	 
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (IllegalStateException e) {
	            System.out.println("Error: " + e.getMessage());
	        }
	    }
	 
	
	    // METODOS AUXILIARES
	
	 
	    private Turno buscarTurnoPorDia(String dia) {
	        for (Turno t : cafe.getTurnos()) {
	            if (t.getDiaSemana().equalsIgnoreCase(dia)) {
	                return t;
	            }
	        }
	        return null;
	    }
	 
	    private int leerEntero() {
	        int dato = 0;
	        try {
	            dato = Integer.parseInt(br.readLine());
	        } catch (NumberFormatException e) {
	            System.out.println("Debe ingresar un número entero.");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return dato;
	    }
	 
	    
	    // MAIN
	
	 
	    public static void main(String[] args) {
	        new ConsolaEmpleado();
	    }
	    
	    
	    
	    
}
