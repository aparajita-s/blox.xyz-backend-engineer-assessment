package xyz.blox.enhanced_distributed_banking_receiver_system.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc

public class ReceiverControllerTest {
    @Autowired
    private MockMvc mockMvc;

    //Retry Same Transaction
    @Test
    public void testDuplicateTransaction() throws Exception {
        // Second request with same Transaction-ID
        mockMvc.perform(post("/receiver/credit")
                .param("senderId", "AccA1")
                .param("accountId", "AccB1")
                .param("amount", "100")
                .header("Transaction-ID", "bf81682d-f007-43e2-83bd-97908d2863f6"))
                .andExpect(status().isBadRequest());
    }
    //Data Integrity Check (Hash Comparison)
    @Test
    public void testDataIntegrityCheckFailure() throws Exception {
        String invalidHash = "cE53c37nbDKhlKqioISfTvjyf2KUoQpr75s02V4GhHo=";  

        mockMvc.perform(post("/receiver/credit")
                .param("senderId", "AccA2")
                .param("accountId", "AccB1")
                .param("amount", "5000")
                .header("Integrity-Hash", invalidHash))
                .andExpect(status().isBadRequest());
    }
    //Invalid Parameters
    @Test
    public void testInvalidParametersMissingSender() throws Exception {
        mockMvc.perform(post("/receiver/credit")
                .param("accountId", "AccB1")
                .param("amount", "100"))
                .andExpect(status().isBadRequest());
    }
    //Invalid Parameters
    @Test
    public void testInvalidParametersMissingReceiver() throws Exception {
        mockMvc.perform(post("/receiver/credit")
                .param("senderId", "AccA1")
                .param("amount", "100"))
                .andExpect(status().isBadRequest());
    }
    //Invalid Parameters
    @Test
    public void testInvalidParametersMissingAmount() throws Exception {
        mockMvc.perform(post("/receiver/credit")
                .param("senderId", "AccA1")
                .param("accountId", "AccB1"))
                .andExpect(status().isBadRequest());
    }
}
