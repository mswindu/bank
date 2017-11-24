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

import static com.snilov.bank.Utils.createAccountJson;
import static com.snilov.bank.Utils.createCardsJson;
import static com.snilov.bank.Utils.createCardsWithIncorrectParametersJson;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CardControllerTests {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        this.mockMvc = builder.build();
    }

    @Test
    public void testCreateNewCardAndNewAccount() throws Exception {
        this.mockMvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createCardsJson("false", "1", "DEBIT")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.blocked").value("false"))
                .andExpect(jsonPath("$.number").value("1"))
                .andExpect(jsonPath("$.type").value("DEBIT"));
    }

    @Test
    public void testCreateNewCardWithExistingAccount() throws Exception {
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
    }

    @Test
    public void testCreateNewCardWithNotExistingAccount() throws Exception {
        String uuidAccount = "random-string-name";

        this.mockMvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createCardsJson("false", "1", "DEBIT", uuidAccount)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("There is no such account"));
    }

    @Test
    public void testCreateNewCardWithIncorrectParameters() throws Exception {
        this.mockMvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createCardsWithIncorrectParametersJson("false", "1", "DEBIT")))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.message").value("Invalid parameters specified."))
                .andExpect(jsonPath("$.errors.*", hasSize(3)))
                .andExpect(jsonPath("$.errors.number").value("Number card cannot be empty"))
                .andExpect(jsonPath("$.errors.blocked").value("Blocked status cannot be empty"))
                .andExpect(jsonPath("$.errors.type").value("Type card cannot be empty"));
    }
}
