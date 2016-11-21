package de.codefest.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

@Entity
public class Rezept {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	public String name;

	public Float bewertung; // zwischen 1 ..5 Sterne

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY) // , mappedBy = "rezept")
	public Set<Schritt> schritte = new HashSet<>();

	public String beschreibung;

	public Rezept() {
	}

	@JsonProperty
	public List<Schritt> getSchritte() {
		ArrayList<Schritt> newArrayList = Lists.newArrayList(schritte);
		Collections.sort(newArrayList, (a, b) -> Integer.compare(a.index, b.index));

		return newArrayList;
	}

	public Integer getTotalTime() {
		Map<Integer, Schritt> maxes = new HashMap<Integer, Schritt>();

		schritte.stream().forEach(s -> {
			if (maxes.containsKey(s.index)) {
				Schritt ss = maxes.get(s.index);
				if (ss.zeit < s.zeit) {
					maxes.put(s.index, s);
				}
			} else {
				maxes.put(s.index, s);
			}
		});

		return maxes.values().stream().mapToInt(s -> s.zeit).sum();
	}

	public Rezept(String name) {
		this.name = name;
	}

	public List<String> getZutaten() {
		return getSchritte().stream().map(s -> s.zutat.name).distinct().collect(Collectors.toList());
	}

	public String getZutatenString() {
		return getZutaten().stream().map(z -> z.toLowerCase()).collect(Collectors.joining());
	}
}