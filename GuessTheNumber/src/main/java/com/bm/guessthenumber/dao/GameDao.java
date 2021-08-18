package com.bm.guessthenumber.dao;

import com.bm.guessthenumber.dto.Game;
import java.util.List;
import java.util.Optional;

/**
 * Handles retrieval and storage of game data
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 18, 2021
 */
public interface GameDao {
    /**
     * Retrieves a list of all stored games
     * 
     * @return The aforementioned list
     */
    public List<Game> getAllGames();

    /**
     * Attempts to retrieve a game in the collection
     * whose id corresponds to this id
     * 
     * If such a game exists, an instance containing that game is returned.
     * 
     * Otherwise, an empty instance will be returned.
     * 
     * @param id
     * @return The aforementioned instances
     */
    public Optional<Game> getGameById(int id);
    
    /**
     * Attempts to inserts a game into the collection of stored games
     * 
     * If the insertion succeeds, then an instance containing the
     * inserted game with its set id is included
     * 
     * Otherwise, an empty instance will be returned
     * 
     * @param gameToInsert
     * @return The aforementioned instance
     */
    public Optional<Game> addGame(Game gameToInsert);

    /**
     * Attempts to mark the game with the indicated id
     * as finished. The value returned depends on the success
     * of this marking process 
     * 
     * @param gameId
     * @return The aforementioned value
     */
    public boolean markGameFinished(int gameId);
}