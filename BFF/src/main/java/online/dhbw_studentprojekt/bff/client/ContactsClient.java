package online.dhbw_studentprojekt.bff.client;

import org.springframework.web.service.annotation.GetExchange;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ContactsClient {

    @GetExchange("/api/contacts/mail/directory")
    int getUnreadInDirectory(String directory);

    @GetExchange("/api/contacts/mail/directories")
    Map<String, Integer> getUnreadInMultipleDirectories(List<String> directories);

    @GetExchange("/api/contacts/phone/contact")
    int getLastCallDate(String contact);

    @GetExchange("/api/contacts/phone/contacts")
    Map<String, LocalDate> getLastCallDates(List<String> contacts);

}
