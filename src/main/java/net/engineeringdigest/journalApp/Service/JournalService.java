package net.engineeringdigest.journalApp.Service;

import net.engineeringdigest.journalApp.Entity.JournalEntry;
import net.engineeringdigest.journalApp.Repository.JournalEntryRepository;
import org.bson.types.ObjectId;
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
    public JournalEntry getEntryById(ObjectId id) {
        JournalEntry entryById = journalEntryRepository.findById(id).get();
        return entryById;
    }
    public void deleteEntryById(ObjectId id) {
        journalEntryRepository.deleteById(id);
    }

    public JournalEntry updateEntryById(ObjectId id, JournalEntry updatedEntry) {
        JournalEntry existingEntry = journalEntryRepository.findById(id).orElse(null);
        if (existingEntry != null) {
            if (updatedEntry.getTitle() != null && !updatedEntry.getTitle().equals("")) {
                existingEntry.setTitle(updatedEntry.getTitle());
            }
            if (updatedEntry.getContent() != null && !updatedEntry.getContent().equals("")) {
                existingEntry.setContent(updatedEntry.getContent());
            }
        }
        return journalEntryRepository.save(existingEntry);
    }
}
