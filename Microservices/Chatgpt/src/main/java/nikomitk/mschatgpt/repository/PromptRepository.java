package nikomitk.mschatgpt.repository;

import nikomitk.mschatgpt.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PromptRepository extends MongoRepository<Message, String> {
    List<Message> findByPromptId(String chatId);

}
