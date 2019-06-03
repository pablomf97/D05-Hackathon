<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<jstl:choose>
	<jstl:when test="${found}">
		<!-- Actor Attributes -->
		<fieldset>
			<legend style="font-size: 21px">
				<spring:message code="actor.personalData" />
			</legend>

			<div style="float: left;">
				<div>
					<strong><spring:message code="actor.name" />: </strong>
					<jstl:out value="${sponsor.name}" />
				</div>

				<br />

				<div>
					<strong><spring:message code="actor.surname" />: </strong>
					<jstl:out value="${sponsor.surname}" />
				</div>

				<br />

				<div>
					<strong><spring:message code="actor.email" />: </strong>
					<jstl:out value="${sponsor.email}" />
				</div>

				<br />

				<div>
					<strong><spring:message code="actor.phone" />: </strong>
					<jstl:out value="${sponsor.phoneNumber}" />
				</div>

				<br />

				<div>
					<strong><spring:message code="actor.address" />: </strong>
					<jstl:out value="${sponsor.address}" />
				</div>

				<display:table pagesize="5" requestURI="${requestURI}"
					class="displaytag" name="${sponsor.socialProfile}" id="profile">

					<display:column titleKey="social.network">
						<jstl:out value="${profile.socialNetwork}"></jstl:out>
					</display:column>

					<display:column titleKey="social.nick">
						<jstl:out value="${profile.nick}"></jstl:out>
					</display:column>

					<display:column titleKey="social.link">
						<a href="${profile.link}">${profile.link}</a>
					</display:column>

					<jstl:if test="${permission}">
						<display:column>
							<button
								onclick="location.href='social/actor/edit.do?id=${sponsor.id}'">
								<spring:message code="social.edit" />
							</button>
						</display:column>
					</jstl:if>
				</display:table>
			</div>

			<div style="float: right;">
				<img style="width: 200px; height: 200px" src="${profile.photo}"
					alt="User photo">
			</div>

		</fieldset>
		<br />

		<jstl:if test="${permission}">
			<button onclick="location.href='social/actor/create.do'">
				<spring:message code="social.create" />
			</button>
		</jstl:if>

	</jstl:when>
	<jstl:otherwise>
		<p class="error">
			<spring:message code="some.error" />
		</p>
	</jstl:otherwise>
</jstl:choose>
