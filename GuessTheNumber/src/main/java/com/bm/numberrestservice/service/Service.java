package com.bm.numberrestservice.service;

import com.bm.numberrestservice.dao.GameDao;
import com.bm.numberrestservice.dto.Game;
import com.bm.numberrestservice.dto.Round;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    public Service(GameDao gameDao) {
	this.gameDao = gameDao;
    }
    
    /**
     * Retrieves a list of all stored games
     * @return The aforementioned list
     */
    public List<Game> getAllGames() {
	return this.gameDao.getAllGames();
    } 

    /**
     * Attempts to retrieve the game corresponding to the id
     * 
     * If such a game is found, an instance containing it is returned
     * 
     * Otherwise, an empty instance is returned
     * @param id
     * @return The aforementioned instance
     */
    public Optional<Game> getGameById(int id) {
	return this.gameDao.getGameById(id);
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

	return this.gameDao.addGame(newGame);
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