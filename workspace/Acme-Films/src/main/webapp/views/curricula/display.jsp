<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="permitAll">
	<form:form modelAttribute="curricula"
		action="curricula/critic/display.do" id="form">
		<form:hidden path="id" />
		<table class="displayStyle">

			<tr>
				<td><strong><spring:message
							code="curricula.personalData" />: </strong></td>


				<td><jstl:out value="${personalData.fullName}"></jstl:out></td>
				<td><a
					href="personalData/critic/display.do?dataId=${personalData.id}&curriculaId=${curricula.id}">
						<spring:message code="curricula.personalData.display" />
				</a></td>

			</tr>

			<tr>
				<td><strong><spring:message
							code="curricula.first.miscellaneousData" />: </strong></td>
				<jstl:if test="${!emptyMiscellaneous}">
					<td><jstl:out value="${miscellanousData.text}"></jstl:out></td>

					<td><a
						href="miscellaneousData/critic/list.do?curriculaId=${curricula.id}">
							<spring:message code="curricula.miscellaneousData.list" />
					</a></td>
				</jstl:if>
				<jstl:if test="${emptyMiscellaneous}">
					<security:authorize access="hasRole('CRITIC')">

						<td><spring:message code="curricula.empty.data" /></td>
						<td>
						<jstl:if test="${not empty principal and principal.curricula.id == currentCurricula.id}">
						<a
							href="miscellaneousData/critic/create.do?curriculaId=${curricula.id}">
								<spring:message code="curricula.miscellaneousData.create" />
						</a></jstl:if></td>
					</security:authorize>

				</jstl:if>
			</tr>

			<tr>
				<td><strong><spring:message
							code="curricula.first.educationData" />: </strong></td>
				<jstl:if test="${!emptyEducation}">
					<td><jstl:out value="${educationData.degree}"></jstl:out></td>

					<td><a
						href="educationData/critic/list.do?curriculaId=${curricula.id}">
							<spring:message code="curricula.educationData.list" />
					</a></td>
				</jstl:if>
				<jstl:if test="${emptyEducation}">
					<security:authorize access="hasRole('CRITIC')">

						<td><spring:message code="curricula.empty.data" /></td>
						<td><jstl:if test="${not empty principal and principal.curricula.id == currentCurricula.id}"><a
							href="educationData/critic/create.do?curriculaId=${curricula.id}">
								<spring:message code="curricula.educationData.create" />
						</a></jstl:if></td>
					</security:authorize>

				</jstl:if>
			</tr>

			<tr>
				<td><strong><spring:message
							code="curricula.first.professionalData" />: </strong></td>
				<jstl:if test="${!emptyProfessional}">
					<td><jstl:out value="${professionalData.title}"></jstl:out></td>

					<td><a
						href="professionalData/critic/list.do?curriculaId=${curricula.id}">
							<spring:message code="curricula.professionalData.list" />
					</a></td>
				</jstl:if>

				<jstl:if test="${emptyProfessional}">
					<security:authorize access="hasRole('CRITIC')">

						<td><spring:message code="curricula.empty.data" /></td>
						<td>
								<jstl:if test="${not empty principal and principal.curricula.id == currentCurricula.id}">
						
						<a
							href="professionalData/critic/create.do?curriculaId=${curricula.id}">
								<spring:message code="curricula.professionalData.create" />
						</a></jstl:if></td>
					</security:authorize>

				</jstl:if>
			</tr>


		</table>
		<security:authorize access="hasRole('CRITIC')">
		<jstl:if test="${not empty principal and principal.curricula.id == currentCurricula.id}">
		
			<input type="submit" name="delete"
				value="<spring:message code="curricula.delete"/>" />
				</jstl:if>
		</security:authorize>
		<acme:cancel
			url="curricula/critic/list.do"
			code="curricula.cancel" />
		
	</form:form>
</security:authorize>