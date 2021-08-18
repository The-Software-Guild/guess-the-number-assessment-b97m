package com.bm.guessthenumber.dto;

/**
 * Transferrable data representing a Guess 
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 18, 2021
 */
public class Guess {
    private String guess;
    private int gameId;

    public String getGuess() {
	return guess;
    }

    public void setGuess(String guess) {
	this.guess = guess;
    }

    public int getGameId() {
	return gameId;
    }

    public void setGameId(int gameId) {
	this.gameId = gameId;
    }
}
