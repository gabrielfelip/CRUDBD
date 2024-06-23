package modelo;


import java.util.Date;

public class HistoricoPeso {
    private String cpf;
    private Date dataRegistro;
    private double peso;

    public HistoricoPeso(String cpf, Date dataRegistro, double peso) {
        this.cpf = cpf;
        this.dataRegistro = dataRegistro;
        this.peso = peso;
    }

    public String getCpf(){
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Date getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(Date dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }
}
