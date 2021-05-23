package br.com.townsq.ninjachallenge.model.entity;

import com.google.common.collect.Multimap;

import br.com.townsq.ninjachallenge.model.UserType;

public class User {
	Multimap<UserType, Integer> types;
	private String email;
	
	public User(String email, Multimap<UserType, Integer> types) {
		this.email = email;
		this.types = types;
	}
	
	public User() {
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Multimap<UserType, Integer> getTypes() {
		return types;
	}

	public void setTypes(Multimap<UserType, Integer> types) {
		this.types = types;
	}

}
