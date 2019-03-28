package com.mrn.mobileappws.service.impl;

import com.mrn.mobileappws.io.entity.AddressEntity;
import com.mrn.mobileappws.io.entity.UserEntity;
import com.mrn.mobileappws.io.repositories.AddressRepository;
import com.mrn.mobileappws.io.repositories.UserRepository;
import com.mrn.mobileappws.service.AddressService;
import com.mrn.mobileappws.shared.dto.AddressDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public AddressServiceImpl(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public List<AddressDto> getAddresses(String userId) {

        List<AddressDto> returnValue = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();

        // check if user exists
        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null) return returnValue;

        Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);
        for(AddressEntity addressEntity : addresses) {
            returnValue.add(modelMapper.map(addressEntity, AddressDto.class));
        }

        return returnValue;

    }

    @Override
    public AddressDto getAddress(String addressId) {

        AddressDto returnValue = null;

        AddressEntity addressEntity = addressRepository.findByAddressId(addressId);

        // map the address entity into addressdto class properties
        if(addressEntity != null)
            returnValue = new ModelMapper().map(addressEntity, AddressDto.class);

        // return address dto obj
        return returnValue;
    }
}
