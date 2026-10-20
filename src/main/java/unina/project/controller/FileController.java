package unina.project.controller;

import unina.project.dao.FileDAO;
import unina.project.entity.Attivita;
import unina.project.entity.Utente;
import unina.project.implementazioniPostgresDAO.FilePostgresDAO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileController {

    private final AutenticazioneController authController;
    private final AttivitaController attivitaController;
    private final FileDAO fileDAO = new FilePostgresDAO();

    public FileController(AutenticazioneController authController, AttivitaController attivitaController) {
        this.authController = authController;
        this.attivitaController = attivitaController;
    }

    public boolean salvaFileERevisione(File fileDaCaricare, String notaDescrittiva) {
        Attivita task = attivitaController.getAttivitaSelezionata();
        Utente autore = authController.getUtenteCorrente();
        if (task == null || autore == null || fileDaCaricare == null) return false;

        try {
            String nomeCompleto = fileDaCaricare.getName();
            String nome = nomeCompleto.contains(".") ? nomeCompleto.substring(0, nomeCompleto.lastIndexOf('.')) : nomeCompleto;
            String estensione = nomeCompleto.contains(".") ? nomeCompleto.substring(nomeCompleto.lastIndexOf('.') + 1) : "";
            byte[] fileInByte = Files.readAllBytes(fileDaCaricare.toPath());

            return fileDAO.salvaFileERevisione(task.getIdAttivita(), autore.getIdUtente(), nome, estensione, fileInByte, notaDescrittiva);
        } catch (IOException e) {
            System.err.println("Errore lettura file su disco: " + e.getMessage());
            return false;
        }
    }

    public boolean scaricaUltimaRevisione(File fileDestinazione) {
        Attivita task = attivitaController.getAttivitaSelezionata();
        if (task == null || fileDestinazione == null) return false;

        try {
            byte[] fileInByte = fileDAO.getContenutoUltimaRevisione(task.getIdAttivita());
            if (fileInByte != null) {
                Files.write(fileDestinazione.toPath(), fileInByte);
                return true;
            }
        } catch (IOException e) {
            System.err.println("Errore scrittura file su disco: " + e.getMessage());
        }
        return false;
    }

    public String getNomeFileOriginale() {
        Attivita task = attivitaController.getAttivitaSelezionata();
        if (task == null) return "Download_File";
        return fileDAO.getNomeFileOriginale(task.getIdAttivita());
    }

    public List<String> getStoricoRevisioni() {
        Attivita task = attivitaController.getAttivitaSelezionata();
        if (task == null) return new ArrayList<>();
        return fileDAO.getStoricoRevisioni(task.getIdAttivita());
    }
}