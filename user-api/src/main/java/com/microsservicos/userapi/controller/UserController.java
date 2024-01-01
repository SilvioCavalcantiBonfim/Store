package com.microsservicos.userapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.microsservicos.dto.UserInputDto;
import com.microsservicos.dto.UserOutputDto;
import com.microsservicos.userapi.service.UserService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/user")
public class UserController {

  private final UserService userService;
  
  public UserController(UserService userService){
    this.userService = userService;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<UserOutputDto> getAllUsers() {
    return userService.getAllUsers();
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public UserOutputDto getUserById(@PathVariable("id") Long id) {
    return userService.findUserById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UserOutputDto createUser(@Valid @RequestBody UserInputDto user) {
    return userService.createUser(user);
  }

  @GetMapping("/cpf/{cpf}")
  @ResponseStatus(HttpStatus.OK)
  public UserOutputDto getUserByCPF(@PathVariable("cpf") String cpf, @RequestParam(name = "key", required = true) String key) {
    return userService.findUserByCpf(cpf, key);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUserById(@PathVariable("id") long id) {
    userService.deleteUser(id);
  }

  @GetMapping("/search")
  @ResponseStatus(HttpStatus.OK)
  public List<UserOutputDto> getUsersBySimilarName(@RequestParam(name = "name", required = true) String name) {
    return userService.findUsersByName(name);
  }
}
