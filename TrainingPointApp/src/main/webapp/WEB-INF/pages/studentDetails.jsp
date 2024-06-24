<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<div class="container" id="content">
    <h1 class="text-center m-3 p-3 text-info">Thành tích sinh viên ${student.userId.firstName} ${student.userId.lastName}</h1>
    <div class="d-flex w-100 justify-content-center">
        <img src="${student.userId.avatar}" alt="alt" style="width: 250px;" class="m-3"/>
    </div>
    <table class="table">
        <tbody>
            <tr>
                <td>
                    <strong>Tên sinh viên</strong>
                </td>
                <td>${student.userId.firstName} ${student.userId.lastName}</td>
            </tr>
            <tr>
                <td>
                    <strong>Mã số sinh viên</strong>
                </td>
                <td>${student.studentCode}</td>
            </tr>
            <tr>
                <td>
                    <strong>Email sinh viên</strong>
                </td>
                <td>${student.userId.email}</td>
            </tr>
            <tr>
                <td>
                    <strong>Lớp</strong>
                </td>
                <td>${student.classId.name}</td>
            </tr>
            <tr>
                <td>
                    <strong>Khoa</strong>
                </td>
                <td>${student.facultyId.name}</td>
            </tr>
            <tr>
                <td>
                    <strong>Điểm rèn luyện</strong>
                </td>
                <td id="point"></td>
            </tr>
            <tr>
                <td>
                    <strong>Xếp loại</strong>
                </td>
                <td id="classification"></td>
            </tr>
        </tbody>
    </table>
    <table class="table table-striped mt-1" id="studentTable">
        <thead>
            <tr>
                <th>Id</th>
                <th>Tên hoạt động</th>
                <th>Điều</th>
                <th>Hình thức tham gia</th>
                <th>Điểm</th>
                <th>Khoa tổ chức</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${registrations}" var="r">
                <tr>
                    <td>${r.id}</td>
                    <td>${r.activity.name}</td>
                    <td>${r.activity.article}</td>
                    <td>${r.participationTypeName}</td>
                    <td>${r.point}</td>
                    <td>${r.activity.faculty}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <h1 class="text-center text-info mb-3 mt-4">Biểu đồ trực quan điểm rèn luyện sinh viên theo các điều</h1>
    <canvas id="myChart"></canvas>
</div>
<div class="exclude-pdf">
    <h1 class="text-center text-info mb-3 mt-4">Danh sách sinh viên báo thiếu</h1>
    <table class="table table-striped mt-1" id="reports">
        <thead>
            <tr>
                <th>Id</th>
                <th>Tên hoạt động</th>
                <th>Điều</th>
                <th>Hình thức tham gia</th>
                <th>Điểm</th>
                <th>Khoa tổ chức</th>
                <th>Ngày báo thiếu</th>
                <th>Minh chứng</th>
                    <c:choose>
                        <c:when test="${pageContext.request.userPrincipal.name != null}">
                        <th></th>
                        <th></th>
                        </c:when>
                    </c:choose>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${reports}" var="r">
                <c:if test="${r.status == 'Pending'}">
                    <tr>
                        <td>${r.id}</td>
                        <td>${r.activityPartType.activity.name}</td>
                        <td>${r.activityPartType.activity.article}</td>
                        <td>${r.activityPartType.participationType}</td>
                        <td>${r.activityPartType.activity.faculty}</td>
                        <td>${r.activityPartType.point}</td>
                        <td>${r.reportDate}</td>
                        <td>
                            <img class="card-img-top" src="${r.proof}" alt="proof" style="width: 100px; cursor: pointer;" onclick="event.stopPropagation(); openModal('${r.proof}')"/>
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
                </c:if>
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
</div>
<button class="btn btn-primary mt-3" onclick="exportToPDF()">Xuất file PDF</button>

<script>
    document.addEventListener("DOMContentLoaded", function () {
    const articles = [
    <c:forEach items="${articles}" var="article">
    { id: "${article.id}", name: "${article.name}" },
    </c:forEach>
    ];
            const registrations = [
    <c:forEach items="${registrations}" var="reg">
            {
            id: "${reg.id}",
                    point: ${reg.point},
                    activity: { article: "${reg.activity.article}",
                            name: '${reg.activity.name}' },
                    participated: ${reg.participated}
            },
    </c:forEach>
            ];
            const articleNames = articles.map(article => article.name);
            const confirmedPoints = articleNames.map(articleName => {
            return registrations.reduce((acc, activity) => {
            if (activity.activity.article === articleName && activity.participated) {
            return acc + activity.point;
            }
            return acc;
            }, 0);
            });
            const maxPoints = [25, 20, 25, 20, 10, 10];
            const totalPoints = articleNames.map((articleName, index) => {
            const totalPoints = registrations.reduce((acc, reg) => {
            if (reg.activity.article === articleName && reg.participated) {
            return acc + reg.point;
            }
            return acc;
            }, 0);
                    return Math.min(totalPoints, maxPoints[index]);
            });
            const totalConfirmedPoints = totalPoints.reduce((acc, points) => acc + points, 0);
            document.getElementById("point").innerHTML = totalConfirmedPoints;
            let classification;
            if (totalConfirmedPoints >= 90) {
    classification = "Xuất sắc";
    } else if (totalConfirmedPoints >= 80) {
    classification = "Tốt";
    } else if (totalConfirmedPoints >= 65) {
    classification = "Khá";
    } else if (totalConfirmedPoints >= 50) {
    classification = "Trung bình";
    } else if (totalConfirmedPoints >= 35) {
    classification = "Yếu";
    } else {
    classification = "Kém";
    }

    document.getElementById("classification").innerHTML = classification;
            const registeredPoints = articleNames.map(articleName => {
            return registrations.reduce((acc, activity) => {
            if (activity.activity.article === articleName && !activity.participated) {
            return acc + activity.point;
            }
            return acc;
            }, 0);
            });
            const data = {
            labels: articleNames,
                    datasets: [
                    {
                    label: "Điểm tối đa",
                            data: [25, 20, 20, 25, 10, 10],
                            backgroundColor: "rgba(255, 99, 132, 0.2)",
                            borderColor: "rgba(255, 99, 132, 1)",
                            borderWidth: 1,
                    },
                    {
                    label: "Điểm xác nhận",
                            data: confirmedPoints,
                            backgroundColor: "rgba(54, 162, 235, 0.2)",
                            borderColor: "rgba(54, 162, 235, 1)",
                            borderWidth: 1,
                    },
                    {
                    label: "Điểm đăng ký",
                            data: registeredPoints,
                            backgroundColor: "rgba(75, 192, 192, 0.2)",
                            borderColor: "rgba(75, 192, 192, 1)",
                            borderWidth: 1,
                    },
                    ],
            };
            const ctx = document.getElementById("myChart").getContext("2d");
            new Chart(ctx, {
            type: 'bar',
                    data: data,
                    options: {
                    maintainAspectRatio: true,
                            responsive: true,
                            legend: {display: true},
                            title: "Thống kê điểm rèn luyện sinh viên",
                    },
            });
    });
            function checkImageLoaded() {
            console.log("Image loaded successfully.");
            }

    async function exportToPDF() {
    const { jsPDF } = window.jspdf;
            const content = document.getElementById('content');
            // Ensure all images are fully loaded
            await waitForImagesToLoad(content);
            html2canvas(content, {
            allowTaint: true,
                    useCORS: true
            }).then(canvas => {
    const imgData = canvas.toDataURL('image/png');
            const pdf = new jsPDF('p', 'mm', 'a4');
            const pdfWidth = pdf.internal.pageSize.getWidth();
            const pdfHeight = (canvas.height * pdfWidth) / canvas.width;
            pdf.addImage(imgData, 'PNG', 0, 0, pdfWidth, pdfHeight);
            pdf.save('student-achievements.pdf');
    });
    }

    function waitForImagesToLoad(container) {
    const images = Array.from(container.getElementsByTagName('img'));
            return Promise.all(images.map(img => {
            if (img.complete) return Promise.resolve();
                    return new Promise(resolve => {
                    img.onload = img.onerror = resolve;
                    });
            }));
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
<script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/html2canvas/1.4.1/html2canvas.min.js"></script>
