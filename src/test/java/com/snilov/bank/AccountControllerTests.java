package com.snilov.bank;

import com.snilov.bank.repository.AccountRepository;
import com.snilov.bank.repository.CardRepository;
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

import static com.snilov.bank.Utils.createAccountJson;
import static com.snilov.bank.Utils.createAccountWithIncorrectParametersJson;
import static com.snilov.bank.Utils.createCardsJson;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountControllerTests {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        this.mockMvc = builder.build();
    }

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private AccountRepository accountRepository;

    @After
    public void tearDown() {
        cardRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    public void testCreateNewAccount() throws Exception {
        this.mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createAccountJson("RUR", "0", "DEBIT")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.currency").value("RUR"))
                .andExpect(jsonPath("$.balance").value("0"))
                .andExpect(jsonPath("$.type").value("DEBIT"));
    }

    @Test
    public void testCreateNewAccountWithIncorrectParameters() throws Exception {
        this.mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createAccountWithIncorrectParametersJson("RUR", "0", "DEBIT")))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.message").value("Invalid parameters specified."))
                .andExpect(jsonPath("$.errors.*", hasSize(3)))
                .andExpect(jsonPath("$.errors.balance").value("Balance cannot be empty"))
                .andExpect(jsonPath("$.errors.currency").value("Currency cannot be empty"))
                .andExpect(jsonPath("$.errors.type").value("Account type cannot be empty"));
    }

    @Test
    public void testInvalidRequest() throws Exception {
        this.mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createAccountJson("RUR", "0", "DEBIT")))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetCardsForAccount() throws Exception {
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

        this.mockMvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createCardsJson("false", "1", "DEBIT", uuidAccount)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.blocked").value("false"))
                .andExpect(jsonPath("$.number").value("1"))
                .andExpect(jsonPath("$.type").value("DEBIT"));

        this.mockMvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createCardsJson("false", "2", "DEBIT", uuidAccount)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.blocked").value("false"))
                .andExpect(jsonPath("$.number").value("2"))
                .andExpect(jsonPath("$.type").value("DEBIT"));

        this.mockMvc.perform(get("/accounts/" + uuidAccount + "/findCard"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.cards", hasSize(2)))
                .andExpect(jsonPath("$._embedded.cards[0].number").value("1"))
                .andExpect(jsonPath("$._embedded.cards[0].type").value("DEBIT"))
                .andExpect(jsonPath("$._embedded.cards[0].blocked").value("false"))
                .andExpect(jsonPath("$._embedded.cards[1].number").value("2"))
                .andExpect(jsonPath("$._embedded.cards[1].type").value("DEBIT"))
                .andExpect(jsonPath("$._embedded.cards[1].blocked").value("false"))
                .andReturn();
    }
}
