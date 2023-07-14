<%@ page import="com.topjava.webapp.model.ContactType" %>
<%@ page import="com.topjava.webapp.model.SectionType" %>
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
            <jsp:useBean id="section" type="com.topjava.webapp.model.AbstractSection"/>
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
                        <c:forEach var="company" items="<%=((CompanySection) section).getCompanies()%>" varStatus="counter">
                            <dl>
                                <dt>Название учереждения:</dt>
                                <dd>
                                    <label>
                                        <input type="text" name="${type}" size="120" value="${company.name}">
                                    </label>
                                </dd>
                            </dl>
                            <dl>
                                <dt>Сайт учереждения:</dt>
                                <dd>
                                    <label>
                                        <input type="text" name="${type}url" size="120" value="${company.website}">
                                    </label>
                                </dd>
                            </dl>
                            <br>
                            <div style="margin-left: 30px">
                                <c:forEach var="period" items="${company.periods}">
                                    <jsp:useBean id="period" type="com.topjava.webapp.model.Company.Period"/>
                                    <dl>
                                        <dt>Начальная дата:</dt>
                                        <dd>
                                            <label>
                                                <input type="text" name="${type}${counter.index}startDate" size="10"
                                                    value="<%=DateUtil.toHtml(period.getStartDate())%>" placeholder="MM/yyyy">
                                            </label>
                                        </dd>
                                    </dl>
                                    <dl>
                                        <dt>Конечная дата:</dt>
                                        <dd>
                                            <label>
                                                <input type="text" name="${type}${counter.index}endDate" size="10"
                                                       value="<%=DateUtil.toHtml(period.getEndDate())%>" placeholder="MM/yyyy"/>
                                            </label>
                                        </dd>
                                    </dl>
                                    <dl>
                                        <dt>Должность:</dt>
                                        <dd>
                                            <label>
                                                <input type="text" name="${type}${counter.index}title" size="10"
                                                       value="${period.title}"/>
                                            </label>
                                        </dd>
                                    </dl>
                                    <dl>
                                        <dt>Описание:</dt>
                                        <dd>
                                            <label>
                                                <input type="text" name="${type}${counter.index}description" size="10"
                                                       value="${period.description}"/>
                                            </label>
                                        </dd>
                                    </dl>
                                </c:forEach>
                            </div>
                        </c:forEach>
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