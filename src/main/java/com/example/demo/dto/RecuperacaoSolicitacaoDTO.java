package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecuperacaoSolicitacaoDTO {

    @NotBlank(message = "o email e obrigatorio")
    @Email(message = "o email deve ser valido ")
    private String email;

}
