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

    /**
     * Retrieves the number of unread emails in multiple directories.
     *
     * @param directories the list of directories to check
     * @return a map of directory names to the number of unread emails
     */
    public Map<String, Integer> getUnreadInMultipleDirectories(List<String> directories) {

        Map<String, Integer> unreadMap = new HashMap<>();

        // Iterate over directories and get unread emails count
        for (String directory : directories) {
            if (directory.isBlank()) {
                continue;
            }
            int unread = getUnreadInDirectory(directory);
            unreadMap.put(directory, unread);
        }

        // If no directories are provided, get unread emails count for INBOX
        if (unreadMap.isEmpty()) {
            unreadMap.put("INBOX", getUnreadInDirectory("INBOX"));
        }
        return unreadMap;
    }

    /**
     * Retrieves the number of unread emails in a specific directory.
     *
     * @param directory the directory to check
     * @return the number of unread emails
     * @throws ResponseStatusException if there is an error accessing the email store
     */
    public int getUnreadInDirectory(String directory) {

        try {
            return emailStore.getFolder(directory).getUnreadMessageCount();
        } catch (MessagingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Exception getting unread emails count", e);
        }
    }

    /**
     * Retrieves the last call dates for multiple contacts.
     *
     * @param contacts the list of contacts to check
     * @return a map of contact names to the last call dates
     */
    public Map<String, LocalDate> getLastCallDates(List<String> contacts) {

        Map<String, LocalDate> lastCallDates = new HashMap<>();

        // Iterate over contacts and get last call date
        for (String contact : contacts) {
            if (contact.isBlank()) {
                continue;
            }
            LocalDate lastCallDate = getLastCallDate(contact);
            lastCallDates.put(contact, lastCallDate);
        }

        return lastCallDates;
    }

    /**
     * Retrieves the last call date for a specific contact.
     *
     * @param contact the contact to check
     * @return the last call date
     */
    public LocalDate getLastCallDate(String contact) {

        return phoneClient.getLastCallDate(contact);
    }

}
