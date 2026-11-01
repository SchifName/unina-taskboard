package unina.project.entity;

public class Notifica {
    private int idNotifica;
    private int idUtente;
    private String messaggio;
    private boolean letta;

    public Notifica(int idNotifica, int idUtente, String messaggio, boolean letta) {
        this.idNotifica = idNotifica;
        this.idUtente = idUtente;
        this.messaggio = messaggio;
        this.letta = letta;
    }

    public int getIdNotifica() { return idNotifica; }
    public int getIdUtente() { return idUtente; }
    public String getMessaggio() { return messaggio; }
    public boolean isLetta() { return letta; }
}