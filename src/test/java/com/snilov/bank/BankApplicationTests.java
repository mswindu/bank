package com.snilov.bank;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
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
		MockHttpServletRequestBuilder builder =
				MockMvcRequestBuilders.post("/accounts")
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(createAccountJson("RUR",
								"0", "DEBIT"));

		this.mockMvc.perform(builder).andExpect(status().isCreated());
	}

	@Test
	public void testCreateCard() throws Exception {
		MockHttpServletRequestBuilder builder =
				MockMvcRequestBuilders.post("/cards")
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(createCardsJson("false",
								"1", "DEBIT"));

		this.mockMvc.perform(builder).andExpect(status().isCreated());
	}

	private static String createAccountJson(String currency, String balance, String type) {
		return "{ \"currency\": \"" + currency + "\", " +
				"\"balance\": \"" + balance + "\", " +
				"\"type\": \"" + type + "\"}";
	}

	private static String createCardsJson(String blocked, String number, String type) {
		return "{ \"blocked\": \"" + blocked + "\", " +
				"\"number\": \"" + number + "\", " +
				"\"type\": \"" + type + "\"}";
	}

}
