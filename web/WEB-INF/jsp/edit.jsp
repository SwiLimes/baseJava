<%@ page import="com.topjava.webapp.model.ContactType" %>
<%@ page import="com.topjava.webapp.model.SectionType" %>
<%@ page import="com.topjava.webapp.model.ListSection" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="resume" scope="request" type="com.topjava.webapp.model.Resume"/>
<html>
<head>
    <meta http-equiv="content-type" content="text/html' charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd>
                <label>
                    <input type="text" name="fullName" size="50" value="${resume.fullName}">
                </label>
            </dd>
        </dl>
        <h2>Контакты:</h2>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.title}</dt>
                <dd>
                    <label>
                        <input type="text" name="${type.name()}" size="50" value="${resume.getContact(type)}">
                    </label>
                </dd>
            </dl>
        </c:forEach>
        <h2>Секции:</h2>
        <c:forEach var="type" items="<%=SectionType.values()%>">
            <c:set var="section" value="${resume.getSection(type)}"/>
            <jsp:useBean id="section" type="com.topjava.webapp.model.AbstractSection" scope="session"/>
            <dl>
                <h3>${type.title}</h3>
                <c:choose>
                    <c:when test="${type.name().equals('PERSONAL') || type.name().equals('OBJECTIVE')}">
                        <label>
                            <input type="text" name="${type}" size="90" value="<%=section%>"/>
                        </label>
                    </c:when>
                    <c:when test="${type.name().equals('ACHIEVEMENT') || type.name().equals('QUALIFICATIONS')}">
                        <label>
                            <textarea name="${type}" cols="90"
                                      rows="6"><%=String.join("\n", ((ListSection) section).getItems())%></textarea>
                        </label>
                    </c:when>
                    <c:when test="${type.name().equals('EXPERIENCE') || type.name().equals('EDUCATION')}">
                    </c:when>
                </c:choose>
            </dl>
        </c:forEach>
        <button type="submit">Сохранить</button>
        <button type="reset" onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="/WEB-INF/jsp/fragments/footer.jsp"/>
</body>
</html>