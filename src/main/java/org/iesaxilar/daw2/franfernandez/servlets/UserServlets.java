package org.iesaxilar.daw2.franfernandez.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.iesaxilar.daw2.franfernandez.daos.UserDAO;
import org.iesaxilar.daw2.franfernandez.daos.UserDAOImpl;
import org.iesaxilar.daw2.franfernandez.entities.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet encargado de gestionar las operaciones CRUD de usuarios.
 *
 * <p>Este servlet actúa como controlador de la aplicación web.
 * Recibe las peticiones realizadas sobre la URL {@code /users},
 * valida los datos recibidos, utiliza el DAO para acceder a la
 * base de datos y selecciona la vista JSP correspondiente.</p>
 *
 * @author Fran Fernandez
 * @version 1.0-SNAPSHOT
 */
@WebServlet("/users")
public class UserServlets extends HttpServlet {

    private static final Logger logger =
            LoggerFactory.getLogger(UserServlets.class);

    private UserDAO userDAO;

    /**
     * Inicializa el servlet y crea la implementación del DAO.
     *
     * @throws ServletException si se produce un error durante la inicialización
     */
    @Override
    public void init() throws ServletException {

        logger.info("Entrando en el método init() de UserServlet");

        try {
            userDAO = new UserDAOImpl();

            logger.info("UserDAO inicializado correctamente.");

        } catch (Exception e) {

            logger.error(
                    "Error al inicializar UserDAO: {}",
                    e.getMessage(),
                    e
            );

            throw new ServletException(
                    "Error al inicializar UserDAO",
                    e
            );

        } finally {

            logger.info("Saliendo del método init() de UserServlet");
        }
    }

    /**
     * Gestiona las peticiones GET del recurso /users.
     *
     * <p>Según el parámetro {@code action}, permite listar usuarios,
     * mostrar el formulario de creación o mostrar el formulario de edición.</p>
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

        logger.info("Entrando en el método doGet() de UserServlet");

        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        try {

            if (action == null || action.isBlank()) {
                action = "list";
            }

            switch (action) {

                case "new":
                    showNewForm(request, response);
                    break;

                case "edit":
                    showEditForm(request, response);
                    break;

                default:
                    listUsers(request, response);
                    break;
            }

        } catch (SQLException e) {

            logger.error(
                    "Error SQL en doGet(): {}",
                    e.getMessage(),
                    e
            );

            throw new ServletException(
                    "Error al procesar la petición",
                    e
            );

        } finally {

            logger.info("Saliendo del método doGet() de UserServlet");
        }
    }

    /**
     * Gestiona las peticiones POST del recurso /users.
     *
     * <p>Según el parámetro {@code action}, permite insertar,
     * actualizar o eliminar usuarios.</p>
     *
     * @param request petición HTTP
     * @param response respuesta HTTP
     * @throws ServletException si se produce un error del servlet
     * @throws IOException si se produce un error de entrada/salida
     */
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("Entrando en el método doPost() de UserServlet");

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        try {

            if (action == null || action.isBlank()) {

                logger.warn(
                        "No se ha recibido ninguna acción en la petición POST."
                );

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Acción no válida."
                );

                return;
            }

            switch (action) {

                case "insert":
                    insertUser(request, response);
                    break;

                case "update":
                    updateUser(request, response);
                    break;

                case "delete":
                    deleteUser(request, response);
                    break;

                default:

                    logger.warn(
                            "Acción POST no reconocida: {}",
                            action
                    );

                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Acción no válida."
                    );
                    break;
            }

        } catch (SQLException e) {

            logger.error(
                    "Error SQL en doPost(): {}",
                    e.getMessage(),
                    e
            );

            throw new ServletException(
                    "Error al procesar la petición",
                    e
            );

        } finally {

            logger.info("Saliendo del método doPost() de UserServlet");
        }
    }

    /**
     * Muestra el formulario para crear un usuario.
     *
     * @param request petición HTTP
     * @param response respuesta HTTP
     * @throws ServletException si se produce un error al cargar la vista
     * @throws IOException si se produce un error de entrada/salida
     */
    private void showNewForm(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("Entrando en showNewForm()");

        request.getRequestDispatcher(
                "views/user/user-form.jsp"
        ).forward(request, response);

        logger.info("Saliendo de showNewForm()");
    }

    /**
     * Muestra el listado de usuarios.
     *
     * @param request petición HTTP
     * @param response respuesta HTTP
     * @throws SQLException si se produce un error al consultar la base de datos
     * @throws ServletException si se produce un error al cargar la vista
     * @throws IOException si se produce un error de entrada/salida
     */
    private void listUsers(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        logger.info("Entrando en listUsers()");

        List<User> listUsers = userDAO.listAllUsers();

        logger.info(
                "Número de usuarios recuperados: {}",
                listUsers.size()
        );

        request.setAttribute("listUsers", listUsers);

        request.getRequestDispatcher(
                "views/user/user-list.jsp"
        ).forward(request, response);

        logger.info("Saliendo de listUsers()");
    }

    /**
     * Muestra el formulario de edición de un usuario.
     *
     * @param request petición HTTP
     * @param response respuesta HTTP
     * @throws SQLException si se produce un error al consultar la base de datos
     * @throws ServletException si se produce un error al cargar la vista
     * @throws IOException si se produce un error de entrada/salida
     */
    private void showEditForm(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        logger.info("Entrando en showEditForm()");

        String idParameter = request.getParameter("id");

        if (idParameter == null || idParameter.isBlank()) {

            logger.warn(
                    "No se ha recibido el identificador del usuario a editar."
            );

            request.setAttribute(
                    "errorMessageKey",
                    "msg.validation.user.idRequired"
            );

            listUsers(request, response);
            return;
        }

        Long id = Long.parseLong(idParameter);

        User existingUser = userDAO.getUserById(id);

        if (existingUser == null) {

            logger.warn(
                    "No se encontró el usuario con id: {}",
                    id
            );

            request.setAttribute(
                    "errorMessageKey",
                    "msg.validation.user.notFound"
            );

            listUsers(request, response);
            return;
        }

        request.setAttribute("user", existingUser);

        request.getRequestDispatcher(
                "views/user/user-form.jsp"
        ).forward(request, response);

        logger.info("Saliendo de showEditForm()");
    }

    /**
     * Inserta un nuevo usuario.
     *
     * <p>Realiza las validaciones del lado del servidor,
     * comprueba que el username no esté repetido, convierte
     * los tipos recibidos desde el formulario y calcula la fecha
     * de caducidad de la contraseña.</p>
     *
     * @param request petición HTTP
     * @param response respuesta HTTP
     * @throws SQLException si se produce un error de base de datos
     * @throws ServletException si se produce un error al cargar la vista
     * @throws IOException si se produce un error de entrada/salida
     */
    private void insertUser(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        logger.info("Entrando en insertUser()");

        String username = request.getParameter("username");
        String passwordHash = request.getParameter("passwordHash");
        String lastPasswordChangeParameter =
                request.getParameter("lastPasswordChange");

        /*
         * Array con los campos obligatorios.
         * El ejercicio pide expresamente esta estructura.
         */
        String[] requiredFields = {
                username,
                passwordHash,
                lastPasswordChangeParameter
        };

        for (String field : requiredFields) {

            if (field == null || field.isBlank()) {

                logger.warn(
                        "Error de validación: existe un campo obligatorio vacío."
                );

                request.setAttribute(
                        "errorMessageKey",
                        "msg.validation.required"
                );

                request.getRequestDispatcher(
                        "views/user/user-form.jsp"
                ).forward(request, response);

                return;
            }
        }

        username = username.trim();
        passwordHash = passwordHash.trim();
        lastPasswordChangeParameter =
                lastPasswordChangeParameter.trim();

        if (username.length() > 40) {

            logger.warn(
                    "Error de validación: username supera los 40 caracteres."
            );

            request.setAttribute(
                    "errorMessageKey",
                    "msg.validation.username.maxlength"
            );

            request.getRequestDispatcher(
                    "views/user/user-form.jsp"
            ).forward(request, response);

            return;
        }

        if (passwordHash.length() > 500) {

            logger.warn(
                    "Error de validación: passwordHash supera los 500 caracteres."
            );

            request.setAttribute(
                    "errorMessageKey",
                    "msg.validation.password.maxlength"
            );

            request.getRequestDispatcher(
                    "views/user/user-form.jsp"
            ).forward(request, response);

            return;
        }

        if (userDAO.existsUserByUsername(username)) {

            logger.warn(
                    "Intento de insertar username duplicado: {}",
                    username
            );

            request.setAttribute(
                    "errorMessageKey",
                    "msg.validation.username.exists"
            );

            request.getRequestDispatcher(
                    "views/user/user-form.jsp"
            ).forward(request, response);

            return;
        }

        LocalDateTime lastPasswordChange;

        try {

            lastPasswordChange =
                    LocalDateTime.parse(lastPasswordChangeParameter);

        } catch (DateTimeParseException e) {

            logger.warn(
                    "Formato incorrecto para lastPasswordChange: {}",
                    lastPasswordChangeParameter
            );

            request.setAttribute(
                    "errorMessageKey",
                    "msg.validation.lastPasswordChange.invalid"
            );

            request.getRequestDispatcher(
                    "views/user/user-form.jsp"
            ).forward(request, response);

            return;
        }

        /*
         * passwordExpiresAt se calcula en Java:
         * exactamente tres meses después de lastPasswordChange.
         */
        LocalDateTime passwordExpiresAt =
                lastPasswordChange.plusMonths(3);

        Boolean active =
                Boolean.valueOf(request.getParameter("active"));

        Boolean accountNonLocked =
                Boolean.valueOf(
                        request.getParameter("accountNonLocked")
                );

        String failedLoginAttemptsParameter =
                request.getParameter("failedLoginAttempts");

        Integer failedLoginAttempts;

        try {

            failedLoginAttempts =
                    (failedLoginAttemptsParameter == null
                            || failedLoginAttemptsParameter.isBlank())
                            ? 0
                            : Integer.valueOf(
                            failedLoginAttemptsParameter
                    );

        } catch (NumberFormatException e) {

            logger.warn(
                    "Valor no válido para failedLoginAttempts: {}",
                    failedLoginAttemptsParameter
            );

            request.setAttribute(
                    "errorMessageKey",
                    "msg.validation.failedLoginAttempts.invalid"
            );

            request.getRequestDispatcher(
                    "views/user/user-form.jsp"
            ).forward(request, response);

            return;
        }

        if (failedLoginAttempts < 0) {

            logger.warn(
                    "Error de validación: failedLoginAttempts es negativo."
            );

            request.setAttribute(
                    "errorMessageKey",
                    "msg.validation.failedLoginAttempts.negative"
            );

            request.getRequestDispatcher(
                    "views/user/user-form.jsp"
            ).forward(request, response);

            return;
        }

        Boolean emailVerified =
                Boolean.valueOf(
                        request.getParameter("emailVerified")
                );

        Boolean mustChangePassword =
                Boolean.valueOf(
                        request.getParameter("mustChangePassword")
                );

        User newUser = new User(
                null,
                username,
                passwordHash,
                active,
                accountNonLocked,
                lastPasswordChange,
                passwordExpiresAt,
                failedLoginAttempts,
                emailVerified,
                mustChangePassword
        );

        userDAO.insertUser(newUser);

        logger.info(
                "Usuario insertado correctamente con id: {}",
                newUser.getId()
        );

        response.sendRedirect("users");

        logger.info("Saliendo de insertUser()");
    }

    /**
     * Actualiza un usuario existente.
     *
     * @param request petición HTTP
     * @param response respuesta HTTP
     * @throws SQLException si se produce un error de base de datos
     * @throws ServletException si se produce un error al cargar la vista
     * @throws IOException si se produce un error de entrada/salida
     */
    private void updateUser(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        logger.info("Entrando en updateUser()");

        String idParameter = request.getParameter("id");
        String username = request.getParameter("username");
        String passwordHash = request.getParameter("passwordHash");
        String lastPasswordChangeParameter =
                request.getParameter("lastPasswordChange");

        if (idParameter == null || idParameter.isBlank()) {

            logger.warn(
                    "No se ha recibido el identificador del usuario."
            );

            request.setAttribute(
                    "errorMessageKey",
                    "msg.validation.user.idRequired"
            );

            request.getRequestDispatcher(
                    "views/user/user-form.jsp"
            ).forward(request, response);

            return;
        }

        /*
         * Array con los campos obligatorios.
         */
        String[] requiredFields = {
                username,
                passwordHash,
                lastPasswordChangeParameter
        };

        for (String field : requiredFields) {

            if (field == null || field.isBlank()) {

                logger.warn(
                        "Error de validación: existe un campo obligatorio vacío."
                );

                request.setAttribute(
                        "errorMessageKey",
                        "msg.validation.required"
                );

                request.getRequestDispatcher(
                        "views/user/user-form.jsp"
                ).forward(request, response);

                return;
            }
        }

        Long id;

        try {

            id = Long.valueOf(idParameter);

        } catch (NumberFormatException e) {

            logger.warn(
                    "Identificador de usuario no válido: {}",
                    idParameter
            );

            request.setAttribute(
                    "errorMessageKey",
                    "msg.validation.id.invalid"
            );

            request.getRequestDispatcher(
                    "views/user/user-form.jsp"
            ).forward(request, response);

            return;
        }

        username = username.trim();
        passwordHash = passwordHash.trim();
        lastPasswordChangeParameter =
                lastPasswordChangeParameter.trim();

        if (username.length() > 40) {

            logger.warn(
                    "Error de validación: username supera los 40 caracteres."
            );

            request.setAttribute(
                    "errorMessageKey",
                    "msg.validation.username.maxlength"
            );

            request.getRequestDispatcher(
                    "views/user/user-form.jsp"
            ).forward(request, response);

            return;
        }

        if (passwordHash.length() > 500) {

            logger.warn(
                    "Error de validación: passwordHash supera los 500 caracteres."
            );

            request.setAttribute(
                    "errorMessageKey",
                    "msg.validation.password.maxlength"
            );

            request.getRequestDispatcher(
                    "views/user/user-form.jsp"
            ).forward(request, response);

            return;
        }

        if (userDAO.existsUserByUsernameAndNotId(username, id)) {

            logger.warn(
                    "Intento de utilizar un username duplicado en edición: {}",
                    username
            );

            request.setAttribute(
                    "errorMessageKey",
                    "msg.validation.username.exists.other"
            );

            request.getRequestDispatcher(
                    "views/user/user-form.jsp"
            ).forward(request, response);

            return;
        }

        LocalDateTime lastPasswordChange;

        try {

            lastPasswordChange =
                    LocalDateTime.parse(lastPasswordChangeParameter);

        } catch (DateTimeParseException e) {

            logger.warn(
                    "Formato incorrecto para lastPasswordChange: {}",
                    lastPasswordChangeParameter
            );

            request.setAttribute(
                    "errorMessageKey",
                    "msg.validation.lastPasswordChange.invalid"
            );

            request.getRequestDispatcher(
                    "views/user/user-form.jsp"
            ).forward(request, response);

            return;
        }

        LocalDateTime passwordExpiresAt =
                lastPasswordChange.plusMonths(3);

        Boolean active =
                Boolean.valueOf(request.getParameter("active"));

        Boolean accountNonLocked =
                Boolean.valueOf(
                        request.getParameter("accountNonLocked")
                );

        String failedLoginAttemptsParameter =
                request.getParameter("failedLoginAttempts");

        Integer failedLoginAttempts;

        try {

            failedLoginAttempts =
                    (failedLoginAttemptsParameter == null
                            || failedLoginAttemptsParameter.isBlank())
                            ? 0
                            : Integer.valueOf(
                            failedLoginAttemptsParameter
                    );

        } catch (NumberFormatException e) {

            logger.warn(
                    "Valor no válido para failedLoginAttempts: {}",
                    failedLoginAttemptsParameter
            );

            request.setAttribute(
                    "errorMessageKey",
                    "msg.validation.failedLoginAttempts.invalid"
            );

            request.getRequestDispatcher(
                    "views/user/user-form.jsp"
            ).forward(request, response);

            return;
        }

        if (failedLoginAttempts < 0) {

            logger.warn(
                    "Error de validación: failedLoginAttempts es negativo."
            );

            request.setAttribute(
                    "errorMessageKey",
                    "msg.validation.failedLoginAttempts.negative"
            );

            request.getRequestDispatcher(
                    "views/user/user-form.jsp"
            ).forward(request, response);

            return;
        }

        Boolean emailVerified =
                Boolean.valueOf(
                        request.getParameter("emailVerified")
                );

        Boolean mustChangePassword =
                Boolean.valueOf(
                        request.getParameter("mustChangePassword")
                );

        User updatedUser = new User(
                id,
                username,
                passwordHash,
                active,
                accountNonLocked,
                lastPasswordChange,
                passwordExpiresAt,
                failedLoginAttempts,
                emailVerified,
                mustChangePassword
        );

        userDAO.updateUser(updatedUser);

        logger.info(
                "Usuario actualizado correctamente. Id: {}",
                id
        );

        response.sendRedirect("users");

        logger.info("Saliendo de updateUser()");
    }

    /**
     * Elimina un usuario mediante su identificador.
     *
     * @param request petición HTTP
     * @param response respuesta HTTP
     * @throws SQLException si se produce un error de base de datos
     * @throws IOException si se produce un error de entrada/salida
     */
    private void deleteUser(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException, IOException {

        logger.info("Entrando en deleteUser()");

        String idParameter = request.getParameter("id");

        if (idParameter == null || idParameter.isBlank()) {

            logger.warn(
                    "No se ha recibido el identificador del usuario a eliminar."
            );

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Identificador no válido."
            );

            return;
        }

        Long id;

        try {

            id = Long.valueOf(idParameter);

        } catch (NumberFormatException e) {

            logger.warn(
                    "Identificador no válido para eliminar: {}",
                    idParameter
            );

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Identificador no válido."
            );

            return;
        }

        userDAO.deleteUser(id);

        logger.info(
                "Usuario eliminado correctamente. Id: {}",
                id
        );

        response.sendRedirect("users");

        logger.info("Saliendo de deleteUser()");
    }
}