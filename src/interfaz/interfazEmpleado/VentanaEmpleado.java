package interfaz.interfazEmpleado;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import modelo.*;
import persistencia.CentralPersistencia;
import persistencia.PersistenciaBoardGameCafeJSON;

public class VentanaEmpleado extends JFrame {

    private static final long serialVersionUID = 1L;

    
    // ATRIBUTOS PRINCIPALES

    private BoardgameCafe cafe;
    private CentralPersistencia central;

    // empleado que inicio sesión
    private Empleado empleadoActual;

    // paneles de la interfaz
    private PanelLoginEmpleado panelLogin;
    private PanelPerfilEmpleado panelPerfil;
    private PanelTurnosEmpleado panelTurnos;
    private PanelJuegosEmpleado panelJuegos;
    private PanelTorneosEmpleado panelTorneos;
    private PanelMesero panelMesero;

    // pestañas principales
    private JTabbedPane tabbedPane;


    // CONSTRUCTOR


    public VentanaEmpleado() {

        // cargar persistencia
        central = new CentralPersistencia(
                new PersistenciaBoardGameCafeJSON("src/datos/boardgamecafe.txt"));

        cafe = central.cargar();

        // si no existe archivo, crear sistema vacío
        if (cafe == null) {
            cafe = new BoardgameCafe(20);
        }

        // configuracion ventana
        setTitle("Dulces n Dados – Empleado");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(900, 620);
        setLocationRelativeTo(null);

        // guardar automáticamente al cerrar
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                central.guardar(cafe);
                dispose();
            }
        });

        // arrancar mostrando login
        mostrarLogin();

        setVisible(true);
    }


    // LOGIN


    // muestra el panel de login
    public void mostrarLogin() {

        panelLogin = new PanelLoginEmpleado(this);

        setContentPane(panelLogin);

        revalidate();
    }

    // =========================
    // PANEL PRINCIPAL
    // =========================

    public void mostrarPanelPrincipal() {

        // crear paneles
        panelPerfil = new PanelPerfilEmpleado(this);
        panelTurnos = new PanelTurnosEmpleado(this);
        panelJuegos = new PanelJuegosEmpleado(this);
        panelTorneos = new PanelTorneosEmpleado(this);

        // crear tabs
        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Mi perfil", panelPerfil);
        tabbedPane.addTab("Turnos", panelTurnos);
        tabbedPane.addTab("Juegos", panelJuegos);
        tabbedPane.addTab("Torneos", panelTorneos);

        // solo los meseros tienen este panel
        if (empleadoActual instanceof Mesero) {

            panelMesero = new PanelMesero(this);

            tabbedPane.addTab("Funciones mesero", panelMesero);
        }

        // refrescar automaticamente cuando cambian de pestaña
        tabbedPane.addChangeListener(e -> refrescarPestanaActual());

        // contenedor principal
        javax.swing.JPanel contenedor =
                new javax.swing.JPanel(new BorderLayout());

        contenedor.add(new PanelBarraEmpleado(this), BorderLayout.NORTH);
        contenedor.add(tabbedPane, BorderLayout.CENTER);

        setContentPane(contenedor);

        revalidate();
    }

    // refresca la pestaña activa
    private void refrescarPestanaActual() {

        int idx = tabbedPane.getSelectedIndex();

        if (idx == 0) panelPerfil.refrescar();

        else if (idx == 1) panelTurnos.refrescar();

        else if (idx == 2) panelJuegos.refrescar();

        else if (idx == 3) panelTorneos.refrescar();

        else if (idx == 4 && panelMesero != null) {
            panelMesero.refrescar();
        }
    }


    // AUTENTICACIÓN


    // valida login y password
    public boolean autenticar(String login, String password) {

        Empleado empleado = cafe.buscarEmpleado(login);

        // login inválido
        if (empleado == null ||
                !empleado.getPassword().equals(password)) {

            return false;
        }

        // guardar empleado actual
        empleadoActual = empleado;

        // abrir interfaz principal
        mostrarPanelPrincipal();

        return true;
    }

    // cerrar sesion
    public void cerrarSesion() {

        central.guardar(cafe);

        empleadoActual = null;

        mostrarLogin();
    }

    // GETTERS DE DATOS


    // datos basicos para mostrar en panels

    public String getNombreEmpleado() {
        return empleadoActual.getNombre();
    }

    public String getTipoEmpleado() {

        if (empleadoActual instanceof Mesero) {
            return "Mesero";
        }

        return "Cocinero";
    }

    public boolean estaEnTurno() {
        return empleadoActual.isEnTurno();
    }

    public String getCodigoDescuento() {
        return empleadoActual.getCodigoDescuento();
    }

    public ArrayList<Turno> getTurnosEmpleado() {
        return empleadoActual.getTurnos();
    }

    public ArrayList<Torneo> getTorneos() {
        return cafe.getTorneos();
    }

    public List<JuegoDeMesa> getJuegosDisponiblesVenta() {
        return cafe.getJuegosDisponiblesVenta();
    }

    public List<JuegoDeMesa> getJuegosDisponiblesPrestamo() {
        return cafe.getJuegosDisponiblesPrestamo();
    }


    // MÉTODOS PARA TABLAS

    // devuelve nombres de juegos para combos/dialogs

    public String[] getNombresJuegosVenta() {

        List<JuegoDeMesa> juegos =
                cafe.getJuegosDisponiblesVenta();

        String[] nombres = new String[juegos.size()];

        for (int i = 0; i < juegos.size(); i++) {

            nombres[i] = juegos.get(i).getNombre();
        }

        return nombres;
    }

    public String[] getNombresJuegosPrestamo() {

        List<JuegoDeMesa> juegos =
                cafe.getJuegosDisponiblesPrestamo();

        String[] nombres = new String[juegos.size()];

        for (int i = 0; i < juegos.size(); i++) {

            nombres[i] = juegos.get(i).getNombre();
        }

        return nombres;
    }

    // COMPRAR JUEGO


    public String comprarJuego(String nombreJuego, int cantidad) {

        JuegoDeMesa juego = cafe.buscarJuego(nombreJuego);

        // validaciones
        if (juego == null) {
            return "Juego no encontrado.";
        }

        if (!juego.estaDisponibleVenta()) {
            return "No hay copias disponibles.";
        }

        if (cantidad <= 0) {
            return "La cantidad debe ser mayor a 0.";
        }

        if (cantidad > juego.getCopiasVenta()) {
            return "No hay suficientes copias.";
        }

        try {

            // registrar venta
            VentaJuego venta =
                    cafe.registrarVentaJuego(
                            empleadoActual,
                            juego,
                            cantidad);

            return "Compra exitosa.\n"
                    + "Descuento de empleado aplicado.\n"
                    + "Total: $" + venta.calcularTotal();

        }

        catch (IllegalStateException e) {

            return "Error: " + e.getMessage();
        }
    }

    
    // SUGERIR PLATILLO


    public String sugerirPlatillo(
            String nombre,
            String descripcion) {

        // validar campos
        if (nombre == null ||
                nombre.trim().isEmpty()) {

            return "El nombre no puede estar vacío.";
        }

        if (descripcion == null ||
                descripcion.trim().isEmpty()) {

            return "La descripción no puede estar vacía.";
        }

        try {

            SugerenciaPlatillo sugerencia =
                    cafe.crearSugerencia(
                            empleadoActual,
                            nombre.trim(),
                            descripcion.trim());

            return "Sugerencia enviada con código: "
                    + sugerencia.getCodigo();
        }

        catch (IllegalStateException e) {

            return "Error: " + e.getMessage();
        }
    }


    // CAMBIOS DE TURNO


    public String solicitarCambioGeneral(
            String diaOfrece,
            String diaDesea,
            String motivo) {

        Turno turnoOfrece =
                buscarTurnoPorDia(diaOfrece);

        Turno turnoDesea =
                buscarTurnoPorDia(diaDesea);

        // validar turnos
        if (turnoOfrece == null) {
            return "El turno que ofreces no existe.";
        }

        if (turnoDesea == null) {
            return "El turno que deseas no existe.";
        }

        try {

            cafe.solicitarCambioGeneral(
                    empleadoActual,
                    turnoOfrece,
                    turnoDesea,
                    motivo);

            return "Solicitud enviada.";

        }

        catch (IllegalStateException e) {

            return "Error: " + e.getMessage();
        }
    }


    // FUNCIONES MESERO


    public String aplicarDescuento(String loginCliente) {

        // seguridad
        if (!(empleadoActual instanceof Mesero)) {
            return "Solo meseros.";
        }

        Cliente cliente =
                cafe.buscarCliente(loginCliente);

        if (cliente == null) {
            return "Cliente no encontrado.";
        }

        try {

            Mesero mesero = (Mesero) empleadoActual;

            cafe.aplicarCodigoDescuento(
                    cliente,
                    mesero.getCodigoDescuento());

            return "Descuento aplicado.";

        }

        catch (IllegalStateException e) {

            return "Error: " + e.getMessage();
        }
    }

    // préstamo para una mesa
    public String prestamoParaMesa(
            int numeroMesa,
            String nombreJuego) {

        if (!(empleadoActual instanceof Mesero)) {
            return "Solo meseros.";
        }

        Cliente cliente =
                cafe.buscarClientePorMesa(numeroMesa);

        if (cliente == null) {
            return "No hay cliente en esa mesa.";
        }

        JuegoDeMesa juego =
                cafe.buscarJuego(nombreJuego);

        if (juego == null) {
            return "Juego no encontrado.";
        }

        try {

            Prestamo prestamo =
                    cafe.crearPrestamo(cliente, juego);

            return "Préstamo registrado:\n" + prestamo;

        }

        catch (IllegalStateException e) {

            return "Error: " + e.getMessage();
        }
    }


    // MÉTODOS AUXILIARES
    

    // busca turno por día
    public Turno buscarTurnoPorDia(String dia) {

        if (dia == null) return null;

        for (Turno t : cafe.getTurnos()) {

            if (t.getDiaSemana()
                    .equalsIgnoreCase(dia.trim())) {

                return t;
            }
        }

        return null;
    }

    // devuelve días de TODOS los turnos
    public String[] getDiasTurnos() {

        String[] dias =
                new String[cafe.getTurnos().size()];

        for (int i = 0;
             i < cafe.getTurnos().size();
             i++) {

            dias[i] =
                    cafe.getTurnos()
                            .get(i)
                            .getDiaSemana();
        }

        return dias;
    }

    // devuelve solo los turnos del empleado
    public String[] getDiasTurnosEmpleado() {

        String[] dias =
                new String[empleadoActual.getTurnos().size()];

        for (int i = 0;
             i < empleadoActual.getTurnos().size();
             i++) {

            dias[i] =
                    empleadoActual.getTurnos()
                            .get(i)
                            .getDiaSemana();
        }

        return dias;
    }
}