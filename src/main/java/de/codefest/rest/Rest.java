package de.codefest.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import de.codefest.data.Rezept;
import de.codefest.data.RezeptRepository;
import de.codefest.data.Schritt;
import de.codefest.data.SchrittRepository;
import de.codefest.data.Zutat;
import de.codefest.data.ZutatenRepository;

@RestController
public class Rest {

	@Autowired
	RezeptRepository rezeptRepo;

	@Autowired
	SchrittRepository schrittRepo;

	@Autowired
	ZutatenRepository zutatenRepo;

	static List<Zutat> cart = new ArrayList<>();

	@RequestMapping(path = "rezepte", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public HttpEntity<?> rezepte() {

		ArrayList<Rezept> newArrayList = Lists.newArrayList(rezeptRepo.findAll());

		ResponseEntity<ArrayList<Rezept>> ok = ResponseEntity.ok(newArrayList);
		return ok;
	}

	@RequestMapping(path = "newRezept", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public HttpEntity<Long> newRezept() {

		// entferne alle r ohne schritte
		Iterable<Rezept> findAll = rezeptRepo.findAll();
		for (Rezept r : findAll) {
			if (r.getSchritte().isEmpty()) {
				rezeptRepo.delete(r.id);
			}
		}

		Rezept r = rezeptRepo.save(new Rezept("Neues Unbenanntes Rezept"));

		return ResponseEntity.ok(r.id);
	}

	@RequestMapping(path = "changeRezeptName", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public HttpEntity<Long> changeRezeptName(@RequestParam(value = "id") Long id,
			@RequestParam(value = "name") String name) {

		Rezept r = rezeptRepo.findOne(id);
		r.name = name;
		r = rezeptRepo.save(r);

		return ResponseEntity.ok(r.id);
	}

	@RequestMapping(path = "changeRezeptBeschreibung", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public HttpEntity<Long> changeRezeptBeschreibung(@RequestParam(value = "id") Long id,
			@RequestParam(value = "name") String beschreibung) {

		Rezept r = rezeptRepo.findOne(id);
		r.beschreibung = beschreibung;
		r = rezeptRepo.save(r);

		return ResponseEntity.ok(r.id);
	}

	@RequestMapping(path = "getRezept", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public HttpEntity<?> getRezept(@RequestParam(value = "id") Long id) {

		Rezept r = rezeptRepo.findOne(id);

		return ResponseEntity.ok(r);
	}

	@RequestMapping(path = "neuerSchritt", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public HttpEntity<Long> neuerSchritt(@RequestParam(value = "rid") Long rid, @RequestParam(value = "zid") Long zid,
			@RequestParam(value = "beschreibung") String beschreibung, @RequestParam(value = "zeit") Integer zeit,
			@RequestParam(value = "index") Integer index) {

		if (zid == null) {
			return ResponseEntity.ok(rid);
		}

		Rezept r = rezeptRepo.findOne(rid);
		Zutat z = zutatenRepo.findOne(zid);
		Schritt s = new Schritt(r, index == null ? 0 : index, beschreibung, z);
		s.zeit = zeit;
		s = schrittRepo.save(s);

		r.schritte.add(s);
		rezeptRepo.save(r);

		return ResponseEntity.ok(r.id);
	}

	@RequestMapping(path = "zutaten", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public HttpEntity<?> zutaten() {

		ArrayList<Zutat> newArrayList = Lists.newArrayList(zutatenRepo.findAll());

		return ResponseEntity.ok(newArrayList);
	}

	@RequestMapping(path = "shopping", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public HttpEntity<?> shopping() {

		ArrayList<Zutat> newArrayList = Lists.newArrayList(zutatenRepo.findAll());

		String string = zutatenToHtml(newArrayList, true);

		return ResponseEntity.ok(string);
	}

	private String zutatenToHtml(List<Zutat> newArrayList, boolean withAddToCart) {
		StringBuilder sb = new StringBuilder();
		for (Zutat zutat : newArrayList) {
			sb.append(zutat.toHTML(withAddToCart));
		}
		return sb.toString();
	}

	@RequestMapping(path = "addToCart", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public HttpEntity<Integer> addToCart(@RequestParam(value = "id") long id) {

		Zutat z = zutatenRepo.findOne(id);
		cart.add(z);

		// Rezept r = new Rezept("xxxxx", "yyyyy");
		return ResponseEntity.ok(cart.size());
	}

	@RequestMapping(path = "rezepteSuche", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public HttpEntity<?> addToCart(@RequestParam(value = "query") String query) {

		if (query == null || query.trim().isEmpty()) {
			return rezepte();
		}

		List<Rezept> z = Lists.newArrayList(rezeptRepo.findAll());

		Iterable<String> split = Splitter.on(Pattern.compile("\\s+")).split(query.toLowerCase());

		Stream<Rezept> theStream = z.stream();
		for (String string : split) {
			theStream = theStream.filter(r -> r.getZutatenString().contains(string));
		}

		List<Rezept> collect = theStream.collect(Collectors.toList());
		return ResponseEntity.ok(collect);
	}

	@RequestMapping(path = "cart", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public HttpEntity<?> cart() {

		// String string = zutatenToHtml(cart, false);
		return ResponseEntity.ok(cart);
	}

	@RequestMapping(path = "clearCart", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public HttpEntity<?> clearCart() {
		cart.clear();
		return ResponseEntity.ok().build();
	}

	@RequestMapping(path = "getRandomCart", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public HttpEntity<Integer> getRandomCart(@RequestParam(value = "count") int count) {

		ArrayList<Zutat> z = Lists.newArrayList(zutatenRepo.findAll());
		Collections.shuffle(z);

		int min = Math.min(count, z.size());
		List<Zutat> collect = z.stream().filter(zz -> zz.scrabblePoint != 0).limit(min).collect(Collectors.toList());

		cart.clear();
		cart.addAll(collect);

		int sum = cart.stream().mapToInt(zu -> zu.scrabblePoint).sum();

		return ResponseEntity.ok(sum);
	}

	@RequestMapping(path = "lol3", method = RequestMethod.POST)
	public HttpEntity<String> lol3(@RequestBody Rezept in) {

		// Rezept r = new Rezept("xxxxx", "yyyyy");
		return ResponseEntity.ok("11111");
	}

}
