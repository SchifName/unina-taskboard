package unina.project.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnessioneDatabase {

    // URL di connessione: jdbc:postgresql://[host]:[porta]/[nome_database]
    private static final String URL = "jdbc:postgresql://localhost:5432/unina_taskboard";

    // INSERISCI QUI LE TUE CREDENZIALI DI PGADMIN
    private static final String USER = "OO8";
    private static final String PASSWORD = "password";

    private static Connection connection = null;

    // Costruttore privato per impedire l'istanziamento di questa classe (Singleton)
    private ConnessioneDatabase() {}

    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Tenta di stabilire la connessione
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connessione al database stabilita con successo!");
            } catch (SQLException e) {
                System.err.println("Errore di connessione al database: " + e.getMessage());
            }
        }
        return connection;
    }

    // Metodo main temporaneo SOLO per testare la connessione
    public static void main(String[] args) {
        getConnection();
    }
}