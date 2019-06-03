<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<jstl:choose>
<jstl:when test="${isPrincipal }">
<security:authorize access="hasAnyRole('MODERATOR','SPONSOR')">

<h1><spring:message code="sponsorship.title.display" /></h1>
<table class="displayStyle">
	<tr>
		<td><strong> <spring:message code="sponsorship.title" /> : </strong></td>
		<td><jstl:out value="${sponsorship.title}"></jstl:out></td>
	</tr>
	
	<tr>
		<td><strong> <spring:message code="sponsorship.banner" /> : </strong></td>
		<td><jstl:out value="${sponsorship.banner}"></jstl:out></td>
	</tr>
	
	<tr>
		<td><strong> <spring:message code="sponsorship.targetPage" /> : </strong></td>
		<td><jstl:out value="${sponsorship.targetPage}"></jstl:out></td>
	</tr>
	
	<jstl:if test="${sponsorship.isActive eq true}">
		<spring:message var="status" code='sponsorship.active' />
	</jstl:if>
	<jstl:if test="${sponsorship.isActive eq null}">
		<spring:message var="status" code='sponsorship.pending' />
	</jstl:if>
	<jstl:if test="${sponsorship.isActive eq false}">
		<spring:message var="status" code='sponsorship.rejected' />
	</jstl:if>

	<tr>
		<td><strong> <spring:message code="sponsorship.isActive" /> : </strong></td>
		<td>${status}</td>
	</tr>
	
</table>
</security:authorize>

<security:authorize access="hasRole('SPONSOR')">

<h2><spring:message code="sponsorship.title.creditcard.display" /></h2>
<table class="displayStyle">
	<tr>
		<td><strong> <spring:message code="sponsorship.holder" /> : </strong></td>
		<td><jstl:out value="${sponsorship.creditCard.holder}"></jstl:out></td>
	</tr>
	<tr>
		<td><strong> <spring:message code="sponsorship.make" /> : </strong></td>
		<td><jstl:out value="${sponsorship.creditCard.make}"></jstl:out></td>
	</tr>
	<tr>
		<td><strong> <spring:message code="sponsorship.number" /> : </strong></td>
		<td><jstl:out value="${sponsorship.creditCard.number}"></jstl:out></td>
	</tr>
	<tr>
		<td><strong> <spring:message code="sponsorship.expirationMonth" /> : </strong></td>
		<td><jstl:out value="${sponsorship.creditCard.expirationMonth}"></jstl:out></td>
	</tr>
	<tr>
		<td><strong> <spring:message code="sponsorship.expirationYear" /> : </strong></td>
		<td><jstl:out value="${sponsorship.creditCard.expirationYear}"></jstl:out></td>
	</tr>
	<tr>
		<td><strong> <spring:message code="sponsorship.CVV" /> : </strong></td>
		<td><jstl:out value="${sponsorship.creditCard.CVV}"></jstl:out></td>
	</tr>
	
</table>

</security:authorize>
	
<security:authorize access="hasAnyRole('MODERATOR','SPONSOR')">

<h2><spring:message code="sponsorship.display.films" /></h2>
<display:table class="displaytag" name="${sponsorship.films}" pagesize="5" 
		requestURI="sponsorship/display.do" id="film">

	<display:column titleKey="sponsorship.film.name" sortable="true">
		<jstl:out value="${film.title}" />
	</display:column>
	
	<display:column >
		<a href="film/display.do?filmId=${film.id}"> <spring:message
				code="sponsorship.display" />
		</a>
	</display:column>
</display:table>

	<input type="button" name="back"
		value="<spring:message code="sponsorship.back" />"
		onclick="window.history.back()" />

</security:authorize>

<security:authorize access="!hasAnyRole('MODERATOR','SPONSOR')">
		<p>
			<spring:message	code="sponsorship.not.allowed" /><br>
		</p>
</security:authorize>
</jstl:when>
<jstl:otherwise>

<p>
	<spring:message	code="sponsorship.not.allowed" /><br>
</p>

</jstl:otherwise>
</jstl:choose>