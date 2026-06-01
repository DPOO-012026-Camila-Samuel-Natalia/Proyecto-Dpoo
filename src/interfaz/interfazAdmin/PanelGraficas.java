package interfaz.interfazAdmin;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import modelo.JuegoDeMesa;
import modelo.Prestamo;
import modelo.VentaCafe;
import modelo.VentaJuego;

public class PanelGraficas extends JPanel
{
    private VentanaAdministrador ventana;

    private JComboBox<String> comboJuegos;
    private JPanel panelPastel;
    private JPanel panelBarras;
    private JPanel panelLineas;

    public PanelGraficas(VentanaAdministrador ventana)
    {
        this.ventana = ventana;
        setLayout(new BorderLayout());

        JTabbedPane pestanas = new JTabbedPane();

        panelPastel = crearPanelPastel();
        panelBarras = crearPanelBarras();
        panelLineas = crearPanelLineas();

        pestanas.addTab("Copias por juego", panelPastel);
        pestanas.addTab("Ventas 5 dias", panelBarras);
        pestanas.addTab("Reservas semana", panelLineas);

        add(pestanas, BorderLayout.CENTER);

        actualizarPastel();
        actualizarBarras();
        actualizarLineas();
    }

    private JPanel crearPanelPastel()
    {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panelSuperior.add(new JLabel("Juego:"));
        comboJuegos = new JComboBox<>(ventana.getNombresJuegos());
        comboJuegos.addActionListener(e -> actualizarPastel());
        panelSuperior.add(comboJuegos);

        panel.add(panelSuperior, BorderLayout.NORTH);
        return panel;
    }

    private void actualizarPastel()
    {
        if (panelPastel == null || comboJuegos == null)
        {
            return;
        }

        String nombreJuego = (String) comboJuegos.getSelectedItem();
        if (nombreJuego == null)
        {
            return;
        }

        JuegoDeMesa juego = null;
        for (JuegoDeMesa j : ventana.getCatalogoJuegos())
        {
            if (j.getNombre().equals(nombreJuego))
            {
                juego = j;
            }
        }

        if (juego == null)
        {
            return;
        }

        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Copias venta", juego.getCopiasVenta());
        dataset.setValue("Copias prestamo", juego.getTotalCopiasPrestamo());

        JFreeChart grafica = ChartFactory.createPieChart(
                "Distribucion de copias - " + juego.getNombre(),
                dataset,
                true,
                true,
                false);

        ChartPanel chartPanel = new ChartPanel(grafica);

        panelPastel.removeAll();

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.add(new JLabel("Juego:"));
        panelSuperior.add(comboJuegos);
        panelPastel.add(panelSuperior, BorderLayout.NORTH);
        panelPastel.add(chartPanel, BorderLayout.CENTER);
        panelPastel.revalidate();
        panelPastel.repaint();
    }

    public void refrescar()
    {
        if (comboJuegos != null)
        {
            comboJuegos.removeAllItems();

            String[] nombres = ventana.getNombresJuegos();
            for (String nombre : nombres)
            {
                comboJuegos.addItem(nombre);
            }
            actualizarPastel();
        }

        actualizarBarras();
        actualizarLineas();
    }

    private JPanel crearPanelBarras()
    {
        return new JPanel(new BorderLayout());
    }

    private void actualizarBarras()
    {
        if (panelBarras == null)
        {
            return;
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
        Calendar calendario = Calendar.getInstance();
        calendario.add(Calendar.DAY_OF_MONTH, -4);

        for (int i = 0; i < 5; i++)
        {
            Date fechaActual = calendario.getTime();
            String etiquetaDia = sdf.format(fechaActual);
            double totalCafe = 0;
            double totalJuegos = 0;

            for (VentaCafe venta : ventana.getVentasCafe())
            {
                if (mismoDia(venta.getFecha(), fechaActual))
                {
                    totalCafe += venta.calcularSubtotal();
                }
            }

            for (VentaJuego venta : ventana.getVentasJuego())
            {
                if (mismoDia(venta.getFecha(), fechaActual))
                {
                    totalJuegos += venta.calcularSubtotal() - venta.getDescuento();
                }
            }

            dataset.addValue(totalCafe, "Cafeteria", etiquetaDia);
            dataset.addValue(totalJuegos, "Juegos", etiquetaDia);

            calendario.add(Calendar.DAY_OF_MONTH, 1);
        }

        JFreeChart grafica = ChartFactory.createBarChart(
                "Ventas sin impuestos - ultimos 5 dias",
                "Dia",
                "Valor vendido",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);

        ChartPanel chartPanel = new ChartPanel(grafica);

        panelBarras.removeAll();
        panelBarras.add(chartPanel, BorderLayout.CENTER);
        panelBarras.revalidate();
        panelBarras.repaint();
    }

    private JPanel crearPanelLineas()
    {
        return new JPanel(new BorderLayout());
    }

    private void actualizarLineas()
    {
        if (panelLineas == null)
        {
            return;
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
        Calendar calendario = Calendar.getInstance();
        calendario.add(Calendar.DAY_OF_MONTH, -6);

        for (int i = 0; i < 7; i++)
        {
            Date fechaActual = calendario.getTime();
            String etiquetaDia = sdf.format(fechaActual);
            int totalPrestamos = 0;

            for (Prestamo prestamo : ventana.getHistorialPrestamos())
            {
                if (mismoDia(prestamo.getFecha(), fechaActual))
                {
                    totalPrestamos++;
                }
            }

            dataset.addValue(totalPrestamos, "Prestamos", etiquetaDia);
            calendario.add(Calendar.DAY_OF_MONTH, 1);
        }

        JFreeChart grafica = ChartFactory.createLineChart(
                "Evolucion de reservas/prestamos - ultima semana",
                "Dia",
                "Cantidad",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);

        ChartPanel chartPanel = new ChartPanel(grafica);

        panelLineas.removeAll();
        panelLineas.add(chartPanel, BorderLayout.CENTER);
        panelLineas.revalidate();
        panelLineas.repaint();
    }

    private boolean mismoDia(Date fecha1, Date fecha2)
    {
        if (fecha1 == null || fecha2 == null)
        {
            return false;
        }

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(fecha1);
        c2.setTime(fecha2);

        return c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
                && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);
    }
}
