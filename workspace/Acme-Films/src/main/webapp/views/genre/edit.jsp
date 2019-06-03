<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('MODERATOR')">

	<h1><spring:message	code="genre.title.edit" /></h1>
	<form:form action="genre/moderator/edit.do"
		modelAttribute="genre" id="form1">

		<form:hidden path="id" />
		
		<p>
			<spring:message code="genre.name.es" />
		</p>
		
		<input type="text" name="nameES" id="nameES"
			value="${genre.name.get('Español')}"
			placeholder="<spring:message code='genre.edit.placeholder.es' />"
			required style="width: 25%">

		<form:errors cssClass="error" path="name" />
		<br /><br />

		<p>
			<spring:message code="genre.name.en" />
		</p>
		<input type="text" name="nameEN" id="nameEN"
			value="${genre.name.get('English')}"
			placeholder="<spring:message code='genre.edit.placeholder.en' />"
			required style="width: 25%">
		<form:errors cssClass="error" path="name" />
		<br /><br />
		
		<acme:submit code="genre.save" name="save"/>&nbsp;

		<jstl:if test="${genre.id != 0}">
			<acme:delete code="genre.delete" name="delete" confirmation="genre.confirm.delete"/>&nbsp;
		</jstl:if>
		<acme:cancel code="genre.cancel" url="genre/moderator/list.do"/><br/><br/>
		<form:errors cssClass="error" code="${message}" />
	</form:form>

	<script>
		$('#form1 input[type=text]')
				.on(
						'change invalid',
						function() {
							var textfield = $(this).get(0);
							textfield.setCustomValidity('');

							if (!textfield.validity.valid) {
								textfield
										.setCustomValidity('<spring:message code='not.blank' />');
							}
						});
	</script>
</security:authorize>

<security:authorize access="!hasRole('MODERATOR')">
		<p>
			<spring:message	code="sponsorship.not.allowed" /><br>
		</p>
</security:authorize>