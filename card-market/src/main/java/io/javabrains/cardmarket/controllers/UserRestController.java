package io.javabrains.cardmarket.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.javabrains.cardmarket.models.CardEntity;
import io.javabrains.cardmarket.models.UserEntity;
import io.javabrains.cardmarket.utils.Tools;

@RestController
public class UserRestController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CardService cardService;
	
	/**
	 * Get user information from name
	 * @param name
	 * @return UserEntity
	 */
	@GetMapping("UserService/user/{name}")
	public UserEntity getUser(@PathVariable String name) {
		return userService.getUserByName(name);
	}
	
	/**
	 * Return information of user if name and password match, otherwise return void string
	 * @param name
	 * @param pswd
	 * @return String 
	 */
	@GetMapping("UserService/user/{name}/{pswd}")
	public String checktUser(@PathVariable String name, @PathVariable String pswd){
		UserEntity user = userService.getUserByName(name);
		if(user == null) {
			return "";
		}
		else if(!user.getPassword().contentEquals(pswd)) {
			return "";
		}
		return Tools.toJsonString(user);
	}
	
	/**
	 * Get all cards that user owns
	 * @param id of the user
	 * @return String the cards of the user 
	 */
	@GetMapping("UserService/user/cards/{id}")
	public String getCards(@PathVariable String id) {
		return userService.getUserById(id).getCards();
		
	}
	
	/**
	 * Get all cards that user does not have
	 * @param id
	 * @return String all the card that user can buy
	 * @throws IOException
	 */
	@GetMapping("UserService/user/BuyCards/{id}")
	public String getBuyCards(@PathVariable String id) throws IOException {
		String userCards = userService.getUserById(id).getCards();
		String buyCards = "";
		int cardNumber = this.getCardNumber();
		String[] userCardsArray = userCards.split("/");
		boolean found = false;
		for (int i = 1; i < cardNumber + 1;  i++) {
			found = false;
			for (int j = 0; j < userCardsArray.length; j++) {
				if(userCardsArray[j].contentEquals(String.valueOf(i))) {
					found = true;
				}
			}
			if(!found) {
				buyCards += String.valueOf(i) + "/";
			}
		}
		return this.CardStringConversion(buyCards);
	}
	
	@GetMapping("UserService/Buy/{name}/{imgId}/{price}")
	public boolean buyCard(@PathVariable String name, @PathVariable String imgId, @PathVariable String price) {
		UserEntity user = userService.getUserByName(name);
		if(user.getMoney() < Integer.parseUnsignedInt(price)) {
			return false;
		}
		user.addCard(imgId);
		user.setMoney(user.getMoney()- Integer.parseInt(price));
		userService.updateUser(user);
		return true;
		
		
	}
	
	@GetMapping("UserService/Sell/{name}/{imgId}/{price}")
	public boolean sellCard(@PathVariable String name, @PathVariable String imgId, @PathVariable String price) {
		UserEntity user = userService.getUserByName(name);
		user.removeCard(imgId);
		user.setMoney(user.getMoney() + Integer.parseInt(price));
		return true;
	}
	
	
	/**
	 * Add user in the database
	 * @param user
	 * @return boolean true if no errors
	 * @throws IOException
	 */
	@PostMapping(value="UserService/addUser", consumes=MediaType.APPLICATION_JSON_VALUE)
	public boolean addUser(@RequestBody UserEntity user) throws IOException {
		int cardnumber = this.getCardNumber();
		System.out.println(cardnumber);
		Random r = new Random();
		Integer randomInt;
		List<Integer> intlist = new ArrayList<Integer>();
		String cards = "";
		for(int i = 0; i < 5 ; i++) {
			randomInt = r.nextInt(cardnumber) + 1;
			while(intlist.contains(randomInt)) {
				randomInt = r.nextInt(cardnumber) + 1;
			}
			intlist.add(randomInt);
			cards += randomInt.toString() + "/";
		}
		user.setCards(this.CardStringConversion(cards));
		return userService.addUser(user);
	}
	
	/**
	 * Get Number of Cards available, call to the CardService
	 * @return int number of cards
	 * @throws IOException
	 */
	private int getCardNumber() throws IOException {
		URL obj = new URL("http://localhost:8080/CardService/card/number");
        HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(httpURLConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                 while ((inputLine = in.readLine()) != null) {
                   response.append(inputLine);
                 } in .close();
        return Integer.parseInt(response.toString());

	}
	/**
	 * Erase the last character of the string
	 * @param String
	 * @return String
	 */
	private String CardStringConversion(String str) {
	        return str = str.substring(0, str.length() - 1);
	}

}
