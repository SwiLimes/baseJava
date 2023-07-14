<%@ page import="com.topjava.webapp.model.TextSection" %>
<%@ page import="com.topjava.webapp.model.ListSection" %>
<%@ page import="com.topjava.webapp.model.CompanySection" %>
<%@ page import="com.topjava.webapp.util.DateUtil" %>
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
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/edit.png" alt="Редактировать"></a></h2>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry" type="java.util.Map.Entry<com.topjava.webapp.model.ContactType, java.lang.String>"/>
                <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br>
        </c:forEach>
    </p>
    <p>
        <c:forEach var="sectionEntry" items="${resume.sections}">
            <jsp:useBean id="sectionEntry"
                         type="java.util.Map.Entry<com.topjava.webapp.model.SectionType, com.topjava.webapp.model.AbstractSection>"/>
            <c:set var="type" value="${sectionEntry.key}"/>
            <c:set var="section" value="${sectionEntry.value}"/>
            <jsp:useBean id="section" type="com.topjava.webapp.model.AbstractSection"/>
            <h2>${type.title}</h2>

        <c:choose>
            <c:when test="${type.name().equals('PERSONAL') || type.name().equals('OBJECTIVE')}">
                    <%=((TextSection) section).getText()%>
            </c:when>
            <c:when test="${type.name().equals('ACHIEVEMENT') || type.name().equals('QUALIFICATIONS')}">
                <ul>
                    <c:forEach var="item" items="<%=((ListSection) section).getItems()%>">
                        <li>${item}</li>
                    </c:forEach>
                </ul>
            </c:when>
            <c:when test="${type.name().equals('EXPERIENCE') || type.name().equals('EDUCATION')}">
                <c:forEach var="company" items="<%=((CompanySection) section).getCompanies()%>">
                    <table id="sections_table">
                        <tr>
                            <td colspan="2">
                                <c:choose>
                                    <c:when test="${empty company.website}">
                                        <h3>${company.name}</h3>
                                    </c:when>
                                    <c:otherwise>
                                        <h3><a href="${company.website}">${company.name}</a></h3>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <c:forEach var="period" items="${company.periods}">
                            <jsp:useBean id="period" type="com.topjava.webapp.model.Company.Period"/>
                            <tr>
                                <td id="dates"><%=DateUtil.toHtml(period.getStartDate()) + " - " +  DateUtil.toHtml(period.getEndDate())%></td>
                                <td><b>${period.title}</b><br>${period.description}</td>
                            </tr>
                        </c:forEach>
                    </table>
                </c:forEach>
            </c:when>
        </c:choose><br>
    </c:forEach>
</section>
<jsp:include page="/WEB-INF/jsp/fragments/footer.jsp"/>
</body>
</html>