package online.dhbw_studentprojekt.mscontacts.config;

import online.dhbw_studentprojekt.mscontacts.client.PhoneClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;

@Configuration
public class ClientConfig {

    @Value("${mail.imap.host}")
    private String mailHost;

    @Value("${mail.imap.port}")
    private int mailPort;

    @Value("${mail.username}")
    private String mailUsername;

    @Value("${mail.password}")
    private String mailPassword;

    @Value("${phone.api.url}")
    private String phoneApiUrl;

    @Bean
    public Store emailStore() {

        Properties properties = new Properties();

        properties.put("mail.imap.host", mailHost);
        properties.put("mail.imap.port", mailPort);
        properties.put("mail.imap.starttls.enable", "true");

        Session emailSession = Session.getDefaultInstance(properties);
        try {
            Store mailStore = emailSession.getStore("imaps");
            mailStore.connect(mailHost, mailUsername, mailPassword);

            return mailStore;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Couldn't get email store", e);
        }
    }

    @Bean
    public PhoneClient phoneClient() {

        RestClient restClient = RestClient.builder()
                .baseUrl(phoneApiUrl)
                .defaultHeader("Authorization", "Bearer " + System.getenv("API_KEY"))
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(PhoneClient.class);
    }

}
