package nikomitk.mschatgpt.controller;

import lombok.RequiredArgsConstructor;
import nikomitk.mschatgpt.service.ChatGPTService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatgpt")
@RequiredArgsConstructor
public class ChatGPTController {

    private final ChatGPTService chatGPTService;

    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public String test(@RequestBody String message, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        if(!authorization.equals(System.getenv("OUR_API_KEY"))) {
            throw new RuntimeException("Unauthorized");
        }
        return chatGPTService.test(message);
    }
}
