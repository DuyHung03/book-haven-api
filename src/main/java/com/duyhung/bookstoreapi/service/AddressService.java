package com.duyhung.bookstoreapi.service;

import com.duyhung.bookstoreapi.dto.AddressDto;
import com.duyhung.bookstoreapi.entity.Address;
import com.duyhung.bookstoreapi.entity.User;
import com.duyhung.bookstoreapi.repository.AddressRepository;
import com.duyhung.bookstoreapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    public String saveAddress(AddressDto addressDto, String userId) {
        Optional<Address> findAddress = addressRepository.findByUserUserId(userId);

        if (findAddress.isPresent()) {
            updateAddress(findAddress.get(), addressDto);
            return "Update address successfully";
        } else {
            createAddress(addressDto, userId);
            return "Create address successfully";
        }
    }

    private void updateAddress(Address address, AddressDto addressDto) {
        address.setPhone(addressDto.getPhone());
        address.setFullName(addressDto.getFullName());
        address.setProvinceId(addressDto.getProvinceId());
        address.setProvinceName(addressDto.getProvinceName());
        address.setDistrictId(addressDto.getDistrictId());
        address.setDistrictName(addressDto.getDistrictName());
        address.setWardCode(addressDto.getWardCode());
        address.setWardName(addressDto.getWardName());
        address.setHouseNumber(addressDto.getHouseNumber());
        addressRepository.save(address);
    }

    private void createAddress(AddressDto addressDto, String userId) {
        Address newAddress = modelMapper.map(addressDto, Address.class);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        newAddress.setUser(user);
        addressRepository.save(newAddress);
    }

    public AddressDto getAddress(String userId){
        Optional<Address> address = addressRepository.findByUserUserId(userId);
        return address.map(value -> modelMapper.map(value, AddressDto.class)).orElse(new AddressDto());
    }


}
