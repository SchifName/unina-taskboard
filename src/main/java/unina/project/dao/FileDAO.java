package unina.project.dao;

import java.util.List;

public interface FileDAO {
    boolean salvaFileERevisione(int idTask, int idAutore, String nome, String estensione, byte[] contenuto, String notaDescrittiva);
    byte[] getContenutoUltimaRevisione(int idTask);
    String getNomeFileOriginale(int idTask);
    List<String> getStoricoRevisioni(int idTask);
}