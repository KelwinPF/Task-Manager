package com.api.task.controller;

import com.api.task.dto.TaskDTO;
import com.api.task.entity.Task;
import com.api.task.entity.User;
import com.api.task.service.TaskService;
import com.api.task.service.UserService;
import com.api.task.util.PriorityEnum;
import com.api.task.util.RoleEnum;
import com.api.task.util.TaskEnum;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TaskControllerTest {

    @MockBean
    TaskService service;

    @MockBean
    UserService userService;

    @Autowired
    MockMvc mvc;

    private static final Long ID = 1L;
    private static final Date DATE = new Date();
    private static final LocalDate TODAY = LocalDate.now();
    private static final PriorityEnum PRIORITY = PriorityEnum.HI;
    private static final String DESCRIPTION = "teste";
    private static final String TITULO = "titulo teste";
    private static final String URL = "/task";

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"ADMIN"})
    public void testSave() throws Exception {
        BDDMockito.given(service.save(Mockito.any(Task.class))).willReturn(getMockTask());

        mvc.perform(MockMvcRequestBuilders.post(URL).content(getJsonPayload())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.deadline").value(TODAY.format(getDateFormater())))
                .andExpect(jsonPath("$.data.descricao").value(DESCRIPTION))
                .andExpect(jsonPath("$.data.titulo").value(TITULO))
                .andExpect(jsonPath("$.data.priority").value(PRIORITY.getValue()));
    }

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"ADMIN"})
    public void testUpdate() throws Exception {

        User w = new User();
        w.setId(ID);

        Task t = new Task();
        t.setId(ID);
        t.setDeadline(DATE);
        t.setPriority(PriorityEnum.LO);
        t.setDescricao("testee");
        t.setTitulo(TITULO);
        t.setUsers(w);

        BDDMockito.given(service.findById(Mockito.anyLong())).willReturn(Optional.of(getMockTask()));
        BDDMockito.given(service.save(Mockito.any(Task.class))).willReturn(t);

        mvc.perform(MockMvcRequestBuilders.put(URL).content(getJsonPayload())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.deadline").value(TODAY.format(getDateFormater())))
                .andExpect(jsonPath("$.data.descricao").value("testee"))
                .andExpect(jsonPath("$.data.titulo").value(TITULO))
                .andExpect(jsonPath("$.data.priority").value(PriorityEnum.LO.getValue()));

    }

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"ADMIN"})
    public void testUpdateInvalidId() throws Exception {

        BDDMockito.given(service.findById(Mockito.anyLong())).willReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.put(URL).content(getJsonPayload())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.errors[0]").value("Tarefa não encontrada"));

    }

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"ADMIN"})
    public void testDelete() throws JsonProcessingException, Exception {

        BDDMockito.given(service.findById(Mockito.anyLong())).willReturn(Optional.of(new Task()));

        mvc.perform(MockMvcRequestBuilders.delete(URL+"/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("Task de id "+ ID +" apagada com sucesso"));
    }

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"ADMIN"})
    public void testDeleteInvalid() throws Exception {

        BDDMockito.given(service.findById(Mockito.anyLong())).willReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.delete(URL+"/99")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.errors[0]").value("Task de id "+ 99 + " não encontrada"));

    }

    private Task getMockTask() {
        User u = new User();
        u.setId(1L);
        u.setRole(RoleEnum.ROLE_ADMIN);

        Task t = new Task();
        t.setId(1L);
        t.setDeadline(DATE);
        t.setPriority(PRIORITY);
        t.setDescricao(DESCRIPTION);
        t.setTitulo(TITULO);
        t.setUsers(u);
        return t;
    }

    public String getJsonPayload() throws JsonProcessingException {
        TaskDTO dto = new TaskDTO();
        dto.setId(ID);
        dto.setDeadline(DATE);
        dto.setDescricao(DESCRIPTION);
        dto.setTitulo(TITULO);
        dto.setPriority(PRIORITY.getValue());
        dto.setUsers(ID);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(dto);
    }

    private DateTimeFormatter getDateFormater() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return formatter;
    }
}
