package org.fsts.gestionbebliothequebackend.controllers;

import org.fsts.gestionbebliothequebackend.entities.Document;
import org.fsts.gestionbebliothequebackend.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    // Endpoint to create a new document from JSON (POST)
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Document> createDocumentFromJson(@RequestBody Document document) {
        Document savedDocument = documentService.saveDocument(document);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDocument);
    }

    // Endpoint to create multiple documents from an Excel file (POST)
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<String> createDocumentsFromExcel(@RequestParam("file") MultipartFile file) {
        try {
            documentService.saveFromExcel(file);
            return ResponseEntity.status(HttpStatus.CREATED).body("Documents added successfully from Excel file.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing Excel file: " + e.getMessage());
        }
    }

    // tout les documents
    @GetMapping
    public List<Document> getAllDocuments() {
        return documentService.findAllDocuments();
    }

    // Endpoint to delete a document by ID (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDocument(@PathVariable Long id) {
        try {
            documentService.deleteDocument(id);
            return ResponseEntity.status(HttpStatus.OK).body("Document deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
