package online.dhbw_studentprojekt.mscontacts.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.dhbw_studentprojekt.mscontacts.service.ContactsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
public class ContactsController {

    private final ContactsService contactsService;

    @GetMapping("/mail/directory")
    public int getUnreadInDirectory(@RequestParam String directory) {

        return contactsService.getUnreadInDirectory(directory);
    }

    @GetMapping("/mail/directories")
    public Map<String, Integer> getUnreadInMultipleDirectories(@RequestParam List<String> directories) {

        return contactsService.getUnreadInMultipleDirectories(directories);
    }

    @GetMapping("/phone/contact")
    public LocalDate getLastCallDate(@RequestParam String contact) {

        return contactsService.getLastCallDate(contact);
    }

    @GetMapping("/phone/contacts")
    public Map<String, LocalDate> getLastCallDates(@RequestParam List<String> contacts) {

        return contactsService.getLastCallDates(contacts);
    }

}
