package org.iesaxilar.daw2.franfernandez.daos;

import org.iesaxilar.daw2.franfernandez.entities.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Define las operaciones de acceso a datos para la entidad {@link User}.
 *
 * <p>Esta interfaz establece el contrato que deberá cumplir
 * la implementación {@code UserDAOImpl} para realizar el CRUD
 * de usuarios.</p>
 *
 * @author Fran Fernandez
 * @version 1.0-SNAPSHOT
 */
public interface UserDAO {

    /**
     * Obtiene todos los usuarios registrados.
     *
     * @return lista de usuarios
     * @throws SQLException si se produce un error al consultar la base de datos
     */
    List<User> listAllUsers() throws SQLException;

    /**
     * Inserta un nuevo usuario en la base de datos.
     *
     * @param user usuario que se desea insertar
     * @throws SQLException si se produce un error durante la inserción
     */
    void insertUser(User user) throws SQLException;

    /**
     * Comprueba si existe un usuario con un determinado nombre de usuario.
     *
     * @param username nombre de usuario que se desea comprobar
     * @return true si existe; false en caso contrario
     * @throws SQLException si se produce un error al consultar la base de datos
     */
    boolean existsUserByUsername(String username) throws SQLException;

    /**
     * Obtiene un usuario mediante su identificador.
     *
     * @param id identificador del usuario
     * @return usuario encontrado o null si no existe
     * @throws SQLException si se produce un error al consultar la base de datos
     */
    User getUserById(Long id) throws SQLException;

    /**
     * Actualiza un usuario existente.
     *
     * @param user usuario con los datos actualizados
     * @throws SQLException si se produce un error durante la actualización
     */
    void updateUser(User user) throws SQLException;

    /**
     * Comprueba si existe otro usuario con el mismo username,
     * excluyendo al usuario indicado por su identificador.
     *
     * <p>Este método será utilizado durante la edición para evitar
     * duplicar usernames.</p>
     *
     * @param username nombre de usuario que se desea comprobar
     * @param id identificador del usuario que se está editando
     * @return true si otro usuario utiliza el username; false en caso contrario
     * @throws SQLException si se produce un error al consultar la base de datos
     */
    boolean existsUserByUsernameAndNotId(String username, Long id)
            throws SQLException;

    /**
     * Elimina un usuario mediante su identificador.
     *
     * @param id identificador del usuario que se desea eliminar
     * @throws SQLException si se produce un error durante la eliminación
     */
    void deleteUser(Long id) throws SQLException;
}