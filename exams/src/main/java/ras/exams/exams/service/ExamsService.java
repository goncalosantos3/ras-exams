package ras.exams.exams.service;

import ras.exams.exams.model.Exam;

import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class ExamsService {
    // Referência para o DAO de exames
    private Map<UUID, Exam> exams; 

    public ExamsService(){
        // Acho que aqui o que temos que fazer é receber uma referência para o DAO de exames
        // para no início de cada execução da aplicação ele consultar a base de dados 
        // para saber os exames disponíveis e construir o mapa de exames
        // depois esta classe serve como um gestor dos exames
        // os objetos do model depois consequentemente teriam que falar com a BD pelos DAO
    }
}
