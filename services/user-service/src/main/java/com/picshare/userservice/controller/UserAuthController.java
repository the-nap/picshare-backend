package com.picshare.userservice.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.picshare.userservice.dto.CredentialDTO;
import com.picshare.userservice.dto.UserDTO;
import com.picshare.userservice.service.UserAuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users/auth")
@RequiredArgsConstructor
@Slf4j
public class UserAuthController {

  private final UserAuthService service;

  @GetMapping
  ResponseEntity<List<UserDTO>> getUsers(@RequestParam String key, @RequestParam String value){
    Optional<List<UserDTO>> result = Optional.of(
        Arrays.asList(
          switch (key) {
            case "id" -> service.getById(value);
            case "username" -> service.getByUsername(value);
            case "email" -> service.getByEmail(value);
            default -> null;
          }
        )
      );

    System.out.println(result);
    
    return ResponseEntity.ok(result.orElse(List.of()));

  }
  
  @GetMapping("/search")
  ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String key, @RequestParam String value, @RequestParam String first, @RequestParam String max){
    List<UserDTO> result =
      switch (key) {
        case "email" -> service.searchByEmail(value, Integer.valueOf(first), Integer.valueOf(max));
        case "username" -> service.searchByUsername(value, Integer.valueOf(first), Integer.valueOf(max));
          default -> null;
    };

    return ResponseEntity.ok(result);
  }

  @PostMapping("/{id}/credentials/verify")
  ResponseEntity<Void> verifyCredentials(@PathVariable String id, @RequestBody CredentialDTO credential){
    if(credential.getType().equals("password") && service.checkPassword(id, credential.getValue()))
      return ResponseEntity.noContent().build();
    else
      return ResponseEntity.badRequest().build();
  }
  
  @PostMapping("/{id}/credentials/update")
  ResponseEntity<Void> updateCredentials(@PathVariable String id, @RequestBody CredentialDTO credential){
    if(credential.getType().equals("password") && service.checkPassword(id, credential.getValue()))
      service.updateCredential(id, credential.getValue());
    else
      return ResponseEntity.badRequest().build();
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/create")
  ResponseEntity<UserDTO> createUser(@RequestBody UserDTO user){
    return ResponseEntity.ok(service.createUser(user));
  }

  @DeleteMapping("/delete")
  ResponseEntity<Void> deleteUser(@RequestParam String id){
    service.deleteUser(id);
    return ResponseEntity.noContent().build();
  }
}
