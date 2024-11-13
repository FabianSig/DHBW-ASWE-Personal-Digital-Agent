package online.dhbw_studentprojekt.mschatgpt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
