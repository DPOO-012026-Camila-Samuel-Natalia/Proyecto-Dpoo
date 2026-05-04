package presentacion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
 
import modelo.*;
import persistencia.CentralPersistencia;
import persistencia.PersistenciaBoardGameCafeJSON;
 

public class ConsolaCliente {

	private BoardgameCafe cafe;
    private BufferedReader br;
    private CentralPersistencia central;
 
    public ConsolaCliente() {
        central = new CentralPersistencia(
                new PersistenciaBoardGameCafeJSON("datos/boardgamecafe.txt"));
        this.cafe = central.cargar();
        if (this.cafe == null) {
            System.out.println("No se encontró archivo. Iniciando sistema vacío.");
            this.cafe = new BoardgameCafe(20);
        }
 
        this.br = new BufferedReader(new InputStreamReader(System.in));
  // el cliente se autentica o registra y se guarda en la variable
        Cliente cliente = menuInicio();
 
        if (cliente != null) {
            mostrarMenu(cliente);
        }
 
        central.guardar(cafe);
        System.out.println("Datos guardados. ¡Hasta luego!");
 
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
 
    // INICIO
 
    private Cliente menuInicio() {
        System.out.println("=== BIENVENIDO AL BOARDGAME CAFÉ ===");
        int op = 0;
        do {
            System.out.println("\nDigite:");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Registrarse");
            System.out.println("0. Salir");
            op = leerEntero();
 
            if (op == 1) {
                return autenticar();
            } else if (op == 2) {
                return registrarse();
            } else if (op != 0) {
                System.out.println("Opción inválida.");
            }
        } while (op != 0);
        return null;
    }
    
    
 // AUTENTICACION Y REGISTRO
    
 
    private Cliente autenticar() {
        System.out.println("\n--- INICIAR SESIÓN ---");
        try {
            System.out.println("Login:");
            String login = br.readLine();
            System.out.println("Contraseña:");
            String password = br.readLine();
 
            Cliente cliente = cafe.buscarCliente(login);
            if (cliente == null || !cliente.getPassword().equals(password)) {
                System.out.println("Login o contraseña incorrectos.");
                return null;
            }
 
            System.out.println("¡Bienvenido, " + cliente.getNombre());
            return cliente;
 
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
 
    private Cliente registrarse() {
        System.out.println("\n--- REGISTRO DE NUEVO CLIENTE ---");
        try {
            System.out.println("Nombre completo:");
            String nombre = br.readLine();
 
            String login = "";
            boolean loginValido = false;
            while (!loginValido) {
                System.out.println("Login (usuario):");
                login = br.readLine();
                if (login == null || login.trim().isEmpty()) {
                    System.out.println("El login no puede estar vacío.");
                } else if (cafe.buscarCliente(login) != null) {
                    System.out.println("Ese login ya está en uso, elige otro.");
                } else {
                    loginValido = true;
                }
            }
 
            System.out.println("Contraseña:");
            String password = br.readLine();
 
            String id = "C" + (cafe.getClientes().size() + 1);
            Cliente nuevo = new Cliente(id, nombre, login, password);
            cafe.agregarCliente(nuevo);
            System.out.println("¡Registro exitoso! Bienvenido, " + nombre + ".");
            return nuevo;
 
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
 // MENÚ PRINCIPAL
   
 
    private void mostrarMenu(Cliente cliente) {
        int op = 0;
        do {
            System.out.println("\n=== MENÚ CLIENTE - " + cliente.getNombre() + " ===");
            System.out.println("Digite:");
            System.out.println("1. Ver mis puntos de fidelidad");
            System.out.println("2. Ver mi bono de torneo");
            System.out.println("3. Ver menú del café");
            System.out.println("4. Ver juegos disponibles para comprar");
            System.out.println("5. Comprar un juego");
            System.out.println("6. Ver mis juegos favoritos");
            System.out.println("7. Agregar juego favorito");
            System.out.println("8. Ver torneos disponibles");
            System.out.println("9. Inscribirse a un torneo");
            System.out.println("10. Desinscribirse de un torneo");
            System.out.println("0. Cerrar sesión");
 
            op = leerEntero();
 
            if (op == 1)       verPuntos(cliente);
            else if (op == 2)  verBono(cliente);
            else if (op == 3)  verMenu();
            else if (op == 4)  verJuegosVenta();
            else if (op == 5)  comprarJuego(cliente);
            else if (op == 6)  verFavoritos(cliente);
            else if (op == 7)  agregarFavorito(cliente);
            else if (op == 8)  verTorneos();
            else if (op == 9)  inscribirseATorneo(cliente);
            else if (op == 10) desinscribirseATorneo(cliente);
            else if (op != 0)  System.out.println("Opción inválida.");
 
        } while (op != 0);
        System.out.println("Sesión cerrada.");
    }
 
    
    // OPCIONES DEL MENÚ
    
 
    private void verPuntos(Cliente cliente) {
        System.out.println("\n--- MIS PUNTOS ---");
        System.out.println("Puntos de fidelidad: " + cliente.getPuntosFidelidad());
    }
 
    private void verBono(Cliente cliente) {
        System.out.println("\n--- BONO DE TORNEO ---");
        if (cliente.tieneBonoTorneo()) {
            System.out.println("Tienes un bono de: $" + cliente.getBonoTorneo());
            System.out.println("Se aplica automáticamente en tu próxima compra de juego.");
        } else {
            System.out.println("No tienes bono activo.");
        }
    }
 
    private void verMenu() {
        System.out.println("\n--- MENÚ DEL CAFÉ ---");
        if (cafe.getProductosMenu().isEmpty()) {
            System.out.println("No hay productos en el menú.");
            return;
        }
        for (ProductoMenu p : cafe.getProductosMenu()) {
            System.out.println(p);
        }
    }
 
    private void verJuegosVenta() {
        System.out.println("\n--- JUEGOS DISPONIBLES PARA COMPRAR ---");
        if (cafe.getJuegosDisponiblesVenta().isEmpty()) {
            System.out.println("No hay juegos disponibles para venta.");
            return;
        }
        for (JuegoDeMesa j : cafe.getJuegosDisponiblesVenta()) {
            System.out.println(j.getNombre() + " - Precio: $" + j.getPrecioUnitario() + " - Copias: " + j.getCopiasVenta());
        }
    }
 
    private void comprarJuego(Cliente cliente) {
        System.out.println("\n--- COMPRAR JUEGO ---");
        verJuegosVenta();
        if (cafe.getJuegosDisponiblesVenta().isEmpty()) return;
 
        try {
            System.out.println("Nombre del juego:");
            String nombre = br.readLine().trim().toLowerCase();
 
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
            if (cantidad > juego.getCopiasVenta()) {
                System.out.println("No hay suficientes copias. Disponibles: " + juego.getCopiasVenta());
                return;
            }
 
            VentaJuego venta = cafe.registrarVentaJuego(cliente, juego, cantidad);
            System.out.println("¡Compra exitosa!");
            System.out.println(venta);
            if (venta.getDescuento() > 0) {
                System.out.println("Descuento aplicado: $" + venta.getDescuento());
            }
            System.out.println("Puntos acumulados: " + cliente.getPuntosFidelidad());
 
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
 
    private void verFavoritos(Cliente cliente) {
        System.out.println("\n--- MIS JUEGOS FAVORITOS ---");
        if (cliente.getJuegosFavoritos().isEmpty()) {
            System.out.println("No tienes juegos favoritos aún.");
            return;
        }
        for (JuegoDeMesa j : cliente.getJuegosFavoritos()) {
            System.out.println("- " + j.getNombre());
        }
    }
 
    private void agregarFavorito(Cliente cliente) {
        System.out.println("\n--- AGREGAR JUEGO FAVORITO ---");
        // mostrar catalogo completo
        if (cafe.getCatalogoJuegos().isEmpty()) {
            System.out.println("No hay juegos en el catálogo.");
            return;
        }
        System.out.println("Juegos disponibles:");
        for (JuegoDeMesa j : cafe.getCatalogoJuegos()) {
            System.out.println("- " + j.getNombre());
        }
 
        try {
            System.out.println("Nombre del juego a agregar como favorito:");
            String nombre = br.readLine().trim().toLowerCase();
 
            JuegoDeMesa juego = cafe.buscarJuego(nombre);
            if (juego == null) {
                System.out.println("Juego no encontrado.");
                return;
            }
            if (cliente.esFanaticoDe(juego)) {
                System.out.println("Ese juego ya está en tus favoritos.");
                return;
            }
            cliente.agregarJuegoFavorito(juego);
            System.out.println("¡" + juego.getNombre() + " agregado a tus favoritos!");
 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
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
            System.out.println("  Cupos: " + t.getCuposOcupados() + "/" + t.getMaxParticipantes() + " (disponibles: " + t.getCuposDisponibles() + ")");
            if (t.getTipo() == TipoTorneo.COMPETITIVO) {
                System.out.println("  Tarifa de entrada: $" + t.getTarifaEntrada());
                System.out.println("  Premio actual: $" + t.getPremioMetalico());
            } else {
                System.out.println("  Bono al ganar: $" + t.getBonoDescuento());
            }
        }
    }
 
    private void inscribirseATorneo(Cliente cliente) {
        System.out.println("\n--- INSCRIBIRSE A TORNEO ---");
        verTorneos();
        if (cafe.getTorneos().isEmpty()) return;
 
        try {
            System.out.println("Código del torneo:");
            String codigo = br.readLine();
            Torneo torneo = cafe.buscarTorneo(codigo);
 
            if (torneo == null) {
                System.out.println("Torneo no encontrado.");
                return;
            }
            if (torneo.estaInscrito(cliente)) {
                System.out.println("Ya estás inscrito en ese torneo.");
                return;
            }
            if (torneo.getCuposDisponibles() == 0) {
                System.out.println("No hay cupos disponibles.");
                return;
            }
 
            System.out.println("Número de cupos a tomar (máximo 3):");
            int cupos = leerEntero();
            if (cupos < 1 || cupos > 3) {
                System.out.println("Debe ser entre 1 y 3.");
                return;
            }
 
            cafe.inscribirClienteTorneo(cliente, torneo, cupos);
            System.out.println("¡Inscripción exitosa en " + torneo.getCodigo() + "!");
 
            if (cliente.esFanaticoDe(torneo.getJuego())) {
                System.out.println("Eres fanático de " + torneo.getJuego().getNombre() + " — se usaron cupos de fanático si estaban disponibles.");
            }
 
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
 
    private void desinscribirseATorneo(Cliente cliente) {
        System.out.println("\n--- DESINSCRIBIRSE DE TORNEO ---");
 
        // mostrar solo los torneos en que esta inscrito
        boolean tieneAlguno = false;
        for (Torneo t : cafe.getTorneos()) {
            if (t.estaInscrito(cliente)) {
                System.out.println("Código: " + t.getCodigo() + " - " + t.getJuego().getNombre());
                tieneAlguno = true;
            }
        }
        if (!tieneAlguno) {
            System.out.println("No estás inscrito en ningún torneo.");
            return;
        }
 
        try {
            System.out.println("Código del torneo del que deseas salir:");
            String codigo = br.readLine();
            Torneo torneo = cafe.buscarTorneo(codigo);
 
            if (torneo == null) {
                System.out.println("Torneo no encontrado.");
                return;
            }
            if (!torneo.estaInscrito(cliente)) {
                System.out.println("No estás inscrito en ese torneo.");
                return;
            }
 
            cafe.desinscribirUsuarioTorneo(cliente, torneo);
            System.out.println("Desinscripción exitosa. Se eliminaron todos tus cupos.");
 
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    
    //METODOS AUXILIARES
    
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
        new ConsolaCliente();
    }
    
    
    
    
}
