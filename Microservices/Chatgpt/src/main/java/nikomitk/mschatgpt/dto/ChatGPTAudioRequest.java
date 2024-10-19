package nikomitk.mschatgpt.dto;

import org.springframework.web.multipart.MultipartFile;

public record ChatGPTAudioRequest(MultipartFile file, String model, String language) {
}
