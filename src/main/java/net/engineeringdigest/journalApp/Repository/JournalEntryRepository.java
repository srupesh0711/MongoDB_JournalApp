package net.engineeringdigest.journalApp.Repository;

import net.engineeringdigest.journalApp.Entity.JournalEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface JournalEntryRepository extends MongoRepository<JournalEntry, String> {

    List<JournalEntry> findByUsername(String username);
    long deleteByIdAndUsername(String id, String username);
}
