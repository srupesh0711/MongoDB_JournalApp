package net.engineeringdigest.journalApp.Service;

import net.engineeringdigest.journalApp.Entity.JournalEntry;
import net.engineeringdigest.journalApp.Repository.JournalEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JournalService {

private JournalEntryRepository journalEntryRepository;


    public JournalService(JournalEntryRepository journalEntryRepository) {
        this.journalEntryRepository = journalEntryRepository;
    }

    public JournalEntry saveEntry(JournalEntry journalEntry){
        JournalEntry save = journalEntryRepository.save(journalEntry);

        return save;
    }

    public List<JournalEntry> RegisteredAll(JournalEntry journalEntry){
        List<JournalEntry> all = journalEntryRepository.findAll();
        return all;
    }
    public JournalEntry getEntryById(String id) {
        JournalEntry entryById = journalEntryRepository.findById(id).get();
        return entryById;
    }
    public void deleteEntryById(String id) {
        journalEntryRepository.deleteById(id);
    }

    public JournalEntry updateEntryById(String id, JournalEntry updatedEntry) {
        JournalEntry existingEntry = journalEntryRepository.findById(id).orElse(null);
        if (existingEntry == null) {
            return null; // or throw an exception
        }
        // Update fields (example: title, content, date)
        existingEntry.setTitle(updatedEntry.getTitle());
        existingEntry.setContent(updatedEntry.getContent());
        existingEntry.setDate(updatedEntry.getDate());
        // Save and return updated entry
        JournalEntry save = journalEntryRepository.save(existingEntry);
        return save;
    }
}
