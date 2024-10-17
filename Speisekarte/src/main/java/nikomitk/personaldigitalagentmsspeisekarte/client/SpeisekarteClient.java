package nikomitk.personaldigitalagentmsspeisekarte.client;

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestClient;
import org.springframework.web.service.annotation.PostExchange;

public interface SpeisekarteClient extends RestClient {

    @PostExchange("/inc/ajax-php_konnektor.inc.php")
    String getSpeisekarte(@RequestBody MultiValueMap<String, String> formData);
}
