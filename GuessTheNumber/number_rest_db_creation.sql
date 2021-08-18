DROP SCHEMA IF EXISTS numberGuessDB;
CREATE SCHEMA numberGuessDB;
USE numberGuessDB;

CREATE TABLE game (
	gameId INT NOT NULL AUTO_INCREMENT,
    inProgress BOOLEAN DEFAULT TRUE,
    answer CHAR(4) NOT NULL,
    CONSTRAINT PK_game PRIMARY KEY (gameId)
);

CREATE TABLE round (
	roundId INT NOT NULL AUTO_INCREMENT,
    guess CHAR(4) NOT NULL,
    exactMatches INT UNSIGNED NOT NULL,
    partialMatches INT UNSIGNED NOT NULL,
    timeOfGuess DATETIME(6) DEFAULT NOW(6),
    gameId INT NOT NULL,
    CONSTRAINT PK_round PRIMARY KEY (roundId),
    CONSTRAINT FK_round_game FOREIGN KEY (gameId)
		REFERENCES game (gameId)
);