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
		<form:form action="comment/filmEnthusiast/createForum.do"
			modelAttribute="comment">

			<form:label path="forum">
				<spring:message code="comment.forum" />:
	</form:label>
			<form:select path="forum">
				<form:option label="-----" value="0" />
				<form:options items="${forums}" itemLabel="comment.forum"
					itemValue="id" />
			</form:select>
			<form:errors cssClass="error" path="forum" />
			<br />
			<br />




			<acme:textbox code="film.body" path="body" />
			<br>
			<br>
		</form:form>
	</jstl:if>
</security:authorize>
