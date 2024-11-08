package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import domain.Comic;
import domain.Personaje;
import domain.Personaje.Editorial;

public class JFramePrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
		
	private List<Comic> comics;
	private int mouseRowPersonajes = -1; // TAREA 4.1
	
	private JTable tablaComics;
	private DefaultTableModel modeloDatosComics;
	private JTable tablaPersonajes;
	private DefaultTableModel modeloDatosPersonajes;
	private JScrollPane scrollPanePersonajes;
	private JTextField txtFiltro;
	
	public JFramePrincipal(List<Comic> comics) {
		//Asignamos la lista de comics a la varaible local
		this.comics = new ArrayList<>(comics); // *Convertimos la lista inmutable en mutable (TAREA 5)

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
				
		//Se define el comportamiento del campo de texto del filtro
		this.txtFiltro = new JTextField(20);
		
		// TAREA 3.1: Se añade un listener para detectar cambios en el campo de texto
		// Implementar un DocumentListener y añadirlo al JTextfield.getDocument().addDocumentListener()
		this.txtFiltro.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				filtrarComics();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				filtrarComics();
			}

			@Override
			public void changedUpdate(DocumentEvent e) { }			
		});
				
		JPanel panelFiltro = new JPanel();
		panelFiltro.add(new JLabel("Filtro por título: "));
		panelFiltro.add(txtFiltro);
		
		JPanel panelComics = new JPanel();
		panelComics.setLayout(new BorderLayout());
		panelComics.add(BorderLayout.CENTER, scrollPaneComics);
		panelComics.add(BorderLayout.NORTH, panelFiltro);	
				
		// TAREA 4.2: MouseMotionAdapter para detectar los movimientos del ratón sobre la tabla de personajes
		this.tablaPersonajes.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				// TAREA 4.2: Actualizar la fila sobre la que está el ratón
				mouseRowPersonajes = tablaPersonajes.rowAtPoint(e.getPoint());
				// Forzar el redibujado de la tabla
				tablaPersonajes.repaint(); // TAREA 4.6
			}			
		});
				
		// TAREA 4.3: MouseAdapter para detectar cuando el ratón sale de la tabla de personajes
		this.tablaPersonajes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				// Cuando el ratón sale de la tabla, se resetea la fila a -1
				mouseRowPersonajes = -1;
			}
		});
		
		// TAREA 5
		// Añadir los listeners
		this.tablaPersonajes.addKeyListener(myKeyListener);
		this.tablaComics.addKeyListener(myKeyListener);
		this.txtFiltro.addKeyListener(myKeyListener);
		
		this.tablaPersonajes.addKeyListener(myKeyListener);
		this.tablaComics.addKeyListener(myKeyListener);
		
		//El Layout del panel principal es un matriz con 2 filas y 1 columna
		this.getContentPane().setLayout(new GridLayout(2, 1));
		this.getContentPane().add(panelComics);
		this.getContentPane().add(this.scrollPanePersonajes);
		
		this.setTitle("Ventana principal de Comics");		
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
		this.setVisible(true);		
	}
	
	// Listener para los eventos de teclado
	KeyListener myKeyListener = new KeyListener() {
		@Override
		public void keyTyped(KeyEvent e) { }

		// TAREA 5: Al pulsar la combinación de teclas CTRL + C, abre un cuadro de diálogo para añadir un nuevo comic
		@Override
		public void keyPressed(KeyEvent e) {
			// CTRL + C - Creación de un nuevo comic
			if (e.getKeyCode() == KeyEvent.VK_C && e.isControlDown()) {
                // TAREA 6.1: Crear JComboBox para seleccionar la editorial
				JComboBox<Editorial> jcomoEditorial = new JComboBox<>(Editorial.values());
                // TAREA 6.2: Personalizar el JComboBox para mostrar imágenes de las editoriales
				jcomoEditorial.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
					JLabel result = new JLabel();
						
					Editorial editorial = (Editorial) value;
					
					switch (editorial) { 
						case MARVEL:
							result.setIcon(new ImageIcon(getClass().getResource("/images/MARVEL.png")));
							break;
						case DC:
							result.setIcon(new ImageIcon(getClass().getResource("/images/DC.png")));
							break;
						default:
					}
					
					if (isSelected) {
						result.setBackground(list.getSelectionBackground());
						result.setForeground(list.getSelectionForeground());
					}
					
					return result;
				});
				
				//Se define el JTextField para el título
				JTextField txtTitulo = new JTextField(30);
				
				// TAREA 6.1: Personalizar el cuadro de diálogo con componentes
				JComponent[] inputs = new JComponent[] {
					new JLabel("Editorial: "),
					jcomoEditorial,
					new JLabel("Título: "),
					txtTitulo
				};	
						
						
				int result = JOptionPane.showConfirmDialog(null, inputs, 
						"Crear un nuevo comic", 
						JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE);
				
				// TAREA 7.1: Verificar si se pulsó OK
				if (result == JOptionPane.OK_OPTION) {
					// TAREA 7.2: Crear el nuevo comic
					if (!txtTitulo.getText().isEmpty()) {
						// TAREA 7.3: Añadir el comic a la lista
						Comic comic = new Comic(comics.size()+1, (Editorial) jcomoEditorial.getSelectedItem(), txtTitulo.getText());
						comics.add(comic);
						//Se borra el filtro
						txtFiltro.setText("");
						//Se cargan de nuevo los comics
						loadComics();
					} else {
						JOptionPane.showMessageDialog(null, "El título no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
					}						
				}
			// TAREA 8: Si está seleccionado un comic en la tabla de comics y se pulsa la combinación de teclas  CTRL + P se abre un cuadro de diálogo para añadir personajes al comic.	
			} else if (tablaComics.getSelectedRow() != -1 && e.getKeyCode() == KeyEvent.VK_P && e.isControlDown()) {					
				Comic comic = comics.get((int) tablaComics.getValueAt(tablaComics.getSelectedRow(), 0) - 1);
				List<Personaje> personajes = new ArrayList<>();
				
				//Se recorren todos los comics
				comics.forEach(c -> {
					//Se recorren todos los personajes del comic
					c.getPersonajes().forEach(p -> {
						//Se añaden a la lista de personajes sin repetición
						if (!personajes.contains(p) && !comic.getPersonajes().contains(p)) {
							personajes.add(p);
						}
					});		
				});					
				
				//Se crea el cuadro de diálogo de modificación de personajes.
				new JDialogPersonajes(personajes, comic);
				
				//Se borra el filtro
				txtFiltro.setText("");
				//Se cargan de nuevo los comics para actualizar el número de personajes
				loadComics();
									
				//Se carga de nuevo la tabla de personajes
				loadPersonajes(comic);
			}
		}

		@Override
		public void keyReleased(KeyEvent e) { }
	};	
	
	private void initTables() {
		//Cabecera del modelo de datos
		Vector<String> cabeceraComics = new Vector<String>(Arrays.asList( "ID", "EDITORIAL", "TÍTULO", "Personajes"));
		//Se crea el modelo de datos para la tabla de comics sólo con la cabecera
		this.modeloDatosComics = new DefaultTableModel(new Vector<Vector<Object>>(), cabeceraComics) {
			private static final long serialVersionUID = 1L;

			// TAREA 1: Bloquear la edición de las columnas "ID" y "Personajes" en la tabla de Comics
			@Override
			public boolean isCellEditable(int row, int column) {
				return column != 0 && column !=3;
			}
		};
		
		//Se crea la tabla de comics con el modelo de datos
		this.tablaComics = new JTable(this.modeloDatosComics);
		
		// *** CAMBIO DE SITIO de abajo a arriba ***
		//Cabecera del modelo de datos
		Vector<String> cabeceraPersonajes = new Vector<String>(Arrays.asList( "ID", "EDITORIAL", "NOMBRE", "EMAIL"));
		//Se crea el modelo de datos para la tabla de comics sólo con la cabecera
		this.modeloDatosPersonajes = new DefaultTableModel(new Vector<Vector<Object>>(), cabeceraPersonajes) {
			private static final long serialVersionUID = 1L;
					
			// TAREA 2: Bloquear la edición de todas las columnas en la tabla de Personajes
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}	
		};
				
		//Se crea la tabla de personajes con el modelo de datos
		this.tablaPersonajes = new JTable(this.modeloDatosPersonajes);
		
		
		//Se define un CellRenderer para las celdas de las dos tabla usando una expresión lambda
		TableCellRenderer cellRenderer = (table, value, isSelected, hasFocus, row, column) -> {
			JLabel result = new JLabel(value.toString());
						
			//Si el valor es de tipo Editorial: se renderiza con la imagen centrada
			if (value instanceof Editorial) {
				Editorial e = (Editorial) value;
				
				result.setText("");		
				result.setToolTipText(e.toString());
				result.setHorizontalAlignment(JLabel.CENTER);
				
				switch (e) { 
					case MARVEL:
						result.setIcon(new ImageIcon("images/MARVEL.png"));
						break;
					case DC:
						result.setIcon(new ImageIcon("images/DC.png"));
						break;
					default:
				}
			//Si el valor es numérico se renderiza centrado
			} else if (value instanceof Number) {
				result.setHorizontalAlignment(JLabel.CENTER);
			} else {
				//Si el valor es texto pero representa un número se renderiza centrado
				try {
					Integer.parseInt(value.toString());
					result.setHorizontalAlignment(JLabel.CENTER);				
				} catch(Exception ex) {
					result.setText(value.toString());
				}
				// TAREA 4.5: Resaltar la fila cuando la fila del ratón es igual a la fila actual
				if (table.equals(tablaPersonajes)) {
					if (row == mouseRowPersonajes) { 
						result.setBackground(new Color(220, 220, 220)); // Resaltar fila
					} else {
						result.setBackground(table.getBackground());
					}
				}
				
				// TAREA 3.2: Resaltar el texto del filtro en la columna "Título"
				if (table.equals(tablaComics)) {
					String filter = txtFiltro.getText();
					String txtValue = value.toString();
					StringBuffer txtHtml = new StringBuffer();						
					String txtResaltado;
					
					if (isSelected) {
						txtResaltado = "<strong style='background-color:white; color: red;'>" + filter + "</strong>";
					} else {
						txtResaltado = "<strong style='background-color:yellow; color: blue;'>" + filter + "</strong>";
					}
											
					txtHtml.append("<html>");
					txtHtml.append(txtValue.substring(0, txtValue.indexOf(filter)));
					txtHtml.append(txtResaltado);
					txtHtml.append(txtValue.substring(txtValue.indexOf(filter) + filter.length(), txtValue.length()));
					txtHtml.append("</html");
					
					result.setText(txtHtml.toString());
				}
			}		
			
			//La filas pares e impares se renderizan de colores diferentes de la tabla de comics			
			if (table.equals(tablaComics)) {
				if (row % 2 == 0) {
					result.setBackground(new Color(250, 249, 249));
				} else {
					result.setBackground(new Color(190, 227, 219));
				}
			//Se usan los colores por defecto de la tabla para las celdas de la tabla de personajes
			} else {
				result.setBackground(table.getBackground());
				result.setForeground(table.getForeground());
			}
			
			// tarea 4.5: Si la fila es igual a la fila sobre la que está el ratón, se cambia el color de fondo
		    if (row == mouseRowPersonajes) {
		        result.setBackground(table.getSelectionBackground());
		        result.setForeground(table.getSelectionForeground());
		    }
			
			//Si la celda está seleccionada se renderiza con el color de selección por defecto
			if (isSelected) {
				result.setBackground(table.getSelectionBackground());
				result.setForeground(table.getSelectionForeground());			
			}
			
			
			result.setOpaque(true);
			
			return result;
		};
		
		//Se define un CellRenderer para las cabeceras de las dos tabla usando una expresión lambda
		TableCellRenderer headerRenderer = (table, value, isSelected, hasFocus, row, column) -> {
			JLabel result = new JLabel(value.toString());			
			result.setHorizontalAlignment(JLabel.CENTER);
			
			switch (value.toString()) {
				case "TÍTULO":
				case "NOMBRE":
				case "EMAIL":
					result.setHorizontalAlignment(JLabel.LEFT);
			}
			
			result.setBackground(table.getBackground());
			result.setForeground(table.getForeground());
			
			result.setOpaque(true);
			
			return result;
		};
		
		//Se crea un CellEditor a partir de un JComboBox()
		JComboBox<Editorial> jComboEditorial = new JComboBox<>(Editorial.values());		
		DefaultCellEditor editorialEditor = new DefaultCellEditor(jComboEditorial);
		
		//Se define la altura de las filas de la tabla de comics
		this.tablaComics.setRowHeight(26);
		
		//Se deshabilita la reordenación de columnas
		this.tablaComics.getTableHeader().setReorderingAllowed(false);
		//Se deshabilita el redimensionado de las columna
		this.tablaComics.getTableHeader().setResizingAllowed(false);
		//Se definen criterios de ordenación por defecto para cada columna
		this.tablaComics.setAutoCreateRowSorter(true);
		
		//Se establecen los renderers al la cabecera y el contenido
		this.tablaComics.getTableHeader().setDefaultRenderer(headerRenderer);		
		this.tablaComics.setDefaultRenderer(Object.class, cellRenderer);
		
		//Se establece el editor específico para la Editorial		
		this.tablaComics.getColumnModel().getColumn(1).setCellEditor(editorialEditor);
		
		//Se define la anchura de la columna Título
		this.tablaComics.getColumnModel().getColumn(2).setPreferredWidth(400);
		
		//Se modifica el modelo de selección de la tabla para que se pueda selecciona únicamente una fila
		this.tablaComics.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//Se define el comportamiento el evento de selección de una fila de la tabla
		this.tablaComics.getSelectionModel().addListSelectionListener(e -> {
			//Se obtiene el ID del comic de la fila seleccionada si es distinta de -1
			if (tablaComics.getSelectedRow() != -1) {
				this.loadPersonajes(this.comics.get((int) tablaComics.getValueAt(tablaComics.getSelectedRow(), 0) - 1));
			}
		});
				
		//Se deshabilita la reordenación de columnas
		this.tablaPersonajes.getTableHeader().setReorderingAllowed(false);
		//Se deshabilita el redimensionado de las columna
		this.tablaPersonajes.getTableHeader().setResizingAllowed(false);
		//Se definen criterios de ordenación por defecto para cada columna
		this.tablaPersonajes.setAutoCreateRowSorter(true);
		
		this.tablaPersonajes.getTableHeader().setDefaultRenderer(headerRenderer);		
		this.tablaPersonajes.setDefaultRenderer(Object.class, cellRenderer);
		this.tablaPersonajes.setRowHeight(26);
		this.tablaPersonajes.getColumnModel().getColumn(2).setPreferredWidth(200);
		this.tablaPersonajes.getColumnModel().getColumn(3).setPreferredWidth(200);
	}
	
	// TAREA 3: Filtra la tabla de comics a partir del valor que se introduzca en la caja de texto. 
	// A medida que se va introduciendo texto, los comics que aparezcen en la tabla deben incluir el texto del filtro en su título. 
	// Si no hay texto, aparecerán todos los comics. 
	private void filtrarComics() {
		//Se vacían las dos tablas
		this.modeloDatosComics.setRowCount(0);
		this.modeloDatosPersonajes.setRowCount(0);
		
		//Se añaden a la tabla sólo los comics que contengan el texto del filtro
		this.comics.forEach(c -> {
			if (c.getTitulo().contains(this.txtFiltro.getText())) {
				this.modeloDatosComics.addRow(
					new Object[] {c.getId(), c.getEditorial(), c.getTitulo(), c.getPersonajes().size()} );
			}
		});
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