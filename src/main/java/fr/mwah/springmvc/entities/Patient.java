package fr.mwah.springmvc.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
// @Data fait appel à lombok. Il faut regarder dans Windows -> Show View -> Outline si les getters et les setters ont été générés
// Sinon, aller sur le site Lombok Project pour télécharger le jar
// Executer le .jar avec Eclipse éteind
// Permet àEclipse de compiler le projet avec Lombok
@Data @AllArgsConstructor @NoArgsConstructor
public class Patient {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	@Size(min =3, max = 30)
	private String name;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull
	private Date dateNaissance;
	private boolean malade;
	@DecimalMin("0") @DecimalMax("20")
	private int score;
}
