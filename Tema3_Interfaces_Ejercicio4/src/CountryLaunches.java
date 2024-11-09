
/**
 * Clase que representa el número de lanzamientos realizado por un determinado país.
 */
public class CountryLaunches {

	private String country; // país que ha realizado los lanzamientos
	private int num; // número de lanzamientos realizado por el páis
	
	/**
	 * Constructor de la clase.
	 * @param country nombre del país en el que se han realizado los lanzamientos
	 * @param launches número de lanzamientos realizados
	 */
	public CountryLaunches(String country, int num) {
		this.country = country;
		this.num = num;
	}
	
	/**
	 * Obtiene el país en el que se han realizado los lanzamientos
	 * @return país en el que se han realizado los lanzamientos
	 */
	public String getCountry() {
		return country;
	}
	
	/**
	 * Obtiene el número de lanzamientos realizado
	 * @return número de lanzamientos realizados
	 */
	public int getNum() {
		return num;
	}
	
	@Override
	public String toString() {
		return String.format("%s: %d", country, num);
	}
}