package Principal;
import java.util.List;
// calcula o lucro ou defict com base no valor dos procediemtnos e uma taxa de comissao para os funcionarios
public class Financeiro {

    double percentualComissao; /*porecentagem ganha pelos dentistas por consulta */
    double gastosGerais; /*valor fixo por consulta pra manutencao do consultorio */
    Agenda agenda;
    CalculodeGastos calculadora;

    public Financeiro(Agenda agenda, double percentual) {
        this.agenda = agenda;
        this.percentualComissao = percentual;
    }

    public double calcularGanhos(int mes, int ano){ 
        List<Atendimento> atendimentos= agenda.getAtendimentos(mes, ano); 
        double ganhos=0;

        for (int i = 0; i < atendimentos.size(); i++) {
            Atendimento atendimento = atendimentos.get(i);
            ganhos+=(atendimento.getProcedimentos().calcularCustoTotal()); /*Valor corresponde ao preco total da consulta*/
        }
        return ganhos;
    }

    public void setPercentualComissao(double percentualComissao) {
        this.percentualComissao = percentualComissao;
    }

    public double calcularGastos(int mes,int ano){
        List<Atendimento> atendimentos= agenda.getAtendimentos(mes, ano); 
        double gastos=0;

        for (int i = 0; i < atendimentos.size(); i++) {
            Atendimento atendimento = atendimentos.get(i);
            gastos+=(atendimento.getProcedimentos().calcularGastos()); /*Valor corresponde aos materiaise manutencao*/
        }
        double ganhos = calcularGanhos(mes, ano);

        double salarioDentistas= ganhos * percentualComissao;
        gastos+=salarioDentistas;
                                             
        return gastos;
    }

    public double calcularBalanco(int mes, int ano){
        return calcularGanhos(mes, ano)-calcularGastos(mes, ano);
    }

    public String gerarRelatorioMensal(int ano){ /* interface do financeiro */
        StringBuilder relatorio = new StringBuilder();

        relatorio.append(String.format("--- Relatório Financeiro Anual para %d ---%n%n", ano));
        relatorio.append(String.format("%-10s %-15s %-15s %-15s%n", "Mês", "Ganhos (R$)", "Gastos (R$)", "Lucro (R$)"));

        for (int mes = 1; mes <= 12; mes++){
            double ganho = calcularGanhos(mes, ano);
            double gasto = calcularGastos(mes, ano);
            double lucro = ganho - gasto;

            relatorio.append(String.format("%02d/%d      %-15.2f      %-15.2f      %-15.2f%n",
                             mes, ano, ganho, gasto, lucro));
        }
        return relatorio.toString();
    }
}
