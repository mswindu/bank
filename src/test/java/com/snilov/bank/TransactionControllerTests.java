package com.snilov.bank;

import com.snilov.bank.account.AccountRepository;
import com.snilov.bank.card.CardRepository;
import com.snilov.bank.transaction.TransactionRepository;
import org.json.JSONObject;
import org.junit.After;
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

import static com.snilov.bank.Utils.*;
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

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Before
    public void setup() {
        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        this.mockMvc = builder.build();
    }

    @After
    public void tearDown() {
        transactionRepository.deleteAll();
        cardRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    public void testCreateNewDepositTransaction() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createCardsJson("false", "1", "DEBIT")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.blocked").value("false"))
                .andExpect(jsonPath("$.number").value("1"))
                .andExpect(jsonPath("$.type").value("DEBIT"))
                .andReturn();

        String uuidCard = (new JSONObject(result.getResponse().getContentAsString())).getString("uuid");

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(uuidCard, "DEPOSIT", "10")))
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
                .content(createCardsJson("false", "1", "DEBIT")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.blocked").value("false"))
                .andExpect(jsonPath("$.number").value("1"))
                .andExpect(jsonPath("$.type").value("DEBIT"))
                .andReturn();

        String uuidCard = (new JSONObject(result.getResponse().getContentAsString())).getString("uuid");

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(uuidCard, "WITHDRAW", "100")))
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
                .content(createCardsJson("false", "1", "DEBIT")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.blocked").value("false"))
                .andExpect(jsonPath("$.number").value("1"))
                .andExpect(jsonPath("$.type").value("DEBIT"))
                .andReturn();

        String uuidCard = (new JSONObject(result.getResponse().getContentAsString())).getString("uuid");

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsWithIncorrectParametersJson(uuidCard, "DEPOSIT", "10")))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.message").value("Invalid parameters specified."))
                .andExpect(jsonPath("$.errors.uuidCard").value("UUID card cannot be empty"))
                .andExpect(jsonPath("$.errors.transactionAmount").value("Transaction amount cannot be empty"));
    }

    @Test
    public void testCreateNewTransactionsWithOneAccountOneCard() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createCardsJson("false", "1", "DEBIT")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.blocked").value("false"))
                .andExpect(jsonPath("$.number").value("1"))
                .andExpect(jsonPath("$.type").value("DEBIT"))
                .andReturn();

        String uuidCard = (new JSONObject(result.getResponse().getContentAsString())).getString("uuid");

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(uuidCard, "DEPOSIT", "10")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionAmount").value("10"))
                .andExpect(jsonPath("$.amountBefore").value("0"))
                .andExpect(jsonPath("$.amountAfter").value("10"));

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(uuidCard, "DEPOSIT", "100")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionAmount").value("100"))
                .andExpect(jsonPath("$.amountBefore").value("10"))
                .andExpect(jsonPath("$.amountAfter").value("110"));

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(uuidCard, "WITHDRAW", "5")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionAmount").value("5"))
                .andExpect(jsonPath("$.amountBefore").value("110"))
                .andExpect(jsonPath("$.amountAfter").value("105"));
    }

    @Test
    public void testCreateNewTransactionsWithOneAccountMultipleCards() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createAccountJson("RUR", "0", "DEBIT")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.currency").value("RUR"))
                .andExpect(jsonPath("$.balance").value("0"))
                .andExpect(jsonPath("$.type").value("DEBIT"))
                .andReturn();

        String uuidAccount = (new JSONObject(result.getResponse().getContentAsString())).getString("uuid");

        result = this.mockMvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createCardsJson("false", "1", "DEBIT", uuidAccount)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.blocked").value("false"))
                .andExpect(jsonPath("$.number").value("1"))
                .andExpect(jsonPath("$.type").value("DEBIT"))
                .andReturn();

        String uuidCard1 = (new JSONObject(result.getResponse().getContentAsString())).getString("uuid");

        result = this.mockMvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createCardsJson("false", "2", "DEBIT", uuidAccount)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.blocked").value("false"))
                .andExpect(jsonPath("$.number").value("2"))
                .andExpect(jsonPath("$.type").value("DEBIT"))
                .andReturn();

        String uuidCard2 = (new JSONObject(result.getResponse().getContentAsString())).getString("uuid");

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(uuidCard1, "DEPOSIT", "10")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionAmount").value("10"))
                .andExpect(jsonPath("$.amountBefore").value("0"))
                .andExpect(jsonPath("$.amountAfter").value("10"));

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(uuidCard2, "DEPOSIT", "100")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionAmount").value("100"))
                .andExpect(jsonPath("$.amountBefore").value("10"))
                .andExpect(jsonPath("$.amountAfter").value("110"));

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(uuidCard1, "WITHDRAW", "5")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionAmount").value("5"))
                .andExpect(jsonPath("$.amountBefore").value("110"))
                .andExpect(jsonPath("$.amountAfter").value("105"));

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(uuidCard2, "WITHDRAW", "50")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionAmount").value("50"))
                .andExpect(jsonPath("$.amountBefore").value("105"))
                .andExpect(jsonPath("$.amountAfter").value("55"));
    }

    @Test
    public void testCreateNewTransactionsWithMultipleAccountsMultipleCards() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createCardsJson("false", "1", "DEBIT")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.blocked").value("false"))
                .andExpect(jsonPath("$.number").value("1"))
                .andExpect(jsonPath("$.type").value("DEBIT"))
                .andReturn();

        String uuidCard1 = (new JSONObject(result.getResponse().getContentAsString())).getString("uuid");

        result = this.mockMvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createCardsJson("false", "2", "DEBIT")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.blocked").value("false"))
                .andExpect(jsonPath("$.number").value("2"))
                .andExpect(jsonPath("$.type").value("DEBIT"))
                .andReturn();

        String uuidCard2 = (new JSONObject(result.getResponse().getContentAsString())).getString("uuid");

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(uuidCard1, "DEPOSIT", "10")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionAmount").value("10"))
                .andExpect(jsonPath("$.amountBefore").value("0"))
                .andExpect(jsonPath("$.amountAfter").value("10"));

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(uuidCard2, "DEPOSIT", "100")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionAmount").value("100"))
                .andExpect(jsonPath("$.amountBefore").value("0"))
                .andExpect(jsonPath("$.amountAfter").value("100"));

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(uuidCard1, "WITHDRAW", "5")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionAmount").value("5"))
                .andExpect(jsonPath("$.amountBefore").value("10"))
                .andExpect(jsonPath("$.amountAfter").value("5"));
    }
}
