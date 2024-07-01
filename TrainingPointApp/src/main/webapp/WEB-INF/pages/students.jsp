<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Danh sách sinh viên</title>
        <script src="<c:url value="/javascript/script.js" />"></script>
        <style>
            .modal {
                display: none;
                position: fixed;
                z-index: 1;
                padding-top: 60px;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                overflow: auto;
                background-color: rgb(0,0,0);
                background-color: rgba(0,0,0,0.4);
            }
            .modal-content {
                margin: auto;
                display: block;
                width: 80%;
                max-width: 700px;
            }
            .close {
                position: absolute;
                top: 15px;
                right: 35px;
                color: #f1f1f1;
                font-size: 40px;
                font-weight: bold;
                transition: 0.3s;
            }
            .close:hover,
            .close:focus {
                color: #bbb;
                text-decoration: none;
                cursor: pointer;
            }
            .clickable-row {
                cursor: pointer;
            }
            .clickable-row td {
                transition: background-color 0.2s;
            }
            .clickable-row:hover td {
                background-color: #f5f5f5;
            }
        </style>
    </head>
    <body>
        <h1 class="text-center text-info m-4">Danh sách sinh viên</h1>

        <!-- Search Input -->
        <div class="text-center mb-3">
            <input type="text" id="searchInput" onkeyup="filterTable()" placeholder="Tìm kiếm sinh viên..." class="form-control w-50 mx-auto">
        </div>

        <table class="table table-striped mt-1" id="studentTable">
            <thead>
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
                            <c:when test="${pageContext.request.userPrincipal.name != null && pageContext.request.isUserInRole('ROLE_ADMIN')}">
                            <th></th>
                            </c:when>
                        </c:choose>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${students}" var="s">
                    <tr class="clickable-row" onclick="goToStudentDetails(${s.id})">
                        <td>${s.id}</td>
                        <td>${s.studentCode}</td>
                        <td>${s.userId.lastName} ${s.userId.firstName}</td>
                        <td>${s.classId.name}</td>
                        <td>${s.facultyId.name}</td>
                        <td>${s.userId.email}</td>
                        <td>${s.userId.phoneNumber}</td>
                        <td>
                            <img class="card-img-top" src="${s.userId.avatar}" alt="Avatar" style="width: 100px; cursor: pointer;" onclick="event.stopPropagation(); openModal('${s.userId.avatar}')"/>
                        </td>
                        <c:choose>
                            <c:when test="${pageContext.request.userPrincipal.name != null && pageContext.request.isUserInRole('ROLE_ADMIN')}">
                                <c:choose>
                                    <c:when test="${s.userId.userRole == 'ROLE_ASSISTANT'}">
                                        <td>
                                            <a onclick="event.stopPropagation(); return confirm('Xác nhận xóa trợ lý sinh viên này?');" class="btn btn-outline-danger" href="<c:url value='/user/remove-assistant/${s.userId.id}' />">
                                                Hủy trợ lý
                                            </a>
                                        </td>
                                    </c:when>
                                    <c:when test="${s.userId.userRole == 'ROLE_USER'}">
                                        <td>
                                            <a onclick="event.stopPropagation(); return confirm('Xác nhận thêm sinh viên này làm trợ lý sinh viên?');" class="btn btn-outline-success" href="<c:url value='/user/add-assistant/${s.userId.id}' />">
                                                Thêm trợ lý
                                            </a>
                                        </td>
                                    </c:when>
                                </c:choose>
                            </c:when>
                        </c:choose>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="myModal" class="modal">
            <span onclick="closeModal()" class="close">
                <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="currentColor" class="bi bi-x" viewBox="0 0 16 16">
                <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708"/>
                </svg>
            </span>
            <img class="modal-content" id="img01">
        </div>

        <div class="d-flex justify-content-center">
            <nav aria-label="Page navigation example">
                <ul class="pagination">
                    <li class="page-item">
                        <a class="page-link" id="pre" aria-label="Previous" style="cursor: pointer" onclick="goPre()">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                    <li class="page-item">
                        <a class="page-link" href="?page=" id="cur">1</a>
                    </li>
                    <li class="page-item">
                        <a class="page-link" id="next" aria-label="Next" style="cursor: pointer" onclick="goNext()">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>
                </ul>
            </nav>
        </div>

        <script>
            function goNext() {
                var curPage = parseInt(document.getElementById("cur").textContent, 10);
                var nextPage = curPage + 1;
                document.getElementById("cur").textContent = nextPage;
                window.location.href = "?page=" + nextPage;
            }

            function goPre() {
                var curPage = parseInt(document.getElementById("cur").textContent, 10);
                if (curPage > 1) {
                    var prevPage = curPage - 1;
                    document.getElementById("cur").textContent = prevPage;
                    window.location.href = "?page=" + prevPage;
                }
            }

            // This part of the script makes sure the current page number is correctly reflected in the URL and element.
            document.addEventListener("DOMContentLoaded", function () {
                const urlParams = new URLSearchParams(window.location.search);
                const page = urlParams.get('page') || 1;
                document.getElementById("cur").textContent = page;
            });
        </script>
        <script>
            function filterTable() {
                const searchInput = document.getElementById('searchInput');
                const filter = searchInput.value.toLowerCase();
                const table = document.getElementById('studentTable');
                const rows = table.getElementsByTagName('tr');

                // Loop through all table rows, and hide those who don't match the search query
                for (let i = 1; i < rows.length; i++) {
                    let cells = rows[i].getElementsByTagName('td');
                    let match = false;
                    for (let j = 0; j < cells.length - 1; j++) { // Exclude the last cell if it contains actions for admin
                        if (cells[j] && cells[j].innerText.toLowerCase().indexOf(filter) > -1) {
                            match = true;
                            break;
                        }
                    }
                    if (match) {
                        rows[i].style.display = "";
                    } else {
                        rows[i].style.display = "none";
                    }
                }
            }

            function openModal(imageSrc) {
                const modal = document.getElementById('myModal');
                const modalImg = document.getElementById("img01");
                modal.style.display = "block";
                modalImg.src = imageSrc;
            }

            function closeModal() {
                const modal = document.getElementById('myModal');
                modal.style.display = "none";
            }

            function goToStudentDetails(studentId) {
                const link = document.createElement('a');
                link.href = "students/" + studentId;
                link.click();
            }
        </script>
    </body>
</html>
