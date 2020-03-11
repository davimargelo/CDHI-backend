package com.cdhi.controllers;

import com.cdhi.domain.Board;
import com.cdhi.dtos.BoardDTO;
import com.cdhi.dtos.NewBoardDTO;
import com.cdhi.services.BoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Api(value = "Board Controller")
@RestController
@RequestMapping(value = "boards")
public class BoardController {

    @Autowired
    BoardService service;

    @ApiOperation(value = "Get Board by id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<BoardDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findOne(id));
    }

    @ApiOperation(value = "User creates new board")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody NewBoardDTO newBoardDTO) {
        Board b = service.create(newBoardDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(b.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

//    @ApiOperation(value = "Get Boards by USER ID")
//    @GetMapping(value = "/user/{id}")
//    public ResponseEntity<BoardDTO> getAllBoardsFromUser(@PathVariable Integer id) {
//        return ResponseEntity.status(HttpStatus.OK).body(service.findOne(id));
//    }
}
