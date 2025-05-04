package com.example.chart.services;

import com.example.chart.dto.AddressDTO;
import com.example.chart.dto.UserDTO;
import com.example.chart.exception.ResourceNotFoundException;
import com.example.chart.models.Address;
import com.example.chart.models.User;
import com.example.chart.repository.AddressRepository;
import com.example.chart.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

public interface UserService {

    // User profile operations
    UserDTO getCurrentUser();
    UserDTO updateCurrentUser(UserDTO userDTO);
    void updatePassword(String currentPassword, String newPassword);

    // Address operations
    AddressDTO addAddress(String email, AddressDTO addressDTO);
    List<AddressDTO> getAddresses(String email);
    void deleteAddress(Long id);

    // Admin operations
    Page<UserDTO> getAllUsers(int page, int size, String sortField, String sortDirection);
    UserDTO getUserById(Long id);
    UserDTO updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
    UserDTO updateUserStatus(Long id, boolean active);
}
