package com.picshare.userservice.controller;

import org.springframework.http.ResponseEntity;
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

@RestController
@RequestMapping("/users/auth")
@RequiredArgsConstructor
public class UserAuthController {

  private final UserAuthService service;

  @GetMapping
  ResponseEntity<UserDTO> searchUsers(@RequestParam String key, @RequestParam String value){
    UserDTO result = switch (key) {
      case "id" -> service.getById(value);
      case "username" -> service.getByUsername(value);
      case "email" -> service.getByEmail(value);
      default -> null;
    };
    return result != null 
      ? ResponseEntity.ok(result)
      : ResponseEntity.notFound().build();
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
}
