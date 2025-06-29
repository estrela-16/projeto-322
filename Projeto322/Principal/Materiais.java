package Principal;
public class Materiais  {
  private String nome;
  private  double valor;


public Materiais(String nome, double valor){
  this.nome = nome;
  this.valor = valor;
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