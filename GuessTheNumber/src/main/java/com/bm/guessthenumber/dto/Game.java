package com.bm.guessthenumber.dto;

import java.util.List;
import java.util.Objects;

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

    @Override
    public int hashCode() {
	int hash = 7;
	hash = 97 * hash + this.gameId;
	hash = 97 * hash + (this.inProgress ? 1 : 0);
	hash = 97 * hash + Objects.hashCode(this.answer);
	hash = 97 * hash + Objects.hashCode(this.rounds);
	return hash;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final Game other = (Game) obj;
	if (this.gameId != other.gameId) {
	    return false;
	}
	if (this.inProgress != other.inProgress) {
	    return false;
	}
	if (!Objects.equals(this.answer, other.answer)) {
	    return false;
	}
	return Objects.equals(this.rounds, other.rounds);
    }
}