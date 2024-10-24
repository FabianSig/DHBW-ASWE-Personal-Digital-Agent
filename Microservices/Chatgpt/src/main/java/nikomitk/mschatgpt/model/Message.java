package nikomitk.mschatgpt.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "messages")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatId;
    private String role;
    private String content;

}
