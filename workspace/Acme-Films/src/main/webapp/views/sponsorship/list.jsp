<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<security:authorize access="hasAnyRole('MODERATOR','SPONSOR')">

<h1><spring:message	code="sponsorship.title.list" />
<jstl:out value="${(film.title)}" /></h1>
	<jstl:choose>
		<jstl:when test="${isPrincipal == 'MODERATOR'}">

			<display:table class="displaytag" name="sponsorships" pagesize="5" 
				requestURI="sponsorship/list.do" id="sponsorship">

				<display:column titleKey="sponsorship.title" sortable="true">
					<jstl:out value="${sponsorship.title}" />
				</display:column>

				<display:column titleKey="sponsorship.banner" sortable="true">
					<jstl:out value="${sponsorship.banner}" />
				</display:column>
				
				<display:column titleKey="sponsorship.title" sortable="true">
					<jstl:out value="${sponsorship.title}" />
				</display:column>

				<display:column titleKey="sponsorship.targetPage" sortable="true">
					<jstl:out value="${sponsorship.targetPage}" />
				</display:column>
				
				<display:column titleKey="sponsorship.sponsor" sortable="true">
					<jstl:out value="${sponsorship.sponsor.name} ${sponsorship.sponsor.surname}" />
				</display:column>
				
				<display:column>
					<a href="sponsorship/display.do?sponsorshipId=${sponsorship.id}"> <spring:message
							code="sponsorship.display" />
					</a>
				</display:column>
				
				<display:column>
					<a id="edit"
						href="sponsorship/action.do?action=accept&sponsorshipId=${sponsorship.id}">
						<spring:message code="sponsorship.accept" />
					</a>
				</display:column>
				
				<display:column>
					<a id="edit"
						href="sponsorship/action.do?action=reject&sponsorshipId=${sponsorship.id}">
						<spring:message code="sponsorship.reject" />
					</a>
				</display:column>
				
			</display:table>
			
		</jstl:when>
		<jstl:otherwise>
			<display:table class="displaytag" name="sponsorships" pagesize="5" 
				requestURI="sponsorship/list.do" id="sponsorship">

				<display:column titleKey="sponsorship.title" sortable="true">
					<jstl:out value="${sponsorship.title}" />
				</display:column>

				<display:column titleKey="sponsorship.banner" sortable="true">
					<jstl:out value="${sponsorship.banner}" />
				</display:column>
				
				<display:column titleKey="sponsorship.title" sortable="true">
					<jstl:out value="${sponsorship.title}" />
				</display:column>

				<display:column titleKey="sponsorship.targetPage" sortable="true">
					<jstl:out value="${sponsorship.targetPage}" />
				</display:column>
				
				<display:column titleKey="sponsorship.isActive" sortable="true">
					<jstl:out value="${sponsorship.isActive}" />
				</display:column>
				
				<display:column>
					<a href="sponsorship/display.do?sponsorshipId=${sponsorship.id}"> <spring:message
							code="sponsorship.display" />
					</a>
				</display:column>
				
				<display:column>
					<a href="sponsorship/edit.do?sponsorshipId=${sponsorship.id}"> <spring:message
							code="sponsorship.edit" />
					</a>
				</display:column>
				
				<display:column>
				<jstl:if test="${sponsorship.isActive eq null}">
					<a href="sponsorship/delete.do?sponsorshipId=${sponsorship.id}"> <spring:message
							code="sponsorship.delete" />
					</a>
				</jstl:if>
				</display:column>
				<display:column>
				<jstl:if test="${sponsorship.isActive eq true}">
					<a id="edit"
						href="sponsorship/action.do?action=reject&sponsorshipId=${sponsorship.id}">
						<spring:message code="sponsorship.deactivate" />
					</a>
				</jstl:if>
				</display:column>
			</display:table>
			
			<input type="button"
				onclick="redirect: location.href = 'sponsorship/create.do';"
				value="<spring:message code='sponsorship.create' />" />
		</jstl:otherwise>
	</jstl:choose>
	
</security:authorize>

<security:authorize access="!hasAnyRole('MODERATOR','SPONSOR')">
		<p>
			<spring:message	code="sponsorship.not.allowed" /><br>
		</p>
</security:authorize>