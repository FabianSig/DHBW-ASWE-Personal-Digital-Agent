package nikomitk.mschatgpt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.annotation.Documented;

@Document(value = "prompt")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Prompt {
    @Id
    private String id;
    private String promptId;
    private String role;
    private String content;
}
