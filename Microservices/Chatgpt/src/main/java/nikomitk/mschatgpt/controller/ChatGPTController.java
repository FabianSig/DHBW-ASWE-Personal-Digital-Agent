package nikomitk.mschatgpt.controller;

import nikomitk.mschatgpt.service.ChatGPTService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatgpt")
public class ChatGPTController {

    @GetMapping("/test")
    public String test(@RequestBody String message) {
        return ChatGPTService.test(message);
    }
}
