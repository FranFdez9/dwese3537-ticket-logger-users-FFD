package org.iesaxilar.daw2.franfernandez.daos;

import org.iesaxilar.daw2.franfernandez.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de {@link UserDAO}.
 *
 * <p>Esta clase contiene las operaciones SQL necesarias para realizar
 * el CRUD de usuarios sobre la tabla {@code users}.</p>
 *
 * @author Fran Fernandez
 * @version 1.0-SNAPSHOT
 */
public class UserDAOImpl implements UserDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(UserDAOImpl.class);

    /**
     * Constructor de UserDAOImpl.
     */
    public UserDAOImpl() {
        logger.debug("Entrando en el constructor de UserDAOImpl");
        logger.debug("Saliendo del constructor de UserDAOImpl");
    }

    /**
     * Obtiene todos los usuarios de la base de datos.
     *
     * @return lista con todos los usuarios
     * @throws SQLException si se produce un error durante la consulta
     */
    @Override
    public List<User> listAllUsers() throws SQLException {

        logger.info("Entrando en el método listAllUsers()");

        String sql = """
                SELECT id,
                       username,
                       passwordHash,
                       active,
                       accountNonLocked,
                       lastPasswordChange,
                       passwordExpiresAt,
                       failedLoginAttempts,
                       emailVerified,
                       mustChangePassword
                FROM users
                ORDER BY id
                """;

        List<User> users = new ArrayList<>();

        try {
            Connection connection =
                    DatabaseConnectionManager.getConnection();

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    users.add(mapResultSetToUser(resultSet));
                }
            }

            logger.info(
                    "Consulta de usuarios completada. Registros obtenidos: {}",
                    users.size()
            );

            logger.info("Saliendo del método listAllUsers()");

            return users;

        } catch (SQLException e) {

            logger.error(
                    "Error al listar los usuarios: {}",
                    e.getMessage(),
                    e
            );

            logger.info("Saliendo del método listAllUsers() con error");

            throw e;
        }
    }

    /**
     * Inserta un nuevo usuario en la base de datos.
     *
     * <p>El identificador no se incluye en la inserción porque la tabla
     * utiliza un campo AUTO_INCREMENT.</p>
     *
     * @param user usuario que se desea insertar
     * @throws SQLException si se produce un error durante la inserción
     */
    @Override
    public void insertUser(User user) throws SQLException {

        logger.info("Entrando en el método insertUser()");

        String sql = """
                INSERT INTO users (
                    username,
                    passwordHash,
                    active,
                    accountNonLocked,
                    lastPasswordChange,
                    passwordExpiresAt,
                    failedLoginAttempts,
                    emailVerified,
                    mustChangePassword
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try {
            Connection connection =
                    DatabaseConnectionManager.getConnection();

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 sql,
                                 Statement.RETURN_GENERATED_KEYS
                         )) {

                statement.setString(1, user.getUsername());
                statement.setString(2, user.getPasswordHash());
                statement.setBoolean(3, Boolean.TRUE.equals(user.getActive()));
                statement.setBoolean(
                        4,
                        Boolean.TRUE.equals(user.getAccountNonLocked())
                );

                setLocalDateTime(
                        statement,
                        5,
                        user.getLastPasswordChange()
                );

                setLocalDateTime(
                        statement,
                        6,
                        user.getPasswordExpiresAt()
                );

                statement.setInt(
                        7,
                        user.getFailedLoginAttempts() != null
                                ? user.getFailedLoginAttempts()
                                : 0
                );

                statement.setBoolean(
                        8,
                        Boolean.TRUE.equals(user.getEmailVerified())
                );

                statement.setBoolean(
                        9,
                        Boolean.TRUE.equals(user.getMustChangePassword())
                );

                statement.executeUpdate();

                try (ResultSet generatedKeys =
                             statement.getGeneratedKeys()) {

                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getLong(1));
                        logger.info(
                                "Usuario insertado correctamente con id: {}",
                                user.getId()
                        );
                    }
                }
            }

            logger.info("Saliendo del método insertUser()");

        } catch (SQLException e) {

            logger.error(
                    "Error al insertar el usuario: {}",
                    e.getMessage(),
                    e
            );

            logger.info("Saliendo del método insertUser() con error");

            throw e;
        }
    }

    /**
     * Comprueba si existe un usuario con el username indicado.
     *
     * @param username username que se desea comprobar
     * @return true si existe; false en caso contrario
     * @throws SQLException si se produce un error durante la consulta
     */
    @Override
    public boolean existsUserByUsername(String username)
            throws SQLException {

        logger.info("Entrando en el método existsUserByUsername()");

        String sql = """
                SELECT COUNT(*)
                FROM users
                WHERE UPPER(username) = UPPER(?)
                """;

        try {
            Connection connection =
                    DatabaseConnectionManager.getConnection();

            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setString(1, username);

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    resultSet.next();

                    boolean exists = resultSet.getInt(1) > 0;

                    logger.info(
                            "Comprobación de username completada. Existe: {}",
                            exists
                    );

                    logger.info(
                            "Saliendo del método existsUserByUsername()"
                    );

                    return exists;
                }
            }

        } catch (SQLException e) {

            logger.error(
                    "Error al comprobar la existencia del username: {}",
                    e.getMessage(),
                    e
            );

            logger.info(
                    "Saliendo del método existsUserByUsername() con error"
            );

            throw e;
        }
    }

    /**
     * Obtiene un usuario mediante su identificador.
     *
     * @param id identificador del usuario
     * @return usuario encontrado o null si no existe
     * @throws SQLException si se produce un error durante la consulta
     */
    @Override
    public User getUserById(Long id) throws SQLException {

        logger.info("Entrando en el método getUserById()");

        String sql = """
                SELECT id,
                       username,
                       passwordHash,
                       active,
                       accountNonLocked,
                       lastPasswordChange,
                       passwordExpiresAt,
                       failedLoginAttempts,
                       emailVerified,
                       mustChangePassword
                FROM users
                WHERE id = ?
                """;

        try {
            Connection connection =
                    DatabaseConnectionManager.getConnection();

            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setLong(1, id);

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    if (resultSet.next()) {

                        User user = mapResultSetToUser(resultSet);

                        logger.info(
                                "Usuario encontrado correctamente con id: {}",
                                id
                        );

                        logger.info("Saliendo del método getUserById()");

                        return user;
                    }
                }
            }

            logger.info(
                    "No se encontró ningún usuario con id: {}",
                    id
            );

            logger.info("Saliendo del método getUserById()");

            return null;

        } catch (SQLException e) {

            logger.error(
                    "Error al obtener el usuario con id {}: {}",
                    id,
                    e.getMessage(),
                    e
            );

            logger.info("Saliendo del método getUserById() con error");

            throw e;
        }
    }

    /**
     * Actualiza un usuario existente.
     *
     * @param user usuario con los datos actualizados
     * @throws SQLException si se produce un error durante la actualización
     */
    @Override
    public void updateUser(User user) throws SQLException {

        logger.info("Entrando en el método updateUser()");

        String sql = """
                UPDATE users
                SET username = ?,
                    passwordHash = ?,
                    active = ?,
                    accountNonLocked = ?,
                    lastPasswordChange = ?,
                    passwordExpiresAt = ?,
                    failedLoginAttempts = ?,
                    emailVerified = ?,
                    mustChangePassword = ?
                WHERE id = ?
                """;

        try {
            Connection connection =
                    DatabaseConnectionManager.getConnection();

            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setString(1, user.getUsername());
                statement.setString(2, user.getPasswordHash());
                statement.setBoolean(3, Boolean.TRUE.equals(user.getActive()));
                statement.setBoolean(
                        4,
                        Boolean.TRUE.equals(user.getAccountNonLocked())
                );

                setLocalDateTime(
                        statement,
                        5,
                        user.getLastPasswordChange()
                );

                setLocalDateTime(
                        statement,
                        6,
                        user.getPasswordExpiresAt()
                );

                statement.setInt(
                        7,
                        user.getFailedLoginAttempts() != null
                                ? user.getFailedLoginAttempts()
                                : 0
                );

                statement.setBoolean(
                        8,
                        Boolean.TRUE.equals(user.getEmailVerified())
                );

                statement.setBoolean(
                        9,
                        Boolean.TRUE.equals(user.getMustChangePassword())
                );

                statement.setLong(10, user.getId());

                int rowsAffected = statement.executeUpdate();

                logger.info(
                        "Actualización completada. Filas modificadas: {}",
                        rowsAffected
                );
            }

            logger.info("Saliendo del método updateUser()");

        } catch (SQLException e) {

            logger.error(
                    "Error al actualizar el usuario: {}",
                    e.getMessage(),
                    e
            );

            logger.info("Saliendo del método updateUser() con error");

            throw e;
        }
    }

    /**
     * Comprueba si existe otro usuario con el mismo username,
     * excluyendo el usuario que se está editando.
     *
     * @param username username que se desea comprobar
     * @param id identificador del usuario que se está editando
     * @return true si existe otro usuario con ese username
     * @throws SQLException si se produce un error durante la consulta
     */
    @Override
    public boolean existsUserByUsernameAndNotId(
            String username,
            Long id
    ) throws SQLException {

        logger.info(
                "Entrando en el método existsUserByUsernameAndNotId()"
        );

        String sql = """
                SELECT COUNT(*)
                FROM users
                WHERE UPPER(username) = UPPER(?)
                  AND id <> ?
                """;

        try {
            Connection connection =
                    DatabaseConnectionManager.getConnection();

            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setString(1, username);
                statement.setLong(2, id);

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    resultSet.next();

                    boolean exists = resultSet.getInt(1) > 0;

                    logger.info(
                            "Comprobación de username duplicado en edición: {}",
                            exists
                    );

                    logger.info(
                            "Saliendo del método existsUserByUsernameAndNotId()"
                    );

                    return exists;
                }
            }

        } catch (SQLException e) {

            logger.error(
                    "Error al comprobar username duplicado en edición: {}",
                    e.getMessage(),
                    e
            );

            logger.info(
                    "Saliendo del método existsUserByUsernameAndNotId() con error"
            );

            throw e;
        }
    }

    /**
     * Elimina un usuario mediante su identificador.
     *
     * @param id identificador del usuario que se desea eliminar
     * @throws SQLException si se produce un error durante la eliminación
     */
    @Override
    public void deleteUser(Long id) throws SQLException {

        logger.info("Entrando en el método deleteUser()");

        String sql = """
                DELETE FROM users
                WHERE id = ?
                """;

        try {
            Connection connection =
                    DatabaseConnectionManager.getConnection();

            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setLong(1, id);

                int rowsAffected = statement.executeUpdate();

                logger.info(
                        "Eliminación completada. Filas eliminadas: {}",
                        rowsAffected
                );
            }

            logger.info("Saliendo del método deleteUser()");

        } catch (SQLException e) {

            logger.error(
                    "Error al eliminar el usuario: {}",
                    e.getMessage(),
                    e
            );

            logger.info("Saliendo del método deleteUser() con error");

            throw e;
        }
    }

    /**
     * Convierte una fila de un ResultSet en una entidad User.
     *
     * @param resultSet resultado de la consulta
     * @return entidad User creada a partir de la fila
     * @throws SQLException si se produce un error al obtener los datos
     */
    private User mapResultSetToUser(ResultSet resultSet)
            throws SQLException {

        logger.debug("Entrando en mapResultSetToUser()");

        Long id = resultSet.getLong("id");
        String username = resultSet.getString("username");
        String passwordHash = resultSet.getString("passwordHash");
        Boolean active = resultSet.getBoolean("active");
        Boolean accountNonLocked =
                resultSet.getBoolean("accountNonLocked");

        LocalDateTime lastPasswordChange =
                getLocalDateTime(resultSet, "lastPasswordChange");

        LocalDateTime passwordExpiresAt =
                getLocalDateTime(resultSet, "passwordExpiresAt");

        Integer failedLoginAttempts =
                resultSet.getInt("failedLoginAttempts");

        Boolean emailVerified =
                resultSet.getBoolean("emailVerified");

        Boolean mustChangePassword =
                resultSet.getBoolean("mustChangePassword");

        User user = new User(
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

        logger.debug("Saliendo de mapResultSetToUser()");

        return user;
    }

    /**
     * Convierte un valor SQL DATETIME en LocalDateTime.
     *
     * @param resultSet resultado de la consulta
     * @param columnName nombre de la columna
     * @return LocalDateTime o null si la columna es null
     * @throws SQLException si se produce un error al obtener el valor
     */
    private LocalDateTime getLocalDateTime(
            ResultSet resultSet,
            String columnName
    ) throws SQLException {

        logger.debug("Entrando en getLocalDateTime()");

        Timestamp timestamp = resultSet.getTimestamp(columnName);

        LocalDateTime value =
                timestamp != null
                        ? timestamp.toLocalDateTime()
                        : null;

        logger.debug("Saliendo de getLocalDateTime()");

        return value;
    }

    /**
     * Establece un LocalDateTime en un PreparedStatement.
     *
     * @param statement sentencia preparada
     * @param parameterIndex posición del parámetro
     * @param value fecha y hora que se desea establecer
     * @throws SQLException si se produce un error al establecer el valor
     */
    private void setLocalDateTime(
            PreparedStatement statement,
            int parameterIndex,
            LocalDateTime value
    ) throws SQLException {

        logger.debug("Entrando en setLocalDateTime()");

        if (value != null) {
            statement.setTimestamp(
                    parameterIndex,
                    Timestamp.valueOf(value)
            );
        } else {
            statement.setTimestamp(parameterIndex, null);
        }

        logger.debug("Saliendo de setLocalDateTime()");
    }
}