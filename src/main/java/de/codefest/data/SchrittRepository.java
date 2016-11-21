package de.codefest.data;

import org.springframework.data.repository.CrudRepository;

public interface SchrittRepository extends CrudRepository<Schritt, Long> {

	// List<Rezept> findByLastName(String lastName);
}