package io.javabrains.cardmarket.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class UserEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	private String name;
	private String surname;
	private String password;
	private Integer money;
	private String cards;
	
	public UserEntity() throws IOException {
		this.money = 5000;

		
	}
	
	public UserEntity(String Name, String Surname, String Password) throws IOException {
		this.name = Name;
		this.surname  = Surname;
		this.password = Password;
		this.money = 5000;
		
	}
	
	public void addCard(String id) {
		if(this.cards.contains(id)) {
			return;
		}
		this.cards += "/" + id;
	}
	
	public void removeCard(String id) {
		this.cards = this.cards.replace("/" + id, "");
		
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	public String getCards() {
		return cards;
	}

	public void setCards(String cards) {
		this.cards = cards;
	}
	
	public String toString() {
		return "Name : " + this.name + "\n" + " Surname : " + this.surname + "\n \n"; 
	}
	
	
	
}
