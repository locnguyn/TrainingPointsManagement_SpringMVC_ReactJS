<%-- 
    Document   : classStats
    Created on : Jun 25, 2024, 6:06:52 AM
    Author     : DELL
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<div id="content" class="p-4">
    <h1 class="text-center text-info m-4">Thống kê thành tích sinh viên lớp ${className}</h1>
    <div class="m-4">
        <div class="row mb-4">
            <div class="col col-md-7 col-12">
                <table class="table table-striped mt-1" id="table">
                    <thead>
                        <tr>
                            <th>Xếp loại</th>
                            <th>Số lượng</th>
                            <th>Tỷ lệ</th>
                        </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
            </div>
            <div class="col col-md-5 col-12">
                <canvas id="myChart"></canvas>
            </div>
        </div>
    </div>
    <table class="table table-striped mt-1" id="studentsTable">
        <thead>
            <tr>
                <th>MSSV</th>
                <th>Họ</th>
                <th>Tên</th>
                <th>Khoa</th>
                <th>Lớp</th>
                <th>Email</th>
                <th>Số điện thoại</th>
                <th>Điểm rèn luyện</th>
                <th>Xếp loại</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="entry" items="${data.students}">
                <c:set var="s" value="${entry.value.student}" />
                <c:set var="points" value="${entry.value.points}" />
                <tr>
                    <td>${s.studentCode}</td>
                    <td>${s.userId.lastName}</td>
                    <td>${s.userId.firstName}</td>
                    <td>${s.facultyId.name}</td>
                    <td>${s.classId.name}</td>
                    <td>${s.userId.email}</td>
                    <td>${s.userId.phoneNumber}</td>
                    <td>${points}</td>
                    <td>
                        <c:choose>
                            <c:when test="${points >= 90}">Xuất sắc</c:when>
                            <c:when test="${points >= 80}">Tốt</c:when>
                            <c:when test="${points >= 65}">Khá</c:when>
                            <c:when test="${points >= 50}">Trung bình</c:when>
                            <c:when test="${points >= 35}">Yếu</c:when>
                            <c:otherwise>Kém</c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
<div class="d-flex">
    <button class="btn btn-primary mt-3 me-2" onclick="exportToPDF()">Xuất file PDF</button>
    <button class="btn btn-primary mt-3 me-2" onclick="exportPDFwithNoChart()">Xuất file PDF không biểu đồ</button>
    <button class="btn btn-secondary mt-3 me-2" onclick="downloadCSV()">Xuất CSV</button>
</div>
<script type="application/json" id="dataStats">
    {
    <c:forEach var="entry" items="${data.stats}" varStatus="status">
        "<c:out value="${entry.key}"/>": <c:out value="${entry.value}"/><c:if test="${!status.last}">,</c:if>
    </c:forEach>
    }
</script>
<script>
    const contextPath = '<%= request.getContextPath()%>';
    document.addEventListener("DOMContentLoaded", () => {
        const dataScript = document.getElementById('dataStats');
        const stats = JSON.parse(dataScript.textContent);

        const labels = Object.keys(stats);
        const values = Object.values(stats);
        const tableBody = document.querySelector('#table > tbody');
        tableBody.innerHTML = '';
        const total = sumArr(values);
        labels.forEach((label, index) => {
            const percentage = (values[index] / total * 100).toFixed(2);
            const row = `<tr>
                            <td>` + label + `</td>
                            <td>` + values[index] + `</td>
                            <td>` + (isNaN(percentage) ? "0" : percentage) + `%</td>
                            </tr>`;
            tableBody.innerHTML += row;
        });

        facultyClassChart = drawChart('myChart', 'pie', labels, values, 'Biểu đồ xếp loại của lớp');
    });
    function checkImageLoaded() {
        console.log("Image loaded successfully.");
    }

    async function exportToPDF() {
        const {jsPDF} = window.jspdf;
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
            pdf.save('class-achievements.pdf');
        });
    }

    function waitForImagesToLoad(container) {
        const images = Array.from(container.getElementsByTagName('img'));
        return Promise.all(images.map(img => {
            if (img.complete)
                return Promise.resolve();
            return new Promise(resolve => {
                img.onload = img.onerror = resolve;
            });
        }));
    }
    function downloadCSV() {
        const className = '<c:out value="${className}" />';
        const semesterId = '<c:out value="${semesterId}" />';

        fetch(contextPath + `/class/csv?semesterId=${semesterId}&className=${className}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.blob();
            })
            .then(blob => {
                const url = window.URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.style.display = 'none';
                a.href = url;
                a.download = 'class_achievements.csv';
                document.body.appendChild(a);
                a.click();
                window.URL.revokeObjectURL(url);
            })
            .catch(error => console.error('There was a problem with the fetch operation:', error));
    }
    function exportPDFwithNoChart() {
        const className = '<c:out value="${className}" />';
        const semesterId = '<c:out value="${semesterId}" />';

        fetch(contextPath + `/class/pdf?semesterId=${semesterId}&className=${className}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.blob();
            })
            .then(blob => {
                const url = window.URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.style.display = 'none';
                a.href = url;
                a.download = 'class_achievements.pdf';
                document.body.appendChild(a);
                a.click();
                window.URL.revokeObjectURL(url);
            })
            .catch(error => console.error('There was a problem with the fetch operation:', error));
    }
</script>
<script src="<c:url value="/javascript/script.js" />"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/html2canvas/1.4.1/html2canvas.min.js"></script>