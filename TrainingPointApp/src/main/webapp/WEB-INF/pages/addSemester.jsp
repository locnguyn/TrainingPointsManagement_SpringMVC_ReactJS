<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<h1 class="text-center text-info m-4">Thêm học kỳ</h1>
<c:url value="/semester/add" var="action" />
<div>${semester.id}</div>
<form:form method="post" action="${action}" modelAttribute="semester" enctype="multipart/form-data">
    <form:errors path="*" cssClass="text text-danger" />
    <!-- Form fields for Student Assistant -->
    <div class="form-floating mb-3 mt-3">
        <form:input class="form-control" path="semesterName" placeholder="Tên Học Kỳ" />
        <label for="firstName">Tên Học Kỳ</label>
        <form:errors path="semesterName" cssClass="text text-danger" />
    </div>

    <div class="form-floating mb-3 mt-3">
        <form:input class="form-control" path="startDate" placeholder="Ngày bắt đầu" type="datetime-local"/>
        <label for="lastName">Ngày bắt đầu Học Kỳ</label>
        <form:errors path="startDate" cssClass="text text-danger" />
    </div>

    <div class="form-floating mb-3 mt-3">
        <form:input class="form-control" path="endDate" placeholder="Ngày kết thúc" type="datetime-local"/>
        <label for="lastName">Ngày kết thúc Học Kỳ</label>
        <form:errors path="endDate" cssClass="text text-danger" />
    </div>

    <div class="form-floating">
        <button class="btn btn-success mt-1" type="submit">
            <c:choose>
                <c:when test="${semester.id > 0}">Cập nhật</c:when>
                <c:otherwise>Thêm</c:otherwise>
            </c:choose>
        </button>
        <form:hidden path="id" />
    </div>
</form:form>
