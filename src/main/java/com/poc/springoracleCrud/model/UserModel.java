package com.poc.springoracleCrud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class UserModel {
	
	@Id
	@GenericGenerator(name = "user_id", strategy = "com.poc.springoracleCrud.config.UserIDGenerator")
	@GeneratedValue(generator = "user_id")
	@Column(name = "user_id")
	private String userid;
	
	@Column(name = "user_name")
	private String username;
	
	@Column(name = "emailid")
	private String emailid;

	@Override
	public String toString() {
		return "[userid=" + userid + ", username=" + username + ", emailid=" + emailid + "]";
	}
	
	
}
