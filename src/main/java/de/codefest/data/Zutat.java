package de.codefest.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity

public class Zutat {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	public String beschreibung;
	public String einheit;
	public String name;
	public int scrabblePoint;
	public String bild;

	public Zutat() {

	}

	public Zutat(String name, String beschreibung, String einheit, int scrabblePoint, String bild) {
		this.name = name;
		this.beschreibung = beschreibung;
		this.einheit = einheit;
		this.scrabblePoint = scrabblePoint;
		this.bild = bild;
	}

	@Transient
	public String toHTML(boolean withAddToCart) {
		return "<hr><h3> " + name + " (" + scrabblePoint + ")</h3>" + //
				"<img width='100px' src='" + bild + "'>  " + beschreibung + "<br>" + //

				(withAddToCart ? "<a onclick='var x = new XMLHttpRequest(); " + //
						"x.open(\"GET\", \"addToCart?id=" + id + "\", true);" + //
						"x.send(null);" + //
						"return false;' href='#'>Add to Cart</a> <br>" : "");

		// "<a href='#' onclick='addToCart?id=" + id + "'>Add to Cart</a> <br>";
	}

}
