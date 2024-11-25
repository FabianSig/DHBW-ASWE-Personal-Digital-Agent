package online.dhbw_studentprojekt.mscontacts.service;

import online.dhbw_studentprojekt.mscontacts.client.PhoneClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class ContactsServiceTest {

    @Mock
    private PhoneClient phoneClient;

    @Mock
    private Store emailClient;

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
    void getUnreadInDirectory_ShouldReturnUnread() throws MessagingException {
        // Arrange
        String directory = "testDirectory";
        Folder folder = Mockito.mock(Folder.class);

        Mockito.when(folder.getUnreadMessageCount()).thenReturn(42);
        Mockito.when(emailClient.getFolder(directory)).thenReturn(folder);
        // Act
        int result = contactsService.getUnreadInDirectory(directory);

        // Assert
        Assertions.assertEquals(42, result);
    }

    @Test
    void getUnreadInMultipleDirectories_ShouldReturnUnreadMap() throws MessagingException {
        // Arrange
        String firstDir = "testDirectory1";
        String secondDir = "testDirectory2";

        Folder testDirectory1 = Mockito.mock(Folder.class);
        Folder testDirectory2 = Mockito.mock(Folder.class);

        Mockito.when(testDirectory1.getUnreadMessageCount()).thenReturn(42);
        Mockito.when(testDirectory2.getUnreadMessageCount()).thenReturn(43);

        Mockito.when(emailClient.getFolder(firstDir)).thenReturn(testDirectory1);
        Mockito.when(emailClient.getFolder(secondDir)).thenReturn(testDirectory2);
        // Act
        Map<String, Integer> result = contactsService.getUnreadInMultipleDirectories(List.of(firstDir, secondDir));

        // Assert
        Assertions.assertEquals(42, result.get(firstDir));
        Assertions.assertEquals(43, result.get(secondDir));
    }

}
