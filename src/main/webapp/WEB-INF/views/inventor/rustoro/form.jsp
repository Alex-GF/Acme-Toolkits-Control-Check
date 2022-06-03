<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="urn:jsptagdir:/WEB-INF/tags"%>

<acme:form readonly="${readonly}">
	<jstl:if test="${acme:anyOf(command, 'show, update, delete')}">
		<acme:input-textbox readonly="true" code="inventor.rustoro.form.label.code" path="code"/>
	</jstl:if>
	<jstl:if test="${command == 'create'}">
		<acme:input-textbox placeholder="201101-ASD" code="inventor.rustoro.form.label.code" path="code"/>	
	</jstl:if>
	<acme:input-textbox code="inventor.rustoro.form.label.name" path="name"/>
	<acme:input-textarea code="inventor.rustoro.form.label.explanation" path="explanation"/>
	<acme:input-moment readonly="true" code="inventor.rustoro.form.label.creationMoment" path="creationMoment"/>	
	<acme:input-moment code="inventor.rustoro.form.label.startDate" path="startDate"/>
	<acme:input-moment code="inventor.rustoro.form.label.finishDate" path="finishDate"/>
	
	<jstl:choose>
		<jstl:when test="${showDefaultCurrency}">	
			<acme:input-money code="inventor.rustoro.form.label.quota" path="defaultCurrency"/>
			<acme:input-money code="inventor.rustoro.form.label.quota" path="quota" readonly="true"/>
		</jstl:when>
		<jstl:otherwise>
			<acme:input-money code="inventor.rustoro.form.label.quota" path="quota"/>
		</jstl:otherwise>
	</jstl:choose>

	<acme:input-textbox code="inventor.rustoro.form.label.moreInfo" path="moreInfo"/>
	<acme:input-textbox readonly="true" code="inventor.rustoro.form.label.itemName" path="itemName"/>

	<jstl:choose>
		<jstl:when test="${acme:anyOf(command, 'show, update, delete')}">
			<acme:submit code="inventor.rustoro.form.button.update" action="/inventor/rustoro/update"/>
			<acme:submit code="inventor.rustoro.form.button.delete" action="/inventor/rustoro/delete"/>
		</jstl:when>
		<jstl:when test="${command == 'create'}">
			<acme:submit code="inventor.rustoro.form.button.create" action="/inventor/rustoro/create?itemId=${itemId}"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>