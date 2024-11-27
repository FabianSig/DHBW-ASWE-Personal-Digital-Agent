package online.dhbw_studentprojekt.mschatgpt.repository;

import online.dhbw_studentprojekt.mschatgpt.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface MessageRepository extends MongoRepository<Message, String> {
}