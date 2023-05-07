package main;

public class Arete {
	
	int Valuation;
	Case caseEntrante;
	Case caseSortante;
	String direction;
	
	public Arete(int Valuation , Case caseEntrante , Case caseSortante,String direction ) {
		
		this.Valuation =Valuation;
		this.caseEntrante = caseEntrante;
		this.caseSortante = caseSortante;
		this.direction=direction;	
	}
}
