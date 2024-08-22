package main.root.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.root.dto.HabilidadeDto;
import main.root.model.Estudante;
import main.root.model.Habilidade;
import main.root.repository.EstudanteRepository;
import main.root.repository.HabilidadeRepository;

@Service
public class HabilidadeService {
	@Autowired
	HabilidadeRepository habilidadeRepository;

	@Autowired
	EstudanteRepository estudanteRepository;

	public List<Habilidade> listarHabilidades() {
		try {
			return habilidadeRepository.findAll();
		} catch (Exception e) {
			throw new RuntimeException("Falha ao buscar todos os habilidades com habilidade", e);
		}
	}

	public Habilidade buscarHabilidadePorId(long id) {
		try {
			return habilidadeRepository.findById(id)
					.orElseThrow(() -> new RuntimeException("Habilidade não encontrado com o ID: " + id));
		} catch (Exception e) {
			throw new RuntimeException("Falha ao buscar habilidade com o ID: " + id, e);
		}
	}

	public Habilidade addHabilidade(HabilidadeDto habilidadeDto) {
		if (habilidadeDto == null) {
			throw new IllegalArgumentException("O DTO do Habilidade não pode ser nulo.");
		}

		Habilidade habilidade = new Habilidade();
		habilidade.setNome(habilidadeDto.getNome());

		Estudante estudante = estudanteRepository.findById(habilidadeDto.getEstudanteId()).orElseThrow(
				() -> new RuntimeException("Estudante com ID " + habilidadeDto.getEstudanteId() + " não encontrado."));

		habilidade.setEstudante(estudante);

		try {
			return habilidadeRepository.save(habilidade);
		} catch (Exception e) {
			throw new RuntimeException("Falha ao salvar o Habilidade", e);
		}
	}

	public List<Habilidade> buscarHabilidadesPorEstudante(Long estudanteId) {
		return habilidadeRepository.findByEstudanteId(estudanteId);
	}

	public Habilidade atualizarHabilidade(long id, HabilidadeDto habilidadeDto) {
		if (!habilidadeRepository.existsById(id)) {
			throw new RuntimeException("Habilidade não encontrado com o ID: " + id);
		}

		Habilidade habilidadeExistente = habilidadeRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Habilidade não encontrado com o ID: " + id));

		habilidadeExistente.setNome(habilidadeDto.getNome());

		return habilidadeRepository.save(habilidadeExistente);
	}

	public void deleteHabilidade(Long id) {
		try {
			if (habilidadeRepository.existsById(id)) {
				habilidadeRepository.deleteById(id);
			} else {
				throw new RuntimeException("Habilidade não encontrado com o ID: " + id);
			}
		} catch (Exception e) {
			throw new RuntimeException("Falha ao deletar o Habilidade com o ID: " + id, e);
		}
	}
}
