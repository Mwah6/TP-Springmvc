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

import fr.mwah.springmvc.dao.PatientRepository;
import fr.mwah.springmvc.entities.Patient;

@Controller
public class PatientController {
	
	@Autowired
	private PatientRepository patientRepository;

	@GetMapping(path = "/index")
	public String index() {
		return "index";

	}
	//Il existe aussi @RequestMapping => annotation générique
	// @GetMapping est un @RequestMapping avec la méthode dèjà égale à Get

	@GetMapping(path = "/patients")
	public String patient(Model model,
			@RequestParam( name="page", defaultValue = "0")int page, //name="page" est optionnel (pas utile ici)
			@RequestParam( name="size", defaultValue = "5")int size,
			@RequestParam( name="keyword", defaultValue = "")String mc) {	

		Page<Patient> pagePatients = patientRepository
				.findByNameContains(mc, PageRequest.of(page, size));
		model.addAttribute("patients", pagePatients.getContent());
		model.addAttribute("pages", new int[pagePatients.getTotalPages()] );
		model.addAttribute("currentPage", page);
		model.addAttribute("keyword", mc);
		model.addAttribute("size", size);
		return "patients";

	}
	//redirection
	@GetMapping(path = "/deletePatient")
	public String delete(Long id, String keyword, int page, int size) {
		patientRepository.deleteById(id);
		return "redirect:/patients?keyword="+keyword+"&page="+page+"&size="+size;
	}
	//sans redirection mais il vfaut préférer la 1ère solution
	@GetMapping(path = "/deletePatient2")
	public String delete(Long id, String keyword, int page, int size, Model model) {
		patientRepository.deleteById(id);
		return patient(model, page, size, keyword);
	}
	@GetMapping(path = "/formPatient")
	public String formPatient(Model model) {
		model.addAttribute("patient", new Patient());
		model.addAttribute("mode", "new");
		return "formPatient";
//		return "formPatient?mode=new"; -> Ne marche pas !
	}
	@PostMapping(path = "/savePatient")
	public String savePatient(Model model, @Valid Patient patient, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) return "formPatient";
		patientRepository.save(patient);
		model.addAttribute("patient", patient);
		return "confirmation";
	}
	@GetMapping(path = "/editPatient")
	public String editPatient(Model model, Long id) {
		Patient p = patientRepository.findById(id).get();
		model.addAttribute("patient", p);
		model.addAttribute("mode", "edit");
		return "formPatient";
//		return "formPatient?mode=edit"; -> Ne marche pas !
		
	}
//	@GetMapping(path="/listPatients")
//	@ResponseBody //->renvoie dans le corps de la réponse ce qui suit, en Json par défaut
//	public List<Patient> listPatients(){
//		return patientRepository.findAll();
//		
//	}
//	
//	@GetMapping(path="/patients/{id}")
//	@ResponseBody //->Cela s'utilise dans un "@Controller"
//	public Patient patientById(@PathVariable Long id){
//		return patientRepository.findById(id).get();
//		
//	}
}
