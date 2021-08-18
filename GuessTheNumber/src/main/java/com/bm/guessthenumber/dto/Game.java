package com.bm.guessthenumber.dto;

import java.util.List;

/**
 * Represents transferrable data representing a game 
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 18, 2021
 */
public class Game {
    private int gameId;
    private boolean inProgress;
    private String answer;	
    private List<Round> rounds;
    
    public static final String ANSWER_HIDDEN = "(Answer Hidden)";

    public int getGameId() {
	return gameId;
    }

    public void setGameId(int gameId) {
	this.gameId = gameId;
    }

    public boolean isInProgress() {
	return inProgress;
    }

    public void setInProgress(boolean inProgress) {
	this.inProgress = inProgress;
    }

    public String getAnswer() {
    	return answer;
    }

    public void setAnswer(String answer) {
	this.answer = answer;
    }

    public List<Round> getRounds() {
	return rounds;
    }

    public void setRounds(List<Round> rounds) {
	this.rounds = rounds;
    }
}