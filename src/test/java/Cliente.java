public class Cliente {

    private String nome;
    private int idade;
    private String id;
    private int risco;

    public Cliente(){
        nome = "N/D";
        idade = 0;
        id = "0";
        risco = 0;
    }

    public Cliente(String nome, int idade, String id, int risco) {
        this.nome = nome;
        this.idade = idade;
        this.id = id;
        this.risco = risco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRisco(int i) {
        return risco;
    }

    public void setRisco(int risco) {
        this.risco = risco;
    }
}
