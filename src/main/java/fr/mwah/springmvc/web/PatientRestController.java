package fr.mwah.springmvc.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fr.mwah.springmvc.dao.PatientRepository;
import fr.mwah.springmvc.entities.Patient;

@RestController
public class PatientRestController {
	@Autowired
	private PatientRepository patientRepository;

	@GetMapping(path="/patients/listPatients")
	public List<Patient> listPatients(){
		return patientRepository.findAll();
		
	}
	
	@GetMapping(path="/patients/patients/{id}")
	public Patient patientById(@PathVariable Long id){
		return patientRepository.findById(id).get();
		
	}
}
