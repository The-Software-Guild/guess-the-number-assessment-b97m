package com.bm.guessthenumber.dto;

import java.time.LocalDateTime;

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
}