package com.snilov.bank;

import com.snilov.bank.repository.AccountRepository;
import com.snilov.bank.repository.CardRepository;
import com.snilov.bank.repository.TransactionRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
                .andExpect(jsonPath("$.typeTransaction").value("DEPOSIT"))
                .andExpect(jsonPath("$.transactionAmount").value("10"))
                .andExpect(jsonPath("$.amountBefore").value("0"))
                .andExpect(jsonPath("$.amountAfter").value("10"))
                .andExpect(jsonPath("$.isCanceled").value("false"));
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
                .content(createTransactionsJson(uuidCard, "WITHDRAW", "-100")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.typeTransaction").value("WITHDRAW"))
                .andExpect(jsonPath("$.transactionAmount").value("-100"))
                .andExpect(jsonPath("$.amountBefore").value("0"))
                .andExpect(jsonPath("$.amountAfter").value("-100"))
                .andExpect(jsonPath("$.isCanceled").value("false"));
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
                .andExpect(jsonPath("$.errors.*", hasSize(3)))
                .andExpect(jsonPath("$.errors.uuidCard").value("UUID card cannot be empty"))
                .andExpect(jsonPath("$.errors.typeTransaction").value("Transaction type cannot by empty"))
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
                .andExpect(jsonPath("$.typeTransaction").value("DEPOSIT"))
                .andExpect(jsonPath("$.transactionAmount").value("10"))
                .andExpect(jsonPath("$.amountBefore").value("0"))
                .andExpect(jsonPath("$.amountAfter").value("10"))
                .andExpect(jsonPath("$.isCanceled").value("false"));

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(uuidCard, "DEPOSIT", "100")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.typeTransaction").value("DEPOSIT"))
                .andExpect(jsonPath("$.transactionAmount").value("100"))
                .andExpect(jsonPath("$.amountBefore").value("10"))
                .andExpect(jsonPath("$.amountAfter").value("110"))
                .andExpect(jsonPath("$.isCanceled").value("false"));

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(uuidCard, "WITHDRAW", "-5")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.typeTransaction").value("WITHDRAW"))
                .andExpect(jsonPath("$.transactionAmount").value("-5"))
                .andExpect(jsonPath("$.amountBefore").value("110"))
                .andExpect(jsonPath("$.amountAfter").value("105"))
                .andExpect(jsonPath("$.isCanceled").value("false"));
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
                .andExpect(jsonPath("$.typeTransaction").value("DEPOSIT"))
                .andExpect(jsonPath("$.transactionAmount").value("10"))
                .andExpect(jsonPath("$.amountBefore").value("0"))
                .andExpect(jsonPath("$.amountAfter").value("10"))
                .andExpect(jsonPath("$.isCanceled").value("false"));

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(uuidCard2, "DEPOSIT", "100")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.typeTransaction").value("DEPOSIT"))
                .andExpect(jsonPath("$.transactionAmount").value("100"))
                .andExpect(jsonPath("$.amountBefore").value("10"))
                .andExpect(jsonPath("$.amountAfter").value("110"))
                .andExpect(jsonPath("$.isCanceled").value("false"));

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(uuidCard1, "WITHDRAW", "-5")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.typeTransaction").value("WITHDRAW"))
                .andExpect(jsonPath("$.transactionAmount").value("-5"))
                .andExpect(jsonPath("$.amountBefore").value("110"))
                .andExpect(jsonPath("$.amountAfter").value("105"))
                .andExpect(jsonPath("$.isCanceled").value("false"));

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(uuidCard2, "WITHDRAW", "-50")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.typeTransaction").value("WITHDRAW"))
                .andExpect(jsonPath("$.transactionAmount").value("-50"))
                .andExpect(jsonPath("$.amountBefore").value("105"))
                .andExpect(jsonPath("$.amountAfter").value("55"))
                .andExpect(jsonPath("$.isCanceled").value("false"));
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
                .andExpect(jsonPath("$.typeTransaction").value("DEPOSIT"))
                .andExpect(jsonPath("$.transactionAmount").value("10"))
                .andExpect(jsonPath("$.amountBefore").value("0"))
                .andExpect(jsonPath("$.amountAfter").value("10"))
                .andExpect(jsonPath("$.isCanceled").value("false"));

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(uuidCard2, "DEPOSIT", "100")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.typeTransaction").value("DEPOSIT"))
                .andExpect(jsonPath("$.transactionAmount").value("100"))
                .andExpect(jsonPath("$.amountBefore").value("0"))
                .andExpect(jsonPath("$.amountAfter").value("100"))
                .andExpect(jsonPath("$.isCanceled").value("false"));

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(uuidCard1, "WITHDRAW", "-5")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.typeTransaction").value("WITHDRAW"))
                .andExpect(jsonPath("$.transactionAmount").value("-5"))
                .andExpect(jsonPath("$.amountBefore").value("10"))
                .andExpect(jsonPath("$.amountAfter").value("5"))
                .andExpect(jsonPath("$.isCanceled").value("false"));
    }

    @Test
    public void testCreateNewTransactionWithNotExistsCard() throws Exception {
        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson("1234567", "DEPOSIT", "10")))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("There is no such card"));
    }

    @Test
    public void testCreateNewTransactionWithBlockedCard() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createCardsJson("true", "1", "DEBIT")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.blocked").value("true"))
                .andExpect(jsonPath("$.number").value("1"))
                .andExpect(jsonPath("$.type").value("DEBIT"))
                .andReturn();

        String uuidCard = (new JSONObject(result.getResponse().getContentAsString())).getString("uuid");

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(uuidCard, "DEPOSIT", "10")))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Card is blocked: " + uuidCard));
    }

    @Test
    public void testRollbackTransaction() throws Exception {
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
                .andExpect(jsonPath("$.typeTransaction").value("DEPOSIT"))
                .andExpect(jsonPath("$.transactionAmount").value("10"))
                .andExpect(jsonPath("$.amountBefore").value("0"))
                .andExpect(jsonPath("$.amountAfter").value("10"))
                .andExpect(jsonPath("$.isCanceled").value("false"));

        result = this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(uuidCard, "DEPOSIT", "100")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.typeTransaction").value("DEPOSIT"))
                .andExpect(jsonPath("$.transactionAmount").value("100"))
                .andExpect(jsonPath("$.amountBefore").value("10"))
                .andExpect(jsonPath("$.amountAfter").value("110"))
                .andExpect(jsonPath("$.isCanceled").value("false"))
                .andReturn();

        String uuidTransaction = (new JSONObject(result.getResponse().getContentAsString())).getString("uuid");

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(uuidCard, "DEPOSIT", "1000")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.typeTransaction").value("DEPOSIT"))
                .andExpect(jsonPath("$.transactionAmount").value("1000"))
                .andExpect(jsonPath("$.amountBefore").value("110"))
                .andExpect(jsonPath("$.amountAfter").value("1110"))
                .andExpect(jsonPath("$.isCanceled").value("false"));

        this.mockMvc.perform(post("/transactions/" + uuidTransaction + "/rollback"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.typeTransaction").value("ROLLBACK"))
                .andExpect(jsonPath("$.transactionAmount").value("-100"))
                .andExpect(jsonPath("$.amountBefore").value("1110"))
                .andExpect(jsonPath("$.amountAfter").value("1010"))
                .andExpect(jsonPath("$.isCanceled").value("false"));
    }

    @Test
    public void testRollbackAgainTransaction() throws Exception {
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

        result = this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(uuidCard, "DEPOSIT", "100")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.typeTransaction").value("DEPOSIT"))
                .andExpect(jsonPath("$.transactionAmount").value("100"))
                .andExpect(jsonPath("$.amountBefore").value("0"))
                .andExpect(jsonPath("$.amountAfter").value("100"))
                .andExpect(jsonPath("$.isCanceled").value("false"))
                .andReturn();

        String uuidTransaction = (new JSONObject(result.getResponse().getContentAsString())).getString("uuid");

        this.mockMvc.perform(post("/transactions/" + uuidTransaction + "/rollback"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.typeTransaction").value("ROLLBACK"))
                .andExpect(jsonPath("$.transactionAmount").value("-100"))
                .andExpect(jsonPath("$.amountBefore").value("100"))
                .andExpect(jsonPath("$.amountAfter").value("0"))
                .andExpect(jsonPath("$.isCanceled").value("false"));

        this.mockMvc.perform(get("/transactions/" + uuidTransaction))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.typeTransaction").value("DEPOSIT"))
                .andExpect(jsonPath("$.transactionAmount").value("100"))
                .andExpect(jsonPath("$.amountBefore").value("0"))
                .andExpect(jsonPath("$.amountAfter").value("100"))
                .andExpect(jsonPath("$.isCanceled").value("true"));

        this.mockMvc.perform(post("/transactions/" + uuidTransaction + "/rollback"))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("You can not cancel the transaction again"));
    }

    @Test
    public void testTransferC2C() throws Exception {
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
                .andExpect(jsonPath("$.typeTransaction").value("DEPOSIT"))
                .andExpect(jsonPath("$.transactionAmount").value("10"))
                .andExpect(jsonPath("$.amountBefore").value("0"))
                .andExpect(jsonPath("$.amountAfter").value("10"))
                .andExpect(jsonPath("$.isCanceled").value("false"));

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(uuidCard2, "DEPOSIT", "10")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.typeTransaction").value("DEPOSIT"))
                .andExpect(jsonPath("$.transactionAmount").value("10"))
                .andExpect(jsonPath("$.amountBefore").value("0"))
                .andExpect(jsonPath("$.amountAfter").value("10"))
                .andExpect(jsonPath("$.isCanceled").value("false"));

        this.mockMvc.perform(post("/transactions/transfer")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransferJson("C2C", uuidCard1, uuidCard2, "10")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.transactions", hasSize(2)))
                .andExpect(jsonPath("$._embedded.transactions[0].typeTransaction").value("TRANSFER"))
                .andExpect(jsonPath("$._embedded.transactions[0].transactionAmount").value("-10"))
                .andExpect(jsonPath("$._embedded.transactions[0].amountBefore").value("10"))
                .andExpect(jsonPath("$._embedded.transactions[0].amountAfter").value("0"))
                .andExpect(jsonPath("$._embedded.transactions[0].isCanceled").value("false"))
                .andExpect(jsonPath("$._embedded.transactions[1].typeTransaction").value("TRANSFER"))
                .andExpect(jsonPath("$._embedded.transactions[1].transactionAmount").value("10"))
                .andExpect(jsonPath("$._embedded.transactions[1].amountBefore").value("10"))
                .andExpect(jsonPath("$._embedded.transactions[1].amountAfter").value("20"))
                .andExpect(jsonPath("$._embedded.transactions[1].isCanceled").value("false"));
    }

    @Test
    public void testTransferA2A() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createAccountJson("RUR", "10", "DEBIT")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.currency").value("RUR"))
                .andExpect(jsonPath("$.balance").value("10"))
                .andExpect(jsonPath("$.type").value("DEBIT"))
                .andReturn();

        String uuidAccount1 = (new JSONObject(result.getResponse().getContentAsString())).getString("uuid");

        result = this.mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createAccountJson("RUR", "10", "DEBIT")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.currency").value("RUR"))
                .andExpect(jsonPath("$.balance").value("10"))
                .andExpect(jsonPath("$.type").value("DEBIT"))
                .andReturn();

        String uuidAccount2 = (new JSONObject(result.getResponse().getContentAsString())).getString("uuid");

        this.mockMvc.perform(post("/transactions/transfer")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransferJson("A2A", uuidAccount1, uuidAccount2, "10")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.transactions", hasSize(2)))
                .andExpect(jsonPath("$._embedded.transactions[0].typeTransaction").value("TRANSFER"))
                .andExpect(jsonPath("$._embedded.transactions[0].transactionAmount").value("-10"))
                .andExpect(jsonPath("$._embedded.transactions[0].amountBefore").value("10"))
                .andExpect(jsonPath("$._embedded.transactions[0].amountAfter").value("0"))
                .andExpect(jsonPath("$._embedded.transactions[0].isCanceled").value("false"))
                .andExpect(jsonPath("$._embedded.transactions[1].typeTransaction").value("TRANSFER"))
                .andExpect(jsonPath("$._embedded.transactions[1].transactionAmount").value("10"))
                .andExpect(jsonPath("$._embedded.transactions[1].amountBefore").value("10"))
                .andExpect(jsonPath("$._embedded.transactions[1].amountAfter").value("20"))
                .andExpect(jsonPath("$._embedded.transactions[1].isCanceled").value("false"));
    }
}
