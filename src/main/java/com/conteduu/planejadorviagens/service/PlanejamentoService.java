package com.conteduu.planejadorviagens.service;

import com.conteduu.planejadorviagens.model.Viagem;
import com.conteduu.planejadorviagens.repository.ViagemRepository;

import java.time.LocalDate;
import java.util.List;

public class PlanejamentoService {

    private final ViagemRepository repository = new ViagemRepository();

    public void addTrip(String destination, LocalDate start, LocalDate end, double cost){
        validarViagem(destination, start, end, cost);
        if (repository.conflict(start, end)) throw new IllegalArgumentException("Conflita com outra viagem");
        repository.save(new Viagem(null, destination, cost, start, end));
    }

    public void atualizarViagem(Viagem viagem, String destino, LocalDate inicio, LocalDate fim, double custo){
        if (viagem == null || viagem.getId() == null){
            throw new IllegalArgumentException("Viagem invalida");
        }
        validarViagem(destino, inicio, fim, custo);
        if (repository.conflitaExcluindoId(viagem.getId(), inicio, fim)){
            throw  new IllegalArgumentException("Conflita com outra viagem");
        }

        viagem.setDestino(destino);
        viagem.setDataInicio(inicio);
        viagem.setDataFim(fim);
        viagem.setCusto(custo);

        repository.atualizar(viagem);
    }

    private static void validarViagem(String destination, LocalDate start, LocalDate end, double cost) {
        if (destination == null || destination.isBlank()) throw new IllegalArgumentException("Destino vazio");
        if (start == null || end == null) throw new IllegalArgumentException("Datas obrigatorias");
        if (start.isAfter(end)) throw new IllegalArgumentException("Data inicio posterior a data fim");
        if (cost < 0) throw new IllegalArgumentException("Custo negativo");
    }

    public List<Viagem> listAll() {
        return repository.listAll();
    }

    public double totalExpense() {
        return repository.sumCost();
    }



}
