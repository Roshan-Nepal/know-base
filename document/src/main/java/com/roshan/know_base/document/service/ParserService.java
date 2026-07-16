package com.roshan.know_base.document.service;

import com.roshan.know_base.common.enums.ErrorCode;
import com.roshan.know_base.document.entity.DocumentType;
import com.roshan.know_base.document.exception.DocumentProcessingException;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

@Service
public class ParserService {
    private final Tika tika;

    public  ParserService(Tika tika){
        this.tika = tika;
    }

    /**
     * Extracts the text from an InputStream
     * @param inputStream the inputStream from which the text is to be extracted
     * @return extracted text from the inputStream
     * @throws DocumentProcessingException if the file is corrupted or parsing fails
     */
    public String extractText(InputStream inputStream){
        try{
            // limit extraction to 10,000,00 i.e 10MB
            // to prevents OutOfMemory crash on malicious and massive file
            BodyContentHandler handler = new BodyContentHandler(10_000_000);
            Metadata metadata = new Metadata();
            tika.getParser().parse(inputStream, handler, metadata, new ParseContext());
            return handler.toString();
        }catch (IOException | TikaException | SAXException e){
            throw new DocumentProcessingException("Document failed processing", ErrorCode.DOCUMENT_PROCESSING_FAILED, HttpStatus.UNPROCESSABLE_CONTENT);
        }
    }

    public DocumentType determineDocumentType(String contentType, String filename) {

        if (filename != null && filename.contains(".")) {
            String extension = filename.substring(filename.lastIndexOf(".")).toLowerCase();

            return switch (extension) {
                case ".md", ".markdown" -> DocumentType.MARKDOWN;
                case ".java", ".py", ".js", ".ts", ".cpp", ".c", ".go", ".rs",
                     ".html", ".css", ".json", ".xml", ".yaml", ".yml", ".sh" -> DocumentType.CODE;
                case ".txt", ".csv" -> DocumentType.TEXT;
                case ".pdf" -> DocumentType.PDF;
                case ".doc", ".docx" -> DocumentType.DOCX;
                default -> fallbackToContentType(contentType);
            };
        }

        return fallbackToContentType(contentType);
    }

    private DocumentType fallbackToContentType(String contentType) {
        if (contentType == null) return DocumentType.TEXT;

        return switch (contentType.toLowerCase()) {
            case "application/pdf" -> DocumentType.PDF;
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                 "application/msword" -> DocumentType.DOCX;
            case "text/markdown", "text/x-markdown" -> DocumentType.MARKDOWN;
            default -> {
                if (contentType.startsWith("text/")) yield DocumentType.TEXT;
                yield DocumentType.TEXT; // Default to text if we truly don't know
            }
        };
    }




}
