<%-- 
    Document   : products
    Created on : Apr 3, 2024, 2:05:29 PM
    Author     : admin
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<h1 class="text-center text-info m-4">QUẢN LÝ HOẠT ĐỘNG NGOẠI KHÓA</h1>
<c:url value="/activities" var="action" />
<form:form method="post" action="${action}" modelAttribute="activity" enctype="multipart/form-data">
    <form:errors path="*" element="div" cssClass="text text-danger" />
    <div class="form-floating mb-3 mt-3">
        <form:input class="form-control"  id="name"  placeholder="Tên hoạt động" path="name" />
        <label for="name">Tên hoạt động</label>
        <form:errors path="name" element="div" cssClass="text text-danger" />
    </div>
    <div class="form-floating mb-3 mt-3">
        <form:input class="form-control"  id="location"  placeholder="Nơi tổ chức" path="location" />
        <label for="name">Nơi tổ chức</label>
        <form:errors path="location" element="div" cssClass="text text-danger" />
    </div>
    <div class="form-floating mb-3 mt-3">
        <form:input class="form-control" type="datetime-local"  id="startDateTime"  placeholder="Ngày giờ bắt đầu" path="startDateTime"/>
        <label for="name">Ngày giờ bắt đầu</label>
        <form:errors path="startDateTime" element="div" cssClass="text text-danger" />
    </div>
    <div class="form-floating mb-3 mt-3">
        <form:input class="form-control" type="datetime-local"  id="endDate"  placeholder="Ngày giờ kết thúc" path="endDate"/>
        <label for="name">Ngày giờ kết thúc</label>
        <form:errors path="endDate" element="div" cssClass="text text-danger" />
    </div>
    <div class="form-floating mb-3 mt-3">
        <form:select class="form-select" id="articleId"  path="articleId">
            <c:forEach items="${articles}" var="c">
                <c:choose>
                    <c:when test="${c.id==activity.articleId.id}">
                        <option value="${c.id}" selected>${c.name}</option>
                    </c:when>
                    <c:otherwise>
                        <option value="${c.id}">${c.name}</option>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </form:select>
        <label for="categoryId" class="form-label">Chọn điều</label>
        <form:errors path="articleId" element="div" cssClass="text text-danger" />
    </div>
    <div class="form-floating mb-3 mt-3">
        <form:select class="form-select" id="facultyId"  path="facultyId">
            <c:forEach items="${faculties}" var="f">
                <c:choose>
                    <c:when test="${f.id==activity.facultyId.id}">
                        <option value="${f.id}" selected>${f.name}</option>
                    </c:when>
                    <c:otherwise>
                        <option value="${f.id}">${f.name}</option>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </form:select>
        <label for="categoryId" class="form-label">Chọn khoa</label>
        <form:errors path="facultyId" element="div" cssClass="text text-danger" />
    </div>
    <div class="form-floating mb-3 mt-3">
        <form:select class="form-select" id="participantId"  path="participantId">
            <c:forEach items="${participants}" var="p">
                <c:choose>
                    <c:when test="${p.id==activity.participantId.id}">
                        <option value="${p.id}" selected>${p.name}</option>
                    </c:when>
                    <c:otherwise>
                        <option value="${p.id}">${p.name}</option>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </form:select>
        <label for="categoryId" class="form-label">Chọn đối tượng tham gia</label>
        <form:errors path="participantId" element="div" cssClass="text text-danger" />
    </div>
    
    <div class="form-floating">
        <button class="btn btn-success mt-1" type="submit">
            <c:choose>
                <c:when test="${activity.id > 0}">Cập nhật hoạt động ngoại khóa</c:when>
                <c:otherwise> Thêm hoạt động ngoại khóa</c:otherwise>
            </c:choose>
        </button>
        <form:hidden path="id" />
        <form:hidden path="userId" value="${userId}"/>
    </div>
</form:form>

