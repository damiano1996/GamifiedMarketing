package com.derin.damiano.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the badword database table.
 * 
 */
@Entity
@Table(name = "badword", schema = "db_gamified_marketing")
@NamedQuery(name = "Badword.findAll", query = "SELECT b FROM Badword b")
public class Badword implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String word;

	public Badword() {
	}

	public Badword(String word) {
		this.word = word;
	}

	public String getWord() {
		return this.word;
	}

	public void setWord(String word) {
		this.word = word;
	}

}