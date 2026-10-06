<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ include file="../../partials/header.jsp" %>

<h1>
    <fmt:message key="msg.user-list.title" />
</h1>

<a href="${pageContext.request.contextPath}/users?action=new">
    <fmt:message key="msg.user-list.add" />
</a>

<br><br>

<table border="1">

    <thead>
        <tr>
            <th><fmt:message key="msg.user-list.id" /></th>
            <th><fmt:message key="msg.user-list.username" /></th>
            <th><fmt:message key="msg.user-list.passwordHash" /></th>
            <th><fmt:message key="msg.user-list.active" /></th>
            <th><fmt:message key="msg.user-list.accountNonLocked" /></th>
            <th><fmt:message key="msg.user-list.lastPasswordChange" /></th>
            <th><fmt:message key="msg.user-list.passwordExpiresAt" /></th>
            <th><fmt:message key="msg.user-list.failedLoginAttempts" /></th>
            <th><fmt:message key="msg.user-list.emailVerified" /></th>
            <th><fmt:message key="msg.user-list.mustChangePassword" /></th>
            <th><fmt:message key="msg.user-list.actions" /></th>
        </tr>
    </thead>

    <tbody>

        <c:forEach var="user" items="${listUsers}">

            <tr>

                <td>${user.id}</td>

                <td>${user.username}</td>

                <td>${user.passwordHash}</td>

                <td>
                    <c:choose>
                        <c:when test="${user.active}">
                            <fmt:message key="msg.boolean.yes" />
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="msg.boolean.no" />
                        </c:otherwise>
                    </c:choose>
                </td>

                <td>
                    <c:choose>
                        <c:when test="${user.accountNonLocked}">
                            <fmt:message key="msg.boolean.yes" />
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="msg.boolean.no" />
                        </c:otherwise>
                    </c:choose>
                </td>

                <td>${user.lastPasswordChange}</td>

                <td>${user.passwordExpiresAt}</td>

                <td>${user.failedLoginAttempts}</td>

                <td>
                    <c:choose>
                        <c:when test="${user.emailVerified}">
                            <fmt:message key="msg.boolean.yes" />
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="msg.boolean.no" />
                        </c:otherwise>
                    </c:choose>
                </td>

                <td>
                    <c:choose>
                        <c:when test="${user.mustChangePassword}">
                            <fmt:message key="msg.boolean.yes" />
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="msg.boolean.no" />
                        </c:otherwise>
                    </c:choose>
                </td>

                <td>

                    <a href="${pageContext.request.contextPath}/users?action=edit&id=${user.id}">
                        <fmt:message key="msg.user-list.edit" />
                    </a>

                    <form action="${pageContext.request.contextPath}/users"
                          method="post"
                          style="display:inline;">

                        <input type="hidden"
                               name="id"
                               value="${user.id}">

                        <input type="hidden"
                               name="action"
                               value="delete">

                        <button type="submit"
                                onclick="return confirm('<fmt:message key="msg.user-list.confirm" />');">

                            <fmt:message key="msg.user-list.delete" />

                        </button>

                    </form>

                </td>

            </tr>

        </c:forEach>

    </tbody>

</table>

<%@ include file="../../partials/footer.jsp" %>