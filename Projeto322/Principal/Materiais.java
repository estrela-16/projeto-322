package Principal;
public class Materiais  {
  private String nome;
  private  double valor;
  private int id;


public Materiais(String nome, double valor){
  this.nome = nome;
  this.valor = valor;
}

// Construtor para RECUPERAR um Material do banco de dados (com ID já definido)
public Materiais(int id, String nome, double valor){
  this.id=id;
  this.nome=nome;
  this.valor = valor;
}

public int getId() {
        return id;
    }

public void setId(int id) {//utilizado depois de adicionar o dentista na tabela para definir o seu id
    this.id = id;
}


public String getNome(){
  return nome; 
}

public double getValor(){
  return valor;
}

public void setValor(double valor){
  this.valor = valor;
}
@Override
   public String toString() {
       return nome + " - R$" + String.format("%.2f", valor);
   }

}
