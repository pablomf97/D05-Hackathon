<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasAnyRole('FILMENTHUSIAST','MODERATOR')">
	<table class="displayStyle">
		<tr>

			<td><strong><spring:message code="group.name" /> : </strong></td>
			<td><jstl:out value="${group.name}">
				</jstl:out></td>
		</tr>
		<tr>

			<td><strong><spring:message code="group.creationDate" />
					: </strong></td>
			<td><jstl:out value="${group.creationDate}">
				</jstl:out></td>
		</tr>
		<tr>

			<td><strong><spring:message code="group.creator" /> :
			</strong></td>
			<td><jstl:out value="${group.creator.userAccount.username}">
				</jstl:out></td>
		</tr>
		<tr>
			<td><strong><spring:message code="group.about" /> : </strong></td>

			<jstl:if test="${not empty group.sagaAbout }">
				<td><jstl:out value="${group.sagaAbout.title}"></jstl:out></td>
			</jstl:if>
			<jstl:if test="${not empty group.filmAbout }">
				<td><jstl:out value="${group.filmAbout.title}"></jstl:out></td>
			</jstl:if>

		</tr>
		<tr>

			<td><strong><spring:message code="group.description" />
					: </strong></td>
			<td><jstl:out value="${group.description}">
				</jstl:out></td>
		</tr>

	</table>
	<!--  Lista de comentarios
	  <jstl:if test="${group.creator eq actor }">
		<input type="button" name="list"
			value="<spring:message code="group.list.members"	/>"
			onclick="redirect: location.href = 'group/filmenthusiast/listMembers.do?Id=${group.id}';" />
			
			<input type="button" name="list"
			value="<spring:message code="group.delete"	/>"
			onclick="redirect: location.href = 'group/delete.do?Id=${group.id}';" />
	</jstl:if>-->
	<!-- Delete -->
	<jstl:if test="${group.creator.id eq actor.id and group.isActive}">
		</jstl:if>	
	
		<input type="button" name="list"
			value="<spring:message code="group.delete"	/>"
			onclick="redirect: location.href = 'group/delete.do?Id=${group.id}';" />

	<!-- edit -->
	<jstl:if test="${group.creator eq actor}">
		<input type="button" name="list"
			value="<spring:message code="group.edit"/>"
			onclick="redirect: location.href = 'group/edit.do?Id=${group.id}';" />
	</jstl:if>
	<!-- moderator -->
	<jstl:if
		test="${contains and group.maximumCapacity le fn:length(group.attenders)}">
		<input type="button" name="list"
			value="<spring:message code="group.request.members"	/>"
			onclick="redirect: location.href = 'group/moderator/activate.do?Id=${group.id}';" />
	</jstl:if>
	<acme:cancel url="group/list.do" code="group.cancel" />
	<security:authorize access="hasRole('FILMENTHUSIAST')">
		<jstl:set var="contains2" value="no" />

		<jstl:forEach var="item2" items="${event.forum.groupMembers}">
			<jstl:if test="${item2 eq actor}">
				<jstl:set var="contains2" value="yes" />
			</jstl:if>
		</jstl:forEach>
		<jstl:if test="${contains2 eq 'no'}">
			<a href="group/filmenthusiast/request.do?Id=${group.id}"> <spring:message
					code="group.request" />
			</a>
		</jstl:if>
	</security:authorize>
</security:authorize>
