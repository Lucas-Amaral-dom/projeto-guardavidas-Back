package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckOutResponseDTO {

    private String posto;

    private LocalDateTime horario;

    private int relatorio;

    private int lesoesPorAguaViva;

    private int incidentesMatutino;

    private int incidentesVespertino;
}