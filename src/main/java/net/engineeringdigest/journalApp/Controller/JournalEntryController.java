package net.engineeringdigest.journalApp.Controller;

import net.engineeringdigest.journalApp.Entity.JournalEntry;
import net.engineeringdigest.journalApp.Service.JournalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("journal/api/v1")
public class JournalEntryController {
    @Autowired
    private JournalService journalService;



   // http://localhost:8080/journal/api/v1
    @GetMapping("/entries")
    public ResponseEntity<List<JournalEntry>>getAll(JournalEntry journalEntry){
        List<JournalEntry> entry = journalService.RegisteredAll(journalEntry);
        return new ResponseEntity<>(entry,HttpStatus.OK);
    }

    @PostMapping("/registry")
    public ResponseEntity<JournalEntry> createEntry(
            @RequestBody JournalEntry journalEntry
    ){
        JournalEntry savedEntry = journalService.saveEntry(journalEntry);
        return new ResponseEntity<>(savedEntry, HttpStatus.CREATED);
    }

    @GetMapping("/entries/{id}")
    public JournalEntry getEntryById(@PathVariable Long id) {
    return null;
    }
    @DeleteMapping("/delete/{id}")
    public JournalEntry deleteEntryById(@PathVariable Long id) {
        return null;
    }
    @PutMapping("/update/{id}")
    public JournalEntry updateEntryById(@PathVariable Long id, @RequestBody JournalEntry updatedEntry) {
        return null;
    }
}
