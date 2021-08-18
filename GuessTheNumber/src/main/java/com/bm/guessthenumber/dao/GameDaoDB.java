package com.bm.guessthenumber.dao;

import com.bm.guessthenumber.dto.Game;
import com.bm.guessthenumber.dto.Round;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

/**
 * An implementation of the GameDao that interacts with a MySQL DB
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 18, 2021
 */
@Repository
public class GameDaoDB implements GameDao {
    private JdbcTemplate jdbc;

    private static final RowMapper<Game> GAME_MAPPER = (ResultSet rs, int index) -> {
	Game game = new Game();
	game.setGameId(rs.getInt("gameId"));
	game.setInProgress(rs.getBoolean("inProgress"));
	game.setAnswer(rs.getString("answer"));
	return game;
    };

    private static final RowMapper<Round> ROUND_MAPPER = (ResultSet rs, int index) -> {
	Round round = new Round();
	round.setRoundId(rs.getInt("roundId"));
	round.setGuess(rs.getString("guess"));
	round.setExactMatches(rs.getInt("exactMatches"));
	round.setPartialMatches(rs.getInt("partialMatches"));
	round.setTimeOfGuess(rs.getTimestamp("timeOfGuess").toLocalDateTime());
	return round;
    };
    
    @Autowired
    public GameDaoDB(JdbcTemplate jdbc) {
	this.jdbc = jdbc;
    }

    @Override
    public List<Game> getAllGames() {
	List<Game> gameList = jdbc.query(
	    "SELECT * FROM game",
	    GAME_MAPPER
	);

	gameList.forEach(game -> occupyRoundListForGame(game));
	return gameList;
    }

    @Override
    public Optional<Game> getGameById(int id) {
	Optional<Game> receivedInstance;
	try {
	    Game possGame = jdbc.queryForObject(
		"SELECT * FROM game WHERE gameId = ?", 
		GAME_MAPPER,
		id
	    );
	    if (possGame == null) {
		receivedInstance = Optional.empty();
	    } else {
		occupyRoundListForGame(possGame);
		receivedInstance = Optional.of(possGame);
	    }
	} catch(DataAccessException ex) {
	    receivedInstance = Optional.empty();
	}
	return receivedInstance;
    }

    @Override
    public Optional<Game> addGame(Game gameToInsert) {
	Optional<Game> receivedInstance;
	if (gameToInsert == null) {
	    receivedInstance = Optional.empty();
	} else {
	    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	    int rowsUpdated = jdbc.update(
		(Connection conn) ->{
		    PreparedStatement statement = conn.prepareStatement(
			"INSERT INTO game (answer) VALUES (?);",
			Statement.RETURN_GENERATED_KEYS
		    );
		    statement.setString(1, gameToInsert.getAnswer());
		    return statement;
		}, 
		keyHolder
	    );

	    if (rowsUpdated > 0) {
		gameToInsert.setGameId(keyHolder.getKey().intValue());
		receivedInstance = Optional.of(gameToInsert);
	    } else {
		receivedInstance = Optional.empty();
	    }
	}
	return receivedInstance;
    }

    private void occupyRoundListForGame(Game gameToOccupy) {
	if (gameToOccupy != null) {
	    List<Round> roundsToInsert = jdbc.query(
		"SELECT * FROM round WHERE gameId = ? ORDER BY timeOfGuess", 
		ROUND_MAPPER,
		gameToOccupy.getGameId()
	    );
	    gameToOccupy.setRounds(roundsToInsert);
	}
    }

    @Override
    public boolean markGameFinished(int gameId) {
	int roundsUpdated = jdbc.update(
	    "UPDATE game SET inProgress = False WHERE gameId = ?",
	    gameId
	);
	return roundsUpdated > 0;
    }
}