package org.fsts.gestionbebliothequebackend.services;

import org.apache.poi.ss.usermodel.*;
import org.fsts.gestionbebliothequebackend.repositories.DocumentRepository;
import org.fsts.gestionbebliothequebackend.entities.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public List<Document> findAllDocuments() {
        return documentRepository.findAll();
    }

    public Optional<Document> findDocumentById(Long id) {
        return documentRepository.findById(id);
    }

    public Document saveDocument(Document document) {
        return documentRepository.save(document);
    }

    public Document updateDocument(Long id, Document newDocument) {
        return documentRepository.findById(id).map(document -> {
            document.setAuteur(newDocument.getAuteur());
            document.setTitre(newDocument.getTitre());
            document.setSousTitre(newDocument.getSousTitre());
            document.setEdition(newDocument.getEdition());
            document.setCote1(newDocument.getCote1());
            document.setCote2(newDocument.getCote2());
            document.setDescripteurs(newDocument.getDescripteurs());
            return documentRepository.save(document);
        }).orElseGet(() -> {
            newDocument.setId(id);
            return documentRepository.save(newDocument);
        });
    }
    public void saveFromExcel(MultipartFile file) throws Exception {
        try (InputStream is = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row
                try {
                Document document = new Document();
                document.setAuteur(getCellValueAsString(row.getCell(0)));
                document.setTitre(row.getCell(1).getStringCellValue());
                document.setSousTitre(getCellValueAsString(row.getCell(2)));
                document.setEdition(getCellValueAsString(row.getCell(3)));
                document.setCote1(getCellValueAsString(row.getCell(4)));
                document.setCote2(getCellValueAsString(row.getCell(5)));
                document.setDescripteurs(getCellValueAsString(row.getCell(6)));


                    documentRepository.save(document);
                } catch (Exception e) {
                    System.out.println("Error saving document: " + e.getMessage());
                    e.printStackTrace(); // This will print the stack trace for debugging
                }
            }
            workbook.close();
        }
    }
    // traiter les cases vide
    private String getCellValueAsString(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return " "; // retourner espace
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return " "; // on va stocker dans la base de donner un " " si la case est vide
        }
    }

    public void deleteDocument(Long id) {
        if (documentRepository.existsById(id)) {
            documentRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Document with ID " + id + " not found.");
        }
    }
}
