package com.snilov.bank;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BankApplicationTests {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup () {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		this.mockMvc = builder.build();
	}

	@Test
	public void testCreateAccount() throws Exception {
		MvcResult result = this.mockMvc.perform(post("/accounts")
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(createAccountJson("RUR","0", "DEBIT")))
				.andDo(print())
				.andExpect(status().isCreated())
				.andReturn();

		String location = result.getResponse().getHeader("Location");

		this.mockMvc.perform(get(location))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.currency").value("RUR"))
				.andExpect(jsonPath("$.balance").value("0"))
				.andExpect(jsonPath("$.type").value("DEBIT"));
	}

	@Test
	public void testCreateNewCardAndNewAccount() throws Exception {
		this.mockMvc.perform(post("/cards")
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(createCardsJson("false","1", "DEBIT")))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.blocked").value("false"))
				.andExpect(jsonPath("$.number").value("1"))
				.andExpect(jsonPath("$.type").value("DEBIT"))
				.andReturn();
	}

	@Test
	public void testCreateNewCardWithExistingAccount() throws Exception {
		MvcResult result = this.mockMvc.perform(post("/accounts")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(createAccountJson("RUR","0", "DEBIT")))
				.andDo(print())
				.andExpect(status().isCreated())
				.andReturn();

		String location = result.getResponse().getHeader("Location");

		result = this.mockMvc.perform(get(location))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.currency").value("RUR"))
				.andExpect(jsonPath("$.balance").value("0"))
				.andExpect(jsonPath("$.type").value("DEBIT"))
				.andReturn();

		String uuid = (new JSONObject(result.getResponse().getContentAsString())).getString("uuid");

		this.mockMvc.perform(post("/cards")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(createCardsJson("false","1", "DEBIT", uuid)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.blocked").value("false"))
				.andExpect(jsonPath("$.number").value("1"))
				.andExpect(jsonPath("$.type").value("DEBIT"));
	}

	private static String createAccountJson(String currency, String balance, String type) {
		return "{ \"currency\": \"" + currency + "\", " +
				"\"balance\": \"" + balance + "\", " +
				"\"type\": \"" + type + "\"}";
	}

	private static String createCardsJson(String blocked, String number, String type, String accountUuid) {
		return "{ \"blocked\": \"" + blocked + "\", " +
				"\"number\": \"" + number + "\", " +
				"\"type\": \"" + type + "\", " +
				"\"accountUuid\": \"" + accountUuid + "\"}";
	}

	private static String createCardsJson(String blocked, String number, String type) {
		return "{ \"blocked\": \"" + blocked + "\", " +
				"\"number\": \"" + number + "\", " +
				"\"type\": \"" + type + "\"}";
	}

	private String getResourceIdFromUrl(String locationUrl) {
		String[] parts = locationUrl.split("/");

		return "123";
		//return Long.valueOf(parts[parts.length - 1]);
	}

}
