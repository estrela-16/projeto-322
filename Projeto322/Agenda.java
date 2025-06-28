import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Agenda {
    private List<Atendimento> atendimentos;

    public Agenda() {
        this.atendimentos = new ArrayList<>();
    }

    public void agendarAtendimento(Atendimento atendimento) {
        atendimentos.add(atendimento);
    }

    public List<Atendimento> getAtendimentos(int mes, int ano) {
        return atendimentos.stream()
            .filter(a -> a.getData().getMonthValue() == mes && a.getData().getYear() == ano)
            .collect(Collectors.toList());
    }

    public List<Atendimento> getTodos() {
        return atendimentos;
    }
}






