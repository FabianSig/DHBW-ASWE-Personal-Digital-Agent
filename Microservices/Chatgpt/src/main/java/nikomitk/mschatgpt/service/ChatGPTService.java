package nikomitk.mschatgpt.service;

import org.springframework.stereotype.Service;

@Service
public class ChatGPTService {
    public static String test(String message) {
        return "Hello Es wurde automatisch deployed Test 2, " + message;
    }
}
