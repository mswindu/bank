package com.snilov.bank;

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
						.content(createAccountJson("RUR",
								"0", "DEBIT")))
				.andDo(print())
				.andExpect(status().isCreated())
				.andReturn();

		String location = result.getResponse().getHeader("Location");

		mockMvc.perform(get(location))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.currency").value("RUR"))
				.andExpect(jsonPath("$.balance").value("0"))
				.andExpect(jsonPath("$.type").value("DEBIT"));
	}

	@Test
	public void testCreateNewCardAndNewAccount() throws Exception {
		MockHttpServletRequestBuilder builder =
				MockMvcRequestBuilders.post("/cards")
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(createCardsJson("false",
								"1", "DEBIT"));

		this.mockMvc.perform(builder).andDo(print()).andExpect(status().isCreated());
	}
//
//	@Test
//	public void testCreateNewCardWithExistingAccount() throws Exception {
//		MvcResult result = this.mockMvc.perform(post("/accounts")
//						.contentType(MediaType.APPLICATION_JSON_UTF8)
//						.content(createAccountJson("RUR","0", "DEBIT")))
//						.andDo(print())
//				.andExpect(status().isCreated())
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//				.andReturn();
//
//		System.out.println("!!!!!!!!!!!!!!");
//		System.out.println(result.getResponse());
//	}

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
