package com.example.backend.service.impl;

import com.example.backend.service.ExcelService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Override
    public byte[] generateExcel() throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Data");
            Row headerRow = sheet.createRow(0);

            Cell headerCell = headerRow.createCell(0);
            headerCell.setCellValue("Header");

            Row dataRow = sheet.createRow(1);
            Cell dataCell = dataRow.createCell(0);
            dataCell.setCellValue("Sample Data");

            workbook.write(out);
            return out.toByteArray();
        }
    }
}