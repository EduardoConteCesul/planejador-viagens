package com.conteduu.planejadorviagens.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.pojo.PojoCodecProvider;

public class MongoConfig {
    private static final String URI = "mongodb://localhost:27017";
    private static final MongoClient client;
    public static final MongoDatabase db;

    static {
        /*
        * 1 - Criar um "codec provider" que converte POJO's
        *   para documentos BSON automaticamente
        */
        PojoCodecProvider pojoClient = PojoCodecProvider.builder().automatic(true).build();

        /*
        *   2- Criar um codev registry que é uma lista de codecs que o mongo vai usar
        *   - Inclui o codec padrao do mongo (String, Int)
        *   - Inclui o codec para POJO's que criamos acima
        */
        MongoClientSettings settings = MongoClientSettings.builder().codecRegistry(
                CodecRegistries.fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        CodecRegistries.fromProviders(pojoClient)
                )
        ).applyConnectionString(new ConnectionString(URI)).build();

        client = MongoClients.create(settings);

        // Obter a referência do banco de dados quue sera usado em toda a aplicação

        db = client.getDatabase("tripplanner");
    }

    // Construtor não instanciavel - Ira impedir que a classe seja instanciada
    private MongoConfig(){}
}
