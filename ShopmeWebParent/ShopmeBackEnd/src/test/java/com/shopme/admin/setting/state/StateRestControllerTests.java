package com.shopme.admin.setting.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;

@SpringBootTest
@AutoConfigureMockMvc
public class StateRestControllerTests {
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	CountryRepository countryRepo;

	@Autowired
	StateRepository stateRepo;

	@Test
	@WithMockUser(username = "mta", password = "something", roles = "admin")
	public void testListByCountries() throws Exception {

		Integer countryId = 2;
		String url = "/states/list_by_country/" + countryId;

		MvcResult result = mockMvc.perform(get(url)).andExpect(status().isOk()).andDo(print()).andReturn();
		String jsonResponse = result.getResponse().getContentAsString();

		State[] states = objectMapper.readValue(jsonResponse, State[].class);

		assertThat(states).hasSizeGreaterThan(0);
	}

	@Test
	@WithMockUser(username = "tofayel", password = "something", roles = "admin")
	public void testCreate() throws JsonProcessingException, Exception {
		String url = "/states/save";

		Integer countryId = 2;

		Country country = countryRepo.findById(countryId).get();
		State state = new State("Arizona", country);

		MvcResult result = mockMvc.perform(
				post(url).contentType("application/json").content(objectMapper.writeValueAsString(state)).with(csrf()))
				.andDo(print()).andExpect(status().isOk()).andReturn();
	
		String response = result.getResponse().getContentAsString();
		Integer stateId = Integer.parseInt(response);
		
		Optional<State> findById = stateRepo.findById(stateId);
	
		assertThat(findById.isPresent());
	}
	@Test
	@WithMockUser(username = "tofayel", password = "something", roles = "admin")
	public void testUpdate() throws JsonProcessingException, Exception {
		String url = "/states/save";

		Integer stateId = 2;

		String stateName = "Alaska";
		
		State state = stateRepo.findById(stateId).get();
		state.setName(stateName);
		
		mockMvc.perform(
				post(url).contentType("application/json").content(objectMapper.writeValueAsString(state)).with(csrf()))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(String.valueOf(stateId)));
	
		
		Optional<State> findById = stateRepo.findById(stateId);
		
		assertThat(findById.isPresent());
		
		State  updateState = findById.get();
		assertThat(updateState.getName()).isEqualTo(stateName);
	}
	
	@Test
	@WithMockUser(username ="tofayel", password = "something", roles = "Admin")
	public void testDeleteState() throws Exception{
		Integer stateId = 6;
		String url = "/states/delete/"+stateId;
		
		mockMvc.perform(get(url)).andExpect(status().isOk());
	
		Optional<State> findById = stateRepo.findById(stateId);
		assertThat(findById).isNotPresent();
	}

}
