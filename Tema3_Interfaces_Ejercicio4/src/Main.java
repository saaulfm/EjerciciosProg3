
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

public class Main {

	public static void main(String[] args) {
		try {			
			//Carga de los datos desde la BBDD
			DBManager dbManager = new DBManager();
			dbManager.connect();			
			List<RocketLaunch> launches = dbManager.getAllLaunches();
			dbManager.disconnect();
						
			Map<String, List<RocketLaunch>> launchesMap = new HashMap<>();
			
			SwingUtilities.invokeLater(() -> new JFrameLanzamientos(launchesMap));			
		} catch (Exception ex) {
			System.err.println();
		}
	}
}