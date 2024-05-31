<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<h1 class="text-center text-info m-4">Danh sách các trợ lý sinh viên</h1>
<table class="table table-striped mt-1">
    <tr>
        <th>Id</th>
        <th>Mã số sinh viên</th>
        <th>Họ tên</th>
        <th>Lớp</th>
        <th>Khoa</th>
        <th>Email</th>
        <th>Số điện thoại</th>
        <th>Avatar</th>
            <c:choose>
                <c:when test="${pageContext.request.userPrincipal.name != null}">
                <th></th>
                </c:when>
            </c:choose>
    </tr>
    <c:forEach items="${assistants}" var="a">
        <tr>
            <td>${a.id}</td>
            <td>${a.studentCode}</td>
            <td>${a.userId.lastName} ${a.userId.firstName}</td>
            <td>${a.classId.name}</td>
            <td>${a.facultyId.name}</td>
            <td>${a.userId.email}</td>
            <td>${a.userId.phoneNumber}</td>
            <td>
                <img class="card-img-top" src="${a.userId.avatar}" alt="Avatar" style="width: 200px; cursor: pointer;" onclick="openModal('${a.userId.avatar}')"/>
            </td>
            <c:choose>
                <c:when test="${pageContext.request.userPrincipal.name != null}">
                    <td>
                        <a onclick="return confirm('Xác nhận xóa trợ lý sinh viên này?');" class="text-danger" href="<c:url value="/assistant/remove/${a.userId.id}" />">
                            <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="currentColor" class="bi bi-x" viewBox="0 0 16 16">
                            <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708"/>
                            </svg>
                        </a>
                    </td>
                </c:when>
            </c:choose>
        </tr>
    </c:forEach>
</table>

<div id="myModal" class="modal">
    <span onclick="closeModal()" class="close">
        <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="currentColor" class="bi bi-x" viewBox="0 0 16 16">
        <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708"/>
        </svg>
    </span>
    <img class="modal-content" id="img01">
</div>

<script src="<c:url value="javascript/script.js" />"></script>