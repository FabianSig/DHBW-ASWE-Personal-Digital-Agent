package nikomitk.mschatgpt.dto.audio;

import org.springframework.web.multipart.MultipartFile;

public record ChatGPTAudioRequest(MultipartFile file, String model, String language) {
}
