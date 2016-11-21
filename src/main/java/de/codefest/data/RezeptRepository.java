package de.codefest.data;

import org.springframework.data.repository.CrudRepository;

public interface RezeptRepository extends CrudRepository<Rezept, Long> {

	// Rezept findByLastName(String lastName);

	// List<Rezept> findByLastName(String lastName);
}