-- 1. UTENTI
INSERT INTO Utente (email, password_utente, nome, cognome) VALUES 
('mario.rossi@studenti.unina.it', 'password123', 'Mario', 'Rossi'),     -- ID 1
('luigi.verdi@studenti.unina.it', 'password123', 'Luigi', 'Verdi'),     -- ID 2
('anna.bianchi@docenti.unina.it', 'docente123', 'Anna', 'Bianchi'),     -- ID 3
('giulia.neri@studenti.unina.it', 'password123', 'Giulia', 'Neri');     -- ID 4

-- 2. PROGETTI
INSERT INTO Progetto (nome, descrizione, id_creatore) VALUES 
('Sistema Gestione Esami', 'Applicativo per la registrazione dei voti OOBD', 1), -- ID 1
('Sito Web Dipartimento', 'Restyling portale ingegneria', 3);                    -- ID 2

-- 3. MEMBRI DEI PROGETTI
INSERT INTO Membro_progetto (id_prog, id_membro) VALUES 
(1, 1), (1, 2), (1, 4), -- Mario, Luigi e Giulia lavorano al progetto 1
(2, 3), (2, 1);         -- Anna e Mario lavorano al progetto 2

-- 4. ATTIVITÀ (Varie tipologie e stati per testare i filtri e il report)
INSERT INTO Attivita (id_prog, titolo, descrizione, scadenza, tipo, stato, id_creatore) VALUES 
(1, 'Progettazione DB', 'Creare lo schema SQL', '2024-10-15', 'Sviluppo', 'Finita', 1),             -- ID 1 (Prog 1)
(1, 'Scrittura Classi Entity', 'Mappare le tabelle in Java', '2024-10-20', 'Sviluppo', 'Presa_in_carico', 1), -- ID 2 (Prog 1)
(1, 'Manuale Utente', 'Scrivere la documentazione', '2024-11-01', 'Documentazione', 'Nuova', 1),    -- ID 3 (Prog 1)
(2, 'Analisi Requisiti', 'Studio dei competitor', '2024-10-10', 'Studio', 'Finita', 3),             -- ID 4 (Prog 2)
(2, 'Sviluppo Frontend', 'Creare la home in HTML', '2024-10-25', 'Sviluppo', 'Presa_in_carico', 3); -- ID 5 (Prog 2)

-- 5. ASSEGNAZIONE MEMBRI ALLE ATTIVITÀ
INSERT INTO Membro_attivita (id_task, id_membro) VALUES 
(1, 1), (1, 2), -- Mario e Luigi hanno finito il DB
(2, 4),         -- Giulia sta scrivendo le Entity
(4, 3),         -- Anna ha fatto l'analisi
(5, 1);         -- Mario sta sviluppando il frontend

-- 6. COMMENTI
INSERT INTO Commento_task (id_task, id_membro, testo) VALUES 
(2, 4, 'Ho quasi finito la classe Utente.java, domani pusho il codice.'),
(5, 3, 'Ricordati di usare il logo ufficiale del dipartimento Mario!');

-- 7. FILE DI CODICE (Solo per le attività di Sviluppo, come da traccia)
INSERT INTO File_task (id_task, nome_file, estensione) VALUES 
(1, 'Database', 'sql'),   -- ID 1 (Appartiene all'attività 1)
(2, 'Utente', 'java');    -- ID 2 (Appartiene all'attività 2)

-- 8. REVISIONI DEI FILE (Con finto codice binario convertendo il testo in BYTEA)
INSERT INTO Revisione_file (id_file_rev, id_task, versione, id_autore, nota_descrittiva, dimensione, contenuto) VALUES 
-- File 1 (Database.sql) ha 2 revisioni
(1, 1, 1, 1, 'Creazione iniziale tabelle', 1024, convert_to('CREATE TABLE Test();', 'UTF8')),
(1, 1, 2, 2, 'Aggiunte chiavi esterne (FK)', 2048, convert_to('CREATE TABLE Test(id INT);', 'UTF8')),

-- File 2 (Utente.java) ha 1 revisione
(2, 2, 1, 4, 'Bozza della classe Entity', 512, convert_to('public class Utente {}', 'UTF8'));