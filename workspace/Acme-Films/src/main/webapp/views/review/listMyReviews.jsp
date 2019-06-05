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
			requestURI="review/moderator/listMyReviews.do" id="row">

			<!-- Attributes-->


			<jstl:if test="${row.status == 'PENDING'}">
				<display:column>
					<a href="review/moderator/accept.do?reviewId=${row.id}"> <spring:message
							code="review.accept" />
					</a>
				</display:column>

				<display:column>
					<a href="review/moderator/reject.do?reviewId=${row.id}"> <spring:message
							code="review.reject" />
					</a>
				</display:column>
			</jstl:if>

			<jstl:if test="${row.status != 'PENDING'}">
				<display:column>

				</display:column>

				<display:column>

				</display:column>
			</jstl:if>

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
				<jstl:choose>
					<jstl:when test="${row.isDraft}">
						<spring:message code="film.draft.true" />
					</jstl:when>
					<jstl:otherwise>
						<spring:message code="film.draft.false" />
					</jstl:otherwise>
				</jstl:choose>
			</display:column>

			<display:column titleKey="review.moderator">
				<jstl:out value="${row.moderator.userAccount.username}"></jstl:out>
			</display:column>


			<display:column>
				<a href="review/moderator/display.do?reviewId=${row.id}"> <spring:message
						code="review.display" />
				</a>
			</display:column>





		</display:table>
		<jstl:if test="${!possible}">
			<h4>
				<strong><jstl:out value="no.permission" /></strong>
			</h4>
		</jstl:if>

	</jstl:if>

	<jstl:if test="${!possible}">
		<spring:message code="review.error" var="error">

		</spring:message>

		<strong><jstl:out value="${error}" /></strong>
	</jstl:if>

</security:authorize>

