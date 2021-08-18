package com.bm.guessthenumber.dao;

import com.bm.guessthenumber.dto.Round;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import javax.sql.RowSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

/**
 * An implementation of the RoundDao that interacts with a MySQL database 
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 18, 2021
 */
@Repository
public class RoundDaoDB implements RoundDao {

    private JdbcTemplate jdbc;

    @Autowired
    public RoundDaoDB(JdbcTemplate jdbc) {
	this.jdbc = jdbc;
    }

    @Override
    public Optional<Round> makeRoundForGameId(Round roundToInsert, int gameId) {
	Optional<Round> receivedInstance;
	if (roundToInsert == null) {
	    receivedInstance = Optional.empty();
	} else {
	    try {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		int rowsUpdated = jdbc.update(
		    (Connection conn) -> {
			PreparedStatement statement = conn.prepareStatement(
			    "INSERT INTO round (gameId, guess, exactMatches, partialMatches)"
			    + " VALUES (?, ?, ?, ?)",
			    Statement.RETURN_GENERATED_KEYS
			);
			statement.setInt(1, gameId);
			statement.setString(2, roundToInsert.getGuess());
			statement.setInt(3, roundToInsert.getExactMatches());
			statement.setInt(4, roundToInsert.getPartialMatches());
			return statement;
		    }, 
		    keyHolder
		);
		if (rowsUpdated > 0) {
		    roundToInsert.setRoundId(keyHolder.getKey().intValue());
		    receivedInstance = Optional.of(roundToInsert);
		} else {
		    receivedInstance = Optional.empty();
		}
	    } catch (DataAccessException ex) {
		receivedInstance = Optional.empty();
	    }
	}
	return receivedInstance;
    }
}
