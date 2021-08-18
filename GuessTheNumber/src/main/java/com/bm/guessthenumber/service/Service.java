package com.bm.guessthenumber.service;

import com.bm.guessthenumber.dao.GameDao;
import com.bm.guessthenumber.dao.RoundDao;
import com.bm.guessthenumber.dto.Game;
import com.bm.guessthenumber.dto.Round;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Acts as the service layer of the application 
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 18, 2021
 */
@Component
public class Service {
    private GameDao gameDao;
    private RoundDao roundDao;

    @Autowired
    public Service(GameDao gameDao, RoundDao roundDao) {
	this.gameDao = gameDao;
	this.roundDao = roundDao;
    }
    
    /**
     * Attempt to generate a new game and add it to the collection
     * 
     * If this succeeds, an instance of the generated game is returned
     * 
     * Otherwise, an empty instance is returned
     * 
     * @return The aforementioned instance
     */
    public Optional<Game> generateGame() {
    	Game newGame = new Game();
	newGame.setInProgress(true);
	newGame.setRounds(new LinkedList<Round>());
	newGame.setAnswer(generateAnswer());
	return gameDao.addGame(newGame);
    }

    /**
     * Processes a guess for a given game
     * 
     * If the game exists and the guess is non-null, then an
     * instance containing the appropriate Round will be returned
     * 
     * Otherwise, an empty instance will be returned
     * 
     * @param gameId
     * @param guess
     * @return the aforementioned instance
     */
    public Optional<Round> processGuess(int gameId, String guess) {
	if (guess == null) {
	    return Optional.empty();
	}

	Optional<Game> possGame = getGameById(gameId);
	if (possGame.isEmpty()) {
	    return Optional.empty();
	}

	String answer = possGame.get().getAnswer();
	int exactMatches = 0;
	int partialMatches = 0;
	for (int i = 0; i < guess.length(); i++) {
	    for (int j = 0; j < answer.length(); j++) {
		if (i == j && guess.charAt(i) == answer.charAt(j)) {
		    exactMatches++;
		} else if (guess.charAt(i) == answer.charAt(j)) {
		    partialMatches++;
		}
	    }	
	}

	if (exactMatches == answer.length()) {
	    gameDao.markGameFinished(gameId);
	}

	Round round = new Round();
	round.setGuess(guess);
	round.setExactMatches(exactMatches);
	round.setPartialMatches(partialMatches);
	round.setTimeOfGuess(LocalDateTime.now());

	return roundDao.makeRoundForGameId(round, gameId);
    }

    /**
     * Retrieves a list of all stored games
     * 
     * @return The aforementioned list
     */
    public List<Game> getAllGames() {
	return this.gameDao.getAllGames();
    } 

    /**
     * Attempts to retrieve the game corresponding to the id.
     * 
     * If such a game is found, an instance containing it is returned
     * 
     * Otherwise, an empty instance is returned
     * @param id
     * @return The aforementioned instance
     */
    public Optional<Game> getGameById(int id) {
	return gameDao.getGameById(id);
    }

    /**
     * Retrieves a list of all the rounds for the game
     * associated with this game id
     * 
     * If no such game exists, or the game has no 
     * rounds yet, the list will be empty.
     * 
     * @param id
     * @return the aforementioned list
     */
    public List<Round> getRoundsByGameId(int id) {
	Optional<Game> possGame = getGameById(id);
	if (possGame.isEmpty()) {
	    return new LinkedList<>();
	}
	return possGame.get().getRounds();
    }
    
    /**
     * Makes a version of the passed in game where the answer is
     * hidden if the passed in game is still in progress
     * 
     * @param game
     * @return The aforementioned game
     */
    public Game adjustedVersionOfGame(Game game) {
	if (game == null) {
	    return game;
	}

	Game receivedGame = new Game();
	receivedGame.setGameId(game.getGameId());
	receivedGame.setInProgress(game.isInProgress());
	receivedGame.setRounds(game.getRounds());
	receivedGame.setAnswer(game.isInProgress() ? Game.ANSWER_HIDDEN : game.getAnswer());

	return receivedGame;
    }

    private String generateAnswer() {
	String generatedAnswer = "";

	int[] digits = new int[9];
	for (int i = 1; i <= 9; i++) {
	    digits[i - 1] = i;
	}
	
	// shuffle the array and then pick the first element for the
	// first digit
	for (int i = 1; i < digits.length; i++) {
	    int randIndex = (int) (Math.random() * (i + 1));
	    if (randIndex == 0) {
		int tmp = digits[0];
		digits[0] = digits[i];
		digits[i] = tmp;
	    }
	}
	generatedAnswer = generatedAnswer + digits[0];

	// shift all elements to the left and then add a zero to the end
	for (int i = 0; i < digits.length - 1; i++) {
	    digits[i] = digits[i + 1];
	}	
	digits[digits.length - 1] = 0;

	// shuffle again and pick off remaining digits 
	for (int i = 1; i < digits.length; i++) {
	    int randIndex = (int) (Math.random() * (i + 1));
	    if (randIndex <= 2) {
		int tmp = digits[randIndex];
		digits[randIndex] = digits[i];
		digits[i] = tmp;
	    }
	}

	generatedAnswer = generatedAnswer + digits[0];
	generatedAnswer = generatedAnswer + digits[1];
	generatedAnswer = generatedAnswer + digits[2];

	return generatedAnswer;
    }
}