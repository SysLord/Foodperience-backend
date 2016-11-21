package de.codefest.data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Schritt {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "rezept")
	@JsonIgnore
	public Rezept rezept;

	public String beschreibung;

	@OneToOne(fetch = FetchType.EAGER)
	// @PrimaryKeyJoinColumn
	public Zutat zutat;

	public int menge;

	public int zeit = 5; // zeit in minuten für diesen Schritt

	// position im Ablauf
	public int index;

	public Schritt() {
	}

	public Schritt(Rezept rezept, int index, String beschreibung, Zutat zutat) {
		this.rezept = rezept;
		this.index = index;
		this.beschreibung = beschreibung;
		this.zutat = zutat;
	}

}
