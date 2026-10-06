package org.iesaxilar.daw2.franfernandez.daos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase encargada de cargar y ejecutar el script SQL de inicialización
 * de la base de datos.
 *
 * <p>Lee el contenido del archivo {@code data.sql} y ejecuta las
 * sentencias SQL contenidas en él.</p>
 *
 * @author Fran Fernandez
 * @version 1.0-SNAPSHOT
 */
public class DataInitializer {

    private static final Logger logger =
            LoggerFactory.getLogger(DataInitializer.class);

    /**
     * Constructor privado para evitar la creación de instancias.
     */
    private DataInitializer() {
        logger.debug("Entrando en el constructor de DataInitializer");
        logger.debug("Saliendo del constructor de DataInitializer");
    }

    /**
     * Carga y ejecuta las sentencias SQL proporcionadas mediante un
     * flujo de entrada.
     *
     * @param sqlFileStream flujo de entrada del archivo data.sql
     * @throws SQLException si se produce un error durante la ejecución SQL
     * @throws IOException si se produce un error al leer el archivo
     */
    public static void loadDataFromSQL(InputStream sqlFileStream)
            throws SQLException, IOException {

        logger.info("Entrando en el método loadDataFromSQL()");

        Connection connection = null;

        try {
            connection = DatabaseConnectionManager.getConnection();
            connection.setAutoCommit(false);

            logger.info("Leyendo el archivo SQL de inicialización...");

            StringBuilder sqlContent = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            sqlFileStream,
                            StandardCharsets.UTF_8))) {

                String line;

                while ((line = reader.readLine()) != null) {
                    sqlContent.append(line).append("\n");
                }
            }

            String[] statements = sqlContent.toString().split(";");

            try (Statement statement = connection.createStatement()) {

                for (String sql : statements) {

                    String trimmedSql = sql.trim();

                    if (!trimmedSql.isEmpty()) {
                        logger.info("Ejecutando sentencia SQL de inicialización...");
                        statement.execute(trimmedSql);
                    }
                }
            }

            connection.commit();

            logger.info("Script SQL ejecutado correctamente.");
            logger.info("Transacción confirmada correctamente.");

        } catch (SQLException | IOException e) {

            logger.error(
                    "Error durante la carga del script SQL: {}",
                    e.getMessage(),
                    e
            );

            if (connection != null) {
                try {
                    connection.rollback();
                    logger.info("Transacción revertida mediante rollback.");
                } catch (SQLException rollbackException) {
                    logger.error(
                            "Error durante el rollback: {}",
                            rollbackException.getMessage(),
                            rollbackException
                    );
                }
            }

            throw e;

        } finally {

            logger.info("Saliendo del método loadDataFromSQL()");
        }
    }
}