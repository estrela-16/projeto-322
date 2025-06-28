import java.util.List;

public class Financeiro {

    double percentualComissao; /*porecentagem ganha pelos dentistas por consulta */
    double gastosGerais; /*valor fixo por consulta pra manutencao do consultorio */
    

    public double calcularGanhos(int mes, int ano){ /*adicionar como parametros mes e ano para localizacao na agenda? */
        List<Atendimento> atendimentos= Agenda.getAtendimentos(mes, ano); 
        double ganhos=0;

        for (int i = 0; i < atendimentos.size(); i++) {
            Atendimento atendimento = atendimentos.get(i);
            ganhos+=(atendimento.getValorConsulta()); /*Valor corresponde ao preco total da consulta*/
        }
        return ganhos;
    }

    public double calcularGastos(int mes,int ano){
        List<Atendimento> atendimentos= Agenda.getAtendimentos(mes, ano); 
        double gastos=0;

        for (int i = 0; i < atendimentos.size(); i++) {
            Atendimento atendimento = atendimentos.get(i);
            gastos+=(atendimento.getCustoMateriais()); /*Valor corresponde aos materiais*/
        }
        double ganhos = calcularGanhos();

        double salarioDentistas= ganhos * percentualComissao;
        gastos+=salarioDentistas;

        gastos+=gastosGerais*atendimentos.size(); /*Adiciona os valores dos gastos gerais*/
                                             
        return gastos;
    }

    public double calcularBalanco(){
        return calcularGanhos()-calcularGastos();
    }

    public void gerarRelatorioMensal(int ano){
        for (int mes = 0; mes <= 12; mes++){
            double ganho = calcularGanhos(mes,ano);
            double gasto = calcularGastos(mes,ano);

            double lucro=ganho-gasto;

             System.out.printf("Mês %02d/%d | Ganho: R$%.2f | Gasto: R$%.2f | Lucro: R$%.2f%n", 
                              mes, ano, ganho, gasto, lucro);
        }
    }
}
