package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	private MeteoDAO meteoDao;
	private List<Citta> tutteLeCitta;
	private List<Citta> soluzioneMigliore;
	
	public Model() {
		meteoDao = new MeteoDAO();
		tutteLeCitta = new ArrayList<Citta>();
	}

	// of course you can change the String output with what you think works best
	public String getUmiditaMedia(int mese) {
		String ritorno = "";
		for(Citta localita: this.meteoDao.getLocalita()) {
			ritorno += localita.getNome() +" "+ this.meteoDao.getMediaUmiditaLocalitaMese(mese, localita) + "\n";
		}
		
		return ritorno;
	}
	
	// of course you can change the String output with what you think works best
	public List<Citta> trovaSequenza(int mese) {
		tutteLeCitta.addAll(this.meteoDao.getLocalita());
		this.soluzioneMigliore = null;
		for(Citta c: tutteLeCitta) {
			c.setRilevamenti(this.meteoDao.getAllRilevamentiLocalitaMese(mese, c));
		}
		List<Citta> parziale = new ArrayList<Citta>();
		cercaSequenza(parziale,0);
		return soluzioneMigliore;
	}

	private void cercaSequenza(List<Citta> parziale, int livello) {
		
		if(livello == NUMERO_GIORNI_TOTALI) {
			Double costo = calcolaCosto(parziale);
			if(soluzioneMigliore == null || costo<calcolaCosto(soluzioneMigliore)) {
				soluzioneMigliore = new ArrayList<>(parziale);
			}
		}
		for(Citta c: tutteLeCitta) {
			if(soluzioneAmmissibile(c, parziale)) {
				parziale.add(c);
				cercaSequenza(parziale, livello+1);
				parziale.remove(parziale.size()-1);
			}
		}
		
	}
	
	public boolean soluzioneAmmissibile(Citta citta, List<Citta> parziale) {
		// VERIFICA GIORNI MASSIMI PER CITTA
		int contatoreGiorniInCitta = 0;
		for(Citta precedente: parziale) {
			if(precedente.equals(citta))
				contatoreGiorniInCitta++;
		}
		
		if(contatoreGiorniInCitta>=NUMERO_GIORNI_CITTA_MAX)
			return false;
		
		// VERIFICA GIORNI CONSECUTIVI IN CITTA
		if(parziale.size()==0)
			return true;
		if(parziale.size()==1 || parziale.size()==2) { // secondo o terzo giorno -> non posso cambiare citta
			if(parziale.get(parziale.size()-1).equals(citta))
				return true;
			else
				return false;
		}
		
		// se ho passato questi controlli posso anche rimanere nella stess citta
		if(parziale.get(parziale.size()-1).equals(citta))
			return true;
		// ma se cambio città devo verificare che nei 3 giorni precedenti sono stato fermo
		if(parziale.get(parziale.size()-1).equals(parziale.get(parziale.size()-2)) && 
				parziale.get(parziale.size()-2).equals(parziale.get(parziale.size()-3)))
			return true;
		
		return false;
	}
	

	public double calcolaCosto(List<Citta> parziale) {
//		int ritorno = 0;
//		for(int i = 0; i<NUMERO_GIORNI_TOTALI; i++) {
//			if(i == 0) {
//				ritorno += parziale.get(i).getRilevamenti().get(i).getUmidita();
//			}
//			else {
//				if(!parziale.get(i).getNome().equals(parziale.get(i-1).getNome())) {
//					ritorno+= parziale.get(i).getRilevamenti().get(i).getUmidita()+100;
//				}
//				else {
//					ritorno += parziale.get(i).getRilevamenti().get(i).getUmidita();
//				}
//			}
//		}
//		return ritorno;
		
		double costo = 0.0;
		//sommatoria delle umidità in ciascuna città, considerando il rilevamento del giorno giusto
		//SOMMA parziale.get(giorno-1).getRilevamenti().get(giorno-1)
		for (int giorno=1; giorno<=NUMERO_GIORNI_TOTALI; giorno++) {
			//dove mi trovo
			Citta c = parziale.get(giorno-1);
			//che umidità ho in quel giorno in quella città?
			double umid = c.getRilevamenti().get(giorno-1).getUmidita();
			costo+=umid;
		}
		//poi devo sommare 100*numero di volte in cui cambio città
		for (int giorno=2; giorno<=NUMERO_GIORNI_TOTALI; giorno++) {
			//dove mi trovo
			if(!parziale.get(giorno-1).equals(parziale.get(giorno-2))) {
				costo +=COST;
			}
		}
		return costo;
	}
}
