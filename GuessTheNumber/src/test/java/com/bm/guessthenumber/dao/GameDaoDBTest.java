package com.bm.guessthenumber.dao;

import com.bm.guessthenumber.dto.Game;
import com.bm.guessthenumber.dto.Round;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Tests for the DB implementation of the GameDao
 * 
 * @author Benjamin Munoz
 */
@SpringBootTest
public class GameDaoDBTest {
    
    @Autowired
    GameDao gameDao;
    
    @Autowired
    RoundDao roundDao;

    @BeforeEach
    public void setUp() {
	assertTrue(roundDao.clearRounds());
	assertTrue(gameDao.clearGames());
    }

    @Test
    public void testAdd() {
	Game gameToInsert = null;
	assertNull(
	    gameDao.addGame(gameToInsert), 
	    "Inserting null should return null results"
	);
	
	gameToInsert = new Game();
	gameToInsert.setAnswer(null);

	assertNull(
	    gameDao.addGame(gameToInsert), 
	    "Inserting with a null answer should return null results"
	);

	gameToInsert.setAnswer("1234");
	gameToInsert.setInProgress(false);
	gameToInsert.setRounds(null);
	Game insertedGame = gameDao.addGame(gameToInsert);

	assertEquals(
	    gameToInsert.getAnswer(), 
	    insertedGame.getAnswer(), 
	    "The answers should be the same"
	);
	assertTrue(insertedGame.isInProgress(), "The game should be in progress");
	assertTrue(insertedGame.getRounds().isEmpty(), "This game should have no rounds");
    }

    @Test
    public void testGetGameById() {
	assertTrue(
	    gameDao.getGameById(1).isEmpty(),
	    "There should be no games in the collection yet"
	);

	Game gameToInsert = new Game();
	gameToInsert.setAnswer("1234");

	Game insertedGame = gameDao.addGame(gameToInsert);
	gameDao.getGameById(insertedGame.getGameId()).ifPresentOrElse(
	    game -> {
		assertEquals(
		    insertedGame.getGameId(), 
		    game.getGameId(),
		    "The ids should match"
		);
		assertEquals(
		    insertedGame.getAnswer(),
		    game.getAnswer(),
		    "The answers should match"
		);
		assertEquals(
		    insertedGame.getRounds(),
		    game.getRounds(),
		    "The round lists should match"
		);
		assertEquals(
		    insertedGame.isInProgress(),
		    game.isInProgress(),
		    "The progresses should match"
		);
	    }, 
	    () -> fail("There should be a game with this id")
	);

	assertTrue(
	    gameDao.getGameById(insertedGame.getGameId() + 1).isEmpty(),
	    "There should be no games with this id"
	);
    }

    @Test
    public void testMarkGameFinished() {
	assertFalse(
	    gameDao.markGameFinished(10), 
	    "There should be no games with this id that are available to mark"
	);

	Game gameToInsert = new Game();
	gameToInsert.setAnswer("1234");

	Game insertedGame = gameDao.addGame(gameToInsert);

	assertTrue(
	    gameDao.markGameFinished(insertedGame.getGameId()),
	    "Marking should succeed"
	);

	gameDao.getGameById(insertedGame.getGameId()).ifPresentOrElse(
	    game -> assertFalse(game.isInProgress(), "The game should be done"),
	    () -> fail("There should be game with this id") 
	);

	// repeat to make sure
	assertTrue(
	    gameDao.markGameFinished(insertedGame.getGameId()),
	    "Marking should succeed"
	);

	gameDao.getGameById(insertedGame.getGameId()).ifPresentOrElse(
	    game -> assertFalse(game.isInProgress(), "The game should be done"),
	    () -> fail("There should be game with this id") 
	);
    }

    @Test
    public void testGetAll() {
	assertTrue(
	    gameDao.getAllGames().isEmpty(), 
	    "There should be no games yet"
	);

	List<Game> producedGames = Stream.of("1234", "4321", "5678", "9821")
	    .map(answer -> {
		Game gameToInsert = new Game();
		gameToInsert.setAnswer(answer);
		return gameDao.addGame(gameToInsert);
	    })
	    .collect(Collectors.toList());

	List<Game> gameListFromDao = gameDao.getAllGames();
	assertEquals(producedGames.size(), gameListFromDao.size());	
	for (int i = 0; i < producedGames.size(); i++) {
	    assertEquals(producedGames.get(i), gameListFromDao.get(i));
	}
    }

    @Test
    public void testClearGames() {
	assertTrue(
	    gameDao.getAllGames().isEmpty(), 
	    "There should be no games yet"
	);

	Stream.of("1234", "4321", "5678", "9821").forEach(answer -> {
	    Game gameToInsert = new Game();
	    gameToInsert.setAnswer(answer);
	    gameDao.addGame(gameToInsert);
	});

	assertFalse(
	    gameDao.getAllGames().isEmpty(), 
	    "The list should not be empty"
	);

	assertTrue(gameDao.clearGames());

	assertTrue(
	    gameDao.getAllGames().isEmpty(), 
	    "The list should now be empty"
	);

	Game gameToInsert = new Game();
	gameToInsert.setAnswer("1234");
	Game insertedGame = gameDao.addGame(gameToInsert);

	Round roundToInsert = new Round();
	roundToInsert.setGuess("5678");
	roundToInsert.setExactMatches(0);
	roundToInsert.setPartialMatches(0);
	roundToInsert.setTimeOfGuess(LocalDateTime.now());
	
	roundDao.makeRoundForGameId(roundToInsert, insertedGame.getGameId());

	assertFalse(
	    gameDao.clearGames(), 
	    "There is a round associated with this game, this should fail"
	);
    }
}