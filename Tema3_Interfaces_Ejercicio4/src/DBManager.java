
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import domain.RocketLaunch;

/**
 * Esta clase contiene el código de gestión de la base de datos.
 *
 */
public class DBManager {
	
	private Connection conn; // conexión a la base de datos 

	/**
	 * Método para conectar a la base de datos.
	 * @throws ClassNotFoundException si se produce un error al cargar el driver de base de datos.
	 * @throws SQLException si se produce un error al establecer la conexión con la base de datos. 
	 */
	public void connect() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		
		conn = DriverManager.getConnection("jdbc:sqlite:db/basedatos.bd");
	}
		
	/**
	 * Cierra la conexión a la base de datos.
	 * @throws SQLException si se produce un error al intentar cerrar la conexión a la base de datos.
	 */
	public void disconnect() throws SQLException {
		conn.close();
	}
		
	/**
	 * Obtiene la lista de todos los lanzamientos existentes en la base de datos.
	 * @return lista de todos los lanzamientos existentes en la base de datos.
	 * @throws SQLException si se produce un error al intentar obtener la lista de lanzamientos. 
	 */
	public List<RocketLaunch> getAllLaunches() throws SQLException {
		List<RocketLaunch> launches = new ArrayList<>();
		try (Statement stmt = conn.createStatement()) {
			try (ResultSet rs = stmt.executeQuery("SELECT * FROM launches")) {
				while(rs.next()) {
					RocketLaunch launch = new RocketLaunch(
						rs.getString("company"),
						LocalDateTime.parse(rs.getString("launch_date")),
						rs.getString("location"),
						rs.getString("detail"),
						rs.getFloat("cost"),
						rs.getBoolean("status")
					);
					
					launches.add(launch);
				}
			}
		}
		
		return launches;
	}
	
	// T2.2 //////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Inserta los lanzamientos recibidos en la tabla de datos exportados ("exported").
	 * Únicamente se insertan las propiedades company, launch_date y status de cada lanzamiento.
	 * @param exported lista de lanzamientos exportados desde la aplicación
	 * @throws SQLException si se produce un error al intentar obtener la lista de lanzamientos.
	 */
	public void exportToDabase(List<RocketLaunch> exported) throws SQLException {

	}
}