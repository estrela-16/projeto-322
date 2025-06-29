package Principal;
public class Main {
    public static void main(String[] args) {
        // Criando uma pessoa
        Pessoa pessoa1 = new Pessoa("Ana Carolina Vieira Araújo", "52.5881.778-40", "(11) 96966 2996");
        
        System.out.println("Nome: " + pessoa1.getNome());
        System.out.println("CPF: " + pessoa1.getCpf());
        System.out.println("Telefone: " + pessoa1.getTelefone());
    
        //gerenciar
        GerenciarProcedimento gerenciador = new GerenciarProcedimento();

        Procedimento p1 = new Procedimento("Profilaxia", "Clínica Geral");
        Procedimento p2 = new Procedimento("Tratamento endodôntico", "Endodontia");
        Procedimento p3 = new Procedimento("Instalação de aparelho fixo", "Ortodontia");
        
        gerenciador.adicionarProcedimento(p1);
        gerenciador.adicionarProcedimento(p2);
        gerenciador.adicionarProcedimento(p3);

        // Exibindo todos os procedimentos cadastrados
        System.out.println("Procedimentos cadastrados:");
        for (Procedimento p : gerenciador.getProcedimentos()) {
            System.out.println("- " + p);
        }

        Procedimento restauracao = new Procedimento("Restauração em resina", "Clínica Geral");

        Materiais algodao = new Materiais("Algodão", 0.50);
        Materiais resina = new Materiais("Resina fotopolimerizável", 25.00);
        Materiais luvas = new Materiais("Luvas descartáveis", 1.00);

        restauracao.adicionarMaterial(algodao);
        restauracao.adicionarMaterial(resina);
        restauracao.adicionarMaterial(luvas);

        System.out.println("Procedimento:");
        System.out.println(restauracao);
        System.out.println("\nMateriais utilizados:");
        for (Materiais m : restauracao.getMateriais()) {
            System.out.println("- " + m.getNome() + ": R$" + String.format("%.2f", m.getValor()));
        }

    }
}



