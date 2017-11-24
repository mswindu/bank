package com.snilov.bank;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.snilov.bank.Utils.createCardsJson;
import static com.snilov.bank.Utils.createTransactionsJson;
import static com.snilov.bank.Utils.createTransactionsWithIncorrectParametersJson;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionControllerTests {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup () {
        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        this.mockMvc = builder.build();
    }

    @Test
    public void testCreateNewDepositTransaction() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createCardsJson("false","1", "DEBIT")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.blocked").value("false"))
                .andExpect(jsonPath("$.number").value("1"))
                .andExpect(jsonPath("$.type").value("DEBIT"))
                .andReturn();

        String uuidCard = (new JSONObject(result.getResponse().getContentAsString())).getString("uuid");

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(uuidCard,"DEPOSIT", "10")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionAmount").value("10"))
                .andExpect(jsonPath("$.amountBefore").value("0"))
                .andExpect(jsonPath("$.amountAfter").value("10"));
    }

    @Test
    public void testCreateNewWithdrawTransaction() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createCardsJson("false","1", "DEBIT")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.blocked").value("false"))
                .andExpect(jsonPath("$.number").value("1"))
                .andExpect(jsonPath("$.type").value("DEBIT"))
                .andReturn();

        String uuidCard = (new JSONObject(result.getResponse().getContentAsString())).getString("uuid");

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(uuidCard,"WITHDRAW", "100")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionAmount").value("100"))
                .andExpect(jsonPath("$.amountBefore").value("0"))
                .andExpect(jsonPath("$.amountAfter").value("-100"));
    }

    @Test
    public void testCreateNewTransactionWithIncorrectParameters() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createCardsJson("false","1", "DEBIT")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.blocked").value("false"))
                .andExpect(jsonPath("$.number").value("1"))
                .andExpect(jsonPath("$.type").value("DEBIT"))
                .andReturn();

        String uuidCard = (new JSONObject(result.getResponse().getContentAsString())).getString("uuid");

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsWithIncorrectParametersJson(uuidCard,"DEPOSIT", "10")))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.message").value("Invalid parameters specified."))
                .andExpect(jsonPath("$.errors.uuidCard").value("UUID card cannot be empty"))
                .andExpect(jsonPath("$.errors.transactionAmount").value("Transaction amount cannot be empty"));
    }

    @Test
    public void testCreateNewTransactionsWithOneAccount() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createCardsJson("false","1", "DEBIT")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.blocked").value("false"))
                .andExpect(jsonPath("$.number").value("1"))
                .andExpect(jsonPath("$.type").value("DEBIT"))
                .andReturn();

        String uuidCard = (new JSONObject(result.getResponse().getContentAsString())).getString("uuid");

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(uuidCard,"DEPOSIT", "10")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionAmount").value("10"))
                .andExpect(jsonPath("$.amountBefore").value("0"))
                .andExpect(jsonPath("$.amountAfter").value("10"));

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(uuidCard,"DEPOSIT", "100")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionAmount").value("100"))
                .andExpect(jsonPath("$.amountBefore").value("10"))
                .andExpect(jsonPath("$.amountAfter").value("110"));

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(uuidCard,"WITHDRAW", "5")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionAmount").value("5"))
                .andExpect(jsonPath("$.amountBefore").value("110"))
                .andExpect(jsonPath("$.amountAfter").value("105"));
    }

    @Test
    public void testCreateNewTransactionsWithMultipleAccounts() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createCardsJson("false","1", "DEBIT")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.blocked").value("false"))
                .andExpect(jsonPath("$.number").value("1"))
                .andExpect(jsonPath("$.type").value("DEBIT"))
                .andReturn();

        String uuidCard1 = (new JSONObject(result.getResponse().getContentAsString())).getString("uuid");

        result = this.mockMvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createCardsJson("false","2", "DEBIT")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.blocked").value("false"))
                .andExpect(jsonPath("$.number").value("2"))
                .andExpect(jsonPath("$.type").value("DEBIT"))
                .andReturn();

        String uuidCard2 = (new JSONObject(result.getResponse().getContentAsString())).getString("uuid");

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(uuidCard1,"DEPOSIT", "10")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionAmount").value("10"))
                .andExpect(jsonPath("$.amountBefore").value("0"))
                .andExpect(jsonPath("$.amountAfter").value("10"));

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(uuidCard2,"DEPOSIT", "100")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionAmount").value("100"))
                .andExpect(jsonPath("$.amountBefore").value("0"))
                .andExpect(jsonPath("$.amountAfter").value("100"));

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(uuidCard1,"WITHDRAW", "5")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionAmount").value("5"))
                .andExpect(jsonPath("$.amountBefore").value("10"))
                .andExpect(jsonPath("$.amountAfter").value("5"));
    }
}
