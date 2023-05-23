<%@ page import="com.topjava.webapp.model.ContactType" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta http-equiv="content-type" content="text/html' charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>Список всех резюме</title>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/fragments/header.jsp"/>
<section>
    <table id="resumes_table">
        <tr>
            <th>Имя</th>
            <th>Email</th>
            <th></th>
            <th></th>
        </tr>
        <jsp:useBean id="resumes" scope="request" type="java.util.List"/>
        <c:forEach var="resume" items="${resumes}">
            <jsp:useBean id="resume" type="com.topjava.webapp.model.Resume"/>
            <tr>
                <td><a href="resume?uuid=${resume.uuid}&action=view">${resume.fullName}</a></td>
                <td>${resume.getContact(ContactType.EMAIL)}</td>
                <td><a href="resume?uuid=${resume.uuid}&action=delete"><img src="img/delete.png" alt="Удалить"></a></td>
                <td><a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/edit.png" alt="Редактировать"></a></td>
            </tr>
        </c:forEach>
    </table>
</section>
<jsp:include page="/WEB-INF/jsp/fragments/footer.jsp"/>
</body>
</html>