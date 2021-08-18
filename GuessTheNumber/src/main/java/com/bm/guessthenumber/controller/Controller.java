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
    public ResponseEntity<Game> createGame() {
	Optional<Game> possGame = service.generateGame();
	if (possGame.isPresent()) {
	    Game game = service.adjustedVersionOfGame(possGame.get());
	    return new ResponseEntity(game, HttpStatus.CREATED);
	} else {
	    return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	}	
    }

    @PostMapping("guess")
    @ResponseStatus(HttpStatus.OK)
    public Round makeGuess(@RequestBody Guess guess) {
	System.out.format("gameId: %d%n", guess.getGameId());
	System.out.format(" guess: %s%n", guess.getGuess());

    	Optional<Round> possRound = service.processGuess(
	    guess.getGameId(), 
	    guess.getGuess()
	);

	if (possRound.isPresent()) {
	    return possRound.get();
	}
	return null;
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
    public List<Round> getRoundsForGame(@PathVariable int gameId) {
	return service.getRoundsByGameId(gameId);
    }
}