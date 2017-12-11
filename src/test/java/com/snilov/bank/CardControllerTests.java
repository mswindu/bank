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

import static com.snilov.bank.utils.Utils.createCardsJson;
import static com.snilov.bank.utils.Utils.createCardsWithIncorrectParametersJson;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.halLinks;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
public class CardControllerTests {

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
    public void testCreateNewCardAndNewAccount() throws Exception {
        //When
        ResultActions resultActions = mockMvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createCardsJson("false", "1", "DEBIT")))
                .andDo(print());

        //Then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.blocked").value("false"))
                .andExpect(jsonPath("$.number").value("1"))
                .andExpect(jsonPath("$.type").value("DEBIT"));

        resultActions.andDo(document("create-new-card-and-new-account",
                links(halLinks(),
                        linkWithRel("self").description("This card"),
                        linkWithRel("blocking").description("Block this card")
                ),
                responseFields(
                        subsectionWithPath("_links").type(JsonFieldType.OBJECT).description("Links"),
                        fieldWithPath("uuid").type(JsonFieldType.STRING).description("UUID card"),
                        fieldWithPath("number").type(JsonFieldType.STRING).description("Card number"),
                        fieldWithPath("type").type(JsonFieldType.STRING).description("Type card, one of: " +
                                Stream.of(TypeCardEnum.values()).map(Enum::name).collect(Collectors.joining(", "))),
                        fieldWithPath("blocked").type(JsonFieldType.BOOLEAN).description("Is blocked card")
                )));
    }

    @Test
    public void testCreateNewCardWithExistingAccount() throws Exception {
        //Given
        Account account = given.account().type(AccountTypeEnum.DEBIT).currency(CurrencyEnum.RUR).balance(0).save();

        //When
        ResultActions resultActions = mockMvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createCardsJson("false", "1", "DEBIT", account.getUuid())))
                .andDo(print());

        //Then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.blocked").value("false"))
                .andExpect(jsonPath("$.number").value("1"))
                .andExpect(jsonPath("$.type").value("DEBIT"));

        resultActions.andDo(document("create-new-card-and-exists-account",
                links(halLinks(),
                        linkWithRel("self").description("This card"),
                        linkWithRel("blocking").description("Block this card")
                ),
                responseFields(
                        subsectionWithPath("_links").type(JsonFieldType.OBJECT).description("Links"),
                        fieldWithPath("uuid").type(JsonFieldType.STRING).description("UUID card"),
                        fieldWithPath("number").type(JsonFieldType.STRING).description("Card number"),
                        fieldWithPath("type").type(JsonFieldType.STRING).description("Type card, one of: " +
                                Stream.of(TypeCardEnum.values()).map(Enum::name).collect(Collectors.joining(", "))),
                        fieldWithPath("blocked").type(JsonFieldType.BOOLEAN).description("Is blocked card")
                )));
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
        //When
        ResultActions resultActions = mockMvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createCardsWithIncorrectParametersJson("false", "1", "DEBIT")))
                .andDo(print());

        //Then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.message").value("Invalid parameters specified."))
                .andExpect(jsonPath("$.errors.*", hasSize(3)))
                .andExpect(jsonPath("$.errors.number").value("Number card cannot be empty"))
                .andExpect(jsonPath("$.errors.blocked").value("Blocked status cannot be empty"))
                .andExpect(jsonPath("$.errors.type").value("Type card cannot be empty"));

        resultActions.andDo(document("create-card-with-incorrect-parameters",
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("Error massage"),
                        subsectionWithPath("errors").type(JsonFieldType.OBJECT).description("Array of validation errors")
                )));
    }

    @Test
    public void testSetBlockedStateCard() throws Exception {
        //Given
        Account account = given.account().type(AccountTypeEnum.DEBIT).currency(CurrencyEnum.RUR).balance(0).save();
        Card card = given.card().number("1").type(TypeCardEnum.DEBIT).blocked(false).account(account).save();

        //When
        ResultActions resultActions = mockMvc.perform(put("/cards/" + card.getUuid() + "/blocking"))
                .andDo(print());

        //Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(card.getUuid()))
                .andExpect(jsonPath("$.blocked").value("true"))
                .andExpect(jsonPath("$.number").value("1"))
                .andExpect(jsonPath("$.type").value("DEBIT"));

        resultActions.andDo(document("blocking-card",
                links(halLinks(),
                        linkWithRel("self").description("This card"),
                        linkWithRel("unblocking").description("Unblock this card")
                ),
                responseFields(
                        subsectionWithPath("_links").type(JsonFieldType.OBJECT).description("Links"),
                        fieldWithPath("uuid").type(JsonFieldType.STRING).description("UUID card"),
                        fieldWithPath("number").type(JsonFieldType.STRING).description("Card number"),
                        fieldWithPath("type").type(JsonFieldType.STRING).description("Type card, one of: " +
                                Stream.of(TypeCardEnum.values()).map(Enum::name).collect(Collectors.joining(", "))),
                        fieldWithPath("blocked").type(JsonFieldType.BOOLEAN).description("Is blocked card")
                )));
    }

    @Test
    public void testSetUnblockedStateCard() throws Exception {
        //Given
        Account account = given.account().type(AccountTypeEnum.DEBIT).currency(CurrencyEnum.RUR).balance(0).save();
        Card card = given.card().number("1").type(TypeCardEnum.DEBIT).blocked(true).account(account).save();

        //When
        ResultActions resultActions = mockMvc.perform(put("/cards/" + card.getUuid() + "/unblocking"))
                .andDo(print());

        //Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(card.getUuid()))
                .andExpect(jsonPath("$.blocked").value("false"))
                .andExpect(jsonPath("$.number").value("1"))
                .andExpect(jsonPath("$.type").value("DEBIT"));

        resultActions.andDo(document("unblocking-card",
                links(halLinks(),
                        linkWithRel("self").description("This card"),
                        linkWithRel("blocking").description("Block this card")
                ),
                responseFields(
                        subsectionWithPath("_links").type(JsonFieldType.OBJECT).description("Links"),
                        fieldWithPath("uuid").type(JsonFieldType.STRING).description("UUID card"),
                        fieldWithPath("number").type(JsonFieldType.STRING).description("Card number"),
                        fieldWithPath("type").type(JsonFieldType.STRING).description("Type card, one of: " +
                                Stream.of(TypeCardEnum.values()).map(Enum::name).collect(Collectors.joining(", "))),
                        fieldWithPath("blocked").type(JsonFieldType.BOOLEAN).description("Is blocked card")
                )));
    }
}
