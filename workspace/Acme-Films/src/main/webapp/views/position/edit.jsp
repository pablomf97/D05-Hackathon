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

	<h1><spring:message	code="position.title.edit" /></h1>
	<form:form action="position/moderator/edit.do"
		modelAttribute="position" id="form1">

		<form:hidden path="id" />

		<p>
			<spring:message code="position.name.es" />
		</p>
		
		<input type="text" name="nameES" id="nameES"
			value="${position.name.get('Español')}"
			placeholder="<spring:message code='position.edit.placeholder.es' />"
			required style="width: 25%">

		<form:errors cssClass="error" path="name" />
		<br />
		<br />

		<p>
			<spring:message code="position.name.en" />
		</p>
		<input type="text" name="nameEN" id="nameEN"
			value="${position.name.get('English')}"
			placeholder="<spring:message code='position.edit.placeholder.en' />"
			required style="width: 25%">
		<form:errors cssClass="error" path="name" />
		<br />
		<br />
		
		<jstl:choose>
			<jstl:when test="${pageContext.response.locale.language == 'es'}">
				<acme:multipleSelect items="${positions}" itemLabel="${position.name.get('Español')}" code="position.parent" path="positions"/>
			</jstl:when>
			<jstl:otherwise>
				<acme:multipleSelect items="${positions}" itemLabel="${position.name.get('English')}" code="position.parent" path="positions"/>
			</jstl:otherwise>
		</jstl:choose>
		
		<acme:submit code="position.save" name="save"/>&nbsp;

		<jstl:if test="${position.id != 0}">
			<acme:delete code="position.delete" name="delete" confirmation="position.confirm.delete"/>&nbsp;
		</jstl:if>
		<acme:cancel code="position.cancel" url="position/administrator/list.do"/><br/><br/>
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