package it.polito.tdp.artsmia.model;

public class Adiacenza {

	private int id1;
	private int id2;
	private int peso;
	public Adiacenza(int id1, int id2, int peso) {
		super();
		this.id1 = id1;
		this.id2 = id2;
		this.peso = peso;
	}
	public int getId1() {
		return id1;
	}
	public int getId2() {
		return id2;
	}
	public int getPeso() {
		return peso;
	}
	
}
