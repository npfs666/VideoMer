package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author GaOu
 *
 */
public class SQLite {
	
	/**
	 * Connection � la bdd
	 */
	private Connection con;
	
	/**
	 * 
	 */
	private Statement st;
	
	/**
	 * Chemin d'acc�s relatif de la bdd
	 */
	private String dbPath = "db.sqlite";

	/** 
	 * Creation d'une nouvelle instance SQLite 
	 */
	public SQLite(String dbpath) {
		
		dbPath = dbpath;
	}
	
	/**
	 * Connection � la base de donn�es
	 * 
	 * @throws ClassNotFoundException Si le driver n'est pas trouv� (librairie)
	 * @throws SQLException Si erreur d'acc�s � la bdd ou que la connection est ferm�e
	 */
	public void connect() throws ClassNotFoundException, SQLException {

		// driver to load
		Class.forName("org.sqlite.JDBC");
		//chargement du driver
		con = DriverManager.getConnection("jdbc:sqlite:"+dbPath);
		
		//pour executer nos requetes SQL
		st = con.createStatement();
	}
	
	/**
	 * Fermeture de la connection avec la bdd
	 * 
	 * @throws SQLException Si erreur d'acc�s � la bdd
	 */
	public void close() throws SQLException {

		con.close();
	}
	
	/**
	 * Execute une requete SQL
	 * 
	 * @param sql requete SQL
	 * @return l'ensemble des r�sultats
	 * @throws SQLException Si erreur d'acc�s � la bdd ou que la connection est ferm�e ou que la requete est mauvaise
	 */
	public ResultSet query(String sql)throws SQLException {
		
		return st.executeQuery(sql);
	}
	
	/*void show_result() {
		
		try {
			while(rs.next()) {
				int id = rs.getInt("ID");
				String login = rs.getString("NOM");
				System.out.println("votre id est ="+id+"\nlogin est:"+login);
			}
		} catch(Exception e){
			System.out.println("Select Error:"+e);
		}
	}*/
}
