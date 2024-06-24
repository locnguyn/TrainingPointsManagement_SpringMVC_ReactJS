<%-- 
    Document   : index
    Created on : May 19, 2024, 2:20:11 PM
    Author     : DELL
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<h1 class="text-center text-info m-4">QUẢN LÝ HOẠT ĐỘNG NGOẠI KHÓA</h1>
<c:url value="/" var="action" />
<form method="get" action="${action}" enctype="multipart/form-data" class="row">
    <div class="col col-4">
        <select id="faculty" name="faculty" class="form-select">
            <option value="">Tất cả</option>
            <c:forEach items="${faculties}" var="f">
                <option value="${f.id}">${f.name}</option>
            </c:forEach>
        </select>
    </div>
    <div class="col col-3">
        <select id="article" name="article" class="form-select">
            <option value="">Tất cả</option>
            <c:forEach items="${articles}" var="a">
                <option value="${a.id}">${a.name}</option>
            </c:forEach>
        </select>
    </div>
    <div class="col col-3">
        <select id="filterType" name="filterType" class="form-select">
            <option value="">Tất cả</option>
            <option value="week">Tuần hiện tại</option>
            <option value="month">Tháng hiện tại</option>
        </select>
    </div>
    <div class="col col-2">
        <button type="submit" class="btn btn-primary">Áp dụng</button>
    </div>
</form>
<table class="table table-striped mt-1">
    <tr>
        <th>Id</th>
        <th>Hoạt động</th>
        <th>Điều</th>
        <th>Đối tượng tham gia</th>
        <th>Ngày bắt đầu</th>
        <th>Ngày kết thúc</th>
        <th>Giờ bắt đầu</th>
        <th>Khoa</th>
        <th>Địa điểm</th>
            <c:choose>
                <c:when test="${pageContext.request.userPrincipal.name != null}">
                <th></th>
                    <c:if test="${pageContext.request.isUserInRole('ROLE_ADMIN')}">
                    <th></th>
                    <th></th>
                    </c:if>
                </c:when>
            </c:choose>
    </tr>
    <c:forEach items="${activities}" var="a">
        <tr>
            <td>${a.id}</td>
            <td>${a.name}</td>
            <td>${a.articleId.name}</td>
            <td>${a.participantId.name}</td>
            <td>
                <fmt:formatDate value="${a.startDateTime}" pattern="dd/MM/yyyy" />
            </td>
            <td>
                <fmt:formatDate value="${a.endDate}" pattern="dd/MM/yyyy" />
            </td>
            <td>
                <fmt:formatDate value="${a.startDateTime}" pattern="HH:mm" />
            </td>
            <td>${a.facultyId.name}</td>
            <td>${a.location}</td>
            <c:choose>
                <c:when test="${pageContext.request.userPrincipal.name != null && !pageContext.request.isUserInRole('ROLE_USER')}">
                    <td class="">
                        <a class="btn btn-primary" href="<c:url value="/activities/${a.id}/participation-type" />">
                            Điểm
                        </a>
                    </td>
                    <c:if test="${pageContext.request.isUserInRole('ROLE_ADMIN')}">
                        <td>
                            <a class="" href="<c:url value="/activities/${a.id}" />">
                                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-pencil-square" viewBox="0 0 16 16">
                                <path d="M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z"/>
                                <path fill-rule="evenodd" d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5z"/>
                                </svg>
                            </a>
                        </td>
                        <td>
                            <c:url value="/delete-activity/${a.id}" var="url" />
                            <a href="${url}" class="text-danger" data-bs-toggle="tooltip" data-bs-placement="top" title="Tooltip on top" onclick="return confirm('Bạn có chắc chắn muốn xóa hoạt động này?');">
                                <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="currentColor" class="bi bi-x" viewBox="0 0 16 16">
                                <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708"/>
                                </svg>
                            </a>
                        </td>
                    </c:if>
                </c:when>
            </c:choose>
        </tr>
    </c:forEach>
</table>

<script src="<c:url value="/javascript/script.js" />"></script>
