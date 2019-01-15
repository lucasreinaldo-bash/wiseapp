package de.djuelg.vostore.Mappa;

public class Mensagens {

    private String mensagem;
    private String autor;


    public Mensagens(){

    }
    public Mensagens ( String mensagem, String autor){
        this.autor = autor;
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }
}
