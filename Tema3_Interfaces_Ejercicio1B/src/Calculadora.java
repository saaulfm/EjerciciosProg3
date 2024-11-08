import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Calculadora extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField display;
	
	public Calculadora() {
		// Configuración básica de la ventana
    	setTitle("Calculadora Básica");
        setSize(300, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10)); // Establecer layout de 10 píxeles de separación
        
        // Crear el display de la calculadora
        display = new JTextField();
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setFont(new Font("Courier", Font.BOLD, 24));
        add(display, BorderLayout.NORTH);
        
        // Crear panel para los botones numéricos y operadores
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        buttonPanel.setLayout(new GridLayout(4, 4, 10, 10)); // Cuadrícula 4x4 y separación lateral y horizontal de 10px
        
        // Crear texto mostrado en los botones
        String[] buttonText = {
        	"7", "8", "9", "/",
        	"4", "5", "6", "*",
        	"1", "2", "3", "-",
        	"0", ".", "=", "+"
        };
        
        // Brear botones y añadir al panel
        for(String text: buttonText) {
        	JButton button = new JButton(text);
        	button.setFont(new Font("Arial", Font.PLAIN, 18)); // Fuente de los botones
            button.setBackground(Color.RED); // Fondo de los botones
            button.setForeground(Color.BLACK); // Color del texto de los botones
            // Listener al botón
            button.addActionListener((e) -> {
            	JButton botonPulsado = (JButton) e.getSource();
            	String symbol = botonPulsado.getText();
            	String newText = display.getText() + " " + symbol;
            	display.setText(newText);
            });
        	buttonPanel.add(button);
        }
        
        // Añadir los botones al panel central
        add(buttonPanel, BorderLayout.CENTER);
        
        // Hacer visible la ventana
        setVisible(true);
	}
	
	public static void main(String[] args) {
		// Crear ventana en el hilo de eventos de swing
		SwingUtilities.invokeLater(() -> {
			@SuppressWarnings("unused")
			Calculadora calculadora = new Calculadora();
		});
		
	}
	
}
