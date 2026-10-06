package org.iesaxilar.daw2.franfernandez.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Locale;

/**
 * Servlet encargado de gestionar el cambio de idioma de la aplicación.
 *
 * <p>Recibe el idioma seleccionado por el usuario, crea el {@link Locale}
 * correspondiente y lo almacena en la sesión para que las páginas JSP
 * puedan utilizarlo mediante JSTL.</p>
 *
 * @author Fran Fernandez
 * @version 1.0-SNAPSHOT
 */
@WebServlet("/changeLanguage")
public class LanguageServlet extends HttpServlet {

    private static final Logger logger =
            LoggerFactory.getLogger(LanguageServlet.class);

    /**
     * Gestiona la petición para cambiar el idioma de la aplicación.
     *
     * @param request petición HTTP
     * @param response respuesta HTTP
     * @throws ServletException si se produce un error del servlet
     * @throws IOException si se produce un error de entrada/salida
     */
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        logger.info(
                "Entrando en el método doGet() de LanguageServlet"
        );

        try {

            String language = request.getParameter("lang");

            if (language != null) {

                Locale locale;

                if (language.equals("es")) {

                    locale = new Locale("es");

                    logger.info(
                            "Idioma seleccionado: Español (es)"
                    );

                } else {

                    locale = new Locale("en");

                    logger.info(
                            "Idioma seleccionado: Inglés (en)"
                    );
                }

                request.getSession().setAttribute(
                        "locale",
                        locale
                );

                logger.info(
                        "El locale '{}' ha sido guardado en la sesión",
                        locale
                );

            } else {

                logger.warn(
                        "No se ha recibido ningún parámetro 'lang'"
                );
            }

            String referer = request.getHeader("Referer");

            if (referer != null) {

                logger.info(
                        "Redirigiendo al usuario a la página anterior: {}",
                        referer
                );

                response.sendRedirect(referer);

            } else {

                logger.warn(
                        "No se encontró la cabecera Referer. " +
                                "Redirigiendo a la página principal."
                );

                response.sendRedirect(
                        request.getContextPath() + "/"
                );
            }

        } catch (Exception e) {

            logger.error(
                    "Se ha producido un error en el método doGet de LanguageServlet: {}",
                    e.getMessage(),
                    e
            );

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error al cambiar el idioma"
            );

        } finally {

            logger.info(
                    "Saliendo del método doGet() de LanguageServlet"
            );
        }
    }
}