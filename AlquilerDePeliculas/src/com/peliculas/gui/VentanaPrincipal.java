package com.peliculas.gui;

import com.peliculas.dao.AlquilerDAO;
import com.peliculas.dao.ClienteDAO;
import com.peliculas.dao.PeliculaDAO;
import com.peliculas.model.Alquiler;
import com.peliculas.model.Cliente;
import com.peliculas.model.Pelicula;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;

public class VentanaPrincipal extends JFrame {
    private final PeliculaDAO peliculaDAO = new PeliculaDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final AlquilerDAO alquilerDAO = new AlquilerDAO();

    private Runnable recargarTodo;

    public VentanaPrincipal() {
        setTitle("Sistema de Alquiler de Películas");
        setSize(850, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane pestañas = new JTabbedPane();
        JPanel panelPeliculas = crearPanelPeliculas();
        JPanel panelClientes = crearPanelClientes();
        JPanel panelAlquileres = crearPanelAlquileres();

        pestañas.addTab("Películas", panelPeliculas);
        pestañas.addTab("Clientes", panelClientes);
        pestañas.addTab("Alquileres", panelAlquileres);

        add(pestañas);
    }

    // --- MÓDULO 1: PELÍCULAS ---
    private JPanel crearPanelPeliculas() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextField txtTitulo = new JTextField(15);
        JTextField txtGenero = new JTextField(15);
        JTextField txtPrecio = new JTextField(15);
        JButton btnGuardar = new JButton("Guardar Película");
        JButton btnEliminar = new JButton("Eliminar Seleccionada");

        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Registro de Películas"));
        form.add(new JLabel("Título:")); form.add(txtTitulo);
        form.add(new JLabel("Género:")); form.add(txtGenero);
        form.add(new JLabel("Precio Alquiler (S/):")); form.add(txtPrecio);
        form.add(btnGuardar); form.add(btnEliminar);

        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Título", "Género", "Precio"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);

        Runnable cargarTabla = () -> {
            model.setRowCount(0);
            for (Pelicula p : peliculaDAO.listar()) {
                model.addRow(new Object[]{p.getId(), p.getTitulo(), p.getGenero(), "S/ " + p.getPrecio()});
            }
        };

        btnGuardar.addActionListener(e -> {
            try {
                String titulo = txtTitulo.getText().trim();
                String genero = txtGenero.getText().trim();
                double precio = Double.parseDouble(txtPrecio.getText().trim());

                if (titulo.isEmpty() || genero.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Complete todos los campos.");
                    return;
                }

                if (peliculaDAO.registrar(new Pelicula(titulo, genero, precio))) {
                    JOptionPane.showMessageDialog(this, "Película guardada correctamente en MySQL.");
                    txtTitulo.setText(""); txtGenero.setText(""); txtPrecio.setText("");
                    if (recargarTodo != null) recargarTodo.run();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese un precio válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEliminar.addActionListener(e -> {
            int fila = table.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione una película de la tabla para eliminar.");
                return;
            }

            int id = (int) model.getValueAt(fila, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar película ID " + id + " de MySQL?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (peliculaDAO.eliminar(id)) {
                    JOptionPane.showMessageDialog(this, "Película eliminada de MySQL.");
                    if (recargarTodo != null) recargarTodo.run();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar de MySQL.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cargarTabla.run();
        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Guardar referencia de recarga
        registrarRecargaPeliculas(cargarTabla);
        return panel;
    }

    // --- MÓDULO 2: CLIENTES ---
    private JPanel crearPanelClientes() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextField txtNombre = new JTextField(15);
        JTextField txtDni = new JTextField(15);
        JTextField txtTelefono = new JTextField(15);
        JButton btnGuardar = new JButton("Guardar Cliente");
        JButton btnEliminar = new JButton("Eliminar Seleccionado");

        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Registro de Clientes"));
        form.add(new JLabel("Nombre:")); form.add(txtNombre);
        form.add(new JLabel("DNI:")); form.add(txtDni);
        form.add(new JLabel("Teléfono:")); form.add(txtTelefono);
        form.add(btnGuardar); form.add(btnEliminar);

        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Nombre", "DNI", "Teléfono"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);

        Runnable cargarTabla = () -> {
            model.setRowCount(0);
            for (Cliente c : clienteDAO.listar()) {
                model.addRow(new Object[]{c.getId(), c.getNombre(), c.getDni(), c.getTelefono()});
            }
        };

        btnGuardar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String dni = txtDni.getText().trim();
            String telefono = txtTelefono.getText().trim();

            if (nombre.isEmpty() || dni.isEmpty() || telefono.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos.");
                return;
            }

            if (clienteDAO.registrar(new Cliente(nombre, dni, telefono))) {
                JOptionPane.showMessageDialog(this, "Cliente guardado en MySQL.");
                txtNombre.setText(""); txtDni.setText(""); txtTelefono.setText("");
                if (recargarTodo != null) recargarTodo.run();
            }
        });

        btnEliminar.addActionListener(e -> {
            int fila = table.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un cliente de la tabla.");
                return;
            }

            int id = (int) model.getValueAt(fila, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar cliente ID " + id + " de MySQL?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (clienteDAO.eliminar(id)) {
                    JOptionPane.showMessageDialog(this, "Cliente eliminado de MySQL.");
                    if (recargarTodo != null) recargarTodo.run();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar cliente de MySQL.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cargarTabla.run();
        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        registrarRecargaClientes(cargarTabla);
        return panel;
    }

    // --- MÓDULO 3: ALQUILERES ---
    private JComboBox<Pelicula> cbPeliculas = new JComboBox<>();
    private JComboBox<Cliente> cbClientes = new JComboBox<>();
    private Runnable cargarTablaPeliculasRef;
    private Runnable cargarTablaClientesRef;

    private JPanel crearPanelAlquileres() {
        JPanel panel = new JPanel(new BorderLayout());
        JButton btnPrestar = new JButton("Registrar Alquiler");
        JButton btnEliminar = new JButton("Eliminar Seleccionado");

        JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Gestión de Alquileres"));
        form.add(new JLabel("Seleccionar Película:")); form.add(cbPeliculas);
        form.add(new JLabel("Seleccionar Cliente:")); form.add(cbClientes);
        form.add(btnPrestar); form.add(btnEliminar);

        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Película", "Cliente", "Fecha Alquiler"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);

        Runnable cargarModulo = () -> {
            cbPeliculas.removeAllItems();
            for (Pelicula p : peliculaDAO.listar()) cbPeliculas.addItem(p);

            cbClientes.removeAllItems();
            for (Cliente c : clienteDAO.listar()) cbClientes.addItem(c);

            model.setRowCount(0);
            for (Alquiler a : alquilerDAO.listar()) {
                model.addRow(new Object[]{a.getId(), a.getTituloPelicula(), a.getNombreCliente(), a.getFechaAlquiler()});
            }
        };

        recargarTodo = () -> {
            if (cargarTablaPeliculasRef != null) cargarTablaPeliculasRef.run();
            if (cargarTablaClientesRef != null) cargarTablaClientesRef.run();
            cargarModulo.run();
        };

        btnPrestar.addActionListener(e -> {
            Pelicula p = (Pelicula) cbPeliculas.getSelectedItem();
            Cliente c = (Cliente) cbClientes.getSelectedItem();

            if (p == null || c == null) {
                JOptionPane.showMessageDialog(this, "Debe registrar películas y clientes primero.");
                return;
            }

            Alquiler a = new Alquiler(p.getId(), c.getId(), Date.valueOf(LocalDate.now()));
            if (alquilerDAO.registrar(a)) {
                JOptionPane.showMessageDialog(this, "Alquiler registrado con éxito en MySQL.");
                recargarTodo.run();
            }
        });

        btnEliminar.addActionListener(e -> {
            int fila = table.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un alquiler de la tabla.");
                return;
            }

            int id = (int) model.getValueAt(fila, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar registro de alquiler ID " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (alquilerDAO.eliminar(id)) {
                    JOptionPane.showMessageDialog(this, "Alquiler eliminado de MySQL.");
                    recargarTodo.run();
                }
            }
        });

        cargarModulo.run();
        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void registrarRecargaPeliculas(Runnable r) { this.cargarTablaPeliculasRef = r; }
    private void registrarRecargaClientes(Runnable r) { this.cargarTablaClientesRef = r; }
}