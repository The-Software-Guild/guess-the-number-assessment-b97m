package com.bm.numberrestservice.controller;

import com.bm.numberrestservice.dto.Game;
import com.bm.numberrestservice.dto.Round;
import com.bm.numberrestservice.service.Service;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Acts as the primary controller of the application
 * 
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 18, 2021
 */
@RestController
public class Controller {

    private Service service;

    @Autowired
    public Controller(Service service) {
	this.service = service;
    }

    @PostMapping("create")
    public ResponseEntity<Game> createGame() {
	Optional<Game> possGame = service.generateGame();
	if (possGame.isPresent()) {
	    return new ResponseEntity(possGame.get(), HttpStatus.CREATED);
	} else {
	    return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	}	
    }

    @PostMapping("guess")
    @ResponseStatus(HttpStatus.OK)
    public Round makeGuess(int gameId, int guess) {
	System.out.println("The game is " + gameId);
	System.out.println("The guess is " + guess);
	return null;
    }

    @GetMapping("game")
    @ResponseStatus(HttpStatus.OK)
    public List<Game> getGameList() {
    	return service.getAllGames();
    }
}