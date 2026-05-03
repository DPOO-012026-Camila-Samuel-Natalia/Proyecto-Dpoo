package persistencia;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

import modelo.*;

// esta clase guarda y carga el sistema en archivo de texto
public class PersistenciaBoardGameCafeJSON implements IPersistenciaBoardGameCafe
{
    private String ruta;

    public PersistenciaBoardGameCafeJSON(String ruta)
    {
        this.ruta = ruta;
    }

    @Override
    public void guardar(BoardgameCafe cafe)
    {
        try
        {
            PrintWriter out = new PrintWriter(new FileWriter(ruta));

            out.println("CAPACIDAD;" + cafe.getCapacidadMaxima());

            Administrador a = cafe.getAdministrador();
            if (a != null)
            {
                out.println("ADMIN;" + a.getId() + ";" + a.getNombre() + ";" + a.getLogin() + ";" + a.getPassword());
            }

            Collection<Cliente> clientes = cafe.getClientes();
            for (Cliente c : clientes)
            {
                String mesa = "null";
                if (c.getMesaActual() != null)
                {
                    mesa = "" + c.getMesaActual().getNumero();
                }

                String codigo = c.getCodigoDescuento();
                if (codigo == null)
                {
                    codigo = "null";
                }

                out.println("CLIENTE;" + c.getId() + ";" + c.getNombre() + ";" + c.getLogin() + ";" + c.getPassword()
                        + ";" + c.getPuntosFidelidad() + ";" + mesa + ";" + codigo + ";" + c.getBonoTorneo());
            }

            Collection<Empleado> empleados = cafe.getEmpleados();
            for (Empleado e : empleados)
            {
                String tipo = "COCINERO";
                if (e instanceof Mesero)
                {
                    tipo = "MESERO";
                }

                out.println("EMPLEADO;" + tipo + ";" + e.getId() + ";" + e.getNombre() + ";" + e.getLogin() + ";"
                        + e.getPassword() + ";" + e.isEnTurno());

                if (e instanceof Mesero)
                {
                    Mesero m = (Mesero) e;
                    for (String juego : m.getJuegosQueExplica())
                    {
                        out.println("EXPLICA;" + m.getLogin() + ";" + juego);
                    }
                }
            }

            for (Mesa m : cafe.getMesas())
            {
                out.println("MESA;" + m.getNumero() + ";" + m.getCapacidad() + ";" + m.getNumPersonas() + ";"
                        + m.isNinos() + ";" + m.hayMenoresDeEdad());
            }

            for (JuegoDeMesa j : cafe.getCatalogoJuegos())
            {
                out.println("JUEGO;" + j.getNombre() + ";" + j.getMinJugadores() + ";" + j.getMaxJugadores() + ";"
                        + j.getEdadMinima() + ";" + j.getCategoria() + ";" + j.isDificil() + ";"
                        + j.getTotalCopiasPrestamo() + ";" + j.getCopiasVenta() + ";" + j.getPrecioUnitario()
                        + ";" + j.getCopiasEnUso());
            }

            for (Cliente c : clientes)
            {
                for (JuegoDeMesa j : c.getJuegosFavoritos())
                {
                    out.println("FAVORITO;" + c.getLogin() + ";" + j.getNombre());
                }
            }

            for (ProductoMenu p : cafe.getProductosMenu())
            {
                if (p instanceof Bebida)
                {
                    Bebida b = (Bebida) p;
                    out.println("BEBIDA;" + b.getNombre() + ";" + b.getPrecio() + ";" + b.isAlcoholica() + ";"
                            + b.isCaliente());
                }
                else if (p instanceof Pasteleria)
                {
                    Pasteleria pa = (Pasteleria) p;
                    out.println("PASTELERIA;" + pa.getNombre() + ";" + pa.getPrecio());
                }
            }

            for (Turno t : cafe.getTurnos())
            {
                out.println("TURNO;" + t.getDiaSemana());

                for (Empleado e : t.getEmpleados())
                {
                    out.println("TURNO_EMPLEADO;" + t.getDiaSemana() + ";" + e.getLogin());
                }
            }

            for (Torneo t : cafe.getTorneos())
            {
                out.println("TORNEO;" + t.getCodigo() + ";" + t.getDiaSemana() + ";" + t.getJuego().getNombre()
                        + ";" + t.getMaxParticipantes() + ";" + t.getTipo() + ";" + t.getTarifaEntrada()
                        + ";" + t.getBonoDescuento());

                for (InscripcionTorneo i : t.getInscripciones())
                {
                    out.println("INSCRIPCION_TORNEO;" + t.getCodigo() + ";" + i.getUsuario().getLogin()
                            + ";" + i.getCuposTomados());
                }
            }

            out.close();
        }
        catch (IOException e)
        {
            System.out.println("Error guardando: " + e.getMessage());
        }
    }

    @Override
    public BoardgameCafe cargar()
    {
        BoardgameCafe cafe = null;
        ArrayList<String> lineas = new ArrayList<String>();

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(ruta));
            String linea = br.readLine();

            while (linea != null)
            {
                lineas.add(linea);
                linea = br.readLine();
            }

            br.close();

            for (String l : lineas)
            {
                String[] p = l.split(";");

                if (p[0].equals("CAPACIDAD") && cafe == null)
                {
                    cafe = new BoardgameCafe(Integer.parseInt(p[1]));
                }
            }

            if (cafe == null)
            {
                return null;
            }

            for (String l : lineas)
            {
                String[] p = l.split(";");

                if (p[0].equals("ADMIN"))
                {
                    cafe.setAdministrador(new Administrador(p[1], p[2], p[3], p[4]));
                }
                else if (p[0].equals("CLIENTE"))
                {
                    Cliente c = new Cliente(p[1], p[2], p[3], p[4]);
                    c.agregarPuntos(Integer.parseInt(p[5]));

                    if (!p[7].equals("null"))
                    {
                        c.setCodigoDescuento(p[7]);
                    }

                    if (p.length > 8)
                    {
                        c.guardarBonoTorneo(Double.parseDouble(p[8]));
                    }

                    cafe.agregarCliente(c);
                }
                else if (p[0].equals("EMPLEADO"))
                {
                    Empleado e;

                    if (p[1].equals("MESERO"))
                    {
                        e = new Mesero(p[2], p[3], p[4], p[5]);
                    }
                    else
                    {
                        e = new Cocinero(p[2], p[3], p[4], p[5]);
                    }

                    if (Boolean.parseBoolean(p[6]))
                    {
                        e.iniciarTurno();
                    }

                    cafe.agregarEmpleado(e);
                }
                else if (p[0].equals("MESA"))
                {
                    Mesa m = new Mesa(Integer.parseInt(p[1]), Integer.parseInt(p[2]));

                    if (Integer.parseInt(p[3]) > 0)
                    {
                        m.ocupar(Integer.parseInt(p[3]), Boolean.parseBoolean(p[4]), Boolean.parseBoolean(p[5]));
                    }

                    cafe.agregarMesa(m);
                }
                else if (p[0].equals("JUEGO"))
                {
                    JuegoDeMesa j = new JuegoDeMesa(p[1], 0, "NA", Integer.parseInt(p[2]),
                            Integer.parseInt(p[3]), Integer.parseInt(p[4]), p[5],
                            Boolean.parseBoolean(p[6]), Integer.parseInt(p[7]),
                            Integer.parseInt(p[8]), Double.parseDouble(p[9]));

                    j.setCopiasEnUso(Integer.parseInt(p[10]));
                    cafe.agregarJuego(j);
                }
                else if (p[0].equals("BEBIDA"))
                {
                    Bebida b = new Bebida(p[1], Double.parseDouble(p[2]),
                            Boolean.parseBoolean(p[3]), Boolean.parseBoolean(p[4]));

                    cafe.agregarProductoMenu(b);
                }
                else if (p[0].equals("PASTELERIA"))
                {
                    Pasteleria pa = new Pasteleria(p[1], Double.parseDouble(p[2]));
                    cafe.agregarProductoMenu(pa);
                }
                else if (p[0].equals("TURNO"))
                {
                    cafe.agregarTurno(new Turno(p[1]));
                }
            }

            for (String l : lineas)
            {
                String[] p = l.split(";");

                if (p[0].equals("EXPLICA"))
                {
                    Empleado e = cafe.buscarEmpleado(p[1]);

                    if (e instanceof Mesero)
                    {
                        Mesero m = (Mesero) e;
                        m.agregarJuegoQueExplica(p[2]);
                    }
                }
                else if (p[0].equals("TURNO_EMPLEADO"))
                {
                    Turno turno = buscarTurno(cafe, p[1]);
                    Empleado empleado = cafe.buscarEmpleado(p[2]);

                    if (turno != null && empleado != null)
                    {
                        cafe.asignarTurno(empleado, turno);
                    }
                }
                else if (p[0].equals("FAVORITO"))
                {
                    Cliente c = cafe.buscarCliente(p[1]);
                    JuegoDeMesa j = cafe.buscarJuego(p[2]);

                    if (c != null && j != null)
                    {
                        c.agregarJuegoFavorito(j);
                    }
                }
                else if (p[0].equals("TORNEO"))
                {
                    JuegoDeMesa juego = cafe.buscarJuego(p[3]);
                    TipoTorneo tipo = TipoTorneo.valueOf(p[5]);

                    Torneo torneo = new Torneo(p[1], p[2], juego, Integer.parseInt(p[4]),
                            tipo, Double.parseDouble(p[6]), Double.parseDouble(p[7]));

                    cafe.agregarTorneo(torneo);
                }
            }

            for (String l : lineas)
            {
                String[] p = l.split(";");

                if (p[0].equals("INSCRIPCION_TORNEO"))
                {
                    Torneo torneo = cafe.buscarTorneo(p[1]);
                    Cliente cliente = cafe.buscarCliente(p[2]);
                    Empleado empleado = cafe.buscarEmpleado(p[2]);
                    int cupos = Integer.parseInt(p[3]);

                    if (torneo != null && cliente != null)
                    {
                        torneo.inscribir(cliente, cupos, cliente.esFanaticoDe(torneo.getJuego()));
                    }
                    else if (torneo != null && empleado != null)
                    {
                        torneo.inscribir(empleado, cupos, false);
                    }
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Error cargando: " + e.getMessage());
        }

        return cafe;
    }

    // busca un turno por dia
    private Turno buscarTurno(BoardgameCafe cafe, String diaSemana)
    {
        Turno encontrado = null;

        for (Turno t : cafe.getTurnos())
        {
            if (t.getDiaSemana().equals(diaSemana))
            {
                encontrado = t;
            }
        }

        return encontrado;
    }
}