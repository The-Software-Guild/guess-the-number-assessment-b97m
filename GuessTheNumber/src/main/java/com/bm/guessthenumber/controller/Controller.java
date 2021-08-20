package com.bm.guessthenumber.controller;

import com.bm.guessthenumber.dto.Game;
import com.bm.guessthenumber.dto.Guess;
import com.bm.guessthenumber.dto.Round;
import com.bm.guessthenumber.service.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("begin")
    @ResponseStatus(HttpStatus.CREATED)
    public int createGame() {
	return service.generateGame().getGameId(); 
    }

    @PostMapping("guess")
    public ResponseEntity makeGuess(@RequestBody Guess guess) {
    	Optional<Round> possRound = service.processGuess(
	    guess.getGameId(), 
	    guess.getGuess()
	);

	if (possRound.isPresent()) {
	    return new ResponseEntity(possRound.get(), HttpStatus.CREATED);
	}
	return new ResponseEntity(
	    "Either the guess was null or too long, or the game id was of a "
	    + "game that does not exist", 
	    HttpStatus.BAD_REQUEST
	);
    }

    @GetMapping("game")
    @ResponseStatus(HttpStatus.OK)
    public List<Game> getGameList() {
    	return service.getAllGames().stream()
	    .map(game -> service.adjustedVersionOfGame(game))
	    .collect(Collectors.toList());
    }

    @GetMapping("game/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable int id) {
	Optional<Game> possGame = service.getGameById(id);
	if (possGame.isEmpty()) {
	    return new ResponseEntity(HttpStatus.NOT_FOUND);
	}
	return new ResponseEntity(
	    service.adjustedVersionOfGame(possGame.get()), 
	    HttpStatus.OK
	);
    }

    @GetMapping("rounds/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Round> getRoundsForGame(@PathVariable int gameId) {
	return service.getRoundsByGameId(gameId);
    }
}