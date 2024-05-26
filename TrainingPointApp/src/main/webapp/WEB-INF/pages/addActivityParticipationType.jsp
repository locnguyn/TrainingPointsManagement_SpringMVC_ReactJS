<%-- 
    Document   : products
    Created on : Apr 3, 2024, 2:05:29 PM
    Author     : admin
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<h1 class="text-center text-info m-4">QUẢN LÝ HOẠT ĐỘNG NGOẠI KHÓA</h1>
<c:url value="/add-activity-participation-type" var="action" />
<form:form method="post" action="${action}" modelAttribute="activityParticipationType" enctype="multipart/form-data">
    <form:errors path="*" element="div" cssClass="text text-danger" />
    <div class="form-floating mb-3 mt-3">
        <form:select class="form-select" id="acitivityId"  path="acitivityId">
            <c:forEach items="${activities}" var="a">
                <c:choose>
                    <c:when test="${a.id==activityParticipationType.acitivityId.id}">
                        <option value="${a.id}" selected>${a.name}</option>
                    </c:when>
                    <c:when test="${a.id==activityId}">
                        <option value="${a.id}" selected>${a.name}</option>
                    </c:when>
                    <c:otherwise>
                        <option value="${a.id}">${a.name}</option>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </form:select>
        <label for="categoryId" class="form-label">Chọn hoạt động</label>
        <form:errors path="acitivityId" element="div" cssClass="text text-danger" />
    </div> 
    <div class="form-floating mb-3 mt-3">
        <form:select class="form-select" id="participationTypeId"  path="participationTypeId">
            <c:forEach items="${participationTypes}" var="p">
                <c:choose>
                    <c:when test="${p.id==activityParticipationType.participationTypeId.id}">
                        <option value="${p.id}" selected>${p.name}</option>
                    </c:when>
                    <c:otherwise>
                        <option value="${p.id}">${p.name}</option>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </form:select>
        <label for="name">Hình thức tham gia</label>
        <form:errors path="participationTypeId" element="div" cssClass="text text-danger" />
    </div>
    <div class="form-floating mb-3 mt-3">
        <form:input class="form-control" type="number"  id="point"  placeholder="Điểm tham gia" path="point" />
        <label for="name">Điểm tham gia</label>
        <form:errors path="point" element="div" cssClass="text text-danger" />
    </div>   
    <div class="form-floating">
        <button class="btn btn-success mt-1" type="submit">
            <c:choose>
                <c:when test="${activityParticipationType.id > 0}">Cập nhật hình thức tham gia hoạt động ngoại khóa</c:when>
                <c:otherwise> Thêm hình thức tham gia hoạt động ngoại khóa</c:otherwise>
            </c:choose>
        </button>
        <form:hidden path="id" />
    </div>
</form:form>