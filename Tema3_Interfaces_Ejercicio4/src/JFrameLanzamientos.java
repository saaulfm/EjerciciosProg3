
import java.awt.BorderLayout;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;

import domain.RocketLaunch;

public class JFrameLanzamientos extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public JFrameLanzamientos(Map<String, List<RocketLaunch>> launches) {
		JTree jTreeCompanies = new JTree();		
		JScrollPane scrollPane = new JScrollPane(jTreeCompanies);
		scrollPane.setBorder(new TitledBorder("Rocket Launches"));
		
		this.setTitle("Rocket Launches GUI");
		this.setLayout(new BorderLayout(0, 0));		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(scrollPane, BorderLayout.CENTER);

		this.setSize(800, 600);
		this.setLocationRelativeTo(null);		
		this.setVisible(true);		
	}	
}
