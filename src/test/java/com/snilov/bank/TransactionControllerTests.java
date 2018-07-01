package com.snilov.bank;

import com.snilov.bank.model.Account;
import com.snilov.bank.model.Card;
import com.snilov.bank.model.Transaction;
import com.snilov.bank.model.enums.*;
import com.snilov.bank.repository.AccountRepository;
import com.snilov.bank.repository.CardRepository;
import com.snilov.bank.repository.TransactionRepository;
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

import java.util.List;

import static com.snilov.bank.utils.Utils.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.halLinks;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
public class TransactionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataGenerator given;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @After
    public void tearDown() {
        transactionRepository.deleteAll();
        cardRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    public void testCreateNewDepositTransaction() throws Exception {
        //Given
        Account account = given.account().type(AccountTypeEnum.DEBIT).currency(CurrencyEnum.RUR).balance(0).save();
        Card card = given.card().number("1").type(TypeCardEnum.DEBIT).blocked(false).account(account).save();

        //When
        ResultActions resultActions = mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(card.getUuid(), "DEPOSIT", "10")))
                .andDo(print());

        //Then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.typeTransaction").value("DEPOSIT"))
                .andExpect(jsonPath("$.transactionAmount").value("10"))
                .andExpect(jsonPath("$.amountBefore").value("0"))
                .andExpect(jsonPath("$.amountAfter").value("10"))
                .andExpect(jsonPath("$.isCanceled").value("false"));

        resultActions.andDo(document("create-transaction-deposit",
                links(halLinks(),
                        linkWithRel("self").description("This transaction"),
                        linkWithRel("rollback").description("Rollback this transaction")
                ),
                responseFields(
                        subsectionWithPath("_links").type(JsonFieldType.OBJECT).description("Links"),
                        fieldWithPath("uuid").type(JsonFieldType.STRING).description("UUID transaction"),
                        fieldWithPath("typeTransaction").type(JsonFieldType.STRING).description("Type transaction"),
                        fieldWithPath("transactionDate").type(JsonFieldType.STRING).description("Date transaction"),
                        fieldWithPath("transactionAmount").type(JsonFieldType.NUMBER).description("Amount transaction"),
                        fieldWithPath("amountBefore").type(JsonFieldType.NUMBER).description("Amount before transaction"),
                        fieldWithPath("amountAfter").type(JsonFieldType.NUMBER).description("Amount after transaction"),
                        fieldWithPath("isCanceled").type(JsonFieldType.BOOLEAN).description("Whether the transaction was canceled")
                )));
    }

    @Test
    public void testCreateNewWithdrawTransaction() throws Exception {
        //Given
        Account account = given.account().type(AccountTypeEnum.DEBIT).currency(CurrencyEnum.RUR).balance(0).save();
        Card card = given.card().number("1").type(TypeCardEnum.DEBIT).blocked(false).account(account).save();

        //When
        ResultActions resultActions = mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(card.getUuid(), "WITHDRAW", "-100")))
                .andDo(print());

        //Then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.typeTransaction").value("WITHDRAW"))
                .andExpect(jsonPath("$.transactionAmount").value("-100"))
                .andExpect(jsonPath("$.amountBefore").value("0"))
                .andExpect(jsonPath("$.amountAfter").value("-100"))
                .andExpect(jsonPath("$.isCanceled").value("false"));

        resultActions.andDo(document("create-transaction-withdraw",
                links(halLinks(),
                        linkWithRel("self").description("This transaction"),
                        linkWithRel("rollback").description("Rollback this transaction")
                ),
                responseFields(
                        subsectionWithPath("_links").type(JsonFieldType.OBJECT).description("Links"),
                        fieldWithPath("uuid").type(JsonFieldType.STRING).description("UUID transaction"),
                        fieldWithPath("typeTransaction").type(JsonFieldType.STRING).description("Type transaction"),
                        fieldWithPath("transactionDate").type(JsonFieldType.STRING).description("Date transaction"),
                        fieldWithPath("transactionAmount").type(JsonFieldType.NUMBER).description("Amount transaction"),
                        fieldWithPath("amountBefore").type(JsonFieldType.NUMBER).description("Amount before transaction"),
                        fieldWithPath("amountAfter").type(JsonFieldType.NUMBER).description("Amount after transaction"),
                        fieldWithPath("isCanceled").type(JsonFieldType.BOOLEAN).description("Whether the transaction was canceled")
                )));
    }

    @Test
    public void testCreateNewTransactionWithIncorrectParameters() throws Exception {
        //Given
        Account account = given.account().type(AccountTypeEnum.DEBIT).currency(CurrencyEnum.RUR).balance(0).save();
        Card card = given.card().number("1").type(TypeCardEnum.DEBIT).blocked(false).account(account).save();

        //When
        ResultActions resultActions = mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsWithIncorrectParametersJson(card.getUuid(), "DEPOSIT", "10")))
                .andDo(print());

        //Then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.message").value("Invalid parameters specified."))
                .andExpect(jsonPath("$.errors.*", hasSize(3)))
                .andExpect(jsonPath("$.errors.uuidCard").value("UUID card cannot be empty"))
                .andExpect(jsonPath("$.errors.typeTransaction").value("Transaction type cannot by empty"))
                .andExpect(jsonPath("$.errors.transactionAmount").value("Transaction amount cannot be empty"));

        resultActions.andDo(document("create-transaction-with-incorrect-parameters",
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("Error massage"),
                        subsectionWithPath("errors").type(JsonFieldType.OBJECT).description("Array of validation errors")
                )));
    }

    @Test
    public void testCreateNewTransactionsWithOneAccountOneCard() throws Exception {
        //Given
        Account account = given.account().type(AccountTypeEnum.DEBIT).currency(CurrencyEnum.RUR).balance(0).save();
        Card card = given.card().number("1").type(TypeCardEnum.DEBIT).blocked(false).account(account).save();

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(card.getUuid(), "DEPOSIT", "10")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.typeTransaction").value("DEPOSIT"))
                .andExpect(jsonPath("$.transactionAmount").value("10"))
                .andExpect(jsonPath("$.amountBefore").value("0"))
                .andExpect(jsonPath("$.amountAfter").value("10"))
                .andExpect(jsonPath("$.isCanceled").value("false"));

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(card.getUuid(), "DEPOSIT", "100")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.typeTransaction").value("DEPOSIT"))
                .andExpect(jsonPath("$.transactionAmount").value("100"))
                .andExpect(jsonPath("$.amountBefore").value("10"))
                .andExpect(jsonPath("$.amountAfter").value("110"))
                .andExpect(jsonPath("$.isCanceled").value("false"));

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(card.getUuid(), "WITHDRAW", "-5")))
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
        //Given
        Account account = given.account().type(AccountTypeEnum.DEBIT).currency(CurrencyEnum.RUR).balance(0).save();
        Card card1 = given.card().number("1").type(TypeCardEnum.DEBIT).blocked(false).account(account).save();
        Card card2 = given.card().number("2").type(TypeCardEnum.DEBIT).blocked(false).account(account).save();

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(card1.getUuid(), "DEPOSIT", "10")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.typeTransaction").value("DEPOSIT"))
                .andExpect(jsonPath("$.transactionAmount").value("10"))
                .andExpect(jsonPath("$.amountBefore").value("0"))
                .andExpect(jsonPath("$.amountAfter").value("10"))
                .andExpect(jsonPath("$.isCanceled").value("false"));

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(card2.getUuid(), "DEPOSIT", "100")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.typeTransaction").value("DEPOSIT"))
                .andExpect(jsonPath("$.transactionAmount").value("100"))
                .andExpect(jsonPath("$.amountBefore").value("10"))
                .andExpect(jsonPath("$.amountAfter").value("110"))
                .andExpect(jsonPath("$.isCanceled").value("false"));

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(card1.getUuid(), "WITHDRAW", "-5")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.typeTransaction").value("WITHDRAW"))
                .andExpect(jsonPath("$.transactionAmount").value("-5"))
                .andExpect(jsonPath("$.amountBefore").value("110"))
                .andExpect(jsonPath("$.amountAfter").value("105"))
                .andExpect(jsonPath("$.isCanceled").value("false"));

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(card2.getUuid(), "WITHDRAW", "-50")))
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
        //Given
        Account account1 = given.account().type(AccountTypeEnum.DEBIT).currency(CurrencyEnum.RUR).balance(0).save();
        Account account2 = given.account().type(AccountTypeEnum.DEBIT).currency(CurrencyEnum.RUR).balance(0).save();
        Card card1 = given.card().number("1").type(TypeCardEnum.DEBIT).blocked(false).account(account1).save();
        Card card2 = given.card().number("2").type(TypeCardEnum.DEBIT).blocked(false).account(account2).save();

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(card1.getUuid(), "DEPOSIT", "10")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.typeTransaction").value("DEPOSIT"))
                .andExpect(jsonPath("$.transactionAmount").value("10"))
                .andExpect(jsonPath("$.amountBefore").value("0"))
                .andExpect(jsonPath("$.amountAfter").value("10"))
                .andExpect(jsonPath("$.isCanceled").value("false"));

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(card2.getUuid(), "DEPOSIT", "100")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.typeTransaction").value("DEPOSIT"))
                .andExpect(jsonPath("$.transactionAmount").value("100"))
                .andExpect(jsonPath("$.amountBefore").value("0"))
                .andExpect(jsonPath("$.amountAfter").value("100"))
                .andExpect(jsonPath("$.isCanceled").value("false"));

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(card1.getUuid(), "WITHDRAW", "-5")))
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
        //When
        ResultActions resultActions = mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson("1234567", "DEPOSIT", "10")));

        //Then
        resultActions.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("There is no such card"));
    }

    @Test
    public void testCreateNewTransactionWithBlockedCard() throws Exception {
        //Given
        Account account = given.account().type(AccountTypeEnum.DEBIT).currency(CurrencyEnum.RUR).balance(0).save();
        Card card = given.card().number("1").type(TypeCardEnum.DEBIT).blocked(true).account(account).save();

        //When
        ResultActions resultActions = mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransactionsJson(card.getUuid(), "DEPOSIT", "10")))
                .andDo(print());

        resultActions.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Card is blocked: " + card.getUuid()));
    }

    @Test
    public void testRollbackTransaction() throws Exception {
        //Given
        Account account = given.account().type(AccountTypeEnum.DEBIT).currency(CurrencyEnum.RUR).balance(0).save();
        Card card = given.card().number("1").type(TypeCardEnum.DEBIT).blocked(false).account(account).save();
        Transaction transaction1 = given.transactionDeposit().uuidCard(card.getUuid()).amount(10).save();
        Transaction transaction2 = given.transactionDeposit().uuidCard(card.getUuid()).amount(100).save();
        Transaction transaction3 = given.transactionDeposit().uuidCard(card.getUuid()).amount(1000).save();

        //When
        ResultActions resultActions = mockMvc.perform(post("/transactions/" + transaction2.getUuid() + "/rollback"))
                .andDo(print());

        //Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.transactions", hasSize(1)))
                .andExpect(jsonPath("$._embedded.transactions[0].uuid").value(transaction2.getUuid()))
                .andExpect(jsonPath("$._embedded.transactions[0].typeTransaction").value("DEPOSIT"))
                .andExpect(jsonPath("$._embedded.transactions[0].transactionAmount").value("100"))
                .andExpect(jsonPath("$._embedded.transactions[0].amountBefore").value("10"))
                .andExpect(jsonPath("$._embedded.transactions[0].amountAfter").value("110"))
                .andExpect(jsonPath("$._embedded.transactions[0].isCanceled").value("true"));

        /*resultActions.andDo(document("create-transaction-rollback",
                links(halLinks(),
                        linkWithRel("self").description("This transaction"),
                        linkWithRel("rollback").description("Rollback this transaction")
                ),
                responseFields(
                        subsectionWithPath("_links").type(JsonFieldType.OBJECT).description("Links"),
                        fieldWithPath("uuid").type(JsonFieldType.STRING).description("UUID transaction"),
                        fieldWithPath("typeTransaction").type(JsonFieldType.STRING).description("Type transaction"),
                        fieldWithPath("transactionDate").type(JsonFieldType.STRING).description("Date transaction"),
                        fieldWithPath("transactionAmount").type(JsonFieldType.NUMBER).description("Amount transaction"),
                        fieldWithPath("amountBefore").type(JsonFieldType.NUMBER).description("Amount before transaction"),
                        fieldWithPath("amountAfter").type(JsonFieldType.NUMBER).description("Amount after transaction"),
                        fieldWithPath("isCanceled").type(JsonFieldType.BOOLEAN).description("Whether the transaction was canceled")
                )));*/
    }

    @Test
    public void testRollbackAgainTransaction() throws Exception {
        //Given
        Account account = given.account().type(AccountTypeEnum.DEBIT).currency(CurrencyEnum.RUR).balance(0).save();
        Card card = given.card().number("1").type(TypeCardEnum.DEBIT).blocked(false).account(account).save();
        Transaction transaction1 = given.transactionDeposit().uuidCard(card.getUuid()).amount(100).save();
        List<Transaction> rollbackTransactions1 = given.transactionRollback().uuidTransaction(transaction1.getUuid()).save();
        Transaction transaction3 = given.transactionDeposit().uuidCard(card.getUuid()).amount(100).save();

        //When
        ResultActions resultActions = mockMvc.perform(post("/transactions/" + transaction1.getUuid() + "/rollback"))
                .andDo(print());

        //Then
        resultActions.andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("You can not cancel the transaction again"));
    }

    @Test
    public void testTransferC2C() throws Exception {
        //Given
        Account account1 = given.account().type(AccountTypeEnum.DEBIT).currency(CurrencyEnum.RUR).balance(10).save();
        Account account2 = given.account().type(AccountTypeEnum.DEBIT).currency(CurrencyEnum.RUR).balance(10).save();
        Card card1 = given.card().number("1").type(TypeCardEnum.DEBIT).blocked(false).account(account1).save();
        Card card2 = given.card().number("2").type(TypeCardEnum.DEBIT).blocked(false).account(account2).save();

        //When
        ResultActions resultActions = this.mockMvc.perform(post("/transactions/transfer")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransferJson("C2C", card1.getUuid(), card2.getUuid(), "10")))
                .andDo(print());

        //Then
        resultActions.andExpect(status().isOk())
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

        resultActions.andDo(document("create-transaction-transfer-C2C",
                responseFields(
                        fieldWithPath("_embedded").description("New transactions"),
                        fieldWithPath("_embedded.transactions").description("Array with returned Transaction resources."),
                        fieldWithPath("_embedded.transactions[].uuid").description("UUID transaction"),
                        fieldWithPath("_embedded.transactions[].typeTransaction").description("Type transaction"),
                        fieldWithPath("_embedded.transactions[].transactionDate").description("Date transaction"),
                        fieldWithPath("_embedded.transactions[].transactionAmount").description("Amount transaction"),
                        fieldWithPath("_embedded.transactions[].amountBefore").description("Amount before transaction"),
                        fieldWithPath("_embedded.transactions[].amountAfter").description("Amount after transaction"),
                        fieldWithPath("_embedded.transactions[].isCanceled").description("Whether the transaction was canceled"),
                        subsectionWithPath("_embedded.transactions[]._links").description("Links")
                )));
    }

    @Test
    public void testTransferC2CRollback() throws Exception {
        //Given
        Account account1 = given.account().type(AccountTypeEnum.DEBIT).currency(CurrencyEnum.RUR).balance(10).save();
        Account account2 = given.account().type(AccountTypeEnum.DEBIT).currency(CurrencyEnum.RUR).balance(10).save();
        Card card1 = given.card().number("1").type(TypeCardEnum.DEBIT).blocked(false).account(account1).save();
        Card card2 = given.card().number("2").type(TypeCardEnum.DEBIT).blocked(false).account(account2).save();
        List<Transaction> transactions = given.transactionTransfer()
                .type(TypeTransferEnum.C2C)
                .uuidPayer(card1.getUuid())
                .uuidPayee(card2.getUuid())
                .amount(10)
                .save();

        //When
        ResultActions resultActions = mockMvc.perform(post("/transactions/" + transactions.get(0).getUuid() + "/rollback"))
                .andDo(print());

        //Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.transactions", hasSize(2)))
                .andExpect(jsonPath("$._embedded.transactions[0].uuid").value(transactions.get(0).getUuid()))
                .andExpect(jsonPath("$._embedded.transactions[0].typeTransaction").value("TRANSFER"))
                .andExpect(jsonPath("$._embedded.transactions[0].transactionAmount").value("-10"))
                .andExpect(jsonPath("$._embedded.transactions[0].amountBefore").value("10"))
                .andExpect(jsonPath("$._embedded.transactions[0].amountAfter").value("0"))
                .andExpect(jsonPath("$._embedded.transactions[0].isCanceled").value("true"))
                .andExpect(jsonPath("$._embedded.transactions[1].uuid").value(transactions.get(1).getUuid()))
                .andExpect(jsonPath("$._embedded.transactions[1].typeTransaction").value("TRANSFER"))
                .andExpect(jsonPath("$._embedded.transactions[1].transactionAmount").value("10"))
                .andExpect(jsonPath("$._embedded.transactions[1].amountBefore").value("10"))
                .andExpect(jsonPath("$._embedded.transactions[1].amountAfter").value("20"))
                .andExpect(jsonPath("$._embedded.transactions[1].isCanceled").value("true"));
    }

    @Test
    public void testTransferA2A() throws Exception {
        //Given
        Account account1 = given.account().type(AccountTypeEnum.DEBIT).currency(CurrencyEnum.RUR).balance(10).save();
        Account account2 = given.account().type(AccountTypeEnum.DEBIT).currency(CurrencyEnum.RUR).balance(10).save();

        //When
        ResultActions resultActions = this.mockMvc.perform(post("/transactions/transfer")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTransferJson("A2A", account1.getUuid(), account2.getUuid(), "10")))
                .andDo(print());

        //Then
        resultActions.andExpect(status().isOk())
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

        resultActions.andDo(document("create-transaction-transfer-A2A",
                responseFields(
                        fieldWithPath("_embedded").description("New transactions"),
                        fieldWithPath("_embedded.transactions").description("Array with returned Transaction resources."),
                        fieldWithPath("_embedded.transactions[].uuid").description("UUID transaction"),
                        fieldWithPath("_embedded.transactions[].typeTransaction").description("Type transaction"),
                        fieldWithPath("_embedded.transactions[].transactionDate").description("Date transaction"),
                        fieldWithPath("_embedded.transactions[].transactionAmount").description("Amount transaction"),
                        fieldWithPath("_embedded.transactions[].amountBefore").description("Amount before transaction"),
                        fieldWithPath("_embedded.transactions[].amountAfter").description("Amount after transaction"),
                        fieldWithPath("_embedded.transactions[].isCanceled").description("Whether the transaction was canceled"),
                        subsectionWithPath("_embedded.transactions[]._links").description("Links")
                )));
    }

    @Test
    public void testTransferA2ARollback() throws Exception {
        //Given
        Account account1 = given.account().type(AccountTypeEnum.DEBIT).currency(CurrencyEnum.RUR).balance(10).save();
        Account account2 = given.account().type(AccountTypeEnum.DEBIT).currency(CurrencyEnum.RUR).balance(10).save();
        List<Transaction> transactions = given.transactionTransfer()
                .type(TypeTransferEnum.A2A)
                .uuidPayer(account1.getUuid())
                .uuidPayee(account2.getUuid())
                .amount(10)
                .save();

        //When
        ResultActions resultActions = mockMvc.perform(post("/transactions/" + transactions.get(0).getUuid() + "/rollback"))
                .andDo(print());

        //Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.transactions", hasSize(2)))
                .andExpect(jsonPath("$._embedded.transactions[0].uuid").value(transactions.get(0).getUuid()))
                .andExpect(jsonPath("$._embedded.transactions[0].typeTransaction").value("TRANSFER"))
                .andExpect(jsonPath("$._embedded.transactions[0].transactionAmount").value("-10"))
                .andExpect(jsonPath("$._embedded.transactions[0].amountBefore").value("10"))
                .andExpect(jsonPath("$._embedded.transactions[0].amountAfter").value("0"))
                .andExpect(jsonPath("$._embedded.transactions[0].isCanceled").value("true"))
                .andExpect(jsonPath("$._embedded.transactions[1].uuid").value(transactions.get(1).getUuid()))
                .andExpect(jsonPath("$._embedded.transactions[1].typeTransaction").value("TRANSFER"))
                .andExpect(jsonPath("$._embedded.transactions[1].transactionAmount").value("10"))
                .andExpect(jsonPath("$._embedded.transactions[1].amountBefore").value("10"))
                .andExpect(jsonPath("$._embedded.transactions[1].amountAfter").value("20"))
                .andExpect(jsonPath("$._embedded.transactions[1].isCanceled").value("true"));
    }
}
