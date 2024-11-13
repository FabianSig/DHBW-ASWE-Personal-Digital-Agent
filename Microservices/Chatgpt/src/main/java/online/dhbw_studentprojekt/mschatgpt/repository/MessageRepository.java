package online.dhbw_studentprojekt.mschatgpt.repository;

import online.dhbw_studentprojekt.mschatgpt.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {

    List<Message> findByChatId(String chatId);
}