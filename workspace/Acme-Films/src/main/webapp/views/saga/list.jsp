<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>


<h1><spring:message	code="saga.title.list" />
<jstl:out value="${saga.film.title}" /></h1>
	<jstl:choose>
		<jstl:when test="${isPrincipal}">

			<display:table class="displaytag" name="sagas" pagesize="5" 
				requestURI="saga/list.do" id="saga">
				
				<display:column titleKey="saga.title" sortable="true">
					<jstl:out value="${saga.title}" />
				</display:column>
				
				<display:column >
					<a href="saga/display.do?sagaId=${saga.id}"> <spring:message
							code="saga.display" />
					</a>

				</display:column>
				
					<display:column>
						<a href="saga/edit.do?sagaId=${saga.id}"> <spring:message
								code="saga.edit" />
						</a>
					</display:column>
					
					<display:column>
					<c:set var="contains" value="false" />
					<c:forEach var="item" items="${noDelete}">
					  <c:if test="${item eq saga}">
					    <c:set var="contains" value="true" />
					  </c:if>
					</c:forEach>
					<jstl:if test="${!contains}">
						<a href="saga/delete.do?sagaId=${saga.id}"> <spring:message
								code="saga.delete" />
						</a>
					</jstl:if>
					</display:column>
				
				
				
			</display:table>

			<input type="button"
				onclick="redirect: location.href = 'saga/create.do';"
				value="<spring:message code='saga.create' />" />
					
		</jstl:when>
		<jstl:otherwise>
			<display:table class="displaytag" name="sagas" pagesize="5" 
				requestURI="saga/list.do" id="saga">

				<display:column titleKey="saga.title" sortable="true">
					<jstl:out value="${saga.title}" />
				</display:column>

				<display:column >
					<a href="saga/display.do?sagaId=${saga.id}"> <spring:message
							code="saga.display" />
					</a>
				</display:column>
			</display:table>
		</jstl:otherwise>
	</jstl:choose>