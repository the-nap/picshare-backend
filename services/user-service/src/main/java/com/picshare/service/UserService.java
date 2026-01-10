package com.picshare.service;

import org.springframework.stereotype.Service;

import com.picshare.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository userRepository;
}
