package com.example.demo.dto;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckOutDTO {

    @NotNull(message = "O ID do posto é obrigatório")
    private Long postoId;

    private MultipartFile foto;

    private String relatorio;

    @NotNull(message = "As lesões por água viva devem ser preenchidas.")
    @Min(value = 0, message = "As lesões não podem ser negativas.")
    private Integer lesoesPorAguaViva;

    @NotNull(message = "Os incidentes matutinos devem ser preenchidos.")
    @Min(value = 0, message = "Os incidentes não podem ser negativos.")
    private Integer incidentesMatutino;

    @NotNull(message = "Os incidentes vespertinos devem ser preenchidos.")
    @Min(value = 0, message = "Os incidentes não podem ser negativos.")
    private Integer incidentesVespertino;
}