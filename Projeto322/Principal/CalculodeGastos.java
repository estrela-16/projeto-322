package Principal;

import java.util.ArrayList;
import java.util.List;

public class CalculodeGastos {
    private List<MateriaisComuns> materialcomum;
    private int numerodeconsultas = 1;
    private List<Contas> conta;
    private double comissao;
    private double servico;

    public CalculodeGastos() {
        this.materialcomum = new ArrayList<>();
        this.conta = new ArrayList<>();
    }

    public List<MateriaisComuns> getMateriaisComunses() {
        return materialcomum;
    }

    public void setMateriaisComunses(List<MateriaisComuns> materiais) {
        this.materialcomum = new ArrayList<>(materiais);
    }

    public List<Contas> getConta() {
        return conta;
    }

    public void setConta(List<Contas> contas) {
        this.conta = new ArrayList<>(contas);
    }

    public void setNumeroDeConsultas(int num) {
        this.numerodeconsultas = Math.max(1, num); // evita divisão por zero
    }

    public void setComissao(double comissao) {
        this.comissao = comissao;
    }

    public void setTaxaServico(double servico) {
        this.servico = servico;
    }

    public double getComissao() {
        return comissao;
    }

    public double getTaxaServico() {
        return servico;
    }

    public double CalculodeMateriais() {
        double total = 0;
        for (MateriaisComuns m : materialcomum) {
            total += m.getQuantidade() * m.getValor();
        }
        return total / numerodeconsultas;
    }

    public double Contas() {
        double total = 0;
        for (Contas c : conta) {
            total += c.getValor();
        }
        return total / numerodeconsultas;
    }

    public double gastosTotais() {
        double materiais = CalculodeMateriais();
        double contas = Contas();
        return materiais + contas;
    }
}
