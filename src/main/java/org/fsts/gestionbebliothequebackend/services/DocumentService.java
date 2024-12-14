package org.fsts.gestionbebliothequebackend.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.poi.ss.usermodel.*;
import org.fsts.gestionbebliothequebackend.repositories.DocumentRepository;
import org.fsts.gestionbebliothequebackend.entities.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Base64;
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

    @Transactional
    public Document saveDocumentFromJson(JsonNode json) throws Exception {
        Document document = new Document();

        // Parse fields from JSON
        document.setTitre(json.get("titre").asText());
        document.setSousTitre(json.get("sousTitre").asText());
        document.setEdition(json.get("edition").asText());
        document.setCote1(json.get("cote1").asText());
        document.setCote2(json.get("cote2").asText());
        document.setStatut(Document.Statut.valueOf(json.get("statut").asText()));
        document.setNbrExemplaire(json.get("nbrExemplaire").asInt());

        // Parse lists for auteurs and descripteurs
        List<String> auteurs = new ArrayList<>();
        json.get("auteurs").forEach(auteur -> auteurs.add(auteur.asText()));
        document.setAuteurs(auteurs);

        List<String> descripteurs = new ArrayList<>();
        json.get("descripteurs").forEach(descripteur -> descripteurs.add(descripteur.asText()));
        document.setDescripteurs(descripteurs);

        // Save document to DB
        return documentRepository.save(document);
    }

    @Transactional
    public List<Document> saveDocumentsFromJsonArray(JsonNode jsonArray) throws Exception {
        List<Document> savedDocuments = new ArrayList<>();

        for (JsonNode json : jsonArray) {
            Document savedDocument = saveDocumentFromJson(json);
            if (json.has("img")) {
                String base64Image = json.get("img").asText();
                byte[] imageBytes = Base64.getDecoder().decode(base64Image);
                savedDocument.setImg(imageBytes);
            }
            savedDocuments.add(savedDocument);
        }

        return savedDocuments;
    }
    public Document updateDocument(Long id, JsonNode json) throws Exception {
        Optional<Document> optionalDocument = documentRepository.findById(id);
        if (optionalDocument.isEmpty()) {
            throw new Exception("Document not found");
        }

        Document document = optionalDocument.get();

        // Update fields from JSON
        document.setTitre(json.get("titre").asText());
        document.setSousTitre(json.get("sousTitre").asText());
        document.setEdition(json.get("edition").asText());
        document.setCote1(json.get("cote1").asText());
        document.setCote2(json.get("cote2").asText());
        document.setStatut(Document.Statut.valueOf(json.get("statut").asText()));
        document.setNbrExemplaire(json.get("nbrExemplaire").asInt());
        //stocker l image
        if (json.has("img")) {
            String base64Image = json.get("img").asText();
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            document.setImg(imageBytes);
        }

        List<String> auteurs = new ArrayList<>();
        json.get("auteurs").forEach(auteur -> auteurs.add(auteur.asText()));
        document.setAuteurs(auteurs);

        List<String> descripteurs = new ArrayList<>();
        json.get("descripteurs").forEach(descripteur -> descripteurs.add(descripteur.asText()));
        document.setDescripteurs(descripteurs);

        // Save updated document to DB
        return documentRepository.save(document);
    }

    public void deleteDocument(Long id) throws Exception {
        if (!documentRepository.existsById(id)) {
            throw new Exception("Document not found");
        }
        documentRepository.deleteById(id);
    }
    public void deleteDocuments(List<Long> ids) throws Exception {
        for (Long id : ids) {
            if (!documentRepository.existsById(id)) {
                throw new Exception("Document with id " + id + " not found");
            }
        }
        documentRepository.deleteAllById(ids);
    }
    public Document changeDocumentStatus(Long id, Document.Statut newStatut) throws Exception {
        Optional<Document> optionalDocument = documentRepository.findById(id);
        if (optionalDocument.isEmpty()) {
            throw new Exception("Document not found");
        }

        Document document = optionalDocument.get();
        document.setStatut(newStatut);

        return documentRepository.save(document);
    }

    public Optional<Document> findById(Long id) {
        return documentRepository.findById(id);
    }



    /*   public void saveFromExcel(MultipartFile file) throws Exception {
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
    }*/
// ell ne fonctionne pas pour l instant
}
