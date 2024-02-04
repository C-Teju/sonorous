package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.entities.Users;
import com.example.demo.repository.UsersRepository;
@Service
public class UsersServiceImplimentation implements UsersService {
	@Autowired
	UsersRepository repo;
	@Override
	public String addUser(Users user) {
		repo.save(user);
		return "User data added successfully !";
	}
	@Override
	public boolean emailExists(String email) {
		if(repo.findByEmail(email)==null) {
			return false;
		}else {
			return true;
		}
		
	}
	@Override
	public boolean validateUser(String email, String password) {
		Users user=repo.findByEmail(email);
		if(user!=null) {
			String db_password=user.getPassword();
			return password.equals(db_password);
		}
				
		else {
			return false;
		}
		
	}
	@Override
	public String getRole(String email) {
		Users user=repo.findByEmail(email);
		return user.getRole();
	}
	@Override
	public Users getUser(String email) {
		return repo.findByEmail(email);
		
	}
	@Override
	public void updateUser(Users user) {
		repo.save(user);		
	}
	@Override
	public Users getUserByEmailAndPassword(String email, String password) {
		
		return repo.findByEmailAndPassword(email, password);
	}

}
