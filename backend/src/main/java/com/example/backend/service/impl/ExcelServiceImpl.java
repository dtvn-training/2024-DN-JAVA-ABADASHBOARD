package com.example.backend.service.impl;

import com.example.backend.service.ExcelService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Override
    public byte[] generateExcel() throws IOException {
        StringBuilder csvContent = new StringBuilder();
        String[] headers = { "ID", "Name", "Email" };
        csvContent.append(String.join(",", headers)).append("\n");

        String[][] data = {
                { "1", "John Doe", "john.doe@example.com" },
                { "2", "Jane Smith", "jane.smith@example.com" }
        };

        for (String[] rowData : data) {
            csvContent.append(String.join(",", rowData)).append("\n");
        }

        return csvContent.toString().getBytes(StandardCharsets.UTF_8);
    }
}
