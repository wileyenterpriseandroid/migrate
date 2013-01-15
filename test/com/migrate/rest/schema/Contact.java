package com.migrate.rest.schema;


import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;

import java.util.Date;

@JsonSerializableSchema(id = Contact.SCHEMA_ID)
public class Contact {
    public static final String SCHEMA_ID = "com.migrate.rest.schema.Contact";

	private String firstname;
	private String lastname;
	private String email;
	private int age;
	private Date birthDate;

    public String getFirstname() {
		return firstname;
	}

    public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

    public String getLastname() {
		return lastname;
	}

    public void setLastname(String lastname) {
		this.lastname = lastname;
	}

    public String getEmail() {
		return email;
	}

    public void setEmail(String email) {
		this.email = email;
	}

    public int getAge() {
		return age;
	}

    public void setAge(int age) {
		this.age = age;
	}

    public Date getBirthDate() {
		return birthDate;
	}

    public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
}
