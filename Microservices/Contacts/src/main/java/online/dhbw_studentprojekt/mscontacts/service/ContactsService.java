package online.dhbw_studentprojekt.mscontacts.service;

import lombok.RequiredArgsConstructor;
import online.dhbw_studentprojekt.mscontacts.client.EmailClient;
import online.dhbw_studentprojekt.mscontacts.client.PhoneClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ContactsService {

    private final EmailClient emailClient;
    private final PhoneClient phoneClient;

    public int getUnreadInDirectory(String directory) {

        return emailClient.getUnreadInDirectory(directory);
    }

    public Map<String, Integer> getUnreadInMultipleDirectories(List<String> directories) {

        Map<String, Integer> unreadMap = new HashMap<>();
        for (String directory : directories) {
            int unread = getUnreadInDirectory(directory);
            unreadMap.put(directory, unread);
        }
        return unreadMap;
    }

    public LocalDate getLastCallDate(String contact) {

        return phoneClient.getLastCallDate(contact);
    }

    public Map<String, LocalDate> getLastCallDates(List<String> contacts) {

        Map<String, LocalDate> lastCallDates = new HashMap<>();
        for (String contact : contacts) {
            LocalDate lastCallDate = getLastCallDate(contact);
            lastCallDates.put(contact, lastCallDate);
        }
        return lastCallDates;
    }

}
