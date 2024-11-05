package online.dhbw_studentprojekt.msnews.client;

import online.dhbw_studentprojekt.msnews.dto.News;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClient;
import org.springframework.web.service.annotation.GetExchange;


public interface NewsClient extends RestClient {

    @GetExchange("/v2/top-headlines?country=us")
    News getNews(@RequestParam(required = false, value = "category") String topic);

}

