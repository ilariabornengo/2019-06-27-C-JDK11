package it.polito.tdp.crimes.model;

public class Adiacenza {
	
	String id1;
	String id2;
	Integer peso;
	public Adiacenza(String id1, String id2, Integer pesoM) {
		super();
		this.id1 = id1;
		this.id2 = id2;
		this.peso = pesoM;
	}
	public String getId1() {
		return id1;
	}
	public void setId1(String id1) {
		this.id1 = id1;
	}
	public String getId2() {
		return id2;
	}
	public void setId2(String id2) {
		this.id2 = id2;
	}
	public Integer getPeso() {
		return peso;
	}
	public void setPeso(Integer peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return id1+" - "+ id2+" - "+peso;
	}
	
	

}
