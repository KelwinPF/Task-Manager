package com.api.task.repository;

import com.api.task.entity.Task;
import com.api.task.entity.User;
import com.api.task.util.PriorityEnum;
import com.api.task.util.RoleEnum;
import com.api.task.util.TaskEnum;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TaskRepositoryTest {

    @Autowired
    TaskRepository repo;

    @Autowired
    UserRepository repo_u;

    @Before
    public void setUp(){
        Task t = new Task();
        t.setTitulo("teste");
        t.setDescricao("desc");
        t.setPriority(PriorityEnum.HI);
        t.setStatus(TaskEnum.PG);
        t.setDeadline(new Date());

        User u = new User();
        u.setName("setup");
        u.setPassword("123456");
        u.setEmail("teste@email.com");
        u.setRole(RoleEnum.ROLE_ADMIN);
        repo_u.save(u);

        t.setUsers(u);

    }

    @After
    public void tearDown() {
        repo.deleteAll();
        repo_u.deleteAll();
    }

    @Test
    public void testSave(){
        User u = new User();
        u.setName("setup");
        u.setPassword("123456");
        u.setEmail("teste2@email.com");
        u.setRole(RoleEnum.ROLE_ADMIN);
        repo_u.save(u);

        Task t = new Task();
        t.setTitulo("teste");
        t.setDescricao("desc");
        t.setPriority(PriorityEnum.HI);
        t.setDeadline(new Date());
        t.setUsers(u);

        Task response = repo.save(t);
        assertNotNull(response);
        assertEquals(response.getTitulo(),"teste");
        assertEquals(response.getDescricao(),"desc");
        assertEquals(response.getPriority(),PriorityEnum.HI);
        assertEquals(response.getStatus(),TaskEnum.PG);
        assertEquals(response.getUsers().getId(),u.getId());
    }


    @Test(expected = ConstraintViolationException.class)
    public void testSaveInvalidTask() {

        Task t = new Task();
        t.setId(null);
        t.setDeadline(new Date());
        t.setUsers(null);
        t.setDescricao("test");
        t.setTitulo(null);
        t.setPriority(null);
        repo.save(t);

    }


}
