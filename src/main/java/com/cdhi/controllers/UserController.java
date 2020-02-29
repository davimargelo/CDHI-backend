package com.cdhi.controllers;

import com.cdhi.domain.User;
import com.cdhi.dtos.NewUserDTO;
import com.cdhi.dtos.UserDTO;
import com.cdhi.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@Api(value = "User Controller")
@RestController
@RequestMapping(value = "users")
public class UserController {

    @Autowired
    UserService service;

    @ApiOperation(value = "Get User by id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<User> getById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findOne(id));
    }

    @ApiOperation(value = "Get Users")
    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll(@RequestParam(value = "name", defaultValue = "") String name) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll(name));
    }

    @ApiOperation(value = "Create User")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid NewUserDTO newUserDTO) {
        User u = service.create(newUserDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(u.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @ApiOperation(value = "Edit User")
    @PutMapping(value = "{id}")
    public ResponseEntity<?> save(@RequestBody @Valid UserDTO userDTO, @PathVariable("id") Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.save(userDTO, id));
    }

    @ApiOperation(value = "Delete User")
    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("User Id: " + id + " deleted successfully!");
    }

    @ApiOperation(value = "Get all Users By PAGE + search name")
    @GetMapping(value = "page")
    public ResponseEntity<Page<User>> getAllByPage(
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10")Integer size,
            @RequestParam(value = "orderBy", defaultValue = "name")String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC")String direction) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllByPage(name, page, size, orderBy, direction));
    }

    @ApiOperation(value = "Changes User PASSWORD")
    @PutMapping(value = "/password/{id}")
    public ResponseEntity<?> changePassword(@PathVariable("id") Integer id,
                                            @RequestBody
                                                @NotNull(message = "'Password' cannot be null")
                                                @NotEmpty(message = "'Password' is required") String newPassword) {
        return ResponseEntity.status(HttpStatus.OK).body(service.save(newPassword, id));
    }

    @ApiOperation(value = "Enables User")
    @GetMapping(value = "confirm/{id}/{key}")
    public ResponseEntity<?> enables(@PathVariable("id") Integer id,
                                     @PathVariable("key") String key) {
        return ResponseEntity.status(HttpStatus.OK).body(service.enable(id, key));
    }

}
