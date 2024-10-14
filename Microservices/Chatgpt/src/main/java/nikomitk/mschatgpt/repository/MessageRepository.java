package nikomitk.mschatgpt.repository;

import nikomitk.mschatgpt.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {
}
