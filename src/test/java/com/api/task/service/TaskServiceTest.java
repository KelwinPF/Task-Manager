package com.api.task.service;

import com.api.task.entity.Task;
import com.api.task.entity.User;
import com.api.task.repository.TaskRepository;
import com.api.task.util.PriorityEnum;
import com.api.task.util.TaskEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TaskServiceTest {

    @MockBean
    TaskRepository repo;

    @Autowired
    TaskService service;

    private static final Date DATE = new Date();
    private static final PriorityEnum PRIORITY = PriorityEnum.HI;
    private static final String DESCRIPTION = "teste";
    private static final String TITULO = "titulo teste";

    @Test
    public void testSave() {
        BDDMockito.given(repo.save(Mockito.any(Task.class))).willReturn(getMockTask());

        Task response = service.save(new Task());

        assertNotNull(response);
        assertEquals(response.getDescricao(), DESCRIPTION);
        assertEquals(response.getTitulo(), TITULO);
    }

    private Task getMockTask() {
        User u = new User();
        u.setId(1L);

        Task t = new Task();
        t.setId(1L);
        t.setDeadline(DATE);
        t.setPriority(PRIORITY);
        t.setDescricao(DESCRIPTION);
        t.setTitulo(TITULO);
        t.setUsers(u);
        return t;
    }

    @Test
    public void testgetTasks() {
        List<Task> list = new ArrayList<>();
        list.add(getMockTask());
        Page<Task> page = new PageImpl(list);

        BDDMockito.given(repo.getTasks(Optional.of(TITULO), Optional.of(1L),Optional.of(1L), Optional.of(TaskEnum.PG), PageRequest.of(0,10))).willReturn(page);

        Page<Task> response = service.getTasks(Optional.of(TITULO),Optional.of(1L),Optional.of(1L),Optional.of(TaskEnum.PG),0);

        assertEquals(response.getSize(), 1);
        assertEquals(response.getContent().get(0).getDescricao(), DESCRIPTION);

    }
}
