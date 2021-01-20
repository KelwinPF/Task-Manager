package com.api.task.repository;
import com.api.task.entity.Task;

import com.api.task.util.TaskEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface TaskRepository extends JpaRepository<Task,Long>{

    @Query("SELECT t FROM Task t WHERE (t.titulo LIKE %:titulo%) AND " +
            "(t.id =:numero or :numero is null) AND (t.users.id = :id_user or :id_user is null) " +
            "AND (t.status = :situacao or :situacao is null)")
    Page<Task> getTasks(@Param("titulo") Optional<String> titulo,
                        @Param("numero") Optional<Long> numero,
                        @Param("id_user") Optional<Long> id_user,
                        @Param("situacao") Optional<TaskEnum> situacao,
                        Pageable page);
    Task getTaskById(Long id);

}
