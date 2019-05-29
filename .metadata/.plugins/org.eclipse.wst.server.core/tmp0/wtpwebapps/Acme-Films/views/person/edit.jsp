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


<security:authorize access="hasRole('PROVIDER')">

	<jstl:choose>
		<jstl:when test="${isPrincipal}">
		<h1><spring:message	code="person.title.edit" /></h1>
		<form:form modelAttribute="person" action="person/edit.do"
			id="form">
	
			<form:hidden path="id" />
			
			<acme:textbox code="person.name" path="name" size="100px" /><br> <br>
			<acme:textbox code="person.surname" path="surname" size="100px" /><br> <br>
			<acme:textbox code="person.gender" path="gender" size="100px" /><br> <br>
			<acme:textbox code="person.nationality" path="nationality" size="100px" /><br> <br>
			<acme:textbox code="person.birthDate" path="birthDate" size="100px" /><br> <br>
			<acme:textbox code="person.photo" path="photo" size="100px" /><br> <br>
			<jstl:choose>
				<jstl:when test="${pageContext.response.locale.language == 'es'}">
					<acme:multipleSelect items="${positions}" itemLabel="${position.name.get('Espa�ol')}" code="position.parent" path="positions"/>
				</jstl:when>
				<jstl:otherwise>
					<acme:multipleSelect items="${positions}" itemLabel="${position.name.get('English')}" code="position.parent" path="positions"/>
				</jstl:otherwise>
			</jstl:choose>
			
			<acme:submit code="person.save" name="save" />&nbsp;
			<acme:cancel url="person/list.do" code="person.cancel" />
			<br />
	
		</form:form>
		</jstl:when>
	
	<jstl:otherwise>
		<p>
			<spring:message	code="person.not.allowed" /><br>
		</p>
	</jstl:otherwise>
	</jstl:choose>
</security:authorize>