package com.quatro.catalog_service.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quatro.catalog_service.domain.dto.CartaRequestDto;
import com.quatro.catalog_service.domain.dto.CartaResponseDto;
import com.quatro.catalog_service.domain.entity.cartas;
import com.quatro.catalog_service.repository.CatalogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CatalogService {

    private final CatalogRepository catalogRepository;

    // 1. Criar uma nova carta
    public CartaResponseDto criarCarta(CartaRequestDto requestDto) {
        
        cartas novaCarta = cartas.builder()
                .nome(requestDto.getNome())
                .tipo(requestDto.getTipo())
                .raridade(requestDto.getRaridade())
                .vida(requestDto.getVida())
                .descricao(requestDto.getDescricao())
                .imagemUrl(requestDto.getImagemUrl())
                .build();

        cartas cartaSalva = catalogRepository.save(novaCarta);

        return converterParaResponseDto(cartaSalva);
    }

    // 2. Exibir e Filtrar cartas (Nome, Raridade ou Tipo)
    public List<CartaResponseDto> filtrarCartas(String nome, String raridade, String tipo) {
        List<cartas> resultado;

        // Chama exatamente os métodos do CatalogRepository
        if (nome != null && !nome.isBlank()) {
            resultado = catalogRepository.findByNome(nome);
            
        } else if (raridade != null && !raridade.isBlank()) {
            resultado = catalogRepository.findByRaridade(raridade);
            
        } else if (tipo != null && !tipo.isBlank()) {
            resultado = catalogRepository.findByTipo(tipo);
            
        } else {
            // Retorna o catálogo inteiro se nenhum filtro for passado
            resultado = catalogRepository.findAll();
        }

        return resultado.stream()
                .map(this::converterParaResponseDto)
                .collect(Collectors.toList());
    }

    // 3. Remover uma carta
    @Transactional
    public void removerCarta(UUID cartaId) {
        // Utilizando o método customizado que você adicionou no seu Repository
        catalogRepository.deleteAllByCartaId(cartaId);
    }

    // Método auxiliar para evitar repetição
    private CartaResponseDto converterParaResponseDto(cartas carta) {
        return CartaResponseDto.builder()
                .id(carta.getCartaId()) 
                .nome(carta.getNome())
                .tipo(carta.getTipo())
                .raridade(carta.getRaridade())
                .vida(carta.getVida())
                .descricao(carta.getDescricao())
                .imagemUrl(carta.getImagemUrl())
                .criadoEm(carta.getCriadoEm())
                .atualizadoEm(carta.getAtualizadoEm())
                .build();
    }
}