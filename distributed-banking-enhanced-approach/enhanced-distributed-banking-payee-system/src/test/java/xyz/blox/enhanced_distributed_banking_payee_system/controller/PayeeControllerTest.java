package xyz.blox.enhanced_distributed_banking_payee_system.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest
@AutoConfigureMockMvc
public class PayeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    //Successful Transfer
    @Test
    public void testSuccessfulTransfer() throws Exception {
        mockMvc.perform(post("/payee/transfer")
                .param("senderAccountId", "AccA1")
                .param("receiverAccountId", "AccB1")
                .param("amount", "100"))
                .andExpect(status().isOk())
                .andExpect(content().string("Transfer Successful"));
    }
    //Duplicate Transaction (Idempotency)
    @Test
    public void testDuplicateTransaction() throws Exception {
        // First request
        mockMvc.perform(post("/payee/transfer")
                .param("senderAccountId", "AccA1")
                .param("receiverAccountId", "AccB1")
                .param("amount", "100")
                .header("Transaction-ID", "bf81682d-f007-43e2-83bd-97908d2863f6"))
                .andExpect(status().isOk())
                .andExpect(content().string("Transfer Successful")); //Part 1 - Will be checked in ReceiverControllerTest
    }
    //Data Integrity Check (Hash Comparison)
    @Test
    public void testDataIntegrityCheckSuccess() throws Exception {
        String validHash = "validHash";  // Use the actual hash logic here

        mockMvc.perform(post("/payee/transfer")
                .param("senderAccountId", "AccA1")
                .param("receiverAccountId", "AccB1")
                .param("amount", "100")
                .header("Integrity-Hash", validHash))
                .andExpect(status().isOk())
                .andExpect(content().string("Transfer Successful"));
    }
    //Rollback on Receiver Failure
    @Test
    public void testRollbackOnReceiverFailure() throws Exception {
        // Simulate Receiver failure by stopping the service or throwing an exception

        mockMvc.perform(post("/payee/transfer")
                .param("senderAccountId", "AccA1")
                .param("receiverAccountId", "AccA1")
                .param("amount", "100"))
                .andExpect(status().isInternalServerError());
    }
    //Invalid Parameters
    @Test
    public void testInvalidParametersMissingSender() throws Exception {
        mockMvc.perform(post("/payee/transfer")
                .param("receiverAccountId", "AccB1")
                .param("amount", "100"))
                .andExpect(status().isBadRequest());
    }
    //Invalid Parameters
    @Test
    public void testInvalidParametersMissingReceiver() throws Exception {
        mockMvc.perform(post("/payee/transfer")
                .param("senderAccountId", "AccA1")
                .param("amount", "100"))
                .andExpect(status().isBadRequest());
    }
    //Invalid Parameters
    @Test
    public void testInvalidParametersMissingAmount() throws Exception {
        mockMvc.perform(post("/payee/transfer")
                .param("senderAccountId", "AccA1")
                .param("receiverAccountId", "AccB1"))
                .andExpect(status().isBadRequest());
    }
}