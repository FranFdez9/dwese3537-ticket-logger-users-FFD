package org.iesaxilar.daw2.franfernandez.listeners;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import org.iesaxilar.daw2.franfernandez.daos.DataInitializer;
import org.iesaxilar.daw2.franfernandez.daos.DatabaseConnectionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.Connection;

/**
 * Listener encargado de gestionar la inicialización y finalización
 * del contexto de la aplicación web.
 *
 * <p>Al iniciar la aplicación, obtiene el script {@code data.sql}
 * y delega su ejecución en {@link DataInitializer}.</p>
 *
 * <p>Al finalizar la aplicación, cierra la conexión con la base de datos.</p>
 *
 * @author Fran Fernandez
 * @version 1.0-SNAPSHOT
 */
@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger logger =
            LoggerFactory.getLogger(AppContextListener.class);

    /**
     * Inicializa la aplicación web y carga los datos iniciales
     * de la base de datos.
     *
     * @param sce evento del contexto de la aplicación
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        logger.info("Entrando en el método contextInitialized()");

        try (Connection connection =
                     DatabaseConnectionManager.getConnection()) {

            logger.info("Iniciando la conexión a la base de datos...");

            InputStream sqlFileStream =
                    sce.getServletContext()
                            .getResourceAsStream("/WEB-INF/classes/data.sql");

            if (sqlFileStream == null) {

                logger.error(
                        "No se pudo encontrar el archivo data.sql en /WEB-INF/classes/"
                );

                return;
            }

            logger.info("Archivo data.sql encontrado correctamente.");

            DataInitializer.loadDataFromSQL(sqlFileStream);

            logger.info("Carga de datos finalizada correctamente.");

        } catch (Exception e) {

            logger.error(
                    "Error al inicializar la aplicación y cargar los datos: {}",
                    e.getMessage(),
                    e
            );

        } finally {

            logger.info("Saliendo del método contextInitialized()");
        }
    }

    /**
     * Libera los recursos utilizados por la aplicación cuando
     * se destruye el contexto web.
     *
     * @param sce evento del contexto de la aplicación
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        logger.info("Entrando en el método contextDestroyed()");

        try {

            logger.info("Cerrando la conexión a la base de datos...");

            DatabaseConnectionManager.closeConnection();

            logger.info("Conexión a la base de datos cerrada correctamente.");

        } catch (Exception e) {

            logger.error(
                    "Error al cerrar los recursos de la aplicación: {}",
                    e.getMessage(),
                    e
            );

        } finally {

            logger.info("Saliendo del método contextDestroyed()");
        }
    }
}