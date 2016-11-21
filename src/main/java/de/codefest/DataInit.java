package de.codefest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import de.codefest.data.Rezept;
import de.codefest.data.RezeptRepository;
import de.codefest.data.Schritt;
import de.codefest.data.SchrittRepository;
import de.codefest.data.Zutat;
import de.codefest.data.ZutatenRepository;

@Component
public class DataInit {

	@Autowired
	RezeptRepository rezeptRepo;
	@Autowired
	SchrittRepository schrittRepo;
	@Autowired
	ZutatenRepository zuztatenRepo;

	Map<String, Zutat> ZUTATEN;

	@PostConstruct
	public void init() {

		List<Zutat> zutaten = Arrays.asList( //
				new Zutat("Keine Zutat", "ein Hauch von Nichts", "ewigkeiten", 0, "img?path=void"),
				new Zutat("Kartoffeln", "gelbe, runde Dinger", "kg", 2, "img?path=kartoffel"), //

				new Zutat("Nudeln", "gelbe, lange Dinger", "kg", 2, "img?path=nudeln"), //
				new Zutat("Tomaten", "rote runde Dinger", "kg", 3, "img?path=tomaten"), //
				new Zutat("Zwiebeln", "weiße runde Dinger", "schmerzen", 5, "img?path=zwiebeln"),
				new Zutat("Wasser", "dursichtiges nasses Zeug", "Wasserbomben", 1, "img?path=wasser"),
				new Zutat("Rollmops", "essigwasser Fisch", "", 6, "img?path=rollmops"),
				new Zutat("frisches Schnitzel", "flach", "Schnitzelmenge", 3, "img?path=schnitzel"),
				new Zutat("Gemüse", "gesund!", "kg", 2, "img?path=gemuese"), //
				new Zutat("Pfanne", "für Gemüse etc.", "Anzahl", 1, "img?path=pfanne"),

				new Zutat("Salami", "Wurst", "cm", 4, "img?path=salami"),
				new Zutat("Schinken", "oftmals gewürfelt vorzufinden", "Anzahl Würfel", 5, "img?path=schinken"),
				new Zutat("Toast", "Weißbrot", "Scheiben", 3, "img?path=toast"),
				new Zutat("langkorn Reis", "qualitäts-Reis", "g", 1, "img?path=reis2"), //
				new Zutat("Putenbrust", "", "g", 1, "img?path=putenbrust"),

				new Zutat("hammermäßige Gewürze", "lecker", "", 1, "img?path=gewuerze")

		);

		zutaten = Lists.newArrayList(zuztatenRepo.save(zutaten));

		ZUTATEN = zutaten.stream().collect(Collectors.toMap(z -> z.name, z -> z));

		Spaghetti();
		bratkartoffeln();

		HammerSchnitzel();
		GemüsePfanne();
		Nudeltoast();
		ReispfannePutenbrust();

	}

	private void bratkartoffeln() {
		Rezept rezept = rezeptRepo.save(new Rezept("Bratkartoffeln"));

		rezept.schritte.add(schrittRepo.save(new Schritt(rezept, 0, "Wasser aufsetzen", ZUTATEN.get("Wasser"))));
		rezept.schritte.add(schrittRepo.save(new Schritt(rezept, 1, "Kartoffeln kochen", ZUTATEN.get("Kartoffeln"))));
		rezept.schritte.add(schrittRepo.save(new Schritt(rezept, 2, "Kartoffeln braten", ZUTATEN.get("Keine Zutat"))));

		rezeptRepo.save(rezept);
	}

	private void Spaghetti() {
		Rezept rezept = rezeptRepo.save(new Rezept("Spaghetti"));

		rezept.schritte.add(schrittRepo.save(new Schritt(rezept, 0, "Wasser aufsetzen", ZUTATEN.get("Wasser"))));
		rezept.schritte.add(schrittRepo.save(new Schritt(rezept, 1, "Zwiebeln schneiden", ZUTATEN.get("Zwiebeln"))));
		rezept.schritte.add(schrittRepo.save(new Schritt(rezept, 2, "Spaghetti ins Wasser", ZUTATEN.get("Nudeln"))));
		rezept.schritte.add(schrittRepo.save(new Schritt(rezept, 3, "Tomaten schneiden", ZUTATEN.get("Tomaten"))));
		rezeptRepo.save(rezept);
	}

	private void HammerSchnitzel() {
		Rezept rezept = rezeptRepo.save(new Rezept("Das hammer Schnitz3l"));

		rezept.schritte
				.add(schrittRepo.save(new Schritt(rezept, 0, "Schnitzel braten", ZUTATEN.get("frisches Schnitzel"))));
		rezept.schritte.add(schrittRepo
				.save(new Schritt(rezept, 1, "hammermäßige Gewürze verwenden", ZUTATEN.get("hammermäßige Gewürze"))));
		rezeptRepo.save(rezept);
	}

	private void GemüsePfanne() {
		Rezept rezept = rezeptRepo.save(new Rezept("GemüsePfanne"));

		rezept.schritte.add(schrittRepo.save(new Schritt(rezept, 0, "Gemüse nehmen", ZUTATEN.get("Gemüse"))));
		rezept.schritte.add(schrittRepo.save(new Schritt(rezept, 1, "in Pfanne zubereiten", ZUTATEN.get("Pfanne"))));
		rezept.schritte.add(schrittRepo.save(new Schritt(rezept, 2, "Optional hammermäßige Gewürze verwenden",
				ZUTATEN.get("hammermäßige Gewürze"))));
		rezeptRepo.save(rezept);
	}

	private void Nudeltoast() {
		Rezept rezept = rezeptRepo.save(new Rezept("Nudeltoast"));

		rezept.schritte.add(schrittRepo.save(new Schritt(rezept, 0, "Nudeln kochen", ZUTATEN.get("Nudeln"))));
		rezept.schritte.add(schrittRepo.save(new Schritt(rezept, 1, "Salami schneiden", ZUTATEN.get("Salami"))));
		rezept.schritte.add(schrittRepo.save(new Schritt(rezept, 2, "Schinken würfeln", ZUTATEN.get("Schinken"))));
		rezept.schritte.add(schrittRepo.save(new Schritt(rezept, 3, "Optional hammermäßige Gewürze verwenden",
				ZUTATEN.get("hammermäßige Gewürze"))));
		rezept.schritte.add(schrittRepo.save(new Schritt(rezept, 4, "Toast toasten", ZUTATEN.get("Toast"))));
		rezept.schritte.add(schrittRepo.save(new Schritt(rezept, 5, "Nudeln auf den Toast", ZUTATEN.get("Nudeln"))));

		rezeptRepo.save(rezept);
	}

	private void ReispfannePutenbrust() {
		Rezept rezept = rezeptRepo.save(new Rezept("Reispfanne mit Putenbrust"));

		rezept.schritte.add(schrittRepo.save(new Schritt(rezept, 0, "garen", ZUTATEN.get("langkorn Reis"))));
		rezept.schritte.add(schrittRepo.save(new Schritt(rezept, 1, "braten", ZUTATEN.get("Putenbrust"))));

		rezeptRepo.save(rezept);
	}
}
