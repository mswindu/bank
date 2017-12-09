package com.snilov.bank;

import com.snilov.bank.model.enums.AccountTypeEnum;
import com.snilov.bank.model.enums.CurrencyEnum;
import com.snilov.bank.repository.AccountRepository;
import com.snilov.bank.repository.CardRepository;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.snilov.bank.Utils.createAccountJson;
import static com.snilov.bank.Utils.createAccountWithIncorrectParametersJson;
import static com.snilov.bank.Utils.createCardsJson;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.halLinks;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
public class AccountControllerTests {

    @Autowired
    private MockMvc mockMvc;

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
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/accounts")
                .content(createAccountJson("RUR", "0", "DEBIT"))
                .contentType(MediaType.APPLICATION_JSON_UTF8));

        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.currency").value("RUR"))
                .andExpect(jsonPath("$.balance").value("0"))
                .andExpect(jsonPath("$.type").value("DEBIT"));

        resultActions.andDo(document("create-account",
                links(halLinks(),
                        linkWithRel("self").description("This account"),
                        linkWithRel("find_cards").description("Search for cards that have an account")
                ),
                responseFields(
                        subsectionWithPath("_links").type(JsonFieldType.OBJECT).description("Links"),
                        fieldWithPath("uuid").type(JsonFieldType.STRING).description("Unique account id"),
                        fieldWithPath("type").type(JsonFieldType.STRING).description("Type of the account, one of: " +
                                Stream.of(AccountTypeEnum.values()).map(Enum::name).collect(Collectors.joining(", "))),
                        fieldWithPath("currency").type(JsonFieldType.STRING).description("Account currency, one of: + " +
                                Stream.of(CurrencyEnum.values()).map(Enum::name).collect(Collectors.joining(", "))),
                        fieldWithPath("balance").type(JsonFieldType.NUMBER).description("Current account balance")
                )));
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

        result = this.mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createAccountJson("RUR", "0", "DEBIT")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.currency").value("RUR"))
                .andExpect(jsonPath("$.balance").value("0"))
                .andExpect(jsonPath("$.type").value("DEBIT"))
                .andReturn();

        String uuidAccount2 = (new JSONObject(result.getResponse().getContentAsString())).getString("uuid");

        this.mockMvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createCardsJson("false", "3", "DEBIT", uuidAccount2)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.blocked").value("false"))
                .andExpect(jsonPath("$.number").value("3"))
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
