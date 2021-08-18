package com.bm.guessthenumber.dao;

import com.bm.guessthenumber.dto.Round;
import java.util.Optional;

/**
 * Handles the storage and retrieval of Round information 
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 18, 2021
 */
public interface RoundDao {
    /**
     * Attempts to make a round for the game associated with this id
     * 
     * If this succeeds, then an instance containing the produced
     * round is returned.
     * 
     * Otherwise, an empty instance is returned.
     * 
     * @param roundToInsert
     * @param gameId
     * @return The aforementioned instance
     */
    public Optional<Round> makeRoundForGameId(Round roundToInsert, int gameId);
}
