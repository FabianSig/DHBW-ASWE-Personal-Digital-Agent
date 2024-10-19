package nikomitk.mschatgpt.dto;

import java.io.File;

public record ChatGPTAudioRequest(File audioFile, String model, String language) {
}
