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
     * Inserts a game into the collection of stored games.
     * 
     * If the game is null, a null result will be returned.
     * 
     * If the game's answer is null, a null result will still be returned.
     * 
     * Otherwise, the inserted game will be returned with its id updated
     * appropriately, its progress set to true, and its round list emptied
     * 
     * @param gameToInsert
     * @return The aforementioned game
     */
    public Game addGame(Game gameToInsert);

    /**
     * Attempts to mark the game with the indicated id
     * as finished. The value returned depends on the success
     * of this marking process 
     * 
     * @param gameId
     * @return The aforementioned value
     */
    public boolean markGameFinished(int gameId);

    /**
     * Attempts to remove all games from the collection.
     * The value returned depends on the success of the deletion.
     * 
     * @return The aforementioned value
     */
    public boolean clearGames();
}