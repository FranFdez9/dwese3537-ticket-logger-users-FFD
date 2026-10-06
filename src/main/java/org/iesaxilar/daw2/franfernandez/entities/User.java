package org.iesaxilar.daw2.franfernandez.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Entidad que representa un usuario de la aplicación.
 *
 * <p>Esta clase representa un registro de la tabla {@code users}
 * de la base de datos.</p>
 *
 * @author Fran Fernandez
 * @version 1.0-SNAPSHOT
 */
public class User {

    private static final Logger logger =
            LoggerFactory.getLogger(User.class);

    private Long id;
    private String username;
    private String passwordHash;
    private Boolean active;
    private Boolean accountNonLocked;
    private LocalDateTime lastPasswordChange;
    private LocalDateTime passwordExpiresAt;
    private Integer failedLoginAttempts;
    private Boolean emailVerified;
    private Boolean mustChangePassword;

    /**
     * Constructor vacío.
     */
    public User() {
        logger.debug("Entrando en el constructor vacío de User");
        logger.debug("Saliendo del constructor vacío de User");
    }

    /**
     * Constructor completo de la entidad User.
     *
     * @param id identificador único del usuario
     * @param username nombre de usuario
     * @param passwordHash contraseña del usuario
     * @param active indica si la cuenta está activa
     * @param accountNonLocked indica si la cuenta no está bloqueada
     * @param lastPasswordChange fecha y hora del último cambio de contraseña
     * @param passwordExpiresAt fecha y hora de caducidad de la contraseña
     * @param failedLoginAttempts número de intentos de login fallidos
     * @param emailVerified indica si el correo electrónico ha sido verificado
     * @param mustChangePassword indica si el usuario debe cambiar la contraseña
     */
    public User(Long id,
                String username,
                String passwordHash,
                Boolean active,
                Boolean accountNonLocked,
                LocalDateTime lastPasswordChange,
                LocalDateTime passwordExpiresAt,
                Integer failedLoginAttempts,
                Boolean emailVerified,
                Boolean mustChangePassword) {

        logger.debug("Entrando en el constructor completo de User");

        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.active = active;
        this.accountNonLocked = accountNonLocked;
        this.lastPasswordChange = lastPasswordChange;
        this.passwordExpiresAt = passwordExpiresAt;
        this.failedLoginAttempts = failedLoginAttempts;
        this.emailVerified = emailVerified;
        this.mustChangePassword = mustChangePassword;

        logger.debug("Saliendo del constructor completo de User");
    }

    /**
     * Obtiene el identificador del usuario.
     *
     * @return identificador del usuario
     */
    public Long getId() {
        logger.debug("Entrando en getId()");
        logger.debug("Saliendo de getId()");
        return id;
    }

    /**
     * Establece el identificador del usuario.
     *
     * @param id identificador del usuario
     */
    public void setId(Long id) {
        logger.debug("Entrando en setId()");
        this.id = id;
        logger.debug("Saliendo de setId()");
    }

    /**
     * Obtiene el nombre de usuario.
     *
     * @return nombre de usuario
     */
    public String getUsername() {
        logger.debug("Entrando en getUsername()");
        logger.debug("Saliendo de getUsername()");
        return username;
    }

    /**
     * Establece el nombre de usuario.
     *
     * @param username nombre de usuario
     */
    public void setUsername(String username) {
        logger.debug("Entrando en setUsername()");
        this.username = username;
        logger.debug("Saliendo de setUsername()");
    }

    /**
     * Obtiene la contraseña.
     *
     * @return contraseña
     */
    public String getPasswordHash() {
        logger.debug("Entrando en getPasswordHash()");
        logger.debug("Saliendo de getPasswordHash()");
        return passwordHash;
    }

    /**
     * Establece la contraseña.
     *
     * @param passwordHash contraseña
     */
    public void setPasswordHash(String passwordHash) {
        logger.debug("Entrando en setPasswordHash()");
        this.passwordHash = passwordHash;
        logger.debug("Saliendo de setPasswordHash()");
    }

    /**
     * Indica si la cuenta está activa.
     *
     * @return true si la cuenta está activa
     */
    public Boolean getActive() {
        logger.debug("Entrando en getActive()");
        logger.debug("Saliendo de getActive()");
        return active;
    }

    /**
     * Establece si la cuenta está activa.
     *
     * @param active estado de la cuenta
     */
    public void setActive(Boolean active) {
        logger.debug("Entrando en setActive()");
        this.active = active;
        logger.debug("Saliendo de setActive()");
    }

    /**
     * Indica si la cuenta no está bloqueada.
     *
     * @return true si la cuenta no está bloqueada
     */
    public Boolean getAccountNonLocked() {
        logger.debug("Entrando en getAccountNonLocked()");
        logger.debug("Saliendo de getAccountNonLocked()");
        return accountNonLocked;
    }

    /**
     * Establece el estado de bloqueo de la cuenta.
     *
     * @param accountNonLocked true si la cuenta no está bloqueada
     */
    public void setAccountNonLocked(Boolean accountNonLocked) {
        logger.debug("Entrando en setAccountNonLocked()");
        this.accountNonLocked = accountNonLocked;
        logger.debug("Saliendo de setAccountNonLocked()");
    }

    /**
     * Obtiene la fecha del último cambio de contraseña.
     *
     * @return fecha y hora del último cambio
     */
    public LocalDateTime getLastPasswordChange() {
        logger.debug("Entrando en getLastPasswordChange()");
        logger.debug("Saliendo de getLastPasswordChange()");
        return lastPasswordChange;
    }

    /**
     * Establece la fecha del último cambio de contraseña.
     *
     * @param lastPasswordChange fecha y hora del último cambio
     */
    public void setLastPasswordChange(LocalDateTime lastPasswordChange) {
        logger.debug("Entrando en setLastPasswordChange()");
        this.lastPasswordChange = lastPasswordChange;
        logger.debug("Saliendo de setLastPasswordChange()");
    }

    /**
     * Obtiene la fecha de caducidad de la contraseña.
     *
     * @return fecha y hora de caducidad
     */
    public LocalDateTime getPasswordExpiresAt() {
        logger.debug("Entrando en getPasswordExpiresAt()");
        logger.debug("Saliendo de getPasswordExpiresAt()");
        return passwordExpiresAt;
    }

    /**
     * Establece la fecha de caducidad de la contraseña.
     *
     * @param passwordExpiresAt fecha y hora de caducidad
     */
    public void setPasswordExpiresAt(LocalDateTime passwordExpiresAt) {
        logger.debug("Entrando en setPasswordExpiresAt()");
        this.passwordExpiresAt = passwordExpiresAt;
        logger.debug("Saliendo de setPasswordExpiresAt()");
    }

    /**
     * Obtiene el número de intentos de login fallidos.
     *
     * @return número de intentos fallidos
     */
    public Integer getFailedLoginAttempts() {
        logger.debug("Entrando en getFailedLoginAttempts()");
        logger.debug("Saliendo de getFailedLoginAttempts()");
        return failedLoginAttempts;
    }

    /**
     * Establece el número de intentos de login fallidos.
     *
     * @param failedLoginAttempts número de intentos fallidos
     */
    public void setFailedLoginAttempts(Integer failedLoginAttempts) {
        logger.debug("Entrando en setFailedLoginAttempts()");
        this.failedLoginAttempts = failedLoginAttempts;
        logger.debug("Saliendo de setFailedLoginAttempts()");
    }

    /**
     * Indica si el correo electrónico ha sido verificado.
     *
     * @return true si el correo ha sido verificado
     */
    public Boolean getEmailVerified() {
        logger.debug("Entrando en getEmailVerified()");
        logger.debug("Saliendo de getEmailVerified()");
        return emailVerified;
    }

    /**
     * Establece el estado de verificación del correo electrónico.
     *
     * @param emailVerified estado de verificación
     */
    public void setEmailVerified(Boolean emailVerified) {
        logger.debug("Entrando en setEmailVerified()");
        this.emailVerified = emailVerified;
        logger.debug("Saliendo de setEmailVerified()");
    }

    /**
     * Indica si el usuario debe cambiar su contraseña.
     *
     * @return true si debe cambiarla
     */
    public Boolean getMustChangePassword() {
        logger.debug("Entrando en getMustChangePassword()");
        logger.debug("Saliendo de getMustChangePassword()");
        return mustChangePassword;
    }

    /**
     * Establece si el usuario debe cambiar su contraseña.
     *
     * @param mustChangePassword true si debe cambiarla
     */
    public void setMustChangePassword(Boolean mustChangePassword) {
        logger.debug("Entrando en setMustChangePassword()");
        this.mustChangePassword = mustChangePassword;
        logger.debug("Saliendo de setMustChangePassword()");
    }
}