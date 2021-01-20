package com.api.task.entity;
import com.api.task.util.PriorityEnum;
import com.api.task.util.TaskEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name="tasks")
public class Task implements Serializable {

    private static final long serialVersionUID = 6079769300175684582L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String titulo;
    private String descricao;
    @JoinColumn(name="users",referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User users;
    @NotNull
    @Enumerated(EnumType.STRING)
    private PriorityEnum priority;
    @Enumerated(EnumType.STRING)
    private TaskEnum status = TaskEnum.PG;
    @NotNull
    private Date deadline;
}