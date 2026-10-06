<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<fmt:setLocale value="${sessionScope.locale.language}" />
<fmt:setBundle basename="messages" />

<html>
    <head>
        <title>
            <fmt:message key="msg.title" />
        </title>
    </head>

    <body>

        <!-- Selector de idioma -->
        <form action="${pageContext.request.contextPath}/changeLanguage"
              method="get">

            <select name="lang" onchange="this.form.submit()">

                <option value="en"
                        ${sessionScope.locale.language == 'en' ? 'selected' : ''}>
                    English
                </option>

                <option value="es"
                        ${sessionScope.locale.language == 'es' ? 'selected' : ''}>
                    Español
                </option>

            </select>

        </form>

        <nav>
            <a href="${pageContext.request.contextPath}/">
                <fmt:message key="msg.nav.home" />
            </a>

            |

            <a href="${pageContext.request.contextPath}/users">
                <fmt:message key="msg.nav.users" />
            </a>
        </nav>