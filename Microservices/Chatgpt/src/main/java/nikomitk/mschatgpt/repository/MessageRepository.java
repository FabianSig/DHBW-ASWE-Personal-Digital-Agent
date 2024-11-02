package nikomitk.mschatgpt.repository;

import nikomitk.mschatgpt.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {

    List<Message> findByChatId(String chatId);
}