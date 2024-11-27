package online.dhbw_studentprojekt.bff.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import online.dhbw_studentprojekt.bff.service.IntentionMessageService;
import online.dhbw_studentprojekt.bff.service.RoutineService;
import online.dhbw_studentprojekt.bff.service.TriggerService;
import online.dhbw_studentprojekt.dto.chatgpt.standard.MessageRequest;
import online.dhbw_studentprojekt.dto.trigger.Trigger;
import online.dhbw_studentprojekt.dto.trigger.TriggerResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(LogicController.class)
class BFFLogicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IntentionMessageService intentionMessageService;

    @MockBean
    private TriggerService triggerService;

    @MockBean
    private RoutineService routineService;

    @Autowired
    private ObjectMapper objectMapper;

    private MessageRequest messageRequest;

    @BeforeEach
    void setUp() {
        messageRequest = new MessageRequest("How do I get from A to B?");
    }

    @Test
    void testSendMessage() throws Exception {
        String responseMessage = "Here is the route information.";

        when(intentionMessageService.getResponseMessage(any(MessageRequest.class)))
                .thenReturn(responseMessage);

        mockMvc.perform(post("/api/logic/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(responseMessage));

        verify(intentionMessageService).getResponseMessage(any(MessageRequest.class));
    }

    @Test
    void testGetTrigger() throws Exception {
        String date = "2024-11-20";
        TriggerResponse triggerResponse = new TriggerResponse(List.of(new Trigger("/api/speisekarte", "2024-11-20")));

        when(triggerService.getTrigger(date)).thenReturn(triggerResponse);

        mockMvc.perform(get("/api/logic/trigger")
                        .param("date", date))
                .andExpect(status().isOk());

    }

    @Test
    void testGetMorningRoutine() throws Exception {
        String morningRoutine = "Morning routine details.";

        when(routineService.getMorningRoutine()).thenReturn(morningRoutine);

        mockMvc.perform(get("/api/logic/morning"))
                .andExpect(status().isOk())
                .andExpect(content().string(morningRoutine));

        verify(routineService).getMorningRoutine();
    }

    @Test
    void testGetMittagRoutine() throws Exception {
        String mittagRoutine = "Mittag routine details.";

        when(routineService.getMittagRoutine()).thenReturn(mittagRoutine);

        mockMvc.perform(get("/api/logic/mittag"))
                .andExpect(status().isOk())
                .andExpect(content().string(mittagRoutine));

        verify(routineService).getMittagRoutine();
    }
}
