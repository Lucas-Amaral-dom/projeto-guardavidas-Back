package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CheckOutDTO;
import com.example.demo.dto.CheckOutResponseDTO;
import com.example.demo.dto.CheckinDTO;
import com.example.demo.dto.CheckinResponseDTO;
import com.example.demo.entity.Arquivo;
import com.example.demo.entity.CheckOut;
import com.example.demo.entity.Checkin;
import com.example.demo.entity.Posto;
import com.example.demo.repository.CheckinRepository;
import com.example.demo.repository.CheckOutRepository;
import com.example.demo.repository.PostoRepository;

@Service
public class CheckService {

    @Autowired
    private PostoRepository postoRepository;

    @Autowired
    private ArquivoService arquivoService;

    @Autowired
    private CheckinRepository checkinRepository;

    @Autowired
    private CheckOutRepository checkOutRepository;

    public CheckinResponseDTO checkin(CheckinDTO dto) {
        Posto posto = postoRepository.findById(dto.getPostoId())
                .orElseThrow(() -> new IllegalArgumentException("Posto not found with id: " + dto.getPostoId()));

        Checkin checkin = new Checkin();

        checkin.setPosto(posto);

        Arquivo arquivo = arquivoService.upload(dto.getFoto());

        checkin.setFoto(arquivo);

        Checkin checkinSalvo = checkinRepository.save(checkin);

        CheckinResponseDTO crd = new CheckinResponseDTO();
        crd.setPosto(posto.getNome());
        crd.setHorario(checkinSalvo.getCreatedAt());

        return crd;
    }

    public CheckOutResponseDTO checkOut(CheckOutDTO dto) {
        Posto posto = postoRepository.findById(dto.getPostoId())
                .orElseThrow(() -> new IllegalArgumentException("Posto não encontrado"));

        CheckOut checkOut = new CheckOut();

        checkOut.setPosto(posto);

        Arquivo arquivo = arquivoService.upload(dto.getFoto());
        if (arquivo == null) {
            checkOut.setFoto(null);
        } else {
            checkOut.setFoto(arquivo);
        }

        // Soma dos novos campos para o relatório numérico
        int relatorioNumerico = dto.getLesoesPorAguaViva()
                + dto.getIncidentesMatutino()
                + dto.getIncidentesVespertino();

        checkOut.setRelatorio(relatorioNumerico);
        
        // Atualizando com os novos campos
        checkOut.setLesoesPorAguaViva(dto.getLesoesPorAguaViva());
        checkOut.setIncidentesMatutino(dto.getIncidentesMatutino());
        checkOut.setIncidentesVespertino(dto.getIncidentesVespertino());

        CheckOut checkOutSalvo = checkOutRepository.save(checkOut);

        CheckOutResponseDTO cord = new CheckOutResponseDTO();
        cord.setPosto(posto.getNome());
        cord.setHorario(checkOutSalvo.getCreatedAt());
        cord.setRelatorio(checkOutSalvo.getRelatorio());
        
        cord.setLesoesPorAguaViva(checkOutSalvo.getLesoesPorAguaViva());
        cord.setIncidentesMatutino(checkOutSalvo.getIncidentesMatutino());
        cord.setIncidentesVespertino(checkOutSalvo.getIncidentesVespertino());

        return cord;
    }
}