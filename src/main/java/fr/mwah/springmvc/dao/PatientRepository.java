package fr.mwah.springmvc.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import fr.mwah.springmvc.entities.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long>{
	public Page<Patient> findByNameContains(String mc, Pageable p);
}
