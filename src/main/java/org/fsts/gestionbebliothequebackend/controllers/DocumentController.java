package org.fsts.gestionbebliothequebackend.controllers;

import com.fasterxml.jackson.databind.JsonNode;
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
@CrossOrigin("http://localhost:3000")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    // saving one document from i json data
    @PostMapping("/save")
    public ResponseEntity<Document> saveDocument(@RequestBody JsonNode json) {
        try {
            Document savedDocument = documentService.saveDocumentFromJson(json);
            return ResponseEntity.ok(savedDocument);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    // saving lot of documents from a json data that have a lot of documents
    @PostMapping("/saveAll")
    public ResponseEntity<List<Document>> saveDocuments(@RequestBody JsonNode jsonArray) {
        try {
            List<Document> savedDocuments = documentService.saveDocumentsFromJsonArray(jsonArray);
            return ResponseEntity.ok(savedDocuments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public Document getById(@PathVariable Long id) {
        return documentService.findById(id).orElseThrow();
    }

    //PUT /documents/changeStatus/1?newStatut=NOT_EXIST
    @PutMapping("/changeStatus/{id}")
    public ResponseEntity<Document> changeDocumentStatus(
            @PathVariable Long id,
            @RequestParam Document.Statut newStatut) {
        try {
            Document updatedDocument = documentService.changeDocumentStatus(id, newStatut);
            return ResponseEntity.ok(updatedDocument);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // tout les documents
    @GetMapping
    public List<Document> getAllDocuments() {
        return documentService.findAllDocuments();
    }

    // modifier un document
    @PutMapping("/update/{id}")
    public ResponseEntity<Document> updateDocument(@PathVariable Long id, @RequestBody JsonNode json) {
        try {
            Document updatedDocument = documentService.updateDocument(id, json);
            return ResponseEntity.ok(updatedDocument);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        try {
            documentService.deleteDocument(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteDocuments(@RequestBody List<Long> ids) {
        try {
            documentService.deleteDocuments(ids);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }




    //*********************************************************************************************************************
    // saving from an exel but it doesn t work for now
   /* @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<String> createDocumentsFromExcel(@RequestParam("file") MultipartFile file) {
        try {
            documentService.saveFromExcel(file);
            return ResponseEntity.status(HttpStatus.CREATED).body("Documents added successfully from Excel file.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing Excel file: " + e.getMessage());
        }
    }*/
}
