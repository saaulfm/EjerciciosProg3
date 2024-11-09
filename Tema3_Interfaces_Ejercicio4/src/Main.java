
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import domain.RocketLaunch;

public class Main {

	public static void main(String[] args) {
		try {			
			//Carga de los datos desde la BBDD
			DBManager dbManager = new DBManager();
			dbManager.connect();			
			List<RocketLaunch> launches = dbManager.getAllLaunches();
			dbManager.disconnect();
						
			Map<String, List<RocketLaunch>> launchesMap = new HashMap<>();
			
			// TAREA 1: Inicializa el mapa de lanzaminetos por compañía
			launches.forEach(l -> {
				launchesMap.putIfAbsent(l.getCompany(), new ArrayList<>());				
				launchesMap.get(l.getCompany()).add(l);
			});

			SwingUtilities.invokeLater(() -> new JFrameLanzamientos(launchesMap));			
		} catch (Exception ex) {
			System.err.println();
		}
	}
}