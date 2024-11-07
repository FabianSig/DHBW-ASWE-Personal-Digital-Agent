package nikomitk.mschatgpt.repository;

import nikomitk.mschatgpt.model.Prompt;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PromptRepository extends MongoRepository<Prompt, String> {
    List<Prompt> findByPromptId(String promptId);

    Optional<Prompt> findFirstByPromptId(String promptId);
}
