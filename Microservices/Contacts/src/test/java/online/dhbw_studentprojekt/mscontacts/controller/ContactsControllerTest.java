package online.dhbw_studentprojekt.mscontacts.controller;

import online.dhbw_studentprojekt.mscontacts.service.ContactsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class ContactsControllerTest {

    @Mock
    private ContactsService contactsService;

    @InjectMocks
    private ContactsController contactsController;

    @Test
    void getLastCallDate_ShouldReturnDate() {
        // Arrange
        String contact = "testContact";

        Mockito.when(contactsService.getLastCallDate(contact)).thenReturn(LocalDate.of(2024, 1, 1));
        // Act
        LocalDate result = contactsController.getLastCallDate(contact);

        // Assert
        Assertions.assertEquals(LocalDate.of(2024, 1, 1), result);
        Mockito.verify(contactsService).getLastCallDate(contact);
    }

    @Test
    void getLastCallDates_ShouldReturnDateMap() {
        // Arrange
        Mockito.when(contactsService.getLastCallDates(List.of("testContact1", "testContact2"))).thenReturn(Map.of("testContact1", LocalDate.of(2024, 1, 1), "testContact2", LocalDate.of(2024, 1, 2)));
        // Act
        Map<String, LocalDate> result = contactsController.getLastCallDates(List.of("testContact1", "testContact2"));

        // Assert
        Assertions.assertEquals(LocalDate.of(2024, 1, 1), result.get("testContact1"));
        Assertions.assertEquals(LocalDate.of(2024, 1, 2), result.get("testContact2"));
        Mockito.verify(contactsService).getLastCallDates(List.of("testContact1", "testContact2"));
    }

    @Test
    void getUnreadInDirectory_ShouldReturnUnread() {
        // Arrange
        String directory = "testDirectory";

        Mockito.when(contactsService.getUnreadInDirectory(directory)).thenReturn(42);
        // Act
        int result = contactsController.getUnreadInDirectory(directory);

        // Assert
        Assertions.assertEquals(42, result);
        Mockito.verify(contactsService).getUnreadInDirectory(directory);
    }

    @Test
    void getUnreadInMultipleDirectories_ShouldReturnUnreadMap() {
        // Arrange
        Mockito.when(contactsService.getUnreadInMultipleDirectories(List.of("testDirectory1", "testDirectory2"))).thenReturn(Map.of("testDirectory1", 42, "testDirectory2", 43));
        // Act
        Map<String, Integer> result = contactsController.getUnreadInMultipleDirectories(List.of("testDirectory1", "testDirectory2"));

        // Assert
        Assertions.assertEquals(42, result.get("testDirectory1"));
        Assertions.assertEquals(43, result.get("testDirectory2"));
        Mockito.verify(contactsService).getUnreadInMultipleDirectories(List.of("testDirectory1", "testDirectory2"));
    }

}
