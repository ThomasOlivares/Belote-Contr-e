package fenetre;

public interface Observable {
  public void addObservateur(Observateur obs);
  public void updateObservateur();
  public void delObservateur();
}