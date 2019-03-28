package com.mrn.mobileappws.service;

import com.mrn.mobileappws.shared.dto.AddressDto;

import java.util.List;

public interface AddressService {

    // get list of addresses
    List<AddressDto> getAddresses(String userId);
    AddressDto getAddress(String addressId);
}
