package de.codefest.data;

import org.springframework.data.repository.CrudRepository;

public interface ZutatenRepository extends CrudRepository<Zutat, Long> {

	// Rezept findByLastName(String lastName);

	// List<Rezept> findByLastName(String lastName);

	public Zutat findByName(String name);

	public Zutat findById(long id);

}