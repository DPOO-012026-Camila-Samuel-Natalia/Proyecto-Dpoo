package presentacion;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
 
import modelo.*;
import persistencia.CentralPersistencia;
import persistencia.PersistenciaBoardGameCafeJSON;
 

public class ConsolaAdministrador {
	private BoardgameCafe cafe;
    private BufferedReader br;
    private CentralPersistencia central;
 
    public ConsolaAdministrador() {
        central = new CentralPersistencia(new PersistenciaBoardGameCafeJSON("datos/boardgamecafe.txt"));
        this.cafe = central.cargar();
        if (this.cafe == null) {
            System.out.println("No se encontró archivo. Iniciando sistema vacío.");
            this.cafe = new BoardgameCafe(20);
        }
 
        this.br = new BufferedReader(new InputStreamReader(System.in));
 
        Administrador admin = autenticar();
 
        if (admin != null) {
            mostrarMenu(admin);
        }
 
        central.guardar(cafe);
        System.out.println("Datos guardados. ¡Hasta luego!");
 
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } }
        
     
        // AUTENTICACIÓN
       
     
        private Administrador autenticar() {
            System.out.println("=== ACCESO ADMINISTRADOR - BOARDGAME CAFÉ ===");
            try {
                System.out.println("Login:");
                String login = br.readLine();
                System.out.println("Contraseña:");
                String password = br.readLine();
     
                Administrador admin = cafe.getAdministrador();
                if (admin == null || !admin.getLogin().equals(login) || !admin.getPassword().equals(password)) {
                    System.out.println("Login o contraseña incorrectos.");
                    return null;
                }
     
                System.out.println("¡Bienvenido, " + admin.getNombre() + "!");
                return admin;
     
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }   
    }
	
     // MENÚ PRINCIPAL
       
        private void mostrarMenu(Administrador admin) {
            int op = 0;
            do {
                System.out.println("\n=== MENÚ ADMINISTRADOR ===");
                System.out.println("Digite:");
                System.out.println("1. Registrar empleado");
                System.out.println("2. Ver empleados");
                System.out.println("3. Gestionar turnos");
                System.out.println("4. Ver solicitudes de cambio de turno pendientes");
                System.out.println("5. Aprobar o rechazar solicitud de turno");
                System.out.println("6. Ver sugerencias de platillos pendientes");
                System.out.println("7. Aprobar o rechazar sugerencia de platillo");
                System.out.println("8. Gestionar inventario de juegos");
                System.out.println("9. Crear torneo");
                System.out.println("10. Inscribir empleado a torneo");
                System.out.println("11. Entregar premio torneo amistoso");
                System.out.println("12. Ver informe de ventas");
                System.out.println("0. Cerrar sesión");
     
                op = leerEntero();
     
                if (op == 1)       registrarEmpleado(admin);
                else if (op == 2)  verEmpleados();
                else if (op == 3)  gestionarTurnos();
                else if (op == 4)  verSolicitudesPendientes();
                else if (op == 5)  procesarSolicitudTurno();
                else if (op == 6)  verSugerenciasPendientes();
                else if (op == 7)  procesarSugerencia();
                else if (op == 8)  gestionarInventario();
                else if (op == 9)  crearTorneo(admin);
                else if (op == 10) inscribirEmpleadoTorneo(admin);
                else if (op == 11) entregarPremioAmistoso();
                else if (op == 12) verInformeVentas();
                else if (op != 0)  System.out.println("Opción inválida.");
     
            } while (op != 0);
            System.out.println("Sesión cerrada.");
        }
        
        
     // EMPLEADOS
        // =========================
     
        private void registrarEmpleado(Administrador admin) {
            System.out.println("\n--- REGISTRAR EMPLEADO ---");
            try {
                System.out.println("Tipo (1=Mesero / 2=Cocinero):");
                int tipo = leerEntero();
                if (tipo != 1 && tipo != 2) {
                    System.out.println("Tipo inválido.");
                    return;
                }
     
                System.out.println("Nombre:");
                String nombre = br.readLine();
     
                String login = "";
                boolean loginValido = false;
                while (!loginValido) {
                    System.out.println("Login:");
                    login = br.readLine().trim();
                    if (login.isEmpty()) {
                        System.out.println("El login no puede estar vacío.");
                    } else if (cafe.buscarEmpleado(login) != null) {
                        System.out.println("Ese login ya está en uso.");
                    } else {
                        loginValido = true;
                    }
                }
     
                System.out.println("Contraseña:");
                String password = br.readLine();
     
                String id = "E" + (cafe.getEmpleados().size() + 1);
                Empleado empleado;
                if (tipo == 1) {
                    empleado = new Mesero(id, nombre, login, password);
                } else {
                    empleado = new Cocinero(id, nombre, login, password);
                }
     
                admin.registrarEmpleado(cafe, empleado);
                System.out.println("Empleado registrado con código de descuento: " + empleado.getCodigoDescuento());
     
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
     
        private void verEmpleados() {
            System.out.println("\n--- EMPLEADOS ---");
            if (cafe.getEmpleados().isEmpty()) {
                System.out.println("No hay empleados registrados.");
                return;
            }
            for (Empleado e : cafe.getEmpleados()) {
                System.out.println(e);
            }
        }
     
       
        // TURNOS
     
     
        private void gestionarTurnos() {
            System.out.println("\n--- GESTIONAR TURNOS ---");
            System.out.println("1. Ver turnos");
            System.out.println("2. Crear turno");
            System.out.println("3. Asignar empleado a turno");
            int op = leerEntero();
     
            if (op == 1) {
                verTurnos();
            } else if (op == 2) {
                crearTurno();
            } else if (op == 3) {
                asignarEmpleadoTurno();
            } else {
                System.out.println("Opción inválida.");
            }
        }
     
        private void verTurnos() {
            System.out.println("\n--- TURNOS ---");
            if (cafe.getTurnos().isEmpty()) {
                System.out.println("No hay turnos registrados.");
                return;
            }
            for (Turno t : cafe.getTurnos()) {
                System.out.println("Día: " + t.getDiaSemana());
                for (Empleado e : t.getEmpleados()) {
                    System.out.println("  - " + e.getNombre()+ " (" + (e instanceof Mesero ? "Mesero" : "Cocinero") + ")");
                }
            }
        }
     
        private void crearTurno() {
            System.out.println("\n--- CREAR TURNO ---");
            try {
                System.out.println("Día de la semana:");
                String dia = br.readLine().trim();
                if (dia.isEmpty()) {
                    System.out.println("El día no puede estar vacío.");
                    return;
                }
                cafe.agregarTurno(new Turno(dia));
                System.out.println("Turno del " + dia + " creado.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
     
        private void asignarEmpleadoTurno() {
            System.out.println("\n--- ASIGNAR EMPLEADO A TURNO ---");
            try {
                verEmpleados();
                System.out.println("Login del empleado:");
                String login = br.readLine().trim();
                Empleado empleado = cafe.buscarEmpleado(login);
                if (empleado == null) {
                    System.out.println("Empleado no encontrado.");
                    return;
                }
     
                verTurnos();
                System.out.println("Día del turno:");
                String dia = br.readLine().trim();
                Turno turno = buscarTurnoPorDia(dia);
                if (turno == null) {
                    System.out.println("Turno no encontrado.");
                    return;
                }
     
                cafe.asignarTurno(empleado, turno);
                System.out.println("Empleado " + empleado.getNombre() + " asignado al turno del " + dia);
     
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        
        // SOLICITUDES DE CAMBIO DE TURNO
      
     
        private void verSolicitudesPendientes() {
            System.out.println("\n--- SOLICITUDES DE CAMBIO DE TURNO PENDIENTES ---");
            if (cafe.getSolicitudesPendientes().isEmpty()) {
                System.out.println("No hay solicitudes pendientes.");
                return;
            }
            for (SolicitudCambioTurno s : cafe.getSolicitudesPendientes()) {
                System.out.println(s);
            }
        }
     
        private void procesarSolicitudTurno() {
            System.out.println("\n--- APROBAR O RECHAZAR SOLICITUD ---");
            verSolicitudesPendientes();
            if (cafe.getSolicitudesPendientes().isEmpty()) return;
     
            try {
                System.out.println("Código de la solicitud:");
                String codigo = br.readLine().trim();
                SolicitudCambioTurno solicitud = buscarSolicitud(codigo);
     
                if (solicitud == null) {
                    System.out.println("Solicitud no encontrada.");
                    return;
                }
     
                System.out.println("1. Aprobar / 2. Rechazar:");
                int op = leerEntero();
     
                if (op == 1) {
                    if (solicitud.getTipoCambio().equals("INTERCAMBIO")) {
                        cafe.aprobarIntercambioTurno(solicitud);
                    } else {
                        cafe.aprobarCambioGeneral(solicitud);
                    }
                    System.out.println("Solicitud aprobada.");
                } else if (op == 2) {
                    cafe.rechazarCambioTurno(solicitud);
                    System.out.println("Solicitud rechazada.");
                } else {
                    System.out.println("Opción inválida.");
                }
     
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
     
       
        // SUGERENCIAS DE PLATILLO
        
        
        private void verSugerenciasPendientes() {
            System.out.println("\n--- SUGERENCIAS DE PLATILLO PENDIENTES ---");
            boolean hayPendientes = false;
            for (SugerenciaPlatillo s : cafe.getSugerenciasPlatillos()) {
                if (s.getEstado().equals("Pendiente")) {
                    System.out.println(s);
                    hayPendientes = true;
                }
            }
            if (!hayPendientes) {
                System.out.println("No hay sugerencias pendientes.");
            }
        }
     
        private void procesarSugerencia() {
            System.out.println("\n--- APROBAR O RECHAZAR SUGERENCIA ---");
            verSugerenciasPendientes();
     
            try {
                System.out.println("Código de la sugerencia:");
                String codigo = br.readLine().trim();
                SugerenciaPlatillo sugerencia = buscarSugerencia(codigo);
     
                if (sugerencia == null) {
                    System.out.println("Sugerencia no encontrada.");
                    return;
                }
     
                System.out.println("1. Aprobar / 2. Rechazar:");
                int op = leerEntero();
     
                if (op == 1) {
                    System.out.println("Tipo (BEBIDA / PASTELERIA):");
                    String tipo = br.readLine().trim().toUpperCase();
                    if (!tipo.equals("BEBIDA") && !tipo.equals("PASTELERIA")) {
                        System.out.println("Tipo inválido.");
                        return;
                    }
                    System.out.println("Precio:");
                    double precio = leerDouble();
                    if (precio <= 0) {
                        System.out.println("El precio debe ser mayor a 0.");
                        return;
                    }
                    cafe.aprobarSugerencia(sugerencia, tipo, precio);
                    System.out.println("Sugerencia aprobada y producto agregado al menú.");
                } else if (op == 2) {
                    cafe.rechazarSugerencia(sugerencia);
                    System.out.println("Sugerencia rechazada.");
                } else {
                    System.out.println("Opción inválida.");
                }
     
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
     
        
        // INVENTARIO
        
     
        private void gestionarInventario() {
            System.out.println("\n--- GESTIONAR INVENTARIO ---");
            System.out.println("1. Ver catálogo de juegos");
            System.out.println("2. Agregar juego nuevo");
            System.out.println("3. Agregar copias de préstamo");
            System.out.println("4. Agregar copias de venta");
            System.out.println("5. Mover copia de venta a préstamo");
            int op = leerEntero();
     
            if (op == 1)      verCatalogoJuegos();
            else if (op == 2) agregarJuego();
            else if (op == 3) agregarCopiasPrestamo();
            else if (op == 4) agregarCopiasVenta();
            else if (op == 5) moverVentaAPrestamo();
            else              System.out.println("Opción inválida.");
        }
     
        private void verCatalogoJuegos() {
            System.out.println("\n--- CATÁLOGO DE JUEGOS ---");
            if (cafe.getCatalogoJuegos().isEmpty()) {
                System.out.println("No hay juegos en el catálogo.");
                return;
            }
            for (JuegoDeMesa j : cafe.getCatalogoJuegos()) {
                System.out.println(j);
            }
        }
     
        private void agregarJuego() {
            System.out.println("\n--- AGREGAR JUEGO NUEVO ---");
            try {
                System.out.println("Nombre:");
                String nombre = br.readLine().trim();
                System.out.println("Categoría (ACCION / CARTAS / TABLERO):");
                String categoria = br.readLine().trim().toUpperCase();
                System.out.println("Mínimo de jugadores:");
                int minJ = leerEntero();
                System.out.println("Máximo de jugadores:");
                int maxJ = leerEntero();
                System.out.println("Edad mínima (0 si no tiene):");
                int edad = leerEntero();
                System.out.println("¿Es difícil? (1=Sí / 0=No):");
                boolean dificil = leerEntero() == 1;
                System.out.println("Copias para préstamo:");
                int copiasPrestamo = leerEntero();
                System.out.println("Copias para venta:");
                int copiasVenta = leerEntero();
                System.out.println("Precio unitario:");
                double precio = leerDouble();
     
                JuegoDeMesa juego = new JuegoDeMesa(nombre, 0, "NA", minJ, maxJ, edad, categoria, dificil, copiasPrestamo, copiasVenta, precio);
                cafe.agregarJuego(juego);
                System.out.println("Juego agregado correctamente.");
     
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
     
        private void agregarCopiasPrestamo() {
            System.out.println("\n--- AGREGAR COPIAS DE PRÉSTAMO ---");
            try {
                verCatalogoJuegos();
                System.out.println("Nombre del juego:");
                String nombre = br.readLine().trim();
                JuegoDeMesa juego = cafe.buscarJuego(nombre);
                if (juego == null) {
                    System.out.println("Juego no encontrado.");
                    return;
                }
                System.out.println("Cantidad a agregar:");
                int cantidad = leerEntero();
                if (cantidad <= 0) {
                    System.out.println("Debe ser mayor a 0.");
                    return;
                }
                juego.agregarCopiasPrestamo(cantidad);
                System.out.println("Copias de préstamo actualizadas: " + juego.getTotalCopiasPrestamo());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
     
        private void agregarCopiasVenta() {
            System.out.println("\n--- AGREGAR COPIAS DE VENTA ---");
            try {
                verCatalogoJuegos();
                System.out.println("Nombre del juego:");
                String nombre = br.readLine().trim();
                JuegoDeMesa juego = cafe.buscarJuego(nombre);
                if (juego == null) {
                    System.out.println("Juego no encontrado.");
                    return;
                }
                System.out.println("Cantidad a agregar:");
                int cantidad = leerEntero();
                if (cantidad <= 0) {
                    System.out.println("Debe ser mayor a 0.");
                    return;
                }
                juego.agregarCopiasVenta(cantidad);
                System.out.println("Copias de venta actualizadas: " + juego.getCopiasVenta());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
     
        private void moverVentaAPrestamo() {
            System.out.println("\n--- MOVER COPIA DE VENTA A PRÉSTAMO ---");
            try {
                verCatalogoJuegos();
                System.out.println("Nombre del juego:");
                String nombre = br.readLine().trim();
                JuegoDeMesa juego = cafe.buscarJuego(nombre);
                if (juego == null) {
                    System.out.println("Juego no encontrado.");
                    return;
                }
                cafe.moverVentaAPrestamo(juego);
                System.out.println("Copia movida. Préstamo: " + juego.getTotalCopiasPrestamo() + " | Venta: " + juego.getCopiasVenta());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
     
    
        // TORNEOS
    
     
        private void crearTorneo(Administrador admin) {
            System.out.println("\n--- CREAR TORNEO ---");
            try {
                verCatalogoJuegos();
                System.out.println("Nombre del juego:");
                String nombreJuego = br.readLine().trim();
                JuegoDeMesa juego = cafe.buscarJuego(nombreJuego);
                if (juego == null) {
                    System.out.println("Juego no encontrado.");
                    return;
                }
     
                System.out.println("Día de la semana:");
                String dia = br.readLine().trim();
     
                System.out.println("Número máximo de participantes:");
                int maxP = leerEntero();
                if (maxP <= 0) {
                    System.out.println("Debe ser mayor a 0.");
                    return;
                }
     
                System.out.println("Tipo (1=Amistoso / 2=Competitivo):");
                int tipoOp = leerEntero();
                if (tipoOp != 1 && tipoOp != 2) {
                    System.out.println("Tipo inválido.");
                    return;
                }
                TipoTorneo tipo = tipoOp == 1 ? TipoTorneo.AMISTOSO : TipoTorneo.COMPETITIVO;
     
                double tarifa = 0;
                double bono = 0;
                if (tipo == TipoTorneo.AMISTOSO) {
                    System.out.println("Valor del bono de descuento al ganador:");
                    bono = leerDouble();
                } else {
                    System.out.println("Tarifa de entrada:");
                    tarifa = leerDouble();
                }
     
                Torneo torneo = admin.crearTorneo(cafe, dia, juego, maxP, tipo, tarifa, bono);
                System.out.println("Torneo creado con código: " + torneo.getCodigo());
     
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalStateException | IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
     
        private void inscribirEmpleadoTorneo(Administrador admin) {
            System.out.println("\n--- INSCRIBIR EMPLEADO A TORNEO ---");
            try {
                verEmpleados();
                System.out.println("Login del empleado:");
                String loginE = br.readLine().trim();
                Empleado empleado = cafe.buscarEmpleado(loginE);
                if (empleado == null) {
                    System.out.println("Empleado no encontrado.");
                    return;
                }
     
                verTorneos();
                System.out.println("Código del torneo:");
                String codigo = br.readLine().trim();
                Torneo torneo = cafe.buscarTorneo(codigo);
                if (torneo == null) {
                    System.out.println("Torneo no encontrado.");
                    return;
                }
                if (torneo.estaInscrito(empleado)) {
                    System.out.println("El empleado ya está inscrito en ese torneo.");
                    return;
                }
     
                System.out.println("Número de cupos (máximo 3):");
                int cupos = leerEntero();
                if (cupos < 1 || cupos > 3) {
                    System.out.println("Debe ser entre 1 y 3.");
                    return;
                }
     
                admin.inscribirEmpleadoATorneo(cafe, empleado, torneo, cupos);
                System.out.println("Empleado " + empleado.getNombre() + " inscrito correctamente.");
     
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalStateException | IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
     
        private void entregarPremioAmistoso() {
            System.out.println("\n--- ENTREGAR PREMIO TORNEO AMISTOSO ---");
            try {
                // mostrar solo torneos amistosos
                boolean hayAmistosos = false;
                for (Torneo t : cafe.getTorneos()) {
                    if (t.getTipo() == TipoTorneo.AMISTOSO) {
                        System.out.println(t);
                        hayAmistosos = true;
                    }
                }
                if (!hayAmistosos) {
                    System.out.println("No hay torneos amistosos.");
                    return;
                }
     
                System.out.println("Código del torneo:");
                String codigoT = br.readLine().trim();
                Torneo torneo = cafe.buscarTorneo(codigoT);
                if (torneo == null) {
                    System.out.println("Torneo no encontrado.");
                    return;
                }
     
                System.out.println("Login del cliente ganador:");
                String loginC = br.readLine().trim();
                Cliente ganador = cafe.buscarCliente(loginC);
                if (ganador == null) {
                    System.out.println("Cliente no encontrado.");
                    return;
                }
     
                cafe.entregarPremioAmistoso(torneo, ganador);
                System.out.println("Premio de $" + torneo.getBonoDescuento() + " entregado a " + ganador.getNombre());
     
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
     
        private void verTorneos() {
            System.out.println("\n--- TORNEOS ---");
            if (cafe.getTorneos().isEmpty()) {
                System.out.println("No hay torneos.");
                return;
            }
            for (Torneo t : cafe.getTorneos()) {
                System.out.println(t);
            }
        }
     
       
        // INFORME DE VENTAS
        
     
        private void verInformeVentas() {
            System.out.println("\n--- INFORME DE VENTAS ---");
            System.out.println(cafe.getInformeVentas());
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
     
        private SolicitudCambioTurno buscarSolicitud(String codigo) {
            for (SolicitudCambioTurno s : cafe.getSolicitudesCambioTurno()) {
                if (s.getCodigo().equals(codigo)) {
                    return s;
                }
            }
            return null;
        }
     
        private SugerenciaPlatillo buscarSugerencia(String codigo) {
            for (SugerenciaPlatillo s : cafe.getSugerenciasPlatillos()) {
                if (s.getCodigo().equals(codigo)) {
                    return s;
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
     
        private double leerDouble() {
            double dato = 0;
            try {
                dato = Double.parseDouble(br.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return dato;
        }
     
     
        // MAIN
  
     
        public static void main(String[] args) {
            new ConsolaAdministrador();
        }
	
}
