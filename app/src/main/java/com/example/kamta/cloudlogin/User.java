package com.example.kamta.cloudlogin;

public class User {

	String name,username,password,email;
	int age;

	public  User(){}
	public User(String name, String username, int age, String email, String password) {
		this.name=name;
		this.username=username;
		this.age=age;
		this.password=password;
		this.email=email;
	}
	
	public User(String username, String password){
		this("",username, -1, "" , password);
	}
}
