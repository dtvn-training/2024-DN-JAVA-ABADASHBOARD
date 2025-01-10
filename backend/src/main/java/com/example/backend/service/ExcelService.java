package com.example.backend.service;

import java.io.IOException;

public interface ExcelService {
    byte[] generateExcel() throws IOException;
}