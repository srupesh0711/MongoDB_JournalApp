package net.engineeringdigest.journalApp.Controller;

import net.engineeringdigest.journalApp.Dto.ApiResponse;
import net.engineeringdigest.journalApp.Dto.JournalEntryDto;
import lombok.RequiredArgsConstructor;
import net.engineeringdigest.journalApp.Entity.JournalEntry;
import net.engineeringdigest.journalApp.Service.JournalService;
import net.engineeringdigest.journalApp.Service.PdfService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/journal")
@RequiredArgsConstructor
public class JournalEntryController {
    private final JournalService journalService;
    private final PdfService pdfService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<JournalEntry>>> getAll() {
        String username = getUsername();
        List<JournalEntry> entries = journalService.findByUsername(username);
        String message = entries.isEmpty() ? "No journal entries found." : "Journal entries retrieved successfully.";
        return ResponseEntity.ok(new ApiResponse<>("success", message, entries));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<JournalEntry>> createEntry(@Valid @RequestBody JournalEntryDto journalEntryDto) {
        String username = getUsername();
        JournalEntry newEntry = new JournalEntry();
        newEntry.setTitle(journalEntryDto.getTitle());
        newEntry.setContent(journalEntryDto.getContent());
        JournalEntry savedEntry = journalService.saveEntry(newEntry, username);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("success", "Journal entry created successfully.", savedEntry));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JournalEntry>> getEntryById(@PathVariable String id) {
        String username = getUsername();
        JournalEntry journalEntry = journalService.findByIdAndUsername(id, username);
        return ResponseEntity.ok(new ApiResponse<>("success", "Journal entry found.", journalEntry));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteEntryById(@PathVariable String id) {
        String username = getUsername();
        journalService.deleteById(id, username);
        return ResponseEntity.ok(new ApiResponse<>("success", "Journal entry deleted successfully.", null));
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<InputStreamResource> getEntryAsPdf(@PathVariable String id) {
        String username = getUsername();
        JournalEntry journalEntry = journalService.findByIdAndUsername(id, username);
        ByteArrayInputStream pdf = pdfService.createPdf(journalEntry);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=" + journalEntry.getTitle().replaceAll("\\s+", "_") + ".pdf");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<JournalEntry>> updateEntryById(@PathVariable String id, @Valid @RequestBody JournalEntryDto updatedEntryDto) {
        String username = getUsername();
        JournalEntry newEntryData = new JournalEntry();
        newEntryData.setTitle(updatedEntryDto.getTitle());
        newEntryData.setContent(updatedEntryDto.getContent());

        JournalEntry updated = journalService.updateEntryById(id, newEntryData, username);
        return ResponseEntity.ok(new ApiResponse<>("success", "Journal entry updated successfully.", updated));
    }

    private String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
