package net.engineeringdigest.journalApp.Controller;

import net.engineeringdigest.journalApp.Dto.ApiResponse;
import net.engineeringdigest.journalApp.Dto.JournalEntryDto;
import net.engineeringdigest.journalApp.Entity.JournalEntry;
import net.engineeringdigest.journalApp.Service.JournalService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("journal/api/v1")
public class JournalEntryController {
    @Autowired
    private JournalService journalService;



   // http://localhost:8080/journal/api/v1/entries
   @GetMapping("/entries")
   public ResponseEntity<ApiResponse<List<JournalEntry>>> getAll() {
       List<JournalEntry> entries = journalService.findAll();
       String message = entries.isEmpty() ? "No journal entries found." : "Journal entries retrieved successfully.";
       return ResponseEntity.ok(new ApiResponse<>("success", message, entries));
   }

    @PostMapping("/registry")
    public ResponseEntity<ApiResponse<JournalEntryDto>> createEntry(@RequestBody JournalEntryDto journalEntry) {
        journalEntry.setDate(LocalDateTime.now());
        JournalEntryDto savedEntry = journalService.saveEntry(journalEntry);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("success", "Journal entry created successfully.", savedEntry));
    }

    @GetMapping("/entries/{id}")
    public ResponseEntity<ApiResponse<JournalEntry>> getEntryById(@PathVariable ObjectId id) {
        JournalEntry entry = journalService.getEntryById(id);
        if (entry != null) {
            return ResponseEntity.ok(new ApiResponse<>("success", "Journal entry found.", entry));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("error", "Journal entry not found for id: " + id, null));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteEntryById(@PathVariable ObjectId id) {
        JournalEntry entry = journalService.getEntryById(id);
        if (entry != null) {
            journalService.deleteEntryById(id);
            return ResponseEntity.ok(new ApiResponse<>("success", "Journal entry deleted successfully.", null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("error", "Journal entry not found for id: " + id, null));
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<JournalEntry>> updateEntryById(@PathVariable ObjectId id, @RequestBody JournalEntryDto updatedEntry) {
        JournalEntry entry = journalService.updateEntryById(id, updatedEntry);
        if (entry != null) {
            return ResponseEntity.ok(new ApiResponse<>("success", "Journal entry updated successfully.", entry));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("error", "Journal entry not found for id: " + id, null));
        }
    }
}
