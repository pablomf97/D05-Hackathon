<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('FILMENTHUSIAST')">
	<jstl:if test="${possible == true && comment.id==0}">
		<form:form action="comment/filmEnthusiast/createFilm.do"
			modelAttribute="comment">

			<form:label path="film">
				<spring:message code="comment.film" />:
	</form:label>
			<form:select path="film">
				<form:option label="-----" value="0" />
				<form:options items="${films}" itemLabel="comment.film"
					itemValue="id" />
			</form:select>
			<form:errors cssClass="error" path="film" />
			<br />
			<br />




			<acme:textbox code="film.body" path="body" />
			<br>
			<br>

			<form:label path="rating">
				<spring:message code="comment.rating" />
			</form:label>
			<form:input path="rating" type="text" min="0" value="0" />
			<form:errors cssClass="error" path="rating" />

		</form:form>
	</jstl:if>
</security:authorize>
