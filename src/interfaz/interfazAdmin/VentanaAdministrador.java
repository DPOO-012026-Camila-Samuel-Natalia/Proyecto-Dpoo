package interfaz.interfazAdmin;
 
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;
 
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import modelo.*;
import persistencia.CentralPersistencia;
import persistencia.PersistenciaBoardGameCafeJSON;
 
/**
 * Ventana principal del admin
 * UNICA CLASE QUE ACCEDE AL MODELO 
 * Los paneles llaman metodos de esta ventana
 */
public class VentanaAdministrador extends JFrame{
	private static final long serialVersionUID = 1L;
    private BoardgameCafe cafe;
    private CentralPersistencia central;
    private Administrador admin;
 
    
    private PanelLogin       panelLogin;
    private PanelEmpleados   panelEmpleados;
    private PanelTurnos      panelTurnos;
    private PanelSolicitudes panelSolicitudes;
    private PanelSugerencias panelSugerencias;
    private PanelInventario  panelInventario;
    private PanelTorneos     panelTorneos;
    private PanelVentas      panelVentas;
    private JTabbedPane      tabbedPane;
    private PanelGraficas    panelGraficas;
 
    
    // CONSTRUCTOR
  
    public VentanaAdministrador()
    {
        central = new CentralPersistencia(new PersistenciaBoardGameCafeJSON("src/datos/boardgamecafe.txt"));
        cafe = central.cargar();
        if (cafe == null) cafe = new BoardgameCafe(20);
 
        setTitle("Dulces n Dados – Administrador");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(980, 680);
        setLocationRelativeTo(null);
 
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                central.guardar(cafe);
                dispose();
            }
        });
 
        mostrarLogin();
        setVisible(true);
    }
 
    
    // NAVEGAR ENTRE PANTALLAS

    public void mostrarLogin()
    {
        panelLogin = new PanelLogin(this);
        setContentPane(panelLogin);
        revalidate();
    }
 
    public void mostrarPanelPrincipal()
    {
        panelEmpleados   = new PanelEmpleados(this);
        panelTurnos      = new PanelTurnos(this);
        panelSolicitudes = new PanelSolicitudes(this);
        panelSugerencias = new PanelSugerencias(this);
        panelInventario  = new PanelInventario(this);
        panelTorneos     = new PanelTorneos(this);
        panelVentas      = new PanelVentas(this);
        panelGraficas    = new PanelGraficas(this);
 
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Empleados",   panelEmpleados);
        tabbedPane.addTab("Turnos",      panelTurnos);
        tabbedPane.addTab("Solicitudes", panelSolicitudes);
        tabbedPane.addTab("Sugerencias", panelSugerencias);
        tabbedPane.addTab("Inventario",  panelInventario);
        tabbedPane.addTab("Torneos",     panelTorneos);
        tabbedPane.addTab("Ventas",      panelVentas);
        tabbedPane.addTab("Graficas", panelGraficas);
 
        tabbedPane.addChangeListener(e -> refrescarPestañaActual()); //Forma compacta de agregar un Listener para cambiar la pantalla
 
        javax.swing.JPanel contenedor = new javax.swing.JPanel(new BorderLayout());
        contenedor.add(new PanelBarraSuperior(this), BorderLayout.NORTH);
        contenedor.add(tabbedPane, BorderLayout.CENTER);
 
        setContentPane(contenedor);
        revalidate();
    }
 
    private void refrescarPestañaActual()
    {
        int idx = tabbedPane.getSelectedIndex();
        if      (idx == 0) panelEmpleados.refrescar();
        else if (idx == 1) panelTurnos.refrescar();
        else if (idx == 2) panelSolicitudes.refrescar();
        else if (idx == 3) panelSugerencias.refrescar();
        else if (idx == 4) panelInventario.refrescar();
        else if (idx == 5) panelTorneos.refrescar();
        else if (idx == 6) panelVentas.refrescar();
        else if (idx == 7) panelGraficas.refrescar();
     
    }
 
    //AUTENTICAR
    public boolean autenticar(String login, String password)
    {
        Administrador encontrado = cafe.getAdministrador();
        if (encontrado == null || !encontrado.getLogin().equals(login) || !encontrado.getPassword().equals(password)){
            return false;
        }
        this.admin = encontrado;
        mostrarPanelPrincipal();
        return true;
    }
 
    public void cerrarSesion()
    {
        central.guardar(cafe);
        admin = null;
        mostrarLogin();
    }
 
    public String getNombreAdmin()
    {
        return admin.getNombre();
    }
 
   
    // DATOS para llenar las tablas
 
    public Collection<Empleado> getEmpleados()
    {
        return cafe.getEmpleados();
    }
 
    public ArrayList<Turno> getTurnos()
    {
        return cafe.getTurnos();
    }
 
    public ArrayList<SolicitudCambioTurno> getSolicitudes()
    {
        return cafe.getSolicitudesCambioTurno();
    }
 
    public ArrayList<SugerenciaPlatillo> getSugerencias()
    {
        return cafe.getSugerenciasPlatillos();
    }
 
    public ArrayList<JuegoDeMesa> getCatalogoJuegos()
    {
        return cafe.getCatalogoJuegos();
    }
 
    public ArrayList<Torneo> getTorneos()
    {
        return cafe.getTorneos();
    }
 
    public String getInformeVentas()
    {
        return cafe.getInformeVentas();
    }
 
    
    // ACCIONES  los paneles lo llaman en vez de al modelo

 
    //  EMPLEADOS 
 
    public boolean loginEmpleadoExiste(String login)
    {
        return cafe.buscarEmpleado(login) != null;
    }
 
    public String registrarEmpleado(String tipo, String nombre, String login, String password)
    {
        String id = "E" + (cafe.getEmpleados().size() + 1);
        Empleado emp = tipo.equals("Mesero")? new Mesero(id, nombre, login, password): new Cocinero(id, nombre, login, password);
        admin.registrarEmpleado(cafe, emp);
        return emp.getCodigoDescuento();
    }
 
    // TURNOS 
 
    public void crearTurno(String dia)
    {
        cafe.agregarTurno(new Turno(dia));
    }
 
    public void asignarEmpleadoATurno(String loginEmpleado, String diaTurno)
    {
        Empleado emp = cafe.buscarEmpleado(loginEmpleado);
        Turno turno = null;
        for (Turno t : cafe.getTurnos())
        {
            if (t.getDiaSemana().equals(diaTurno)) turno = t;
        }
        if (emp != null && turno != null)
        {
            cafe.asignarTurno(emp, turno);
        }
    }
 
    // Arreglo "login – nombre" para mostrar en el selector
    public String[] getOpcionesEmpleados()
    {
        String[] opciones = new String[cafe.getEmpleados().size()];
        int i = 0;
        for (Empleado e : cafe.getEmpleados())
        {
            opciones[i] = e.getLogin() + " – " + e.getNombre();
            i++;
        }
        return opciones;
    }
 
    // Arreglo con dias de los turnos para mostrar en el selector
    public String[] getDiasTurnos()
    {
        String[] dias = new String[cafe.getTurnos().size()];
        int i = 0;
        for (Turno t : cafe.getTurnos())
        {
            dias[i] = t.getDiaSemana();
            i++;
        }
        return dias;
    }
 
    // SOLICITUDES 
 
    public void aprobarSolicitud(String codigo)
    {
        SolicitudCambioTurno s = buscarSolicitud(codigo);
        if (s == null) return;
        if (s.getTipoCambio().equals("INTERCAMBIO"))
            cafe.aprobarIntercambioTurno(s);
        else
            cafe.aprobarCambioGeneral(s);
    }
 
    public void rechazarSolicitud(String codigo)
    {
        SolicitudCambioTurno s = buscarSolicitud(codigo);
        if (s != null) cafe.rechazarCambioTurno(s);
    }
 
    private SolicitudCambioTurno buscarSolicitud(String codigo)
    {
        for (SolicitudCambioTurno s : cafe.getSolicitudesCambioTurno())
        {
            if (s.getCodigo().equals(codigo)) return s;
        }
        return null;
    }
 
    // SUGERENCIAS 
 
    public void aprobarSugerencia(String codigo, String tipo, double precio)
    {
        SugerenciaPlatillo s = buscarSugerencia(codigo);
        if (s != null) cafe.aprobarSugerencia(s, tipo, precio);
    }
 
    public void rechazarSugerencia(String codigo)
    {
        SugerenciaPlatillo s = buscarSugerencia(codigo);
        if (s != null) cafe.rechazarSugerencia(s);
    }
 
    private SugerenciaPlatillo buscarSugerencia(String codigo)
    {
        for (SugerenciaPlatillo s : cafe.getSugerenciasPlatillos())
        {
            if (s.getCodigo().equals(codigo)) return s;
        }
        return null;
    }
 
    //  INVENTARIO 
 
    public void agregarJuego(String nombre, String cat, int minJ, int maxJ,int edad, boolean dificil, int copP, int copV, double precio){
        JuegoDeMesa juego = new JuegoDeMesa(nombre, 0, "NA", minJ, maxJ, edad, cat, dificil, copP, copV, precio);
        cafe.agregarJuego(juego);
    }
 
    public String agregarCopiasPrestamo(String nombre, int cantidad) {
        JuegoDeMesa j = cafe.buscarJuego(nombre);
        if (j == null) return "Juego no encontrado.";
        j.agregarCopiasPrestamo(cantidad);
        return "Copias de préstamo: " + j.getTotalCopiasPrestamo();
    }
 
    public String agregarCopiasVenta(String nombre, int cantidad){
        JuegoDeMesa j = cafe.buscarJuego(nombre);
        if (j == null) return "Juego no encontrado.";
        j.agregarCopiasVenta(cantidad);
        return "Copias de venta: " + j.getCopiasVenta();
    }
 
    public String moverVentaAPrestamo(String nombre){
        JuegoDeMesa j = cafe.buscarJuego(nombre);
        if (j == null) return "Juego no encontrado.";
        cafe.moverVentaAPrestamo(j);
        return "Copia movida.\nVenta: " + j.getCopiasVenta()+ " | Préstamo: " + j.getTotalCopiasPrestamo();
    }
 
    // Arreglo con los nombres de los juegos para el selector
    public String[] getNombresJuegos() {
        String[] nombres = new String[cafe.getCatalogoJuegos().size()];
        int i = 0;
        for (JuegoDeMesa j : cafe.getCatalogoJuegos())
        {
            nombres[i] = j.getNombre();
            i++;
        }
        return nombres;
    }
 
    //  TORNEOS 
 
    public String crearTorneo(String nombreJuego, String dia, int maxP,TipoTorneo tipo, double tarifa, double bono){
        JuegoDeMesa juego = cafe.buscarJuego(nombreJuego);
        Torneo torneo = admin.crearTorneo(cafe, dia, juego, maxP, tipo, tarifa, bono);
        return torneo.getCodigo();
    }
 
    public void inscribirEmpleadoATorneo(String loginEmp, String codigoTorneo, int cupos){
        Empleado emp = cafe.buscarEmpleado(loginEmp);
        Torneo torneo = cafe.buscarTorneo(codigoTorneo);
        admin.inscribirEmpleadoATorneo(cafe, emp, torneo, cupos);
    }
 
    public String entregarPremio(String codigoTorneo, String loginCliente){
        Torneo torneo = cafe.buscarTorneo(codigoTorneo);
        Cliente ganador = cafe.buscarCliente(loginCliente);
        if (ganador == null) return null; // null indica que no se encontro el cliente
        cafe.entregarPremioAmistoso(torneo, ganador);
        return ganador.getNombre();
    }
 
    // Arreglo con los codigos de todos los torneos
    public String[] getCodigosTorneos(){
        String[] codigos = new String[cafe.getTorneos().size()];
        int i = 0;
        for (Torneo t : cafe.getTorneos())
        {
            codigos[i] = t.getCodigo();
            i++;
        }
        return codigos;
    }
 
    // Arreglo con los codigos solo de los torneos amistosos
    public String[] getCodigosTorneosAmistosos() {
        int cant = 0;
        for (Torneo t : cafe.getTorneos())
        {
            if (t.getTipo() == TipoTorneo.AMISTOSO) cant++;
        }
        String[] codigos = new String[cant];
        int i = 0;
        for (Torneo t : cafe.getTorneos())
        {
            if (t.getTipo() == TipoTorneo.AMISTOSO)
            {
                codigos[i] = t.getCodigo();
                i++;
            }
        }
        return codigos;
    }
 
    public ArrayList<VentaCafe> getVentasCafe()
    {
    	return cafe.getVentasCafe();
    }
    
    public ArrayList<VentaJuego> getVentasJuego()
    {
    	return cafe.getVentasJuego();
    }
    public ArrayList<Prestamo> getHistorialPrestamos()
    {
    	return cafe.getHistorialPrestamos();
    }
    
}

