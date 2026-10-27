package unina.project.dao;

import  unina.project.entity.Progetto;
import  java.util.List;

public interface ProgettoDAO {
    List<Progetto> getProgettiByUtente(int idUtente);
    boolean creaProgetto(String nome, String descrizione, int idCreatore);
    boolean aggiungiMembro(int idProgetto, int idUtente);
}
