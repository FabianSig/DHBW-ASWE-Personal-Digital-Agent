package online.dhbw_studentprojekt.msprefs.repository;

import online.dhbw_studentprojekt.msprefs.model.Preference;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PrefsRepository extends MongoRepository<Preference, String> { }
