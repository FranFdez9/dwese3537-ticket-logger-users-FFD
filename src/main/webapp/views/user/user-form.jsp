<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ include file="../../partials/header.jsp" %>

<h1>
    <c:choose>
        <c:when test="${user == null}">
            <fmt:message key="msg.user-form.add" />
        </c:when>
        <c:otherwise>
            <fmt:message key="msg.user-form.edit" />
        </c:otherwise>
    </c:choose>
</h1>

<c:if test="${not empty errorMessageKey}">
    <div class="error-message">
        <fmt:message key="${errorMessageKey}" />
    </div>
</c:if>

<form action="${pageContext.request.contextPath}/users"
      method="post">

    <!-- Identificador -->
    <input type="hidden"
           name="id"
           value="${user != null ? user.id : ''}" />

    <!-- Acción -->
    <input type="hidden"
           name="action"
           value="${user == null ? 'insert' : 'update'}" />

    <!-- Username -->
    <label for="username">
        <fmt:message key="msg.user-form.username" />:
    </label>

    <input type="text"
           name="username"
           id="username"
           maxlength="40"
           required
           value="${user != null ? user.username : ''}" />

    <br><br>

    <!-- Password -->
    <label for="passwordHash">
        <fmt:message key="msg.user-form.passwordHash" />:
    </label>

    <input type="text"
           name="passwordHash"
           id="passwordHash"
           maxlength="500"
           required
           value="${user != null ? user.passwordHash : ''}" />

    <br><br>

    <!-- Active -->
    <label for="active">
        <fmt:message key="msg.user-form.active" />:
    </label>

    <input type="checkbox"
           name="active"
           id="active"
           value="true"
           <c:if test="${user == null or user.active}">
               checked="checked"
           </c:if> />

    <br><br>

    <!-- Account Non Locked -->
    <label for="accountNonLocked">
        <fmt:message key="msg.user-form.accountNonLocked" />:
    </label>

    <input type="checkbox"
           name="accountNonLocked"
           id="accountNonLocked"
           value="true"
           <c:if test="${user == null or user.accountNonLocked}">
               checked="checked"
           </c:if> />

    <br><br>

    <!-- Last Password Change -->
    <label for="lastPasswordChange">
        <fmt:message key="msg.user-form.lastPasswordChange" />:
    </label>

    <input type="datetime-local"
           name="lastPasswordChange"
           id="lastPasswordChange"
           required
           value="${user != null && user.lastPasswordChange != null
                    ? user.lastPasswordChange
                    : ''}" />

    <br><br>

    <!-- Password Expires At -->
    <label for="passwordExpiresAt">
        <fmt:message key="msg.user-form.passwordExpiresAt" />:
    </label>

    <input type="datetime-local"
           name="passwordExpiresAt"
           id="passwordExpiresAt"
           readonly
           value="${user != null && user.passwordExpiresAt != null
                    ? user.passwordExpiresAt
                    : ''}" />

    <br><br>

    <!-- Failed Login Attempts -->
    <label for="failedLoginAttempts">
        <fmt:message key="msg.user-form.failedLoginAttempts" />:
    </label>

    <input type="number"
           name="failedLoginAttempts"
           id="failedLoginAttempts"
           min="0"
           value="${user != null ? user.failedLoginAttempts : '0'}" />

    <br><br>

    <!-- Email Verified -->
    <label for="emailVerified">
        <fmt:message key="msg.user-form.emailVerified" />:
    </label>

    <input type="checkbox"
           name="emailVerified"
           id="emailVerified"
           value="true"
           <c:if test="${user != null and user.emailVerified}">
               checked="checked"
           </c:if> />

    <br><br>

    <!-- Must Change Password -->
    <label for="mustChangePassword">
        <fmt:message key="msg.user-form.mustChangePassword" />:
    </label>

    <input type="checkbox"
           name="mustChangePassword"
           id="mustChangePassword"
           value="true"
           <c:if test="${user != null and user.mustChangePassword}">
               checked="checked"
           </c:if> />

    <br><br>

    <!-- Botón -->
    <c:choose>

        <c:when test="${user == null}">
            <input type="submit"
                   value="<fmt:message key='msg.user-form.create' />" />
        </c:when>

        <c:otherwise>
            <input type="submit"
                   value="<fmt:message key='msg.user-form.update' />" />
        </c:otherwise>

    </c:choose>

</form>

<br>

<a href="${pageContext.request.contextPath}/users">
    <fmt:message key="msg.user-form.returnback" />
</a>

<%@ include file="../../partials/footer.jsp" %>