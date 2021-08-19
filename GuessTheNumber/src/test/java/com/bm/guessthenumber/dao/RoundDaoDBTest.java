package com.bm.guessthenumber.dao;

import com.bm.guessthenumber.dto.Game;
import com.bm.guessthenumber.dto.Round;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Tests for the DB implementation of the RoundDao
 * @author Benjamin Munoz
 */
@SpringBootTest
public class RoundDaoDBTest {
    
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
    public void testMakeRoundForGameId() {
	Game gameToInsert = new Game();
	gameToInsert.setAnswer("1234");
	Game insertedGame = gameDao.addGame(gameToInsert);

	Optional<Round> receivedInstance = roundDao.makeRoundForGameId(
	    null, 
	    insertedGame.getGameId()
	);
	assertTrue(
	    receivedInstance.isEmpty(), 
	    "No null rounds should be inserted"
	);

	Round roundToInsert = new Round();
	roundToInsert.setGuess(null);
	roundToInsert.setExactMatches(0);
	roundToInsert.setPartialMatches(0);
	roundToInsert.setTimeOfGuess(null);

	receivedInstance = roundDao.makeRoundForGameId(
	    roundToInsert, 
	    insertedGame.getGameId()
	);
	assertTrue(
	    receivedInstance.isEmpty(),
	    "Rounds with null fields should not be inserted"
	);

	roundToInsert.setGuess("1234");
	receivedInstance = roundDao.makeRoundForGameId(
	    roundToInsert, 
	    insertedGame.getGameId()
	);
	assertTrue(
	    receivedInstance.isEmpty(),
	    "Rounds with null fields should not be inserted"
	);

	roundToInsert.setTimeOfGuess(LocalDateTime.now());
	receivedInstance = roundDao.makeRoundForGameId(
	    roundToInsert, 
	    insertedGame.getGameId()
	);
	assertTrue(
	    receivedInstance.isPresent(),
	    "This round insertion should succeed"
	);

	Round insertedRound = receivedInstance.get();

	receivedInstance = roundDao.makeRoundForGameId(
	    roundToInsert, 
	    insertedGame.getGameId() + 1
	);
	assertTrue(
	    receivedInstance.isEmpty(),
	    "This round insertion should fail"
	);

	Game game = gameDao.getGameById(insertedGame.getGameId()).get();

	System.out.println(game.getRounds());
	System.out.println(insertedRound);
	assertTrue(game.getRounds().contains(insertedRound), "This round should be contained");	
    }

    @Test
    public void testClearRounds() {
    	assertTrue(
	    roundDao.clearRounds(), 
	    "The DB is empty, so this should succeed"
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

	assertTrue(
	    roundDao.clearRounds(), 
	    "Clearing the rounds should still succeed"
	);
    }
}
