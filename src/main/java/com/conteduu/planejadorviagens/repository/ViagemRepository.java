package com.conteduu.planejadorviagens.repository;

import com.conteduu.planejadorviagens.config.MongoConfig;
import com.conteduu.planejadorviagens.model.Viagem;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Sorts.ascending;

public class ViagemRepository {
    private final MongoCollection<Viagem> collection = MongoConfig.db
            .getCollection("tripplanner", Viagem.class);

    public void save(Viagem viagem){
        collection.insertOne(viagem);
    }

    public void atualizar(Viagem viagem){
        collection.replaceOne(Filters.eq("_id", viagem.getId()), viagem);
    }

    public List<Viagem> listAll(){
        return collection.find().sort(ascending("dataInicio")).into(new ArrayList<>());
    }

    public double sumCost(){
        return collection.find().into(new ArrayList<>()) // Converte para lista
                .stream() // Usa a stream API do Java (Como se fosse um forEach)
                .mapToDouble(Viagem::getCusto) // Extrai o custo mapeando a variavel
                .sum(); // Soma todos os valores
    }

    public boolean conflict(LocalDate start, LocalDate end){
        long quantityConflicts = collection.countDocuments(Filters.and(
                Filters.lte("dataInicio", end),
                Filters.gte("dataFim", start)
        ));
        return quantityConflicts > 0;
    }

    public boolean conflitaExcluindoId(ObjectId ignorarId, LocalDate inicio, LocalDate fim){
        long qtd = collection.countDocuments(Filters.and(
                Filters.lte("dataInicio", fim),
                Filters.gte("dataFim", inicio),
                Filters.ne("_id", ignorarId)
        ));
        return qtd > 0;
    }
}
