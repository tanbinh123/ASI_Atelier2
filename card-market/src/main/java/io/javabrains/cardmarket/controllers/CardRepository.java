package io.javabrains.cardmarket.controllers;


import org.springframework.data.repository.CrudRepository;


import io.javabrains.cardmarket.models.CardEntity;

public interface CardRepository extends CrudRepository<CardEntity, Integer>{
	
	public CardEntity findByName(String Name);

	public CardEntity findById(int id);
	
	public long count();

}