package com.api.task.controller;

import com.api.task.dto.TaskDTO;
import com.api.task.entity.Task;
import com.api.task.entity.User;
import com.api.task.response.Response;
import com.api.task.service.TaskService;
import com.api.task.util.PriorityEnum;
import com.api.task.util.TaskEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("task")
public class TaskController {


    @Autowired
    private TaskService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<TaskDTO>> create(@Valid @RequestBody TaskDTO dto, BindingResult result) {

        Response<TaskDTO> response = new Response<TaskDTO>();

        if (result.hasErrors()) {
            result.getAllErrors().forEach(e -> response.getErrors().add(e.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Task task = service.save(this.convertDtoToEntity(dto));
        response.setData(this.convertEntityToDto(task));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<TaskDTO>> update(@Valid @RequestBody TaskDTO dto, BindingResult result) {

        Response<TaskDTO> response = new Response<TaskDTO>();

        Optional<Task> t = service.findById(dto.getId());

        if (!t.isPresent()) {
            result.addError(new ObjectError("Task", "Tarefa não encontrada"));
        }
        if (result.hasErrors()) {
            result.getAllErrors().forEach(r -> response.getErrors().add(r.getDefaultMessage()));

            return ResponseEntity.badRequest().body(response);
        }
        TaskEnum tsk = t.get().getStatus();

        Task saved = service.save(this.convertDtoToEntity(dto));

        saved.setStatus(tsk);

        response.setData(this.convertEntityToDto(service.save(saved)));
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping(value = "/{taskid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<String>> delete(@PathVariable Long taskid) {

        Response<String> response = new Response<String>();

        Optional<Task> t = service.findById(taskid);

        if (!t.isPresent()) {
            response.getErrors().add("Task de id " + taskid + " não encontrada");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        service.deleteById(taskid);
        response.setData("Task de id "+ taskid + " apagada com sucesso");
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping(value = "/{taskid}/conclude")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<String>> conclude(@PathVariable Long taskid) {

        Response<String> response = new Response<String>();

        Optional<Task> t = service.findById(taskid);

        if (!t.isPresent()) {
            response.getErrors().add("Task de id " + taskid + " não encontrada");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        service.concludeById(taskid);
        response.setData("Task de id "+ taskid + " concluida com sucesso");
        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    public ResponseEntity<Response<Page<TaskDTO>>> get(@RequestParam("titulo") Optional<String> titulo,
                                                           @RequestParam("numero") Optional<Long> numero,
                                                           @RequestParam("user") Optional<Long> id_usuario,
                                                           @RequestParam("status") Optional<String> status,
                                                           @RequestParam(name = "page", defaultValue = "0") int page) {

        Response<Page<TaskDTO>> response = new Response<Page<TaskDTO>>();
        Optional<TaskEnum> task_enum = Optional.empty();
        if(status.isPresent() && TaskEnum.getEnum(status.get()) != null){
            task_enum = Optional.of(TaskEnum.getEnum(status.get()));
        }
        Page<Task> items = service.getTasks(titulo,numero,id_usuario,task_enum,page);
        Page<TaskDTO> dto = items.map(i -> this.convertEntityToDto(i));
        response.setData(dto);
        return ResponseEntity.ok().body(response);
    }

    private Task convertDtoToEntity(TaskDTO dto) {
        Task task = new Task();
        task.setTitulo(dto.getTitulo());
        task.setDescricao(dto.getDescricao());
        task.setDeadline(dto.getDeadline());
        task.setPriority(PriorityEnum.getEnum(dto.getPriority()));

        User u = new User();
        u.setId(dto.getUsers());
        task.setUsers(u);
        task.setId(dto.getId());
        return task;

    }

    private TaskDTO convertEntityToDto(Task t) {
        TaskDTO dto = new TaskDTO();
        dto.setId(t.getId());
        dto.setDeadline(t.getDeadline());
        dto.setDescricao(t.getDescricao());
        dto.setTitulo(t.getTitulo());
        dto.setPriority(t.getPriority().getValue());
        dto.setStatus(t.getStatus().getValue());

        return dto;
    }
}
