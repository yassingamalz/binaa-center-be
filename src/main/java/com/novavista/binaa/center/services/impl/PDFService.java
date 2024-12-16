package com.novavista.binaa.center.services.impl;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class PDFService {
    private static final String TEMPLATES_PATH = "pdf-templates/";
    private static final String ARABIC_FONT_PATH = "fonts/arabic.ttf";

    public byte[] fillFormTemplate(String templateName, Map<String, String> formFields) throws IOException {
        try (PDDocument document = loadTemplate(templateName)) {
            PDAcroForm form = document.getDocumentCatalog().getAcroForm();

            if (form == null) {
                throw new IllegalStateException("No form found in template");
            }

            // Enable Unicode text handling
            form.setNeedAppearances(true);

            // Load Arabic font
            ClassPathResource fontResource = new ClassPathResource(ARABIC_FONT_PATH);
            PDType0Font arabicFont = PDType0Font.load(document, fontResource.getInputStream());

            // Create default resources with Arabic font
            PDResources resources = new PDResources();
            String fontName = resources.add(arabicFont).getName();

            // Set resources and default appearance
            form.setDefaultResources(resources);
            String defaultAppearance = String.format("/%s 12 Tf 0 g", fontName);

            // Fill form fields with Arabic text support
            for (Map.Entry<String, String> field : formFields.entrySet()) {
                PDField formField = form.getField(field.getKey());
                if (formField instanceof PDTextField textField) {
                    textField.setDefaultAppearance(defaultAppearance);
                    textField.setValue(field.getValue());
                }
            }

            // Convert to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }

    private PDDocument loadTemplate(String templateName) throws IOException {
        ClassPathResource resource = new ClassPathResource(TEMPLATES_PATH + templateName);
        return PDDocument.load(resource.getInputStream());
    }
}