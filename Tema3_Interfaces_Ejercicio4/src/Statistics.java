
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import domain.CountryLaunches;
import domain.RocketLaunch;
import domain.RocketLaunch.Status;

public class Statistics {

	/**
	 * Obtiene la lista compañías que realizan lanzamientos en orden alfabético inverso.
	 * @param launches List<RocketLaunch> con los lanzamientos.
	 * @return List<String> con los nombres de las compañías en orden alfabético inverso.
	 */
	public List<String> getCompanies(List<RocketLaunch> launches) {
		List<String> companies = new ArrayList<>();
		
		//Se recorres la lista de lanzamientos.
		launches.forEach(launch -> {
			//Si la compañía no está en la lista de compañías.
			if (!companies.contains(launch.getCompany())) {
				//Se añade a la lista.
				companies.add(launch.getCompany());
			}
		});
		
		//Se ordena la lista en orden inverso a la ordenación por defecto: [Z ->A ]
		Collections.sort(companies, Collections.reverseOrder());
		
		return companies;
	}
	
	/**
	 * Obtiene el porcentaje de lanzamientos exitosos por compañía.
	 * @param launches List<RocketLaunch> con los lanzamientos.
	 * @return Map<String, Float> con los ratios éxito en los lanzamientos para cada compañía
	 */
	public Map<String, Float> getSuccessRatios(List<RocketLaunch> launches) {
		Map<String, Float> sucessRatios = new HashMap<>();
		//Mapa auxiliar para almacenar los contadores para cada compañía
		//Número de lanzamientos exitosos y total de lanzamientos
		Map<String, List<Integer>> launchRatios = new HashMap<>();
		
		//Se recorre la lista de lanzamientos
		launches.forEach(launch -> {			
			//Se añade una entrada al mapa con valor 0 para los dos contadores.
			launchRatios.putIfAbsent(launch.getCompany(), new ArrayList<>(Arrays.asList(0, 0)));			
			//Se incrementa el número de lanzamientos totales (posición 1 de la lista)
			launchRatios.get(launch.getCompany()).set(1, launchRatios.get(launch.getCompany()).get(1)+1);
			//Si el lanzamiento es existoso
			if (launch.getStatus().equals(Status.SUCCESS)) {
				//Se incrementa el número de lanzamientos exitosos (posición 0 de la lista)
				launchRatios.get(launch.getCompany()).set(0, launchRatios.get(launch.getCompany()).get(0)+1);
			}
		});
				
		//Se procesan las claves del mapa launchRatios
		launchRatios.keySet().forEach(company -> {
			//Se calcula el ratio de éxito a partir de los dos contadores
			//Se añade la entrada al mapa successRatios
			sucessRatios.put(company, (float) launchRatios.get(company).get(0) / launchRatios.get(company).get(1));
		});
		
		return sucessRatios;
	}
	
	/**
	 * Obtiene el número de lanzamientos realizados desde cada país, ordenado de mayor a menor
	 * número de lanzamientos. 
	 * @param launches List<RocketLaunch> con los lanzamientos. 
	 * @return lista List<CountryLaunches> con los lanzamientos por país, ordenados de mayor a menor.
	 */
	public List<CountryLaunches> getLaunchesPerCountry(List<RocketLaunch> launches) {
		List<CountryLaunches> countryLaunches = new ArrayList<>();		
		//Mapa auxiliar para contabilizar los lanzamientos por país
		Map<String, Integer> countryLaunchesMap = new HashMap<>();
		
		//Se procesan los lanzamientos uno a uno
		launches.forEach(launch -> {
			String location = launch.getLocation(); 
			//Se obtiene el nombre del país.
			String country = location.substring(location.lastIndexOf(",")+1, location.length());
			
			//Se crea el objeto CountryLaunches y se añade al mapa
			countryLaunchesMap.putIfAbsent(country, Integer.valueOf(0));
			
			//Se incrementa el número de lanzamientos del país.
			countryLaunchesMap.put(country, countryLaunchesMap.get(country)+1);
		});
		
		//Se procesa el mapa countryLaunchesMap y se crea la lista de CountryLaunches
		countryLaunchesMap.keySet().forEach(country ->
			countryLaunches.add(new CountryLaunches(country, countryLaunchesMap.get(country)))
		);
		
		//Comparador ordenar CountryLaunch de menor a mayor número de lanzamientos.
		Comparator<CountryLaunches> comparator = (cL1, cL2) -> {
			return Integer.compare(cL1.getNum(), cL2.getNum());
		};
		
		//Se ordena la lista de countryLaunches de mayor a menor número de lanzamientos.
		Collections.sort(countryLaunches, comparator.reversed());
		
		return countryLaunches;
	}	
}