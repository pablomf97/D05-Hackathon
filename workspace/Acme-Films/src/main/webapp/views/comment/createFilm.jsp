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
				<form:options items="${films}" itemLabel="title" itemValue="id" />
			</form:select>
			<form:errors cssClass="error" path="film" />
			<br />
			<br />

			<form:label path="forum">
				<spring:message code="comment.forum" />:
	</form:label>
			<form:select path="forum">
				<form:option label="-----" value="0" />
				<form:options items="${forums}" itemLabel="name" itemValue="id" />
			</form:select>
			<form:errors cssClass="error" path="forum" />
			<br />
			<br />




			<acme:textarea code="comment.body" path="body" />
			<br>
			<br>

			<form:label path="rating">
				<spring:message code="comment.rating" />
			</form:label>
			<form:input path="rating" type="number" min="0" max="10" step="any" />
			<form:errors cssClass="error" path="rating" />

			<br>
			<br>


			<acme:submit name="save" code="comment.save" />

			<acme:cancel url="../" code="comment.back" />


		</form:form>


	</jstl:if>

	<jstl:if test="${possible == false && comment.id!=0}">
		<spring:message code="comment.nopermission" var="permission"></spring:message>

		<strong><jstl:out value="${permission}">
			</jstl:out></strong>
	</jstl:if>

	<jstl:if test="${possible == false && comment.id ==0}">
		

			<strong><jstl:out value="${error}"></jstl:out></strong>
		
	</jstl:if>
	
</security:authorize>

