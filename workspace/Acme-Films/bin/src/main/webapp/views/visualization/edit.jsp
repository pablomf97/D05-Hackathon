<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<security:authorize access="hasRole('MODERATOR')">

	<jstl:choose>
		<jstl:when test="${isPrincipal}">
		<h1><spring:message	code="visualization.title.edit" /></h1>
		<form:form modelAttribute="visualization" action="visualization/edit.do"
			id="form">
	
			<form:hidden path="id"/>
			<jstl:if test="${visualization.id == 0}">
				<form:hidden path="film"/>
			</jstl:if>
			
			<acme:textbox code="visualization.siteName" path="siteName" size="100px" /><br> <br>
			<acme:textbox code="visualization.price" path="price" size="100px" /><br> <br>
			<acme:textbox code="visualization.link" path="link" size="100px" /><br> <br>
			
			<acme:submit code="visualization.save" name="save" />&nbsp;
			<acme:cancel url="visualization/list.do?filmId=${visualization.film.id }" code="visualization.cancel" />
			<br />
	
		</form:form>
		</jstl:when>
	
	<jstl:otherwise>
		<p>
			<spring:message	code="visualization.not.allowed" /><br>
		</p>
	</jstl:otherwise>
	</jstl:choose>
</security:authorize>
