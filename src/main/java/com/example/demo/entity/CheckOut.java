package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "checkouts")
@EqualsAndHashCode(callSuper = false)
public class CheckOut extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posto_id") 
    private Posto posto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foto_id")
    private Arquivo foto;

    @Column(name = "relatorio_numerico")
    private Integer relatorio;               // total (mantido para compatibilidade)

    @Column(name = "relatorio_matutino")
    private Integer relatorioMatutino;       // novo: relatório do turno matutino

    @Column(name = "relatorio_vespertino")
    private Integer relatorioVespertino;     // novo: relatório do turno vespertino

    @Column(name = "lesoes_por_agua_viva")
    private Integer lesoesPorAguaViva;

    @Column(name = "incidentes_matutino")
    private Integer incidentesMatutino;

    @Column(name = "incidentes_vespertino")
    private Integer incidentesVespertino;

    @Column(name = "prevencoes_matutino")
    private Integer prevencoesMatutino;

    @Column(name = "prevencoes_vespertino")
    private Integer prevencoesVespertino;
}