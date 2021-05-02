package it.polito.tdp.meteo.model;

import java.time.LocalDate;


public class Rilevamento {
	
	private String localita;
	private LocalDate data;
	private int umidita;

	public Rilevamento(String localita, LocalDate localDate, int umidita) {
		this.localita = localita;
		this.data = localDate;
		this.umidita = umidita;
	}

	public String getLocalita() {
		return localita;
	}

	public void setLocalita(String localita) {
		this.localita = localita;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public int getUmidita() {
		return umidita;
	}

	public void setUmidita(int umidita) {
		this.umidita = umidita;
	}

	// @Override
	// public String toString() {
	// return localita + " " + data + " " + umidita;
	// }

	@Override
	public String toString() {
		return String.valueOf(umidita);
	}

	

}
