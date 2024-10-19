package nikomitk.mschatgpt.dto;

import org.springframework.web.multipart.MultipartFile;

public record ChatGPTAudioRequest(MultipartFile audioFile, String model, String language) {
}
