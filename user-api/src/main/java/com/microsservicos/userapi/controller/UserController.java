package com.microsservicos.userapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
  public List<UserOutputDto> retrieveAllUsers() {
    return userService.retrieveAllUsers();
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public UserOutputDto retrieveUserById(@PathVariable("id") Long id) {
    return userService.retrieveUserById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UserOutputDto addNewUser(@Valid @RequestBody UserInputDto user) {
    return userService.registerNewUser(user);
  }

  @GetMapping("/cpf/{cpf}")
  @ResponseStatus(HttpStatus.OK)
  public UserOutputDto retrieveUserByCPF(@PathVariable("cpf") String cpf, @RequestHeader(name = "key", required = true) String key) {
    return userService.retrieveUserByCpf(cpf, key);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void removeUserById(@PathVariable("id") long id) {
    userService.removeUser(id);
  }

  @GetMapping("/search")
  @ResponseStatus(HttpStatus.OK)
  public List<UserOutputDto> retrieveUsersBySimilarName(@RequestParam(name = "name", required = true) String name) {
    return userService.retrieveUsersByName(name);
  }
}
