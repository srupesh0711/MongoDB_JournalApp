package net.engineeringdigest.journalApp.Service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import net.engineeringdigest.journalApp.Entity.JournalEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    private static final Logger log = LoggerFactory.getLogger(PdfService.class);

    public ByteArrayInputStream createPdf(JournalEntry entry) {
        log.info("Creating PDF for entry: {}", entry.getTitle());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Document document = new Document();
        PdfWriter.getInstance(document, out);

        document.open();
        document.add(new Paragraph(entry.getTitle(), new Font(Font.HELVETICA, 24, Font.BOLD)));
        document.add(new Paragraph("Date: " + entry.getDate().toString()));
        document.add(new Paragraph("\n"));
        document.add(new Paragraph(entry.getContent()));
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}