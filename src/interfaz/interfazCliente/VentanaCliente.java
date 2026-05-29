package interfaz.interfazCliente;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
 
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
 
import modelo.*;
import persistencia.CentralPersistencia;
import persistencia.PersistenciaBoardGameCafeJSON;
import java.util.List;
 

public class VentanaCliente extends JFrame
{
    private static final long serialVersionUID = 1L;
 
   
    private BoardgameCafe cafe;
    private CentralPersistencia central;
    private Cliente clienteActual;
 
    
    private PanelLoginCliente    panelLogin;
    private PanelPerfilCliente   panelPerfil;
    private PanelMenuCafe        panelMenu;
    private PanelJuegosCliente   panelJuegos;
    private PanelFavoritos       panelFavoritos;
    private PanelTorneosCliente  panelTorneos;
    private JTabbedPane          tabbedPane;
 
    
    // CONSTRUCTOR
    
    public VentanaCliente()
    {
        central = new CentralPersistencia(new PersistenciaBoardGameCafeJSON("src/datos/boardgamecafe.txt"));
        cafe = central.cargar();
        if (cafe == null) cafe = new BoardgameCafe(20);
 
        setTitle("Dulces n Dados – Cliente");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(900, 620);
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
 
   
    public void mostrarLogin()
    {
        panelLogin = new PanelLoginCliente(this);
        setContentPane(panelLogin);
        revalidate();
    }
 
    public void mostrarPanelPrincipal()
    {
        panelPerfil    = new PanelPerfilCliente(this);
        panelMenu      = new PanelMenuCafe(this);
        panelJuegos    = new PanelJuegosCliente(this);
        panelFavoritos = new PanelFavoritos(this);
        panelTorneos   = new PanelTorneosCliente(this);
 
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Mi perfil",  panelPerfil);
        tabbedPane.addTab("Menú café",  panelMenu);
        tabbedPane.addTab("Juegos",     panelJuegos);
        tabbedPane.addTab("Favoritos",  panelFavoritos);
        tabbedPane.addTab("Torneos",    panelTorneos);
 
        tabbedPane.addChangeListener(e -> refrescarPestañaActual());
 
        javax.swing.JPanel contenedor = new javax.swing.JPanel(new BorderLayout());
        contenedor.add(new PanelBarraCliente(this), BorderLayout.NORTH);
        contenedor.add(tabbedPane, BorderLayout.CENTER);
 
        setContentPane(contenedor);
        revalidate();
    }
 
    private void refrescarPestañaActual()
    {
        int idx = tabbedPane.getSelectedIndex();
        if      (idx == 0) panelPerfil.refrescar();
        else if (idx == 1) panelMenu.refrescar();
        else if (idx == 2) panelJuegos.refrescar();
        else if (idx == 3) panelFavoritos.refrescar();
        else if (idx == 4) panelTorneos.refrescar();
    }
 
    //Autenticar
    //True si el login es correcto
    public boolean autenticar(String login, String password)
    {
        Cliente encontrado = cafe.buscarCliente(login);
        if (encontrado == null || !encontrado.getPassword().equals(password))
        {
            return false;
        }
        this.clienteActual = encontrado;
        mostrarPanelPrincipal();
        return true;
    }
 
    // Registra cliente nuevo y lo pone como el cliente actual
    public boolean registrar(String nombre, String login, String password)
    {
        if (cafe.buscarCliente(login) != null) return false;
        String id = "C" + (cafe.getClientes().size() + 1);
        Cliente nuevo = new Cliente(id, nombre, login, password);
        cafe.agregarCliente(nuevo);
        this.clienteActual = nuevo;
        mostrarPanelPrincipal();
        return true;
    }
 
    public boolean loginExiste(String login)
    {
        return cafe.buscarCliente(login) != null;
    }
 
    public void cerrarSesion()
    {
        central.guardar(cafe);
        clienteActual = null;
        mostrarLogin();
    }
 
   
    //Datos para llenar las tablas
    public String getNombreCliente()     { return clienteActual.getNombre(); }
    public int getPuntosFidelidad()      { return clienteActual.getPuntosFidelidad(); }
    public boolean tieneBonoTorneo()     { return clienteActual.tieneBonoTorneo(); }
    public double getBonoTorneo()        { return clienteActual.getBonoTorneo(); }
 
    public ArrayList<ProductoMenu> getProductosMenu()
    {
        return cafe.getProductosMenu();
    }
 
    public List<JuegoDeMesa> getJuegosDisponiblesVenta()
    {
        return cafe.getJuegosDisponiblesVenta();
    }
 
    public ArrayList<JuegoDeMesa> getJuegosFavoritos()
    {
        return clienteActual.getJuegosFavoritos();
    }
 
    public ArrayList<JuegoDeMesa> getCatalogoJuegos()
    {
        return cafe.getCatalogoJuegos();
    }
 
    public ArrayList<Torneo> getTorneos()
    {
        return cafe.getTorneos();
    }
 
   //ACCIONES
 
    // JUEGOS
 
    // Compra un juego y devuelve el total pagado, o -1 si hay error
    public String comprarJuego(String nombreJuego, int cantidad)
    {
        JuegoDeMesa juego = cafe.buscarJuego(nombreJuego);
        if (juego == null)            return "Juego no encontrado.";
        if (!juego.estaDisponibleVenta()) return "No hay copias disponibles.";
        if (cantidad > juego.getCopiasVenta()) return "No hay suficientes copias. Disponibles: " + juego.getCopiasVenta();
 
        VentaJuego venta = cafe.registrarVentaJuego(clienteActual, juego, cantidad);
        String msg = "Compra exitosa.\nTotal: $" + venta.calcularTotal();
        if (venta.getDescuento() > 0) msg += "\nDescuento aplicado: $" + venta.getDescuento();
        msg += "\nPuntos acumulados: " + clienteActual.getPuntosFidelidad();
        return msg;
    }
 
    // Devuelve arreglo con nombres de juegos disponibles para venta
    public String[] getNombresJuegosVenta()
    {
        List<JuegoDeMesa> disponibles = cafe.getJuegosDisponiblesVenta();
        String[] nombres = new String[disponibles.size()];
        for (int i = 0; i < disponibles.size(); i++)
        {
            nombres[i] = disponibles.get(i).getNombre();
        }
        return nombres;
    }
 
    // FAVORITOS
 
    public boolean esFavorito(String nombreJuego)
    {
        JuegoDeMesa juego = cafe.buscarJuego(nombreJuego);
        return juego != null && clienteActual.esFanaticoDe(juego);
    }
 
    public String agregarFavorito(String nombreJuego)
    {
        JuegoDeMesa juego = cafe.buscarJuego(nombreJuego);
        if (juego == null) return "Juego no encontrado.";
        if (clienteActual.esFanaticoDe(juego)) return "Ese juego ya está en tus favoritos.";
        clienteActual.agregarJuegoFavorito(juego);
        return juego.getNombre() + " agregado a tus favoritos.";
    }
 
    // Devuelve arreglo con nombres de todos los juegos del catalogo
    public String[] getNombresCatalogoJuegos()
    {
        String[] nombres = new String[cafe.getCatalogoJuegos().size()];
        for (int i = 0; i < cafe.getCatalogoJuegos().size(); i++)
        {
            nombres[i] = cafe.getCatalogoJuegos().get(i).getNombre();
        }
        return nombres;
    }
 
    // TORNEOS
 
    public String inscribirseATorneo(String codigoTorneo, int cupos)
    {
        Torneo torneo = cafe.buscarTorneo(codigoTorneo);
        if (torneo == null)               return "Torneo no encontrado.";
        if (torneo.estaInscrito(clienteActual)) return "Ya estás inscrito en ese torneo.";
        if (torneo.getCuposDisponibles() == 0)  return "No hay cupos disponibles.";
        cafe.inscribirClienteTorneo(clienteActual, torneo, cupos);
        return "Inscripción exitosa en " + codigoTorneo + ".";
    }
 
    public String desinscribirseATorneo(String codigoTorneo)
    {
        Torneo torneo = cafe.buscarTorneo(codigoTorneo);
        if (torneo == null)                return "Torneo no encontrado.";
        if (!torneo.estaInscrito(clienteActual)) return "No estás inscrito en ese torneo.";
        cafe.desinscribirUsuarioTorneo(clienteActual, torneo);
        return "Desinscripción exitosa.";
    }
 
    // Devuelve codigos de torneos en los que el cliente está inscrito
    public String[] getCodigosTorneosInscritos()
    {
        int cant = 0;
        for (Torneo t : cafe.getTorneos())
        {
            if (t.estaInscrito(clienteActual)) cant++;
        }
        String[] codigos = new String[cant];
        int i = 0;
        for (Torneo t : cafe.getTorneos())
        {
            if (t.estaInscrito(clienteActual))
            {
                codigos[i] = t.getCodigo();
                i++;
            }
        }
        return codigos;
    }
 
    // Devuelve codigos de todos los torneos
    public String[] getCodigosTorneos()
    {
        String[] codigos = new String[cafe.getTorneos().size()];
        for (int i = 0; i < cafe.getTorneos().size(); i++)
        {
            codigos[i] = cafe.getTorneos().get(i).getCodigo();
        }
        return codigos;
    }
}
