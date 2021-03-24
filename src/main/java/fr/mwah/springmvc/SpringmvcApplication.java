package fr.mwah.springmvc;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import fr.mwah.springmvc.dao.PatientRepository;
import fr.mwah.springmvc.entities.Patient;
//"implements CommandLineRunner"cpermet d'insérer quelques patients dans la BdD
@SpringBootApplication
public class SpringmvcApplication implements CommandLineRunner{

	@Autowired
	private PatientRepository patientRepository;
	public static void main(String[] args) {
		//Spring fait le démarrage
		SpringApplication.run(SpringmvcApplication.class, args);
		//puis applique la méthode run()
	}

	@Override
	public void run(String... args) throws Exception {
		patientRepository.save(new Patient(null, "Patrick", new Date(),false,8));
		patientRepository.save(new Patient(null, "Jean", new Date(),true,6));
		patientRepository.save(new Patient(null, "Michel", new Date(),true,15));
	
		patientRepository.findAll().forEach(p-> System.out.println(p.getName()));
	}

}
