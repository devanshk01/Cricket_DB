-- Sponsorships Table
CREATE TABLE Sponsorships (
    SponsorID INT PRIMARY KEY,
    SponsorName VARCHAR(100) NOT NULL,
    Amount DECIMAL(10, 2) CHECK (Amount >= 0),
    StartDate DATE NOT NULL,
    EndDate DATE NOT NULL CHECK (EndDate > StartDate)
);

-- Coaches Table
CREATE TABLE Coaches (
    CoachID INT PRIMARY KEY,
    HeadCoach VARCHAR(100) NOT NULL,
    BowlingCoach VARCHAR(100),
    BattingCoach VARCHAR(100),
    FieldingCoach VARCHAR(100)
);

-- Governing Body Table
CREATE TABLE GoverningBody (
    GoverningBodyName VARCHAR(100) PRIMARY KEY,
    President VARCHAR(100),
    VicePresident VARCHAR(100),
    CEO VARCHAR(100),
    Treasurer VARCHAR(100),
    Revenue DECIMAL(15, 2) CHECK (Revenue >= 0),
    Headquarters VARCHAR(100)
);

-- Team Table
CREATE TABLE Team (
    TeamName VARCHAR(100) PRIMARY KEY,
    CoachID INT,
    GoverningBodyName VARCHAR(100),
    YearOfFoundation YEAR CHECK (YearOfFoundation >= 1800),
    NumberOfChampionships INT DEFAULT 0 CHECK (NumberOfChampionships >= 0),
    TeamODIRank INT CHECK (TeamODIRank > 0),
    TeamT20Rank INT CHECK (TeamT20Rank > 0),
    TeamTestRank INT CHECK (TeamTestRank > 0),
    CONSTRAINT fk_CoachID FOREIGN KEY (CoachID) REFERENCES Coaches(CoachID)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_GoverningBodyName FOREIGN KEY (GoverningBodyName) REFERENCES GoverningBody(GoverningBodyName)
        ON DELETE SET NULL ON UPDATE CASCADE
);

-- Player Table
CREATE TABLE Player (
    PlayerID INT PRIMARY KEY,
    FullName VARCHAR(100) NOT NULL,
    DateOfBirth DATE NOT NULL,
    Nationality VARCHAR(50) NOT NULL,
    Role VARCHAR(50) NOT NULL CHECK (Role IN ('Batsman', 'Bowler', 'All-rounder')),
    BattingStyle VARCHAR(50),
    BowlingStyle VARCHAR(50),
    MatchesPlayed INT DEFAULT 0 CHECK (MatchesPlayed >= 0),
    Runs INT DEFAULT 0 CHECK (Runs >= 0),
    Wickets INT DEFAULT 0 CHECK (Wickets >= 0),
    Average DECIMAL(5, 2) CHECK (Average >= 0),
    StrikeRate DECIMAL(5, 2) CHECK (StrikeRate >= 0),
    Economy DECIMAL(5, 2) CHECK (Economy >= 0),
    PlayerODIRank INT CHECK (PlayerODIRank > 0),
    PlayerT20Rank INT CHECK (PlayerT20Rank > 0),
    PlayerTestRank INT CHECK (PlayerTestRank > 0),
    TeamName VARCHAR(100),
    CONSTRAINT fk_TeamName FOREIGN KEY (TeamName) REFERENCES Team(TeamName)
        ON DELETE SET NULL ON UPDATE CASCADE
);

-- Venue Table
CREATE TABLE Venue (
    StadiumName VARCHAR(100) PRIMARY KEY,
    City VARCHAR(100) NOT NULL,
    Capacity INT CHECK (Capacity > 0)
);

-- Match Table
CREATE TABLE Match (
    MatchID INT PRIMARY KEY,
    Team1Name VARCHAR(100) NOT NULL,
    Team2Name VARCHAR(100) NOT NULL,
    StadiumName VARCHAR(100) NOT NULL,
    InChampID INT,
    WinnerTeamName VARCHAR(100),
    POTMPlayerID INT,
    Date DATE NOT NULL,
    Scorecard TEXT,
    Result TEXT,
    CONSTRAINT fk_Team1Name FOREIGN KEY (Team1Name) REFERENCES Team(TeamName)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_Team2Name FOREIGN KEY (Team2Name) REFERENCES Team(TeamName)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_StadiumName FOREIGN KEY (StadiumName) REFERENCES Venue(StadiumName)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_WinnerTeamName FOREIGN KEY (WinnerTeamName) REFERENCES Team(TeamName)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_POTMPlayerID FOREIGN KEY (POTMPlayerID) REFERENCES Player(PlayerID)
        ON DELETE SET NULL ON UPDATE CASCADE
);

-- Umpire Table
CREATE TABLE Umpire (
    UmpireID INT PRIMARY KEY,
    FullName VARCHAR(100) NOT NULL,
    DateOfBirth DATE NOT NULL,
    Nationality VARCHAR(50),
    Matches INT DEFAULT 0 CHECK (Matches >= 0)
);

-- Performance Table
CREATE TABLE Performance (
    PlayerID INT,
    MatchID INT,
    RunsScored INT DEFAULT 0 CHECK (RunsScored >= 0),
    WicketsTaken INT DEFAULT 0 CHECK (WicketsTaken >= 0),
    StrikeRate DECIMAL(5, 2) CHECK (StrikeRate >= 0),
    Economy DECIMAL(5, 2) CHECK (Economy >= 0),
    CatchesTaken INT DEFAULT 0 CHECK (CatchesTaken >= 0),
    RunOuts INT DEFAULT 0 CHECK (RunOuts >= 0),
    PRIMARY KEY (PlayerID, MatchID),
    CONSTRAINT fk_PlayerID FOREIGN KEY (PlayerID) REFERENCES Player(PlayerID)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_MatchID FOREIGN KEY (MatchID) REFERENCES Match(MatchID)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- International Championship Table
CREATE TABLE InternationalChampionship (
    InChampID INT PRIMARY KEY,
    StartDate DATE NOT NULL,
    EndDate DATE NOT NULL CHECK (EndDate > StartDate),
    PastWinners TEXT,
    Format VARCHAR(50) NOT NULL
);

-- Umpires (Match Assignments) Table
CREATE TABLE Umpires (
    UmpireID INT,
    MatchID INT,
    PRIMARY KEY (UmpireID, MatchID),
    CONSTRAINT fk_UmpireID FOREIGN KEY (UmpireID) REFERENCES Umpire(UmpireID)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_MatchID FOREIGN KEY (MatchID) REFERENCES Match(MatchID)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- SponsoredBy (Sponsorship for Players) Table
CREATE TABLE SponsoredBy (
    SponsorID INT,
    PlayerID INT,
    PRIMARY KEY (SponsorID, PlayerID),
    CONSTRAINT fk_SponsorID FOREIGN KEY (SponsorID) REFERENCES Sponsorships(SponsorID)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_PlayerID FOREIGN KEY (PlayerID) REFERENCES Player(PlayerID)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- Sponsors (Sponsorship for Teams) Table
CREATE TABLE Sponsors (
    SponsorID INT,
    TeamName VARCHAR(100),
    PRIMARY KEY (SponsorID, TeamName),
    CONSTRAINT fk_SponsorID FOREIGN KEY (SponsorID) REFERENCES Sponsorships(SponsorID)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_TeamName FOREIGN KEY (TeamName) REFERENCES Team(TeamName)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- Plays Table (Teams playing Matches)
CREATE TABLE Plays (
    TeamName VARCHAR(100),
    MatchID INT,
    PRIMARY KEY (TeamName, MatchID),
    CONSTRAINT fk_TeamName FOREIGN KEY (TeamName) REFERENCES Team(TeamName)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_MatchID FOREIGN KEY (MatchID) REFERENCES Match(MatchID)
        ON DELETE CASCADE ON UPDATE CASCADE
);
