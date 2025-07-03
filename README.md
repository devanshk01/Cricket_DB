
# 🏏 Cricket_DB

A fully normalized SQL-based relational database schema to manage a wide spectrum of cricket-related entities including players, matches, teams, venues, and governing bodies.

---

## 📦 Database Schema Overview

The schema consists of 10+ interrelated tables designed with referential integrity, cascading rules, domain constraints, and normalization principles:

### 📋 Tables and Key Columns

- **Sponsorships**
  - `SponsorID` (PK), `SponsorName`, `Amount`, `StartDate`, `EndDate`

- **Coaches**
  - `CoachID` (PK), `HeadCoach`, `BowlingCoach`, `BattingCoach`, `FieldingCoach`

- **GoverningBody**
  - `GoverningBodyName` (PK), `President`, `VicePresident`, `CEO`, `Treasurer`, `Revenue`, `Headquarters`

- **Team**
  - `TeamName` (PK), `CoachID` (FK), `GoverningBodyName` (FK), `YearOfFoundation`, `NumberOfChampionships`,
    `TeamODIRank`, `TeamT20Rank`, `TeamTestRank`

- **Player**
  - `PlayerID` (PK), `FullName`, `DateOfBirth`, `Nationality`, `Role`, `BattingStyle`, `BowlingStyle`,
    `MatchesPlayed`, `Runs`, `Wickets`, `Average`, `StrikeRate`, `Economy`, `PlayerODIRank`, `PlayerT20Rank`,
    `PlayerTestRank`, `TeamName` (FK)

- **Venue**
  - `StadiumName` (PK), `City`, `Capacity`

- **Match**
  - `MatchID` (PK), `Team1Name` (FK), `Team2Name` (FK), `StadiumName` (FK), `InChampID`, `WinnerTeamName` (FK),
    `POTMPlayerID` (FK), `Date`, `Scorecard`, `Result`

- **Umpire**
  - `UmpireID` (PK), `FullName`, `DateOfBirth`, `Nationality`, `Matches`

- **Performance**
  - Composite PK: (`PlayerID`, `MatchID`), `RunsScored`, `WicketsTaken`, `StrikeRate`, `Economy`, `CatchesTaken`, `RunOuts`

- **InternationalChampionship**
  - `InChampID` (PK), `StartDate`, `EndDate`, `PastWinners`, `Format`

- **Umpires**
  - Composite PK: (`UmpireID`, `MatchID`)

- **SponsoredBy**
  - Composite PK: (`SponsorID`, `PlayerID`)

- **Sponsors**
  - Composite PK: (`SponsorID`, `TeamName`)

- **Plays**
  - Composite PK: (`TeamName`, `MatchID`)

---

## ⚙️ Running the Schema

1. **Clone the Repository**  
   ```bash
   git clone https://github.com/devanshk01/Cricket_DB.git
   cd Cricket_DB
   ```

2. **Run SQL DDL Script**  
   Ensure you are connected to your SQL client or DBMS:

   ```sql
   CREATE DATABASE CricketDB;
   USE CricketDB;
   -- Paste DDL commands from DDL-script.sql
   ```

3. **Verify Foreign Keys & Constraints**  
   Many tables enforce ON DELETE/UPDATE CASCADE or SET NULL for referential integrity.

---

## 🔍 Suggested SQL Queries

- **All-rounders Ranked by Strike Rate**
  ```sql
  SELECT FullName, StrikeRate FROM Player
  WHERE Role = 'All-rounder'
  ORDER BY StrikeRate DESC;
  ```

- **Team-wise Match Participation**
  ```sql
  SELECT TeamName, COUNT(MatchID) AS Matches
  FROM Plays
  GROUP BY TeamName;
  ```

- **Player Match Performance Summary**
  ```sql
  SELECT p.FullName, pf.RunsScored, pf.WicketsTaken
  FROM Player p
  JOIN Performance pf ON p.PlayerID = pf.PlayerID
  WHERE pf.MatchID = 101;
  ```

---

## 🧠 Design Features

- Composite primary keys in many-to-many mapping tables
- Rich use of CHECK constraints for domain validation
- Use of YEAR and DATE types for temporal accuracy
- Role and Rank-based player tracking
- Governance structure embedded via `GoverningBody` and `Coaches`

---

## 🤝 Contributors

- Devansh Kukadia – 202303030  
- Frinad Kandoriya – 202303044  
- Yash Shah – 202303004

