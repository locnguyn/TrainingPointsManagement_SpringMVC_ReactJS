<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<h1 class="text-center text-info m-4">KIỂM DUYỆT BÁO THIẾU ĐIỂM RÈN LUYỆN</h1>
<table class="table table-striped mt-1">
    <tr>
        <th>Id</th>
        <th>Mã số sinh viên</th>
        <th>Họ tên</th>
        <th>Lớp</th>
        <th>Hoạt động</th>
        <th>Hình thức tham gia</th>
        <th>Điểm</th>
        <th>Minh chứng</th>
            <c:choose>
                <c:when test="${pageContext.request.userPrincipal.name != null}">
                <th></th>
                <th></th>
                </c:when>
            </c:choose>
    </tr>
    <c:forEach items="${reports}" var="r">
        <tr>
            <td>${r.id}</td>
            <td>${r.studentId.studentCode}</td>
            <td>${r.studentId.userId.lastName} ${r.studentId.userId.firstName}</td>
            <td>${r.studentId.classId.name}</td>
            <td>${r.activityParticipationTypeId.activityId.name}</td>
            <td>${r.activityParticipationTypeId.participationTypeId.name}</td>
            <td>${r.activityParticipationTypeId.point}</td>
            <td>
                <img class="card-img-top" src="${r.proof}" alt="Minh chứng" style="width: 200px; cursor: pointer;" onclick="openModal('${r.proof}')"/>
            </td>
            <c:choose>
                <c:when test="${pageContext.request.userPrincipal.name != null}">
                    <td>
                        <a class="" href="<c:url value="/report-missing/confirm/${r.id}" />" onclick="return confirm('Xác nhận sinh viên có tham gia hoạt động này?');">
                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-check-square" viewBox="0 0 16 16">
                            <path d="M14 1a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1zM2 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2z"/>
                            <path d="M10.97 4.97a.75.75 0 0 1 1.071 1.05l-3.992 4.99a.75.75 0 0 1-1.08.02L4.324 8.384a.75.75 0 1 1 1.06-1.06l2.094 2.093 3.473-4.425z"/>
                            </svg>
                        </a>
                    </td>
                    <td>
                        <a onclick="return confirm('Từ chối báo thiếu này?');" class="text-danger" href="<c:url value="/report-missing/reject/${r.id}" />">
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
    <span class="close" onclick="closeModal()" class="text-danger">
        <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="currentColor" class="bi bi-x" viewBox="0 0 16 16">
        <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708"/>
        </svg>
    </span>
    <img class="modal-content" id="img01">
</div>

<script src="<c:url value="javascript/script.js" />"></script>