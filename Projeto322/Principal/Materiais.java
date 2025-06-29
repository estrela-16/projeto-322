package Principal;
public class Materiais implements Id_Banco{
  private String nome;
  private  double valor;
  private int id;


public Materiais(String nome, double valor){
  this.id=0;
  this.nome = nome;
  this.valor = valor;
}
//retornar materiais do bd
public Materiais(int id, String nome, double valor){
  this.id=0;
  this.nome = nome;
  this.valor = valor;
}

public Materiais(){
  super();
}

public void setId(int id){
  this.id=id;
}

public int getId(){
  return id;
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

@Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Materiais other = (Materiais) obj;
    return nome != null && nome.equals(other.nome);
}

@Override
public int hashCode() {
    return nome != null ? nome.hashCode() : 0;
}
}