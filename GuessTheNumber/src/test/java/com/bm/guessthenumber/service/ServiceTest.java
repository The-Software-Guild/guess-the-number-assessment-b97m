/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bm.guessthenumber.service;

import com.bm.guessthenumber.dto.Game;
import com.bm.guessthenumber.dto.Round;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Tests for the Service Layer
 * 
 * @author Benjamin Munoz
 */
@SpringBootTest
public class ServiceTest {
    
    @Autowired
    Service service;
    
    @BeforeEach
    public void setUp() {
	assertTrue(service.clearData());
    }
    
    @Test
    public void testGenerateGame() {
	Set<Character> charSet = new HashSet<>();

	for (int i = 0; i < 10; i++) {
	    charSet.clear();
	    Game generatedGame = service.generateGame();
	    String answer = generatedGame.getAnswer();
	    for (int j = 0; j < answer.length(); j++){
		charSet.add(answer.charAt(j));
	    }
	    assertEquals(
		charSet.size(), 
		4, 
		"There should be four distinct characters in an answer"
	    );
	}
    }

    @Test
    public void testProcessGuess() {
	Game generatedGame = service.generateGame();

	assertTrue(
	    service.processGuess(generatedGame.getGameId(), null).isEmpty(),
	    "Null guesses should not be processed"
	);

	assertTrue(
	    service.processGuess(generatedGame.getGameId() + 1, "4444").isEmpty(),
	    "Guesses for nonexistent games should not be processed"
	);

	char[] answerChars = generatedGame.getAnswer().toCharArray();
	char[] guessChars = new char[answerChars.length];

	// test a guess where no matches of any kind are present
	int maxAnswerCharValue = answerChars[0];
	for (int i = 1; i < answerChars.length; i++) {
	    maxAnswerCharValue = Math.max(maxAnswerCharValue, answerChars[i]);
	}
	for (int i = 0; i < answerChars.length; i++) {
	    guessChars[i] = (char) (maxAnswerCharValue + 1);
	}

	service.processGuess(
	    generatedGame.getGameId(), 
	    String.valueOf(guessChars)
	).ifPresentOrElse(
	    round -> {
		assertEquals(
		    round.getExactMatches(), 
		    0, 
		    "No exact matches should be present"
		);
		assertEquals(
		    round.getPartialMatches(),
		    0,
		    "No partial matches should be present either"
		);
	    }, () -> fail("A Round should be generated here")
	);
	assertTrue(
	    service.getGameById(generatedGame.getGameId()).get().isInProgress(),
	    "The game should still be in progress"
	);

	// test a guess where the first and last chars are swapped
	for (int i = 0; i < answerChars.length; i++) {
	    guessChars[i] = answerChars[i];
	}
	char tmp = guessChars[0];
	guessChars[0] = guessChars[guessChars.length - 1];
	guessChars[guessChars.length - 1] = tmp;

	service.processGuess(
	    generatedGame.getGameId(), 
	    String.valueOf(guessChars)
	).ifPresentOrElse(
	    round -> {
		assertEquals(
		    round.getExactMatches(), 
		    2, 
		    "Two exact matches should be present"
		);
		assertEquals(
		    round.getPartialMatches(),
		    2,
		    "Two partial matches should be present"
		);
	    }, () -> fail("A Round should be generated here")
	);
	assertTrue(
	    service.getGameById(generatedGame.getGameId()).get().isInProgress(),
	    "The game should still be in progress"
	);

	// test a correct guess
	for (int i = 0; i < answerChars.length; i++) {
	    guessChars[i] = answerChars[i];
	}
	service.processGuess(
	    generatedGame.getGameId(), 
	    String.valueOf(guessChars)
	).ifPresentOrElse(
	    round -> {
		assertEquals(
		    round.getExactMatches(), 
		    4, 
		    "Two exact matches should be present"
		);
		assertEquals(
		    round.getPartialMatches(),
		    0,
		    "Two partial matches should be present"
		);
	    }, () -> fail("A Round should be generated here")
	);
	assertFalse(
	    service.getGameById(generatedGame.getGameId()).get().isInProgress(),
	    "The game should be finished"    
	);
    }

    @Test
    public void testGetAllGames() {
	List<Game> generatedGames = new LinkedList<>();
	for (int i = 0; i < 10; i++) {
	    generatedGames.add(service.generateGame());
	}

	List<Game> containedGames = service.getAllGames();

	assertEquals(generatedGames.size(), containedGames.size());
	for (int i = 0; i < generatedGames.size(); i++) {
	    assertEquals(generatedGames.get(i), containedGames.get(i));
	}
    }

    @Test
    public void testGetGameById() {
	List<Game> generatedGames = new LinkedList<>();
	for (int i = 0; i < 10; i++) {
	    generatedGames.add(service.generateGame());
	}

	generatedGames.forEach(game -> {
	    assertEquals(
		game,
		service.getGameById(game.getGameId()).get(),
		"There should be a game with this id"
	    );
	});

	int maxId = generatedGames.stream()
	    .map(game -> game.getGameId())
	    .reduce(0, (a, b) -> Math.max(a, b));

	assertTrue(
	    service.getGameById(maxId + 10).isEmpty(),
	    "There should be no game with this id"
	);
    }

    @Test
    public void testGetRoundsByGameId() {
	Game generatedGame = service.generateGame();
	
	List<String> guesses = new LinkedList<>();
	String currGuess;

	char[] answerChars = generatedGame.getAnswer().toCharArray();
	char[] guessChars = new char[answerChars.length];

	// a guess where no matches of any kind are present
	int maxAnswerCharValue = answerChars[0];
	for (int i = 1; i < answerChars.length; i++) {
	    maxAnswerCharValue = Math.max(maxAnswerCharValue, answerChars[i]);
	}
	for (int i = 0; i < answerChars.length; i++) {
	    guessChars[i] = (char) (maxAnswerCharValue + 1);
	}
	currGuess = String.valueOf(guessChars);
	guesses.add(currGuess);
	service.processGuess(
	    generatedGame.getGameId(), 
    	    currGuess
	);

	// a guess where the first and last chars are swapped
	for (int i = 0; i < answerChars.length; i++) {
	    guessChars[i] = answerChars[i];
	}
	char tmp = guessChars[0];
	guessChars[0] = guessChars[guessChars.length - 1];
	guessChars[guessChars.length - 1] = tmp;

	currGuess = String.valueOf(guessChars);
	guesses.add(currGuess);
	service.processGuess(
	    generatedGame.getGameId(), 
    	    currGuess
	);

	// a correct guess
	for (int i = 0; i < answerChars.length; i++) {
	    guessChars[i] = answerChars[i];
	}
	currGuess = String.valueOf(guessChars);
	guesses.add(currGuess);
	service.processGuess(
	    generatedGame.getGameId(), 
    	    currGuess
	);
	
	List<Round> rounds = service.getRoundsByGameId(generatedGame.getGameId() + 10);
	assertTrue(rounds.isEmpty(), "There should be no rounds with this id");

	rounds = service.getRoundsByGameId(generatedGame.getGameId());

	assertEquals(guesses.size(), rounds.size());
	for (int i = 0; i < guesses.size(); i++){
	    assertEquals(guesses.get(i), rounds.get(i).getGuess());
	}
    }

    @Test
    public void testAdjustedVersionOfGame() {
	assertNull(
	    service.adjustedVersionOfGame(null), 
	    "The adjusted version of a null should be null"
	);

	Game inProgressGame = new Game();
	inProgressGame.setAnswer("1234");
	inProgressGame.setInProgress(true);

	Game adjustedGame = service.adjustedVersionOfGame(inProgressGame);
	assertEquals(inProgressGame.getGameId(), adjustedGame.getGameId());
	assertEquals(inProgressGame.getRounds(), adjustedGame.getRounds());
	assertEquals(inProgressGame.isInProgress(), adjustedGame.isInProgress());
	assertEquals(inProgressGame.getAnswer(), "1234");
	assertEquals(adjustedGame.getAnswer(), Game.ANSWER_HIDDEN);

	Game finishedGame = new Game();
	finishedGame.setAnswer("3456");
	finishedGame.setInProgress(false);
	adjustedGame = service.adjustedVersionOfGame(finishedGame);
	assertEquals(finishedGame, adjustedGame);
    }
}
