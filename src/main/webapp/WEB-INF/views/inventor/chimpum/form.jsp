<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="urn:jsptagdir:/WEB-INF/tags"%>

<acme:form readonly="${readonly}">
	<acme:input-textbox readonly="true" code="inventor.chimpum.form.label.code" path="code"/>	
	<acme:input-textbox code="inventor.chimpum.form.label.title" path="title"/>
	<acme:input-textarea code="inventor.chimpum.form.label.description" path="description"/>
	<acme:input-moment readonly="true" code="inventor.chimpum.form.label.creationMoment" path="creationMoment"/>	
	<acme:input-moment code="inventor.chimpum.form.label.startDate" path="startDate"/>
	<acme:input-moment code="inventor.chimpum.form.label.finishDate" path="finishDate"/>
	
	<jstl:choose>
		<jstl:when test="${showDefaultCurrency}">	
			<acme:input-money code="inventor.chimpum.form.label.budget" path="defaultCurrency"/>
			<acme:input-money code="inventor.chimpum.form.label.budget" path="budget" readonly="true"/>
		</jstl:when>
		<jstl:otherwise>
			<acme:input-money code="inventor.chimpum.form.label.budget" path="budget"/>
		</jstl:otherwise>
	</jstl:choose>

	<acme:input-textbox code="inventor.chimpum.form.label.link" path="link"/>
	<acme:input-textbox readonly="true" code="inventor.chimpum.form.label.itemName" path="itemName"/>

	<jstl:choose>
		<jstl:when test="${acme:anyOf(command, 'show, update, delete')}">
			<acme:submit code="inventor.chimpum.form.button.update" action="/inventor/chimpum/update"/>
			<acme:submit code="inventor.chimpum.form.button.delete" action="/inventor/chimpum/delete"/>
		</jstl:when>
		<jstl:when test="${command == 'create'}">
			<acme:submit code="inventor.chimpum.form.button.create" action="/inventor/chimpum/create?itemId=${itemId}"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>