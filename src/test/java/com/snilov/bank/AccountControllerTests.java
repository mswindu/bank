package com.snilov.bank;

import com.snilov.bank.model.Account;
import com.snilov.bank.model.Card;
import com.snilov.bank.model.enums.AccountTypeEnum;
import com.snilov.bank.model.enums.CurrencyEnum;
import com.snilov.bank.model.enums.TypeCardEnum;
import com.snilov.bank.repository.AccountRepository;
import com.snilov.bank.repository.CardRepository;
import com.snilov.bank.utils.TestDataGenerator;
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
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.snilov.bank.utils.Utils.createAccountJson;
import static com.snilov.bank.utils.Utils.createAccountWithIncorrectParametersJson;
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
    private TestDataGenerator given;

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
        //When
        ResultActions resultActions = mockMvc.perform(
                post("/accounts")
                .content(createAccountJson("RUR", "0", "DEBIT"))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print());

        //Then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.currency").value("RUR"))
                .andExpect(jsonPath("$.balance").value("0"))
                .andExpect(jsonPath("$.type").value("DEBIT"));

        resultActions.andDo(document("create-account",
                links(halLinks(),
                        linkWithRel("self").description("This account"),
                        linkWithRel("find_cards").description("Finding cards belonging to the account")
                ),
                responseFields(
                        subsectionWithPath("_links").type(JsonFieldType.OBJECT).description("Links"),
                        fieldWithPath("uuid").type(JsonFieldType.STRING).description("UUID account"),
                        fieldWithPath("type").type(JsonFieldType.STRING).description("Account type, one of: " +
                                Stream.of(AccountTypeEnum.values()).map(Enum::name).collect(Collectors.joining(", "))),
                        fieldWithPath("currency").type(JsonFieldType.STRING).description("Currency, one of: " +
                                Stream.of(CurrencyEnum.values()).map(Enum::name).collect(Collectors.joining(", "))),
                        fieldWithPath("balance").type(JsonFieldType.NUMBER).description("Account balance")
                )));
    }

    @Test
    public void testCreateNewAccountWithIncorrectParameters() throws Exception {
        //When
        ResultActions resultActions = mockMvc.perform(
                post("/accounts")
                .content(createAccountWithIncorrectParametersJson("RUR", "0", "DEBIT"))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print());

        //Then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.message").value("Invalid parameters specified."))
                .andExpect(jsonPath("$.errors.*", hasSize(3)))
                .andExpect(jsonPath("$.errors.balance").value("Balance cannot be empty"))
                .andExpect(jsonPath("$.errors.currency").value("Currency cannot be empty"))
                .andExpect(jsonPath("$.errors.type").value("Account type cannot be empty"));

        resultActions.andDo(document("create-account-with-incorrect-parameters",
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("Error massage"),
                        subsectionWithPath("errors").type(JsonFieldType.OBJECT).description("Array of validation errors")
                )));
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
        //Given
        Account account1 = given.account().type(AccountTypeEnum.DEBIT).currency(CurrencyEnum.RUR).balance(0).save();
        Account account2 = given.account().type(AccountTypeEnum.DEBIT).currency(CurrencyEnum.RUR).balance(0).save();
        Card card1 = given.card().number("1").type(TypeCardEnum.DEBIT).blocked(false).account(account1).save();
        Card card2 = given.card().number("2").type(TypeCardEnum.DEBIT).blocked(false).account(account1).save();
        Card card3 = given.card().number("3").type(TypeCardEnum.DEBIT).blocked(false).account(account2).save();

        //When
        ResultActions resultActions = mockMvc.perform(get("/accounts/" + account1.getUuid() + "/findCard"))
                .andDo(print());

        //Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.cards", hasSize(2)))
                .andExpect(jsonPath("$._embedded.cards[0].uuid").value(card1.getUuid()))
                .andExpect(jsonPath("$._embedded.cards[0].number").value("1"))
                .andExpect(jsonPath("$._embedded.cards[0].type").value("DEBIT"))
                .andExpect(jsonPath("$._embedded.cards[0].blocked").value("false"))
                .andExpect(jsonPath("$._embedded.cards[1].uuid").value(card2.getUuid()))
                .andExpect(jsonPath("$._embedded.cards[1].number").value("2"))
                .andExpect(jsonPath("$._embedded.cards[1].type").value("DEBIT"))
                .andExpect(jsonPath("$._embedded.cards[1].blocked").value("false"));

        resultActions.andDo(document("get-card-for-account",
                responseFields(
                        fieldWithPath("_embedded").description("'Card' array with Account resources."),
                        fieldWithPath("_embedded.cards").description("Array with returned Card resources."),
                        fieldWithPath("_embedded.cards[].uuid").description("Card uuid"),
                        fieldWithPath("_embedded.cards[].number").description("Card number."),
                        fieldWithPath("_embedded.cards[].type").description("Card type."),
                        fieldWithPath("_embedded.cards[].blocked").description("Is blocked card"),
                        subsectionWithPath("_embedded.cards[]._links").description("Links")
                )));
    }
}
