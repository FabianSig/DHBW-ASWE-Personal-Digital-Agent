package online.dhbw_studentprojekt.msprefs.controller;

import online.dhbw_studentprojekt.msprefs.config.TestSecurityConfig;
import online.dhbw_studentprojekt.msprefs.model.Preference;
import online.dhbw_studentprojekt.msprefs.service.PrefsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(PrefsController.class)
@Import(TestSecurityConfig.class)
class PrefsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PrefsService prefsService;

    @Test
    void getPref_shouldReturnPreferenceWhenFound() throws Exception {
        String id = "testId";
        Preference preference = new Preference(id, List.of("value1", "value2"));
        Mockito.when(prefsService.getPref(id)).thenReturn(Optional.of(preference));

        mockMvc.perform(get("/api/prefs")
                        .param("id", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void createPref_shouldReturnCreatedStatus() throws Exception {
        String requestBody = """
                {
                    "id": "testId",
                    "value": ["value1", "value2"]
                }
                """;

        mockMvc.perform(post("/api/prefs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        Mockito.verify(prefsService, Mockito.times(1)).createPref(Mockito.any(online.dhbw_studentprojekt.dto.prefs.Preference.class));
    }

    @Test
    void deletePref_shouldReturnNoContentStatus() throws Exception {
        String id = "testId";

        mockMvc.perform(delete("/api/prefs")
                        .param("id", id))
                .andExpect(status().isNoContent());

        Mockito.verify(prefsService, Mockito.times(1)).deletePref(id);
    }
}

