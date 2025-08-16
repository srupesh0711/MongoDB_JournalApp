package net.engineeringdigest.journalApp.Service;

import net.engineeringdigest.journalApp.Dto.JournalEntryDto;
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

    public JournalEntryDto saveEntry(JournalEntryDto journalEntryDto){
        JournalEntry journalEntryToSave = new JournalEntry();
        journalEntryToSave.setTitle(journalEntryDto.getTitle());
        journalEntryToSave.setContent(journalEntryDto.getContent());
        journalEntryToSave.setDate(journalEntryDto.getDate());
        JournalEntry savedEntry = journalEntryRepository.save(journalEntryToSave);
        journalEntryDto.setId(savedEntry.getId());
        return journalEntryDto;
    }

    public List<JournalEntry> findAll(){
        return journalEntryRepository.findAll();
    }
    public JournalEntry getEntryById(ObjectId id) {
        return journalEntryRepository.findById(id).orElse(null);
    }
    public void deleteEntryById(ObjectId id) {
        journalEntryRepository.deleteById(id);
    }

    public JournalEntry updateEntryById(ObjectId id, JournalEntryDto updatedEntry) {
        return journalEntryRepository.findById(id).map(existingEntry -> {
            if (updatedEntry.getTitle() != null && !updatedEntry.getTitle().isEmpty()) {
                existingEntry.setTitle(updatedEntry.getTitle());
            }
            if (updatedEntry.getContent() != null && !updatedEntry.getContent().isEmpty()) {
                existingEntry.setContent(updatedEntry.getContent());
            }
            return journalEntryRepository.save(existingEntry);
        }).orElse(null);
    }
}
