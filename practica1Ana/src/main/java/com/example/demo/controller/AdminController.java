package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.DatabaseExportService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DatabaseExportService databaseExportService;

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportDatabase() {
        byte[] sqlFile = databaseExportService.exportDatabaseToSql();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=productos.sql")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(sqlFile);
    }
}
