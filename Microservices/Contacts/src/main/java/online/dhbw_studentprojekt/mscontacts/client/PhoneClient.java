package online.dhbw_studentprojekt.mscontacts.client;

import java.time.LocalDate;

public interface PhoneClient {

    LocalDate getLastCallDate(String contact);

}
