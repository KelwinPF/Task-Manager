package com.api.task.service.impl;

import com.api.task.entity.Task;
import com.api.task.repository.TaskRepository;

import com.api.task.service.TaskService;
import com.api.task.util.TaskEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    TaskRepository repo;

    @Value("${pagination.items_per_page}")
    private int itemsPerPage;

    @Override
    public Task save(Task t){
        return repo.save(t);
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    @Override
    public Optional<Task> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public Page<Task> getTasks(Optional<String> titulo,Optional<Long> numero,Optional<Long> id_usuario,Optional<TaskEnum> situacao, int page) {

        PageRequest pg = PageRequest.of(page, itemsPerPage);
        if(titulo.isEmpty()) {
            titulo = Optional.of("%%");
        }
        return repo.getTasks(titulo,numero,id_usuario,situacao,pg);
    }

    @Override
    public void concludeById(Long id) {
        Task t = repo.getTaskById(id);
        t.setStatus(TaskEnum.CD);
        repo.save(t);
    }
}
