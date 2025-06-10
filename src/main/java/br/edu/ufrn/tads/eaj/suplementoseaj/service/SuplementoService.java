package br.edu.ufrn.tads.eaj.suplementoseaj.service;

import br.edu.ufrn.tads.eaj.suplementoseaj.domain.Suplemento;
import br.edu.ufrn.tads.eaj.suplementoseaj.repository.SuplementoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SuplementoService {

    private final SuplementoRepository suplementoRepository;

    @Autowired
    public SuplementoService(SuplementoRepository suplementoRepository) {
        this.suplementoRepository = suplementoRepository;
    }

    public List<Suplemento> listarSuplementosAtivos() {
        return suplementoRepository.findByIsDeletedIsNull();
    }
    
    public List<Suplemento> listarTodosSuplementos() {
        return suplementoRepository.findAll();
    }
    
    public Optional<Suplemento> buscarPorId(Long id) {
        return suplementoRepository.findById(id);
    }
    
    public void deletarSuplemento(Long id) {
        Optional<Suplemento> suplementoOpt = suplementoRepository.findById(id);
        if (suplementoOpt.isPresent()) {
            Suplemento suplemento = suplementoOpt.get();
            suplemento.setIsDeleted(new Date());
            suplementoRepository.save(suplemento);
        }
    }
    
    public void restaurarSuplemento(Long id) {
        Optional<Suplemento> suplementoOpt = suplementoRepository.findById(id);
        if (suplementoOpt.isPresent()) {
            Suplemento suplemento = suplementoOpt.get();
            suplemento.setIsDeleted(null);
            suplementoRepository.save(suplemento);
        }
    }

    public void salvarSuplemento(Suplemento suplemento) {
        suplementoRepository.save(suplemento);
    }
}