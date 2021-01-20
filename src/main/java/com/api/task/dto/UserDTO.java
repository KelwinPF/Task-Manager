package com.api.task.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class UserDTO {

    private Long id;
    @Email(message="Email inválido")
    private String email;
    @Length(min=3,max=50,message = "O nome deve conter entre 3 e 50 caracteres")
    private String name;
    @NotNull(message = "informe a senha")
    private String password;
    @NotNull(message = "Informe a role de acesso")
    @Pattern(regexp="^(ROLE_ADMIN|ROLE_USER)$", message = "Para a role de acesso somente são aceitos os valores ROLE_ADMIN ou ROLE_USER")
    private String role;

}
