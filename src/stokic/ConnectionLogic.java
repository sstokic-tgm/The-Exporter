package stokic;

import com.mysql.jdbc.jdbc2.optional.*;
import java.sql.*;
import java.util.regex.*;

import javax.sql.*;

/**
 * Diese Klasse implementiert die Connection Logik. Es wird also die Verbindung zur Datenbank verwaltet, ebenfalls wie das schliessen der
 * Verbindung zur Datenbank. Weiters verwaltet diese Klasse eine execute-Methode die den vom Benutzer eingegeben String bereitstellt und an die 
 * Datenbank absendet.
 * 
 * @author Stokic Stefan
 * @version 1.3
 */
public class ConnectionLogic {

	private String host, user, pass, db;
	private MysqlDataSource ds;
	private Connection con;
	private ResultSet rs;
	private PreparedStatement st;
	private boolean status = false;

	/**
	 * Diese Methode uebernimmt die vom Benutzer angegebenen Connection-Parameter.
	 * @param host der Hostname
	 * @param user der Username
	 * @param pass das Password
	 * @param db der Datenbankname
	 */
	public void setConnection(String host, String user, String pass, String db) {

		this.host = host;
		this.user = user;
		this.pass = pass;
		this.db = db;
	}

	/**
	 * Diese Methode stellt eine Verbindung zur Datenbank.
	 * @return ob die Verbindung erfolgreich war oder nicht.
	 */
	public boolean connect() {

		ds = new MysqlDataSource();

		ds.setServerName(getHost());
		//ds.setPort(7188); // Fuer die Server2Go-Datenbank
		ds.setUser(getUser());
		ds.setPassword(getPass());
		ds.setDatabaseName(getDB());

		try {

			con = ds.getConnection();
			System.out.println("Erfolgreich verbunden!");
			return status = true;
			
		}catch(SQLException sqle) {

			System.out.println("Error: " + sqle.getMessage());
			return status = false;
		}
	}

	/**
	 * Diese Methode bricht die Verbindung zur Datenbank ab.
	 */
	public void disconnect() {

		if(status == true) {

			try {

				con.close();
				
				if(st != null && rs != null) {
					
					st.close();
					rs.close();
					st = null;
					rs = null;
				}
				con = null;
				System.out.println("Erfolgreich geschlossen!");
				status = false;
				
			}catch(SQLException sqle) {

				System.out.println("Error: " + sqle.getMessage());
				status = true;
			}
		}
	}

	/**
	 * Der vom Benutzer eingegebener SQL Befehl wird an die Datenbank geschickt bzw. der vom Benutzer eingegebener SQL Befehl wird ausgefuehrt.
	 * @param sql der SQL Befehl vom Benutzer
	 * @return die Rückgabe des SQL Befehls
	 */
	public ResultSet execute(String sql) {

		try {

			st = con.prepareStatement(sql);
			rs = st.executeQuery();
			return rs;
			
		}catch(SQLException sqle) {

			System.out.println("Error: " + sqle.getMessage());
			return null;
		}
	}

	/**
	 * Getter-Methode fuer den Hostname
	 * @return den Hostname
	 */
	public String getHost() {

		return this.host;
	}

	/**
	 * Getter-Methode fuer den Username
	 * @return den Username
	 */
	public String getUser() {

		return this.user;
	}

	/**
	 * Getter-Methode fuer das Password
	 * @return das Password
	 */
	public String getPass() {

		return this.pass;
	}

	/**
	 * Getter-Methode fuer den Datenbanknamen
	 * @return den Datenbanknamen
	 */
	public String getDB() {

		return this.db;
	}

	/**
	 * Getter-Methode fuer den jeweiligen Connection-Status der Datenbank
	 * @return den derzeitigen Connection-Status mit der Datenbank (falls verbunden true, wenn nicht false)
	 */
	public boolean getStatus() {

		return this.status;
	}

	/**
	 * Setter-Methode die den jeweiligen Connection-Status der Datenbank setzt
	 * @param status den Connection-Status der Datenbank
	 */
	public void setStatus(boolean status) {

		this.status = status;
	}
}