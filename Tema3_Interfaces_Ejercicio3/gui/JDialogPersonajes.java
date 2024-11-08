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
		
		if (comic != null) {
			//Se ordena la lista de personajes del comic actual alfabéticamente
			Collections.sort(comic.getPersonajes(), (p1, p2) -> {
				return p1.getNombre().compareToIgnoreCase(p2.getNombre());
			});
			
			//Se crea el modelo de datos para el JList de personajes del comic
			DefaultListModel<Personaje> personajesComicModel = new DefaultListModel<>();
			this.comic.getPersonajes().forEach(p -> {
				personajesComicModel.addElement(p);
			});
	
			this.listPersonajesComic = new JList<Personaje>(personajesComicModel);
			scrollPaneComic = new JScrollPane(this.listPersonajesComic);
			scrollPaneComic.setBorder(new TitledBorder("Personajes del comic '" + comic.getTitulo() + "'"));
		}
		
		JPanel panelCentral = new JPanel();
		panelCentral.add(btnAdd);
		panelCentral.add(btnRemove);
		
		//Se cargan las listas de personajes
		cargarListasPersonajes();
		
		// TAREA 9: Acción para el botón "Añadir >" - Mover personaje de la lista de personajes existentes
		btnAdd.addActionListener((e) -> {
			if (!listPersonajesTodos.isSelectionEmpty()) {
				Personaje pSeleccionado = listPersonajesTodos.getSelectedValue();
				personajesTodosModel.removeElement(pSeleccionado);
				personajesComicModel.addElement(pSeleccionado);
						
				personajes.remove(pSeleccionado);
				comic.getPersonajes().add(pSeleccionado);
						
				//Se cargan de nuevo las listas para ordenarlas alfabéticamente
				cargarListasPersonajes();
			}
		});
		        
		// TAREA 9: Acción para el botón "< Eliminar" - Mover personaje de la lista del cómic a la lista de personajes disponibles
		btnRemove.addActionListener((e) -> {
			if (!listPersonajesComic.isSelectionEmpty()) {
				Personaje pSeleccionado = listPersonajesComic.getSelectedValue();
				personajesComicModel.removeElement(pSeleccionado);
				personajesTodosModel.addElement(pSeleccionado);
				
				comic.getPersonajes().remove(pSeleccionado);
				personajes.add(pSeleccionado);
				
				//Se cargan de nuevo las listas para ordenarlas alfabéticamente
				cargarListasPersonajes();
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
