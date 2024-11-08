package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import domain.Comic;
import domain.Personaje;

public class JDialogPersonajes extends JDialog {

	private static final long serialVersionUID = 1L;

	private List<Personaje> personajes;
	private Comic comic;
	private JList<Personaje> listPersonajesTodos;
	private DefaultListModel<Personaje> personajesTodosModel; // *Modelo de personajes
	private JScrollPane scrollPaneTodos;
	private JList<Personaje> listPersonajesComic;
	private DefaultListModel<Personaje> personajesComicModel; // *Modelo de cómics
	private JScrollPane scrollPaneComic;
	private JButton btnAdd = new JButton("Añadir >");
	private JButton btnRemove = new JButton("< Eliminar");
	
	public JDialogPersonajes(List<Personaje> personajes, Comic comic) {
		this.personajes = personajes;
		this.comic = comic;
		
		// TAREA 9
		//Se crea el modelo de datos para el JList de todos los personajes
		personajesTodosModel = new DefaultListModel<>();
		this.listPersonajesTodos = new JList<Personaje>(personajesTodosModel);
		this.listPersonajesTodos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);			
		JScrollPane scrollPaneTodos = new JScrollPane(this.listPersonajesTodos);		
		scrollPaneTodos.setBorder(new TitledBorder("Personajes existentes"));
		scrollPaneTodos.setPreferredSize(new Dimension(380, 250));
				
		//Se crea el modelo de datos para el JList de personajes del comic
		personajesComicModel = new DefaultListModel<>();
		this.listPersonajesComic = new JList<Personaje>(personajesComicModel);
		this.listPersonajesComic.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPaneComic = new JScrollPane(this.listPersonajesComic);			
		scrollPaneComic.setBorder(new TitledBorder("Personajes del comic '" + comic.getTitulo() + "'"));
		scrollPaneComic.setPreferredSize(new Dimension(380, 250));
		
		if (personajes != null) {
			//Se ordena la lista de personajes totales alfabéticamente
			Collections.sort(personajes, (p1, p2) -> {
				return p1.getNombre().compareToIgnoreCase(p2.getNombre());
			});
			
			//Se crea el modelo de datos para el JList de todo los personajes
			DefaultListModel<Personaje> personajesTodosModel = new DefaultListModel<>();
			this.personajes.forEach(p -> {
				personajesTodosModel.addElement(p);
			});
		
			this.listPersonajesTodos = new JList<Personaje>(personajesTodosModel);
			scrollPaneTodos = new JScrollPane(this.listPersonajesTodos);
			scrollPaneTodos.setBorder(new TitledBorder("Personajes existentes"));
		}
		
		// TAREA 9: Ordenar la lista de personajes existentes alfabéticamente
        if (personajes != null) {
            Collections.sort(personajes, (p1, p2) -> {
                return p1.getNombre().compareToIgnoreCase(p2.getNombre());
            });
            personajes.forEach(p -> personajesTodosModel.addElement(p)); // **Añadimos personajes a la lista existente
        }

        // TAREA 9: Ordenar la lista de personajes del comic alfabéticamente
        if (comic != null) {
            Collections.sort(comic.getPersonajes(), (p1, p2) -> {
                return p1.getNombre().compareToIgnoreCase(p2.getNombre());
            });
            comic.getPersonajes().forEach(p -> personajesComicModel.addElement(p)); // **Añadimos personajes del cómic a la lista existente
        }
			
		//Se crea el modelo de datos para el JList de personajes del comic
		DefaultListModel<Personaje> personajesComicModel = new DefaultListModel<>();
		this.comic.getPersonajes().forEach(p -> {
			personajesComicModel.addElement(p);
		});
	
		this.listPersonajesComic = new JList<Personaje>(personajesComicModel);
		scrollPaneComic = new JScrollPane(this.listPersonajesComic);
		scrollPaneComic.setBorder(new TitledBorder("Personajes del comic '" + comic.getTitulo() + "'"));

		
		JPanel panelCentral = new JPanel();
		panelCentral.add(btnAdd);
		panelCentral.add(btnRemove);
		
		//Se cargan las listas de personajes
		cargarListasPersonajes();
		
		// TAREA 9: Acción para el botón "Añadir >" - Mover personaje de la lista de personajes existentes
		btnAdd.addActionListener((e) -> {
		    if (!listPersonajesTodos.isSelectionEmpty()) {
		        Personaje pSeleccionado = listPersonajesTodos.getSelectedValue();
		        
		        // Verificar si el personaje ya está en el cómic
		        if (!comic.getPersonajes().contains(pSeleccionado)) {
		            // Eliminar de personajes disponibles (panel izquierdo)
		            personajesTodosModel.removeElement(pSeleccionado); 
		            // Añadir al cómic (panel derecho)
		            personajesComicModel.addElement(pSeleccionado); 
		            personajes.remove(pSeleccionado); // Eliminar de la lista de personajes disponibles
		            comic.getPersonajes().add(pSeleccionado); // Añadir al cómic
		            
		            // Recargar listas para ordenarlas
		            cargarListasPersonajes();
		        } else {
		            JOptionPane.showMessageDialog(this, 
		                "Este personaje ya está en el cómic.", 
		                "Advertencia", 
		                JOptionPane.WARNING_MESSAGE);
		        }
		    }
		});
		        
		// TAREA 9: Acción para el botón "< Eliminar" - Mover personaje de la lista del cómic a la lista de personajes disponibles
		btnRemove.addActionListener((e) -> {
		    if (!listPersonajesComic.isSelectionEmpty()) {
		        Personaje pSeleccionado = listPersonajesComic.getSelectedValue();
		        
		        // Verificar si el personaje está en el cómic
		        if (comic.getPersonajes().contains(pSeleccionado)) {
		            // Confirmación antes de eliminar
		            int respuesta = JOptionPane.showConfirmDialog(this,
		                    "¿Está seguro que desea eliminar a este personaje del cómic?", 
		                    "Confirmar eliminación", 
		                    JOptionPane.YES_NO_OPTION,
		                    JOptionPane.QUESTION_MESSAGE);

		            if (respuesta == JOptionPane.YES_OPTION) {
		                // Eliminar del cómic (panel derecho)
		                personajesComicModel.removeElement(pSeleccionado); 
		                // Añadir a personajes disponibles (panel izquierdo)
		                personajesTodosModel.addElement(pSeleccionado); 
		                comic.getPersonajes().remove(pSeleccionado); // Eliminar del cómic
		                personajes.add(pSeleccionado); // Añadir a la lista de personajes disponibles
		                
		                // Recargar listas para ordenarlas
		                cargarListasPersonajes();
		            }
		        } else {
		            JOptionPane.showMessageDialog(this, 
		                "Este personaje no está en el cómic.", 
		                "Advertencia", 
		                JOptionPane.WARNING_MESSAGE);
		        }
		    }
		}); 	
		
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(scrollPaneTodos, BorderLayout.WEST);
		this.getContentPane().add(scrollPaneComic, BorderLayout.EAST);
		this.getContentPane().add(panelCentral, BorderLayout.CENTER);		
		
		this.setTitle("Modificar personajes del comic '" + comic.getTitulo() + "'");
		this.setSize(1200, 300);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	// TAREA 9: Método para cargar lista de personajes
	private void cargarListasPersonajes() {		
		//Se ordena la lista de personajes totales por nombre
		Collections.sort(personajes, (p1, p2) -> {
			return p1.getNombre().compareToIgnoreCase(p2.getNombre());
		});
		
		personajesTodosModel.clear();
		
		this.personajes.forEach(p -> {
			personajesTodosModel.addElement(p);
		});
		
		//Se ordena la lista de personajes del comic actual alfabéticamente
		Collections.sort(comic.getPersonajes(), (p1, p2) -> {
			return p1.getNombre().compareToIgnoreCase(p2.getNombre());
		});
		
		personajesComicModel.clear();
		
		this.comic.getPersonajes().forEach(p -> {
			personajesComicModel.addElement(p);
		});
	}
}
