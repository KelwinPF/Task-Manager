package com.api.task.service;

import com.api.task.entity.Task;
import com.api.task.util.TaskEnum;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface TaskService {

    Task save(Task t);

    void deleteById(Long id);
    Optional<Task> findById(Long id);
    void concludeById(Long id);
    Page<Task> getTasks(Optional<String> titulo, Optional<Long> numero, Optional<Long> id_usuario, Optional<TaskEnum> situacao, int page);
}
