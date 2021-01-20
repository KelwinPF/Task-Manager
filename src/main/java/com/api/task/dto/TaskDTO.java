package com.api.task.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class TaskDTO {

    private Long id;
    @Length(min=3,max=40,message = "O nome deve conter entre 3 e 40 caracteres")
    private String titulo;
    private String descricao;
    @Pattern(regexp="^(IN_PROGRESS|CONCLUDED)$", message = "Para status somente são aceitos os valores IN_PROGRESS ou CONCLUDED")
    private String status;
    @NotNull(message = "Informe a role de acesso")
    @Pattern(regexp="^(HIGH|MID|LOW)$", message = "Para prioridade somente são aceitos os valores HIGH,MID ou LOW")
    private String priority;
    @NotNull(message = "Informe o id do usuário")
    private Long users;
    @NotNull(message = "Informe uma data")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", locale = "pt-BR", timezone = "Brazil/East")
    private Date deadline;

}