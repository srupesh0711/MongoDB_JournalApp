package net.engineeringdigest.journalApp.Service;

import net.engineeringdigest.journalApp.Entity.JournalEntry;
import net.engineeringdigest.journalApp.Entity.User;
import net.engineeringdigest.journalApp.Exceptions.JournalEntryNotFoundException;
import lombok.RequiredArgsConstructor;
import net.engineeringdigest.journalApp.Repository.JournalEntryRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JournalService {

    private final JournalEntryRepository journalEntryRepository;

    public JournalEntry saveEntry(JournalEntry journalEntry, String username) {
        journalEntry.setUsername(username);
        journalEntry.setDate(new Date());
        return journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> findByUsername(String username) {
        return journalEntryRepository.findByUsername(username);
    }

    public JournalEntry findByIdAndUsername(String id, String username) {
        JournalEntry journalEntry = journalEntryRepository.findById(id)
                .orElseThrow(() -> new JournalEntryNotFoundException("Journal entry not found with id: " + id));
        if (!journalEntry.getUsername().equals(username)) {
            throw new JournalEntryNotFoundException("Journal entry not found with id: " + id + " for this user.");
        }
        return journalEntry;
    }

    public void deleteById(String id, String username) {
        if (journalEntryRepository.deleteByIdAndUsername(id, username) == 0) {
            throw new JournalEntryNotFoundException("Journal entry not found with id: " + id + " for this user, or you do not have permission to delete it.");
        }
    }

    public JournalEntry updateEntryById(String id, JournalEntry newEntry, String username) {
        JournalEntry oldEntry = findByIdAndUsername(id, username);
        oldEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().isEmpty() ? newEntry.getTitle() : oldEntry.getTitle());
        oldEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().isEmpty() ? newEntry.getContent() : oldEntry.getContent());
        return journalEntryRepository.save(oldEntry);
    }
}
