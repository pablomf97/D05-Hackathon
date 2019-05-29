<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<h1><spring:message	code="person.title.list" />
<jstl:out value="${person.film.title}" /></h1>
	<jstl:choose>
		<jstl:when test="${isPrincipal}">

			<display:table class="displaytag" name="persons" pagesize="5" 
				requestURI="person/list.do" id="person">

				<display:column titleKey="person.name" sortable="true">
					<jstl:out value="${person.name}" />
				</display:column>

				<display:column titleKey="person.surname" sortable="true">
					<jstl:out value="${person.surname}" />
				</display:column>
				
				<jstl:choose>
					<jstl:when test="${pageContext.response.locale.language == 'es'}">
						<td><strong> <spring:message code="person.positions" />	: </strong></td>
						<jstl:forEach items="${person.positions}" var="name">
							<td><jstl:out value="${name.get('English')}"/></td>	
						</jstl:forEach>
					</jstl:when>
					<jstl:otherwise>
						<td><strong> <spring:message code="person.positions" />	: </strong></td>
						<jstl:forEach items="${person.positions}" var="name">
							<td><jstl:out value="${name.get('English')}"/></td>	
						</jstl:forEach>
					</jstl:otherwise>
				</jstl:choose>				

				<display:column>
					<a href="person/display.do?personId=${person.id}"> <spring:message
							code="person.display" />
					</a>
				</display:column>
				
				<display:column>
					<a href="person/delete.do?personId=${person.id}"> <spring:message
							code="person.delete" />
					</a>
				</display:column>
				
			</display:table>
		</jstl:when>
		<jstl:otherwise>
			<display:table class="displaytag" name="persons" pagesize="5" 
				requestURI="person/list.do" id="person">

				<display:column titleKey="person.name" sortable="true">
					<jstl:out value="${person.name}" />
				</display:column>

				<display:column titleKey="person.surname" sortable="true">
					<jstl:out value="${person.surname}" />
				</display:column>
				
				<jstl:choose>
					<jstl:when test="${pageContext.response.locale.language == 'es'}">
						<td><strong> <spring:message code="person.positions" />	: </strong></td>
						<jstl:forEach items="${person.positions}" var="name">
							<td><jstl:out value="${name.get('English')}"/></td>	
						</jstl:forEach>
					</jstl:when>
					<jstl:otherwise>
						<td><strong> <spring:message code="person.positions" />	: </strong></td>
						<jstl:forEach items="${person.positions}" var="name">
							<td><jstl:out value="${name.get('English')}"/></td>	
						</jstl:forEach>
					</jstl:otherwise>
				</jstl:choose>				

				<display:column>
					<a href="person/display.do?personId=${person.id}"> <spring:message
							code="person.display" />
					</a>
				</display:column>
				
			</display:table>
		</jstl:otherwise>
	</jstl:choose>