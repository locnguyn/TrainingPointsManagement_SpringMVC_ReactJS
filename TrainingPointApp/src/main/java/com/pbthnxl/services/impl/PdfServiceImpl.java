package com.pbthnxl.services.impl;

import com.pbthnxl.services.PdfService;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class PdfServiceImpl implements PdfService {

    @Override
    public ByteArrayInputStream createPdf(Map<String, String> params) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try ( PdfWriter writer = new PdfWriter(out);  PdfDocument pdf = new PdfDocument(writer);  Document document = new Document(pdf, PageSize.A4)) {

            /// Đường dẫn tới font Noto Sans Regular trong thư mục resources của project
            String fontPath = "./arial.ttf";

            // Tạo PdfFont từ file font
            PdfFont font = PdfFontFactory.createFont(fontPath, "Identity-H");

            // Set font cho document
            document.setFont(font);

            // Title: ĐỀ CƯƠNG MÔN HỌC
            Paragraph title = new Paragraph("ĐỀ CƯƠNG MÔN HỌC")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold();
            document.add(title);
            

            // Add details
            document.add(new Paragraph(String.format("Mã đề cương: %s", params.get("outlineId"))).setFont(font));
            document.add(new Paragraph(String.format("Tên môn học: %s", params.get("subjectName"))).setFont(font));
            document.add(new Paragraph(String.format("Tên khoa: %s", params.get("facultyName"))).setFont(font));
            document.add(new Paragraph(String.format("Tên giảng viên soạn: %s", params.get("instructorName"))).setFont(font));

            // Create a table
            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                    .useAllAvailableWidth();
            table.addCell("Ngày bắt đầu soạn");
            table.addCell(params.get("startDate"));
            table.addCell("Ngày hoàn thành");
            table.addCell(params.get("endDate"));
            table.addCell("Tín chỉ thực hành");
            table.addCell(params.get("practicalCredits"));
            table.addCell("Tín chỉ lý thuyết");
            table.addCell(params.get("theoreticalCredits"));
            document.add(table);

            // Add additional information
            document.add(new Paragraph(String.format("Mô tả: %s", params.get("description"))).setFont(font));
            document.add(new Paragraph(String.format("Trạng thái: %s", params.get("status"))).setFont(font));
            document.add(new Paragraph(String.format("Khóa (năm): %s", params.get("courseYear"))).setFont(font));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}