<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('MODERATOR')">
	<!-- Listing grid -->
	<jstl:if test="${possible}">

		<display:table pagesize="5" class="displaytag" name="reviews"
			requestURI="review/moderator/listToAssign.do" id="row">


			<!-- Attributes-->

			<display:column titleKey="review.title" sortable="true">
				<jstl:out value="${row.title }"></jstl:out>
			</display:column>

			<display:column titleKey="review.body" sortable="true">
				<jstl:out value="${row.body}"></jstl:out>
			</display:column>

			<display:column titleKey="review.rating">
				<jstl:out value="${row.rating}"></jstl:out>
			</display:column>


			<display:column titleKey="review.status" sortable="">
				<jstl:out value="${row.status}"></jstl:out>
			</display:column>


			<display:column titleKey="review.creationDate" sortable="true">
				<jstl:out value="${row.creationDate}"></jstl:out>
			</display:column>

			<display:column titleKey="review.draft">
				<jstl:out value="${row.isDraft}"></jstl:out>
			</display:column>

			<display:column>
				<a href="review/moderator/display.do?reviewId=${row.id}"> <spring:message
						code="review.display" />
				</a>
			</display:column>


			<display:column>
				<a href="review/moderator/assign.do?reviewId=${row.id}"> <spring:message
						code="review.assign" />
				</a>
			</display:column>




		</display:table>



	</jstl:if>

	<jstl:if test="${!possible}">
		<spring:message code="review.empty" var="error">

		</spring:message>

		<strong><jstl:out value="${error}" /></strong>
	</jstl:if>



</security:authorize>

