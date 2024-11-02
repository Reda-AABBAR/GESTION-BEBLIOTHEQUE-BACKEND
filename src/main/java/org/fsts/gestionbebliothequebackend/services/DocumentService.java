package org.fsts.gestionbebliothequebackend.services;

import org.fsts.gestionbebliothequebackend.repositories.DocumentRepository;
import org.fsts.gestionbebliothequebackend.entities.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;

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

                Document document = new Document();
                document.setAuteur(row.getCell(0).getStringCellValue());
                document.setTitre(row.getCell(1).getStringCellValue());
                document.setSousTitre(row.getCell(2).getStringCellValue());
                document.setEdition(row.getCell(3).getStringCellValue());
                document.setCote1(row.getCell(4).getStringCellValue());
                document.setCote2(row.getCell(5).getStringCellValue());
                document.setDescripteurs(row.getCell(6).getStringCellValue());

                documentRepository.save(document);
            }
            workbook.close();
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
