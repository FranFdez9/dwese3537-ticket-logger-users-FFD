package org.iesaxilar.daw2.franfernandez.daos;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestiona la conexión con la base de datos MariaDB.
 *
 * <p>La clase utiliza el patrón Singleton para disponer
 * de una única instancia de conexión compartida por la aplicación.</p>
 *
 * <p>Las credenciales de conexión se obtienen desde
 * las variables definidas en el archivo .env.</p>
 *
 * @author Fran Fernandez
 * @version 1.0-SNAPSHOT
 */
public class DatabaseConnectionManager {

    private static final Logger logger =
            LoggerFactory.getLogger(DatabaseConnectionManager.class);

    private static Connection connection;

    private DatabaseConnectionManager() {
        logger.debug("Entrando en el constructor de DatabaseConnectionManager");
        logger.debug("Saliendo del constructor de DatabaseConnectionManager");
    }

    /**
     * Obtiene una conexión activa con la base de datos.
     *
     * <p>Si no existe una conexión o la conexión actual está cerrada,
     * se crea una nueva utilizando las variables de entorno.</p>
     *
     * @return conexión activa con MariaDB
     * @throws SQLException si se produce un error al conectar
     */
    public static synchronized Connection getConnection() throws SQLException {

        logger.info("Entrando en el método getConnection()");

        try {
            if (connection == null || connection.isClosed()) {

                logger.info("No existe una conexión activa. Creando conexión con MariaDB...");

                Dotenv dotenv = Dotenv.load();

                String dbUrl = dotenv.get("DB_URL");
                String dbUser = dotenv.get("DB_USER");
                String dbPassword = dotenv.get("DB_PASSWORD");

                logger.info("Iniciando la conexión a la base de datos MariaDB...");

                connection = DriverManager.getConnection(
                        dbUrl,
                        dbUser,
                        dbPassword
                );

                logger.info("Conexión a la base de datos establecida exitosamente.");
            } else {
                logger.info("Se reutiliza la conexión existente con la base de datos.");
            }

            logger.info("Saliendo del método getConnection() correctamente.");

            return connection;

        } catch (SQLException e) {

            logger.error(
                    "Error al establecer la conexión con la base de datos: {}",
                    e.getMessage(),
                    e
            );

            throw e;
        }
    }

    /**
     * Cierra la conexión con la base de datos si está abierta.
     */
    public static synchronized void closeConnection() {

        logger.info("Entrando en el método closeConnection()");

        try {
            if (connection != null && !connection.isClosed()) {

                logger.info("Cerrando la conexión con la base de datos...");

                connection.close();

                logger.info("Conexión cerrada correctamente.");
            } else {
                logger.info("No existe una conexión activa que cerrar.");
            }

        } catch (SQLException e) {

            logger.error(
                    "Error al cerrar la conexión con la base de datos: {}",
                    e.getMessage(),
                    e
            );

        } finally {
            connection = null;
            logger.info("Saliendo del método closeConnection()");
        }
    }
}