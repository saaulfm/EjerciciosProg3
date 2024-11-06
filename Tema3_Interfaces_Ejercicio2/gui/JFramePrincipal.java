package gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import domain.Comic;
import domain.Personaje.Editorial;

public class JFramePrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
		
	private List<Comic> comics;
	
	private JTable tablaComics;
	private DefaultTableModel modeloDatosComics;
	private JTable tablaPersonajes;
	private DefaultTableModel modeloDatosPersonajes;
	private JScrollPane scrollPanePersonajes;
	
	public JFramePrincipal(List<Comic> comics) {
		//Asignamos la lista de comics a la varaible local
		this.comics = comics;

		//Se inicializan las tablas y sus modelos de datos
		this.initTables();
		//Se cargan los comics en la tabla de comics
		this.loadComics();
		
		//La tabla de comics se inserta en un panel con scroll
		JScrollPane scrollPaneComics = new JScrollPane(this.tablaComics);
		scrollPaneComics.setBorder(new TitledBorder("Comics"));
		this.tablaComics.setFillsViewportHeight(true);
		
		//La tabla de personajes se inserta en otro panel con scroll
		this.scrollPanePersonajes = new JScrollPane(this.tablaPersonajes);
		this.scrollPanePersonajes.setBorder(new TitledBorder("Personajes"));		
		this.tablaPersonajes.setFillsViewportHeight(true);
		
		//El Layout del panel principal es un matriz con 2 filas y 1 columna
		this.getContentPane().setLayout(new GridLayout(2, 1));
		this.getContentPane().add(scrollPaneComics);
		this.getContentPane().add(this.scrollPanePersonajes);
		
		this.setTitle("Ventana principal de Comics");		
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
		this.setVisible(true);		
	}
	
	private void initTables() {
		//Cabecera del modelo de datos
		Vector<String> cabeceraComics = new Vector<String>(Arrays.asList( "ID", "EDITORIAL", "TÍTULO", "PERSONAJES"));
		//Se crea el modelo de datos para la tabla de comics sólo con la cabecera
		this.modeloDatosComics = new DefaultTableModel(new Vector<Vector<Object>>(), cabeceraComics);
		//Se crea la tabla de comics con el modelo de datos
		this.tablaComics = new JTable(this.modeloDatosComics);
		
		// TAREA 1: Renderizar la editorial como una imagen con el logo de DC o MARVEL y añadir el nombre como ToolTipText
	    TableCellRenderer cellRenderer = (table, value, isSelected, hasFocus, row, column) -> {
	        JLabel result = new JLabel(value != null ? value.toString() : "");

	        if (value instanceof Editorial) {
	            Editorial e = (Editorial) value;
	            result.setText("");
	            result.setToolTipText(e.toString()); // ToolTip con el nombre de la editorial
	            result.setHorizontalAlignment(JLabel.CENTER);

	            // Colocar imagen según la editorial
	            switch (e) {
	                case MARVEL:
	                    result.setIcon(new ImageIcon("images/MARVEL.png"));
	                    break;
	                case DC:
	                    result.setIcon(new ImageIcon("images/DC.png"));
	                    break;
	                default:
	                    break;
	            }
	        } else if (value instanceof Number) {
	            // TAREA 5: Alinear números al centro
	            result.setHorizontalAlignment(JLabel.CENTER);
	        } else {
	            // TAREA 5: Alinear texto a la izquierda
	            result.setHorizontalAlignment(JLabel.LEFT);
	        }
	        
	        // TAREA 4: Aplicar color de selección cuando la celda esté seleccionada
	        if (isSelected) {
	            result.setBackground(table.getSelectionBackground()); // Color de fondo de selección
	            result.setForeground(table.getSelectionForeground()); // Color de texto de selección
	        } else {
	            // TAREA 3: Colorear filas pares e impares con colores específicos
	            if (row % 2 == 0) {
	                result.setBackground(new Color(250, 249, 249)); // Color de fondo para filas pares
	            } else {
	                result.setBackground(new Color(190, 227, 219)); // Color de fondo para filas impares
	            }
	            result.setForeground(table.getForeground()); // Color de texto normal
	        }

	        result.setOpaque(true);
	        return result;
	    };
	    
	 // TAREA 5: Renderer para la cabecera, alineando columnas de texto a la izquierda y numéricas al centro
	    TableCellRenderer headerRenderer = (table, value, isSelected, hasFocus, row, column) -> {
	        JLabel header = new JLabel(value.toString());
	        if ("ID".equals(value) || "EDITORIAL".equals(value)) {
	            header.setHorizontalAlignment(JLabel.CENTER); // Alinear columnas numéricas al centro
	        } else {
	            header.setHorizontalAlignment(JLabel.LEFT); // Alinear texto a la izquierda
	        }
	        header.setOpaque(true);
	        header.setBackground(table.getTableHeader().getBackground());
	        header.setForeground(table.getTableHeader().getForeground());
	        return header;
	    };
	    
	    // Aplicar renderer de las TAREAS 1, 3, 4 y 5
	    this.tablaComics.setDefaultRenderer(Object.class, cellRenderer);
	    this.tablaComics.getTableHeader().setDefaultRenderer(headerRenderer);
	    
	    // TAREA 2: Modificar la altura de todas las filas de la tabla a 26 píxeles
	    this.tablaComics.setRowHeight(26);
	    
	    // TAREA 6: Redimensionar la columna "TÍTULO" a 400 píxeles
	    this.tablaComics.getColumnModel().getColumn(2).setPreferredWidth(400);
		
		//Se modifica el modelo de selección de la tabla para que se pueda selecciona únicamente una fila
		this.tablaComics.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// Aplicar renderer de la TAREA 1
		this.tablaComics.setDefaultRenderer(Object.class, cellRenderer); // Aplicar renderer de TAREA 1
		//Se define el comportamiento el evento de selección de una fila de la tabla
		this.tablaComics.getSelectionModel().addListSelectionListener(e -> {
			//Cuando se selecciona una fila, se actualiza la tabla de personajes
			this.loadPersonajes(this.comics.get((int)tablaComics.getValueAt(tablaComics.getSelectedRow(), 0)-1));
		});
		
		//Cabecera del modelo de datos
		Vector<String> cabeceraPersonajes = new Vector<String>(Arrays.asList( "ID", "EDITORIAL", "NOMBRE", "EMAIL"));
		//Se crea el modelo de datos para la tabla de comics sólo con la cabecera
		this.modeloDatosPersonajes = new DefaultTableModel(new Vector<Vector<Object>>(), cabeceraPersonajes);
		//Se crea la tabla de personajes con el modelo de datos
		this.tablaPersonajes = new JTable(this.modeloDatosPersonajes);
	}
	
	private void loadComics() {
		//Se borran los datos del modelo de datos
		this.modeloDatosComics.setRowCount(0);
		//Se añaden los comics uno a uno al modelo de datos
		this.comics.forEach(c -> this.modeloDatosComics.addRow(
				new Object[] {c.getId(), c.getEditorial(), c.getTitulo(), c.getPersonajes().size()} )
		);
	}
	
	private void loadPersonajes(Comic comic) {
		//Se borran los datos del modelo de datos
		this.modeloDatosPersonajes.setRowCount(0);

		//Se añaden los personajes uno a uno al modelo de datos
		comic.getPersonajes().forEach(p -> this.modeloDatosPersonajes.addRow(
				new Object[] {p.getId(), p.getEditorial(), p.getNombre(), p.getEmail()} )
		);
		
		//Se modifica el texto del bode de la lista de personajes 
		this.scrollPanePersonajes.setBorder(new TitledBorder(String.format("Personajes del comic '%s' [%d]",
				comic.getTitulo(), comic.getPersonajes().size())));
	}
}