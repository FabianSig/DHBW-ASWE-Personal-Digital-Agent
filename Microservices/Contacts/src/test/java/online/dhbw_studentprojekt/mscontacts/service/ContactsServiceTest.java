package online.dhbw_studentprojekt.mscontacts.service;

import online.dhbw_studentprojekt.mscontacts.client.EmailClient;
import online.dhbw_studentprojekt.mscontacts.client.PhoneClient;
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
public class ContactsServiceTest {

    @Mock
    private PhoneClient phoneClient;

    @Mock
    private EmailClient emailClient;

    @InjectMocks
    private ContactsService contactsService;

    @Test
    void getLastCallDate_ShouldReturnDate() {
        // Arrange
        String contact = "testContact";

        Mockito.when(contactsService.getLastCallDate(contact)).thenReturn(LocalDate.of(2024, 1, 1));
        // Act
        LocalDate result = contactsService.getLastCallDate(contact);

        // Assert
        Assertions.assertEquals(LocalDate.of(2024, 1, 1), result);
        Mockito.verify(phoneClient).getLastCallDate(contact);
    }

    @Test
    void getLastCallDates_ShouldReturnDateMap() {
        // Arrange
        Mockito.when(phoneClient.getLastCallDate("testContact1")).thenReturn(LocalDate.of(2024, 1, 1));
        Mockito.when(phoneClient.getLastCallDate("testContact2")).thenReturn(LocalDate.of(2024, 1, 2));
        // Act
        Map<String, LocalDate> result = contactsService.getLastCallDates(List.of("testContact1", "testContact2"));

        // Assert
        Assertions.assertEquals(LocalDate.of(2024, 1, 1), result.get("testContact1"));
        Assertions.assertEquals(LocalDate.of(2024, 1, 2), result.get("testContact2"));
    }

    @Test
    void getUnreadInDirectory_ShouldReturnUnread() {
        // Arrange
        String directory = "testDirectory";

        Mockito.when(contactsService.getUnreadInDirectory(directory)).thenReturn(42);
        // Act
        int result = contactsService.getUnreadInDirectory(directory);

        // Assert
        Assertions.assertEquals(42, result);
        Mockito.verify(emailClient).getUnreadInDirectory(directory);
    }

    @Test
    void getUnreadInMultipleDirectories_ShouldReturnUnreadMap() {
        // Arrange
        Mockito.when(emailClient.getUnreadInDirectory("testDirectory1")).thenReturn(42);
        Mockito.when(emailClient.getUnreadInDirectory("testDirectory2")).thenReturn(43);
        // Act
        Map<String, Integer> result = contactsService.getUnreadInMultipleDirectories(List.of("testDirectory1", "testDirectory2"));

        // Assert
        Assertions.assertEquals(42, result.get("testDirectory1"));
        Assertions.assertEquals(43, result.get("testDirectory2"));
    }
}
