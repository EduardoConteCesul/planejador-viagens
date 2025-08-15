package com.conteduu.planejadorviagens.service;

import com.conteduu.planejadorviagens.model.Viagem;
import com.conteduu.planejadorviagens.repository.ViagemRepository;

import java.time.LocalDate;
import java.util.List;

public class PlanejamentoService {

    private final ViagemRepository repository = new ViagemRepository();

    public void addTrip(String destination, LocalDate start, LocalDate end, double cost){
        validarDadosViagem(destination, start, end, cost);
        if (repository.conflict(start, end)) throw new IllegalArgumentException("Conflita com outra viagem");
        repository.save(new Viagem(null, destination, cost, start, end));
    }

    public void salvarAlteracoes(Viagem viagem){
        validarDadosViagem(viagem.getDestino(), viagem.getDataInicio(), viagem.getDataFim(), viagem.getCusto());
        repository.updateTrip(viagem);
    }

    public List<Viagem> listAll() {
        return repository.listAll();
    }

    public double totalExpense() {
        return repository.sumCost();
    }


    private void validarDadosViagem(String destination, LocalDate start, LocalDate end, double cost){
        if (destination == null || destination.isBlank()) throw new IllegalArgumentException("Destino vazio");
        if (start == null || end == null) throw new IllegalArgumentException("Datas obrigatorias");
        if (start.isAfter(end)) throw new IllegalArgumentException("Data inicio posterior a data fim");
        if (cost < 0) throw new IllegalArgumentException("Custo negativo");
    }

}
