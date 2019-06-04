<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('CRITIC')">
	<jstl:if test="${possible && review.id==0}">
		<form:form action="review/critic/create.do" modelAttribute="review">

			<acme:textbox code="review.title" path="title" />
			<br>
			<br>

			<acme:textarea code="review.body" path="body" />
			<br>
			<br>

			<form:label path="rating">
				<spring:message code="review.rating" />
			</form:label>
			<form:input path="rating" type="number" min="0" max="10" step="any" />
			<form:errors cssClass="error" path="rating" />

			<br>
			<br>


			<form:label path="film">
				<spring:message code="review.film" />:
	</form:label>
			<form:select path="film">
				<form:option label="-----" value="0" />
				<form:options items="${finalFilms}" itemLabel="title" itemValue="id" />
			</form:select>
			<form:errors cssClass="error" path="film" />
			<br />
			<br />


			<acme:submit name="saveFinal2" code="review.saveFinal" />
			

			<acme:submit name="saveDraft2" code="review.saveDraft" />
			

			<acme:cancel url="review/critic/listAll.do" code="review.cancel" />



		</form:form>
		
		
	</jstl:if>

	<jstl:if test="${possible && review.id!=0 && review.isDraft}">
			<form:form action="review/critic/edit.do?reviewId=${review.id}"
				modelAttribute="review">

			<form:hidden path="id" />

			<acme:textbox code="review.title" path="title" />
			<br>
			<br>

			<acme:textbox code="review.body" path="body" />
			<br>
			<br>

			<form:label path="rating">
				<spring:message code="review.rating" />
			</form:label>
			<form:input path="rating" type="number" min="0" max="10" step="any" />
			<form:errors cssClass="error" path="rating" />

			<br>
			<br>


			<acme:submit name="saveFinal" code="review.saveFinal" />
		

			<acme:submit name="saveDraft" code="review.saveDraft" />
		

			<acme:delete name="delete" confirmation="review.confirmation"
				code="review.delete" />

			<acme:cancel url="review/critic/listAll.do" code="review.cancel" />



		</form:form>
	</jstl:if>

	<jstl:if test="${possible && review.id!=0 && !(review.isDraft)}">
		<h4>
			<strong><jstl:out value="No permission" /></strong>
		</h4>
	</jstl:if>

</security:authorize>

<security:authorize access="hasRole('MODERATOR')">
	<form:form action="review/moderator/reject.do" modelAttribute="review">

			<form:hidden path="id"/>
			
			<acme:textarea code="review.rejectReason" path="rejectReason" />
			<br>
			<br>


			<acme:cancel url="review/moderator/listMyReviews.do" code="review.cancel" />


			<acme:submit name="reject" code="review.reject"/>
		</form:form>
	
</security:authorize>
