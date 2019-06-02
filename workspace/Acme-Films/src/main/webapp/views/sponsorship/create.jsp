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


<security:authorize access="hasRole('SPONSOR')">

	<jstl:choose>
		<jstl:when test="${isPrincipal}">
		<h1><spring:message	code="sponsorship.title.edit" /></h1>
		<form:form modelAttribute="createSponsorshipFormObject" action="sponsorship/create.do"
			id="form">
	
			<jstl:if test="${sponsorship.isActive eq null}">
			
				<acme:textbox code="sponsorship.title" path="title" size="100px" /><br> <br>
				<acme:textbox code="sponsorship.banner" path="banner" size="100px" /><br> <br>
				<acme:textbox code="sponsorship.targetPage" path="targetPage" size="100px" /><br> <br>
				
				<acme:multipleSelect items="${films}" itemLabel="title" code="sponsorship.films" path="films"/><br>
			
			</jstl:if>
			
			<acme:textbox code="sponsorship.holder" path="holder" size="100px" /><br> <br>
			<acme:textbox code="sponsorship.make" path="make" size="100px" /><br> <br>
			<acme:textbox code="sponsorship.number" path="number" size="100px" /><br> <br>
			<acme:textbox code="sponsorship.expirationMonth" path="expirationMonth" size="100px" /><br> <br>
			<acme:textbox code="sponsorship.expirationYear" path="expirationYear" size="100px" /><br> <br>
			<acme:textbox code="sponsorship.CVV" path="CVV" size="100px" /><br> <br>
			
			<acme:submit code="sponsorship.save" name="newSp" />&nbsp;
			<acme:cancel url="sponsorship/list.do" code="sponsorship.cancel" />
			<br />
	
		</form:form>
		</jstl:when>
	
	<jstl:otherwise>
		<p>
			<spring:message	code="sponsorship.not.allowed" /><br>
		</p>
	</jstl:otherwise>
	</jstl:choose>
</security:authorize>
