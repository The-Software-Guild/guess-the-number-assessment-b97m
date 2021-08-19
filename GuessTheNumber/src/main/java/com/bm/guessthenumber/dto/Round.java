package com.bm.guessthenumber.dto;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents transferrable data representing a round 
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 18, 2021
 */
public class Round {
    private int roundId;
    private String guess;
    private int exactMatches;
    private int partialMatches;
    private LocalDateTime timeOfGuess;

    public int getRoundId() {
	return roundId;
    }

    public void setRoundId(int roundId) {
	this.roundId = roundId;
    }

    public String getGuess() {
	return guess;
    }

    public void setGuess(String guess) {
	this.guess = guess;
    }

    public String getResult() {
	return String.format("e:%dp:%d", exactMatches, partialMatches);
    }

    public int getExactMatches() {
	return exactMatches;
    }

    public void setExactMatches(int exactMatches) {
	this.exactMatches = exactMatches;
    }

    public int getPartialMatches() {
	return partialMatches;
    }

    public void setPartialMatches(int partialMatches) {
	this.partialMatches = partialMatches;
    }

    public LocalDateTime getTimeOfGuess() {
	return timeOfGuess;
    }

    public void setTimeOfGuess(LocalDateTime timeOfGuess) {
	this.timeOfGuess = timeOfGuess;
    }

    @Override
    public int hashCode() {
	int hash = 7;
	hash = 29 * hash + this.roundId;
	hash = 29 * hash + Objects.hashCode(this.guess);
	hash = 29 * hash + this.exactMatches;
	hash = 29 * hash + this.partialMatches;
	hash = 29 * hash + Objects.hashCode(this.timeOfGuess);
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
	final Round other = (Round) obj;
	if (this.roundId != other.roundId) {
	    return false;
	}
	if (this.exactMatches != other.exactMatches) {
	    return false;
	}
	if (this.partialMatches != other.partialMatches) {
	    return false;
	}
	return Objects.equals(this.guess, other.guess);
	// Due to the way the timestamps are configured on the
	// database, the timestamps that appear on the database may
	// differ ever so slightly from the ones in this application
    }

    @Override
    public String toString() {
	return "Round{" + "roundId=" + roundId + ", guess=" + guess + ", exactMatches=" + exactMatches + ", partialMatches=" + partialMatches + ", timeOfGuess=" + timeOfGuess + '}';
    }
}