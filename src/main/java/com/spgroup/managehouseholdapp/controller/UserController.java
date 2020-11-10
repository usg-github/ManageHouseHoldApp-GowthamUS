package com.spgroup.managehouseholdapp.controller;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spgroup.managehouseholdapp.dao.UserTransDAO;
import com.spgroup.managehouseholdapp.exceptions.UserNotFoundException;
import com.spgroup.managehouseholdapp.model.UserTrans;
import com.spgroup.managehouseholdapp.model.ui.User;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserTransDAO userTransDAO;

	Function<UserTrans, User> externalToUser = new Function<UserTrans, User>() {

		public User apply(UserTrans userTrans) {
			User user = new User();
			user.setFullName(userTrans.getFullName());
			user.setUsername(userTrans.getUsername());
			user.setEmail(userTrans.getEmail());
			user.setPassword(userTrans.getPassword());
			return user;
		}
	};

    // get all users
    @GetMapping("/valid")
    public void isValidUser(@RequestBody User user) {
    	UserTrans userTrans = new UserTrans();                         
    	userTrans.setUsername(user.getUsername());                          
    	userTrans.setPassword(user.getPassword());
    	Example<UserTrans> example = Example.of(userTrans);
    	userTransDAO.findOne(example).orElseThrow(()-> new UserNotFoundException("User not found with username :" + user.getUsername()));
    }

    // get user by id
    @GetMapping("/{id}")
    public User getUserById(@PathVariable(value = "id") long userId) {
        UserTrans userTrans = this.userTransDAO.findById(userId).orElseThrow(()-> new UserNotFoundException("User not found with id :" + userId));
        return externalToUser.apply(userTrans);
    }
/*
    // create user
    @PostMapping
    public void createUser(@RequestBody UserTrans user) {
        this.userTransDAO.save(user);
    }

    // update user
    @PutMapping("/{id}")
    public UserTrans updateUser(@RequestBody User user, @PathVariable("id") long userId) {
    	UserTrans existingUser = this.userTransDAO.findById(userId).orElseThrow(()-> new UserNotFoundException("User not found with id :" + userId));
        existingUser.setFullName(user.getFullName());
        existingUser.setEmail(user.getEmail());
        return this.userTransDAO.save(existingUser);
    }

*/

}
