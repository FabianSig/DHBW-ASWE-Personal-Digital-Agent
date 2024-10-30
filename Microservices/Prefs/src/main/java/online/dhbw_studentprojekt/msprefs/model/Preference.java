package online.dhbw_studentprojekt.msprefs.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "preferences")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Preference {
    @Id
    private String id;
    private List<String> value;

    public online.dhbw_studentprojekt.dto.prefs.Preference toDto() {
        return new online.dhbw_studentprojekt.dto.prefs.Preference(id, value);
    }
}
