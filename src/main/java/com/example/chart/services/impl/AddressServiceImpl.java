package com.example.chart.services.impl;

import com.example.chart.dto.AddressDTO;
import com.example.chart.dto.AddressResponseDTO;
import com.example.chart.models.Address;
import com.example.chart.models.User;
import com.example.chart.repository.AddressRepository;
import com.example.chart.repository.UserRepository;
import com.example.chart.services.AddressService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public AddressDTO createAddress(AddressDTO addressDTO) {
        User user = userRepository.findById(addressDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Address address = new Address();
        address.setStreet(addressDTO.getStreet());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setZipCode(addressDTO.getZipCode());
        address.setCountry(addressDTO.getCountry());
        address.setPhoneNumber(addressDTO.getPhoneNumber());
        address.setUserId(user.getId());

        // If this is the first address, set it as default
        if (addressRepository.countByUserId(addressDTO.getUserId()) == 0) {
            address.setDefault(true);
        }

        Address savedAddress = addressRepository.save(address);
        return convertToDTO(savedAddress);
    }

    @Override
    @Transactional
    public AddressDTO updateAddress(Long id, AddressDTO addressDTO) {
        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));

        // Update fields
        existingAddress.setStreet(addressDTO.getStreet());
        existingAddress.setCity(addressDTO.getCity());
        existingAddress.setState(addressDTO.getState());
        existingAddress.setZipCode(addressDTO.getZipCode());
        existingAddress.setCountry(addressDTO.getCountry());
        existingAddress.setPhoneNumber(addressDTO.getPhoneNumber());

        Address updatedAddress = addressRepository.save(existingAddress);
        return convertToDTO(updatedAddress);
    }

    @Override
    @Transactional
    public void deleteAddress(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));

        // If this was the default address, set another address as default
        if (address.isDefault()) {
            addressRepository.findByUserIdAndIsDefaultFalse(address.getUserId())
                    .stream()
                    .findFirst()
                    .ifPresent(newDefault -> {
                        newDefault.setDefault(true);
                        addressRepository.save(newDefault);
                    });
        }

        addressRepository.delete(address);
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponseDTO getAddress(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));
        User user = userRepository.findById(address.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found"));

        AddressResponseDTO addressResponseDTO = new AddressResponseDTO();
        addressResponseDTO.setId(address.getId());
        addressResponseDTO.setStreet(address.getStreet());
        addressResponseDTO.setCity(address.getCity());
        addressResponseDTO.setState(address.getState());
        addressResponseDTO.setCountry(address.getCountry());
        addressResponseDTO.setPhoneNumber(address.getPhoneNumber());
        addressResponseDTO.setDefault(address.isDefault());
        addressResponseDTO.setUserId(address.getUserId());
        addressResponseDTO.setZipCode(address.getZipCode());
        addressResponseDTO.setFullName(user.getFirstName() + " " + user.getLastName());
        addressResponseDTO.setEmail(user.getEmail());
        return addressResponseDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponseDTO> getUserAddresses(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        List<Address> addresses = addressRepository.findByUserId(userId);
        List<AddressResponseDTO> addressResponseDTOS = new ArrayList<>();
        for (Address address : addresses) {
            AddressResponseDTO addressResponseDTO = new AddressResponseDTO();
            addressResponseDTO.setId(address.getId());
            addressResponseDTO.setStreet(address.getStreet());
            addressResponseDTO.setCity(address.getCity());
            addressResponseDTO.setState(address.getState());
            addressResponseDTO.setCountry(address.getCountry());
            addressResponseDTO.setPhoneNumber(address.getPhoneNumber());
            addressResponseDTO.setDefault(address.isDefault());
            addressResponseDTO.setUserId(address.getUserId());
            addressResponseDTO.setZipCode(address.getZipCode());
            addressResponseDTO.setFullName(user.getFirstName() + " " + user.getLastName());
            addressResponseDTO.setEmail(user.getEmail());
            addressResponseDTOS.add(addressResponseDTO);
        }
        return addressResponseDTOS;
    }

    @Override
    @Transactional
    public AddressDTO setDefaultAddress(Long userId, Long addressId) {
        // Unset current default address
        addressRepository.findByUserIdAndIsDefaultTrue(userId)
                .ifPresent(currentDefault -> {
                    currentDefault.setDefault(false);
                    addressRepository.save(currentDefault);
                });

        // Set new default address
        Address newDefault = addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));

        if (!newDefault.getUserId().equals(userId)) {
            throw new IllegalStateException("Address does not belong to user");
        }

        newDefault.setDefault(true);
        Address savedAddress = addressRepository.save(newDefault);
        return convertToDTO(savedAddress);
    }

    @Override
    @Transactional(readOnly = true)
    public AddressDTO getDefaultAddress(Long userId) {
        Address defaultAddress = addressRepository.findByUserIdAndIsDefaultTrue(userId)
                .orElseThrow(() -> new EntityNotFoundException("No default address found"));
        return convertToDTO(defaultAddress);
    }

    private AddressDTO convertToDTO(Address address) {
        AddressDTO dto = new AddressDTO();
        dto.setStreet(address.getStreet());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setZipCode(address.getZipCode());
        dto.setCountry(address.getCountry());
        dto.setPhoneNumber(address.getPhoneNumber());
        dto.setUserId(address.getUserId());
        dto.setDefault(address.isDefault());
        return dto;
    }
} 