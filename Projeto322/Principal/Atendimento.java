package Principal;
import java.time.LocalDate;


public class Atendimento implements Id_Banco{
   private int id;
   private LocalDate data;
   private Procedimento procedimento;
   private Paciente paciente;
   private String horario;
   private Dentista dentista;
   private StatusPagamento statusP;


    public Atendimento(LocalDate data, String horario, Procedimento procedimento, Paciente paciente, Dentista dentista, StatusPagamento statusP) {
        this.data = data;
        this.horario = horario;
        this.procedimento = procedimento;
        this.paciente = paciente;
        this.dentista = dentista;
        this.statusP = statusP;
    }

    public Atendimento(int id, LocalDate data, String horario, Procedimento procedimento, Paciente paciente, Dentista dentista, StatusPagamento statusP) {
        this.id=id;
        this.data = data;
        this.horario = horario;
        this.procedimento = procedimento;
        this.paciente = paciente;
        this.dentista = dentista;
        this.statusP = statusP;
    }

    // Construtor vazio para o DAO e para atualizações dinâmicas
    public Atendimento() {
    }

    @Override
   public int getId() {
       return id;
   }

   @Override
   public void setId(int id) {
       this.id = id;
   }

   public LocalDate getData(){
       return data;
   }

   public void setData(LocalDate data) {
       this.data = data;
   }

   public Procedimento getProcedimentos() {
       return procedimento;
   }

   public void setProcedimento(Procedimento procedimento) {
       this.procedimento = procedimento;
   }

   public Paciente getPaciente(){
       return paciente;
  }

   public void setPaciente(Paciente paciente) {
       this.paciente = paciente;
   }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public Dentista getDentista() {
        return dentista;
    }

    public void setDentista(Dentista dentista) {
        this.dentista = dentista;
    }

    public StatusPagamento getStatusP() {
        return statusP;
    }

    public void setStatusP(StatusPagamento statusP) {
        this.statusP = statusP;
    }


   public double getCustoMateriais(){ /*custo dos materiais + manutencao do consultorio */
        return procedimento.calcularCustoTotal();
   }

   public void mudarStatusDePagamento(){
    if (statusP != null){
        if (statusP == StatusPagamento.PAGO){
            statusP = StatusPagamento.NAO_PAGO;
        } else {
            statusP = StatusPagamento.PAGO;
        }

    }
   }

}
