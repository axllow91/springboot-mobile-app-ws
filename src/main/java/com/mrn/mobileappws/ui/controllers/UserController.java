package com.mrn.mobileappws.ui.controllers;


import com.mrn.mobileappws.exceptions.UserServiceException;
import com.mrn.mobileappws.service.AddressService;
import com.mrn.mobileappws.service.UserService;
import com.mrn.mobileappws.shared.dto.AddressDto;
import com.mrn.mobileappws.shared.dto.UserDto;
import com.mrn.mobileappws.ui.model.request.PasswordResetModel;
import com.mrn.mobileappws.ui.model.request.PasswordResetRequestModel;
import com.mrn.mobileappws.ui.model.request.UserDetailsRequestModel;
import com.mrn.mobileappws.ui.model.response.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * The new default http url is defined in application.properties source file
 * Every path will need to include the default url which you will be redirect is
 * http://localhost:8080/mobile-app-ws/users
 **/
@RestController
@RequestMapping("/users") // http://localhost:8080/users replaced with http://localhost:8080/mobile-app-ws/users
public class UserController {

    private final UserService userService;
    private final AddressService addressService;

    public UserController(UserService userService, AddressService addressService) {
        this.userService = userService;
        this.addressService = addressService;
    }


    @GetMapping(path = "/{id}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    // produces information in xml and json representation
    public UserRest getUser(@PathVariable String id) {

        UserRest returnValue = new UserRest();
        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto, returnValue);

        return returnValue;

    }

    // creating a user (post request) create user endpoint can consumes and produces information
    // in both format XML and JSON
    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {

        // UserDto userDto = new UserDto();
        // BeanUtils.copyProperties(userDetails, userDto);
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);
        UserRest returnValue = modelMapper.map(createdUser, UserRest.class);

        return returnValue;
    }

    @PutMapping(path = "/{id}",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public UserRest updateUser(@PathVariable String id,
                               @RequestBody UserDetailsRequestModel userDetails) {

        UserRest returnValue = new UserRest();

        if (userDetails.getFirstName().isEmpty())
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        UserDto userDto = new UserDto();
        // copy the existent values into the userDto object
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto updateUser = userService.updateUser(id, userDto);
        BeanUtils.copyProperties(updateUser, returnValue);

        return returnValue;
    }

    @DeleteMapping(path = "/{id}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public OperationStatusModel deleteUser(@PathVariable String id) {

        OperationStatusModel returnValue = new OperationStatusModel();
        // operation on going is DELETE
        returnValue.setOperationName(RequestOperationName.DELETE.name());

        userService.deleteUser(id);

        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

        return returnValue;
    }

    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "limit", defaultValue = "25") int limit) {

        List<UserRest> returnValue = new ArrayList<>();

        List<UserDto> users = userService.getUsers(page, limit);

        // Convert user dto object into a user rest object
        for (UserDto userDto : users) {
            UserRest userModel = new UserRest();
            BeanUtils.copyProperties(userDto, userModel);
            returnValue.add(userModel);
        }

        return returnValue;
    }


    // http://localhost:8080/mobile-app-ws/users/{userId}/addresses/{addressId}
    @GetMapping(path = "/{id}/addresses",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})

    // produces information in xml and json representation
    // General helper to easily create a wrapper for a collection of entities.
    public Resources<AddressesRest> getUserAddresses(@PathVariable String id) {

        List<AddressesRest> addressesRestsListModel = new ArrayList<>();


        List<AddressDto> addressesDto = addressService.getAddresses(id);
        if (addressesDto != null && !addressesDto.isEmpty()) {

            Type listType = new TypeToken<List<AddressesRest>>() {
            }.getType();
            addressesRestsListModel = new ModelMapper().map(addressesDto, listType);


            // add links to each user addresses
            for(AddressesRest addressRest: addressesRestsListModel) {

                // create link for every collection object (user/s)
                Link addressLink = linkTo(methodOn(UserController.class)
                                        .getUserAddress(id, addressRest.getAddressId())).withSelfRel();
                addressRest.add(addressLink);

                Link userLink = linkTo(methodOn(UserController.class)
                        .getUser(id)).withRel("user");
                addressRest.add(userLink);

            }
        }

        return new Resources<>(addressesRestsListModel);

    }

    @GetMapping(path = "/{userId}/addresses/{addressId}", produces = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE, "application/hal+json"})
    //A simple Resource wrapping a domain object and adding links to it.
    public Resource<AddressesRest> getUserAddress(@PathVariable String userId, @PathVariable String addressId) {

        AddressDto addressDto = addressService.getAddress(addressId);

        ModelMapper modelMapper = new ModelMapper();

        // create link address with hateoas http://localhost:8080/mobile-app-ws/users/userid/addresses/addressId
        // self address
        // we replace the hardcoded path with the help of methodOn()
        // this method inspect the user controller and the user mapping
        // and will help with the building that link (so we get rid of the hardcoded url path)
        /*Link addressLink = linkTo(methodOn(UserController.class))
                .slash(userId)
                .slash("addresses")
                .slash(addressId)
                .withSelfRel();*/
        Link addressLink = linkTo(methodOn(UserController.class)
                .getUserAddress(userId, addressId))
                .withSelfRel();


        // create user link http://localhost:8080/mobile-app-ws/users/userid
        Link userLink = linkTo(methodOn(UserController.class)
                .getUser(userId))
                .withRel("user");

        // create addresses link http://localhost:8080/mobile-app-ws/users/userid/addresses
        Link addressesLink = linkTo(methodOn(UserController.class)
                .getUserAddresses(userId))
                .withRel("addresses");

        AddressesRest addressesRestModel = modelMapper.map(addressDto, AddressesRest.class);

        // add to the address obj the link
        // will not have the extended add support method
        // if we do not extend ResourceSupport in the rest class
        addressesRestModel.add(addressLink);
        addressesRestModel.add(userLink);
        addressesRestModel.add(addressesLink);

        return new Resource<>(addressesRestModel);
    }

    /**
    *   http://localhost:8080/mobile-app-ws/users/email-verification?token="token"
    **/
    @GetMapping(path = "/email-verification",
        produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public OperationStatusModel verifyEmailToken(@RequestParam(value = "token") String token) {

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());

        // if token not expired and is valid return true
        // otherwise return false
        boolean isVerified = userService.verifyEmailToken(token);

        // return status
        if(isVerified)
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        else
            returnValue.setOperationResult(RequestOperationStatus.ERROR.name());

        return returnValue;
    }

    /**
     * http://localhost:8080/mobile-app-ws/users/password-reset-request
     * **/
    @PostMapping(path = "/password-reset-request",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public OperationStatusModel requestReset(
            @RequestBody PasswordResetRequestModel passwordResetRequestModel) {

        OperationStatusModel returnValue = new OperationStatusModel();

        boolean operationResult = userService.requestPasswordReset(passwordResetRequestModel.getEmail());

        returnValue.setOperationName(RequestOperationName.REQUEST_PASSWORD_RESET.name());
        returnValue.setOperationResult(RequestOperationStatus.ERROR.name());

        if(operationResult) {
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }

        return returnValue;

    }

    @PostMapping(path = "/password-reset",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public OperationStatusModel resetPassword(@RequestBody PasswordResetModel passwordResetModel) {

        OperationStatusModel returnValue = new OperationStatusModel();

        boolean operationResult = userService.resetPassword(
                passwordResetModel.getToken(),
                passwordResetModel.getPassword());

        returnValue.setOperationName(RequestOperationName.PASSWORD_RESET.name());
        returnValue.setOperationResult(RequestOperationStatus.ERROR.name());

        if(operationResult)
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

        return returnValue;
    }


}

