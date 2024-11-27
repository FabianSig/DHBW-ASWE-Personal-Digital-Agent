package online.dhbw_studentprojekt.mscontacts.service;

import lombok.RequiredArgsConstructor;
import online.dhbw_studentprojekt.mscontacts.client.PhoneClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.mail.Store;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ContactsService {

    private final Store emailStore;
    private final PhoneClient phoneClient;

    public Map<String, Integer> getUnreadInMultipleDirectories(List<String> directories) {

        Map<String, Integer> unreadMap = new HashMap<>();
        for (String directory : directories) {
            if(directory.isBlank()) {
                continue;
            }
            int unread = getUnreadInDirectory(directory);
            unreadMap.put(directory, unread);
        }
        return unreadMap;
    }

    public int getUnreadInDirectory(String directory) {

        try {
            return emailStore.getFolder(directory).getUnreadMessageCount();
        } catch (MessagingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Exception getting unread emails count", e);
        }
    }

    public Map<String, LocalDate> getLastCallDates(List<String> contacts) {

        Map<String, LocalDate> lastCallDates = new HashMap<>();
        for (String contact : contacts) {
            if(!contact.isBlank()) {
                continue;
            }
            LocalDate lastCallDate = getLastCallDate(contact);
            lastCallDates.put(contact, lastCallDate);
        }
        return lastCallDates;
    }

    public LocalDate getLastCallDate(String contact) {
        // TODO remove
        Map<String, LocalDate> callDates = new HashMap<>();
        callDates.put("Mama", LocalDate.of(2024, 10, 24));
        callDates.put("Papa", LocalDate.of(2024, 9, 15));
        return callDates.get(contact);
    }

}
