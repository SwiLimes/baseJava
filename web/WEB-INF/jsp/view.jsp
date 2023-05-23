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
            <h2><%=sectionEntry.getKey().getTitle()%></h2>
        <c:choose>
        <c:when test="${sectionEntry.key.name().equals('PERSONAL') || sectionEntry.key.name().equals('OBJECTIVE')}">
                <%=((TextSection) sectionEntry.getValue()).getText()%>
        </c:when>
        <c:when test="${sectionEntry.key.name().equals('ACHIEVEMENT') || sectionEntry.key.name().equals('QUALIFICATIONS')}">
    <ul>
        <c:forEach var="item" items="<%=((ListSection) sectionEntry.getValue()).getItems()%>">
            <li>${item}</li>
        </c:forEach>
    </ul>
    </c:when>
    <c:when test="${sectionEntry.key.name().equals('EXPERIENCE') || sectionEntry.key.name().equals('EDUCATION')}">
        <c:forEach var="company" items="<%=((CompanySection) sectionEntry.getValue()).getCompanies()%>">
            <table id="sections_table">
                <tr>
                    <td colspan="2">
                        <c:choose>
                            <c:when test="${company.website != null}">
                                <h3><a href="${company.website}">${company.name}</a></h3>
                            </c:when>
                            <c:otherwise>
                                <h3>${company.name}</h3>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <c:forEach var="period" items="${company.periods}">
                    <jsp:useBean id="period" type="com.topjava.webapp.model.Company.Period"/>
                    <tr>
                        <td id="dates"><%=DateUtil.toHtml(period.getStartDate(), period.getEndDate())%></td>
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