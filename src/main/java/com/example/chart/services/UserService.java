package com.example.chart.services;

import com.example.chart.dto.address.AddressDTO;
import com.example.chart.dto.UserDTO;
import org.springframework.data.domain.Page;

import java.util.List;

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
