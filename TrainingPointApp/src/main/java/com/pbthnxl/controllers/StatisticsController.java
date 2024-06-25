package com.pbthnxl.controllers;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.pbthnxl.pojo.Student;
import com.pbthnxl.services.ClassService;
import com.pbthnxl.services.SemesterService;
import com.pbthnxl.services.StatisticService;
import com.pbthnxl.utils.Utils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private StatisticService statisticService;
    
    @Autowired
    private ClassService classService;
    
    @Autowired
    private SemesterService semesterService;

    @GetMapping("/faculty-class")
    public ResponseEntity<Map<String, Object>> getStatisticsByFacultyAndClass(@RequestParam(name = "semesterId", defaultValue = "0") int semesterId) {
        Map<String, Object> stats = statisticService.getStatisticsByFacultyAndClass(semesterId);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
    
    @GetMapping("/faculty/{id}")
    public ResponseEntity<Map<String, Integer>> getStatisticsByFaculty(@PathVariable("id") int id, @RequestParam(name = "semesterId", defaultValue = "0") int semesterId) {
        Map<String, Integer> stats = statisticService.getStatisticsForFaculty(id, semesterId);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
    
    @GetMapping("/classification/{id}")
    public ResponseEntity<Map<String, Integer>> getStatisticsClassificationByFaculty(@PathVariable("id") int id, @RequestParam(name = "semesterId", defaultValue = "0") int semesterId) {
        Map<String, Integer> stats = statisticService.getClassificationStatistics(id, semesterId);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
    
    @GetMapping("/classification")
    public ResponseEntity<Map<String, Integer>> getStatisticsClassification(@RequestParam(name = "semesterId", defaultValue = "0") int semesterId) {
        Map<String, Integer> stats = statisticService.getClassificationStatistics(0, semesterId);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
    
    @GetMapping("/class")
    public ResponseEntity<Map<String, Object>> getStatisticsByClass(@RequestParam(name = "semesterId", defaultValue = "0") int semesterId, @RequestParam(name = "className", defaultValue = "0") String className) {
        com.pbthnxl.pojo.Class clazz = this.classService.findByName(className);
        Map<String, Object> stats = statisticService.getStatisticsForClass(clazz.getId(), semesterId);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
    
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllStatistics(@RequestParam(name = "semesterId", defaultValue = "0") int semesterId) {
        Map<String, Object> stats = statisticService.getStatisticsForClass(0, semesterId);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
    
    @GetMapping("/pdf")
    public ResponseEntity<Resource> exportClassStatsToPDF(@RequestParam(name = "semesterId", defaultValue = "0") int semesterId) {
        
        Map<String, Object> stats = this.statisticService.getStatisticsForClass(0, semesterId);

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(byteArrayOutputStream);
            PdfDocument pdf = new PdfDocument(writer);
            // Tiêu đề
            try (Document document = new Document(pdf, PageSize.A4)) {
                String fontPath = "./arial.ttf";
                

                PdfFont font = PdfFontFactory.createFont(fontPath, "Identity-H");

                document.setFont(font);
                String semesterName = "";
                if(semesterId != 0){
                    semesterName = semesterService.getSemesterById(semesterId).getSemesterName() + "";
                }
                document.add(new Paragraph("Thống kê thành tích sinh viên toàn trường" + (!semesterName.isEmpty() ? " Học Kỳ " + semesterName: semesterName))
                        .setFontSize(18).setBold().setTextAlignment(TextAlignment.CENTER));

                Map<String, Integer> statData = (Map<String, Integer>) stats.get("stats");
                Table statTable = new Table(UnitValue.createPercentArray(new float[]{3, 3}))
                        .useAllAvailableWidth();
                statTable.addHeaderCell(new Cell().add(new Paragraph("Xếp loại"))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                        .setTextAlignment(TextAlignment.CENTER));
                statTable.addHeaderCell(new Cell().add(new Paragraph("Số lượng"))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                        .setTextAlignment(TextAlignment.CENTER));

                for (Map.Entry<String, Integer> entry : statData.entrySet()) {
                    statTable.addCell(new Cell().add(new Paragraph(entry.getKey()))
                            .setTextAlignment(TextAlignment.CENTER));
                    statTable.addCell(new Cell().add(new Paragraph(String.valueOf(entry.getValue())))
                            .setTextAlignment(TextAlignment.CENTER));
                }

                document.add(statTable.setMarginTop(20));


                Map<String, Map<String, Object>> students = (Map<String, Map<String, Object>>) stats.get("students");
                Table studentTable = new Table(UnitValue.createPercentArray(new float[]{
                    10, 10, 10, 15, 15, 10, 15, 5
                })).useAllAvailableWidth();
                studentTable.addHeaderCell(new Cell().add(new Paragraph("MSSV"))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                        .setTextAlignment(TextAlignment.CENTER));
                studentTable.addHeaderCell(new Cell().add(new Paragraph("Họ"))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                        .setTextAlignment(TextAlignment.CENTER));
                studentTable.addHeaderCell(new Cell().add(new Paragraph("Tên"))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                        .setTextAlignment(TextAlignment.CENTER));
                studentTable.addHeaderCell(new Cell().add(new Paragraph("Khoa"))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                        .setTextAlignment(TextAlignment.CENTER));
                studentTable.addHeaderCell(new Cell().add(new Paragraph("Lớp"))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                        .setTextAlignment(TextAlignment.CENTER));
                studentTable.addHeaderCell(new Cell().add(new Paragraph("Số điện thoại"))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                        .setTextAlignment(TextAlignment.CENTER));
                studentTable.addHeaderCell(new Cell().add(new Paragraph("Điểm rèn luyện"))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                        .setTextAlignment(TextAlignment.CENTER));
                studentTable.addHeaderCell(new Cell().add(new Paragraph("Xếp loại"))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                        .setTextAlignment(TextAlignment.CENTER));

                for (Map<String, Object> entry : students.values()) {
                    Student student = (Student) entry.get("student");
                    int points = (int) entry.get("points");

                    String grade = Utils.getRating(points);

                    studentTable.addCell(new Cell().add(new Paragraph(student.getStudentCode()))
                            .setTextAlignment(TextAlignment.CENTER));
                    studentTable.addCell(new Cell().add(new Paragraph(student.getUserId().getLastName()))
                            .setTextAlignment(TextAlignment.CENTER));
                    studentTable.addCell(new Cell().add(new Paragraph(student.getUserId().getFirstName()))
                            .setTextAlignment(TextAlignment.CENTER));
                    studentTable.addCell(new Cell().add(new Paragraph(student.getFacultyId().getName()))
                            .setTextAlignment(TextAlignment.CENTER));
                    studentTable.addCell(new Cell().add(new Paragraph(student.getClassId().getName()))
                            .setTextAlignment(TextAlignment.CENTER));
                    studentTable.addCell(new Cell().add(new Paragraph(student.getUserId().getPhoneNumber()))
                            .setTextAlignment(TextAlignment.CENTER));
                    studentTable.addCell(new Cell().add(new Paragraph(String.valueOf(points)))
                            .setTextAlignment(TextAlignment.CENTER));
                    studentTable.addCell(new Cell().add(new Paragraph(grade))
                            .setTextAlignment(TextAlignment.CENTER));
                }

                document.add(studentTable.setMarginTop(20));
            }

            ByteArrayResource resource = new ByteArrayResource(byteArrayOutputStream.toByteArray());
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=class_achievements.pdf");
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(resource.contentLength())
                    .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/csv")
    public ResponseEntity<Resource> exportStatsToCSV(@RequestParam(name = "semesterId", defaultValue = "0") int semesterId) {
        Map<String, Object> stats = this.statisticService.getStatisticsForClass(0, semesterId);

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream); CSVPrinter csvPrinter = new CSVPrinter(outputStreamWriter, CSVFormat.EXCEL.withHeader('\ufeff' +
                "MSSV", "Họ", "Tên", "Khoa", "Lớp", "Email", "Số điện thoại", "Điểm rèn luyện", "Xếp loại"))) {

            Map<String, Map<String, Object>> students = (Map<String, Map<String, Object>>) stats.get("students");

            for (Map<String, Object> entry : students.values()) {
                Student student = (Student) entry.get("student");
                String studentCode = student.getStudentCode();
                String lastName = student.getUserId().getLastName();
                String firstName = student.getUserId().getFirstName();
                String facultyName = student.getFacultyId().getName();
                String classNameStr = student.getClassId().getName();
                String email = student.getUserId().getEmail();
                String phoneNumber = student.getUserId().getPhoneNumber();
                int points = (int) entry.get("points");

                String grade = Utils.getRating(points);

                csvPrinter.printRecord(studentCode, lastName, firstName, facultyName, classNameStr, email, phoneNumber, points, grade);
            }

            csvPrinter.flush();

            ByteArrayResource resource = new ByteArrayResource(byteArrayOutputStream.toByteArray());
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=class_achievements.csv");
            headers.setContentType(org.springframework.http.MediaType.parseMediaType("text/csv;charset=UTF-8"));
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(resource.contentLength())
                    .contentType(org.springframework.http.MediaType.parseMediaType("text/csv;charset=UTF-8"))
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
