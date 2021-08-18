package com.bm.numberrestservice.dao;

import com.bm.numberrestservice.dto.Game;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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

    private RowMapper<Game> GAME_MAPPER = (ResultSet rs, int index) -> {
	Game game = new Game();
	game.setGameId(rs.getInt("gameId"));
	game.setInProgress(rs.getBoolean("inProgress"));
	game.setAnswer(rs.getString("answer"));
	return game;
    };
    
    @Autowired
    public GameDaoDB(JdbcTemplate jdbc) {
	this.jdbc = jdbc;
    }

    @Override
    public List<Game> getAllGames() {
	return jdbc.query(
	    "SELECT * FROM game",
	    GAME_MAPPER
	);
    }

    @Override
    public Optional<Game> getGameById(int id) {
	Game possGame = jdbc.queryForObject(
	    "SELECT * FROM game WHERE gameId = ?", GAME_MAPPER, id
	);
	if (possGame == null) {
	    return Optional.empty();
	}
	return Optional.of(possGame);
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
}