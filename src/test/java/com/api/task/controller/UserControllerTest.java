package com.api.task.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.api.task.dto.UserDTO;
import com.api.task.entity.User;
import com.api.task.service.UserService;
import com.api.task.util.RoleEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

    private static final Long Id = 1L;
    private static final String EMAIL = "teste@email.com";
    private static final String NAME = "User test";
    private static final String URL = "/user";
    private static final String PASSWORD = "123456";

    @MockBean
    UserService service;

    @Autowired
    MockMvc mvc;

    @Test
    public void testService() throws Exception {

        BDDMockito.given(service.save(Mockito.any(User.class))).willReturn(getMockUser());
        mvc.perform(MockMvcRequestBuilders.post(URL)
                .content(getJsonPayload(Id,EMAIL,NAME,PASSWORD))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(Id))
                .andExpect(jsonPath("$.data.email").value(EMAIL))
                .andExpect(jsonPath("$.data.name").value(NAME))
                .andExpect(jsonPath("$.data.role").value(RoleEnum.ROLE_ADMIN.toString()))
                .andExpect(jsonPath("$.data.password").doesNotExist());

    }

    @Test
    public void testSaveInvalidUser() throws JsonProcessingException,Exception{
        mvc.perform(MockMvcRequestBuilders.post(URL).content(getJsonPayload(Id, "email", NAME, PASSWORD))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Email inválido"));
    }

    public User getMockUser(){
        User u = new User();
        u.setId(Id);
        u.setEmail(EMAIL);
        u.setName(NAME);
        u.setPassword(PASSWORD);
        u.setRole(RoleEnum.ROLE_ADMIN);

        return u;
    }

    public String getJsonPayload(Long id,String Email,String name,String password) throws JsonProcessingException {
        UserDTO dto = new UserDTO();
        dto.setId(id);
        dto.setEmail(Email);
        dto.setName(name);
        dto.setPassword(password);
        dto.setRole(RoleEnum.ROLE_ADMIN.toString());

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(dto);
    }
}

