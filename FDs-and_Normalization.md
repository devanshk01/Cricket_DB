# Functional Dependencies:

### 1. Sponsorships Table
- **`SponsorID`** → `SponsorName`, `Amount`, `StartDate`, `EndDate`  
  _`SponsorID` uniquely determines the sponsor’s name, amount, start date, and end date._


### 2. Coaches Table
- **`CoachID`** → `HeadCoach`, `BowlingCoach`, `BattingCoach`, `FieldingCoach`  
  _`CoachID` uniquely determines each coaching role._


### 3. GoverningBody Table
- **`GoverningBodyName`** → `President`, `VicePresident`, `Secretary`, `Treasurer`, `Revenue`, `Headquarters`  
  _`GoverningBodyName` uniquely determines its leadership, revenue, and headquarters._


### 4. Team Table
- **`TeamName`** → `CoachID`, `GoverningBodyName`, `YearOfFoundation`, `NumberOfChampionships`, `TeamODIRank`, `TeamT20Rank`, `TeamTestRank`  
  _`TeamName` uniquely determines coach, governing body, foundation year, championships, and rankings._

  
### 5. Player Table
- **`PlayerID`** → `FullName`, `DateOfBirth`, `Nationality`, `Role`, `BattingStyle`, `BowlingStyle`, `MatchesPlayed`, `Runs`, `Wickets`, `Average`, `StrikeRate`, `Economy`, `PlayerODIRank`, `PlayerT20Rank`, `PlayerTestRank`, `TeamName`  
  _`PlayerID` uniquely determines all player attributes and associated team._


### 6. Venue Table
- **`StadiumName`** → `City`, `Capacity`  
  _`StadiumName` uniquely determines city and capacity._


### 7. Match Table
1. **`MatchID`** → `Team1Name`, `Team2Name`, `StadiumName`, `InChampID`, `WinnerTeamName`, `POTMPlayerID`, `Date`, `Scorecard`, `Result`  
   _`MatchID` uniquely determines teams, venue, championship, winner, player-of-the-match, date, scorecard, and result._  
2. **(`Team1Name`, `Team2Name`)** → `MatchID`  
   _Team combination uniquely identifies the match._  
3. **`WinnerTeamName`** → `MatchID`  
   _`WinnerTeamName` uniquely identifies the match._


### 8. Umpire Table
- **`UmpireID`** → `FullName`, `DateOfBirth`, `Nationality`, `Matches`  
  _`UmpireID` uniquely determines an umpire’s personal details and match count._


### 9. Performance Table
- **(`PlayerID`, `MatchID`)** → `RunsScored`, `WicketsTaken`, `StrikeRate`, `Economy`, `CatchesTaken`, `RunOuts`  
  _Player–match pair uniquely determines performance stats._


### 10. International Championship Table
- **`InChampID`** → `StartDate`, `EndDate`, `PastWinners`, `Format`  
  _`InChampID` uniquely determines start/end dates, past winners, and format._

---
# List of Anamolies:

### 1. UPDATE Anomalies
- **`TeamName` Change**  
  - If a team’s name changes, you must update **`TeamName`** in **`Player`**, **`Match`**, and **`Team`** tables. Missing one leads to inconsistent names.  
- **`Coach` Information**  
  - When a coach’s details (e.g. **`FullName`**, **`Nationality`**) change, every redundant copy must be updated. Otherwise **`CoachID`** references become inconsistent.  
- **`Player` Information**  
  - Changing a player’s details (e.g. **`MobileNumber`**, **`Email`**) requires updates in all tables where **`PlayerID`** appears. Redundant storage heightens inconsistency risk.  
- **`Sponsor` Details**  
  - If a sponsor’s attributes (e.g. **`SponsorName`**, **`Amount`**) change, you must update all related rows in **`Sponsorships`**, **`Team`**, and **`Match`** tables. Omitting one leads to conflicting sponsor data.


### 2. DELETE Anomalies
- **`Player` Deletion**  
  - Deleting from **`Player`** may cascade and remove related **`Performance`** or **`Match`** entries, losing historical stats.  
- **`Team` Deletion**  
  - Removing a row in **`Team`** can inadvertently delete **`Match`** and **`Performance`** records tied to **`TeamName`**, erasing match history.  
- **`Sponsor` Deletion**  
  - Deleting from **`Sponsorships`** might drop linked **`Team`** or **`Match`** records if foreign-key constraints cascade, losing team-sponsor relationships.  
- **`Match` Deletion**  
  - Removing a **`MatchID`** can delete all **`Performance`** rows for that match, erasing player performance history.


### 3. INSERT Anomalies
- **Adding a `Player`**  
  - You cannot insert into **`Player`** without an existing **`TeamName`**, forcing premature **`Team`** inserts or placeholder values.  
- **Adding a `Match`**  
  - Inserting a row in **`Match`** requires both **`Team1Name`** and **`Team2Name`** to exist, which may necessitate incomplete **`Team`** entries.  
- **Adding a `Coach`**  
  - If **`Team`** rows mandate a **`CoachID`**, you must insert a **`Coach`** first or use null/placeholder, causing dependency issues.  
- **Adding a `Sponsor`**  
  - Creating a **`Sponsorships`** entry may require referenced **`TeamName`** and **`PlayerID`** to exist, preventing independent sponsor insertion.

---

---

# List of Redundancies


### 1. Sponsorship Redundancies
● Multiple sponsorship records could exist for the same sponsor across various teams or players. For example, if a sponsor supports multiple teams or players, details like `SponsorName`, `Amount`, `StartDate`, and `EndDate` might be repeated for each sponsorship. This redundancy can be avoided by normalizing the sponsorship table or using junction tables that link sponsors to multiple entities (teams/players) without repeating the sponsor details.


### 2. Coach Redundancies
● Coach information (`CoachID`, names) is related to multiple teams. If a coach is associated with several teams, their details may be duplicated in those teams. For example, the same `HeadCoach`, `BowlingCoach`, `BattingCoach`, or `FieldingCoach` could be associated with multiple teams, leading to redundancy.


### 3. Player Information Redundancy in Performance Table
● The `PlayerID` in the `Performance` table refers to the player’s identity. If certain performance-related information (such as `FullName`, `Role`, or `Nationality`) is being stored repeatedly for each performance, then it's redundant, as player details are already stored in the `Player` table.  
● Instead, the performance should only capture the specific statistics for that match (e.g., `RunsScored`, `WicketsTaken`), with player details referred to via foreign keys.


### 4. Team Information in the Match Table
● Team information (`TeamName`) is stored in both the `Match` and `Team` tables. For each match, you need to reference two teams, but storing full team information in both places would be redundant. If the same information (like `YearOfFoundation`, `Rankings`) is repeatedly stored in the `Match` table or somewhere else, it should only reside in the `Team` table.


### 5. Umpire Information
● If umpire details (like their name, nationality, or date of birth) are repeated across multiple matches in the `Match-Umpire` relationship, it would result in redundancy. Since umpires are often associated with multiple matches, these details should only exist in the `Umpire` table and be referenced using foreign keys.


### 6. Venue Redundancy
● Venue information like the stadium name, city, or capacity is tied to multiple matches. If this information is repeated for each match (i.e., if match records store venue details instead of a reference to the venue), it would lead to redundancy. Instead, match records should only store a reference (foreign key) to the `Venue` table, which holds the actual venue details.


### 7. Governing Body Redundancy
● Governing body details like the name, president, and headquarters are related to multiple teams. If this information is repeated for each team or across other entities, it results in redundancy. The `GoverningBodyName` field should reference only unique governing body records.


### 8. Player Rankings
● Player rankings (`ODI Rank`, `T20 Rank`, `Test Rank`) are stored in the `Player` table. If ranking information is stored multiple times across other entities (e.g., in `Performance` or `Match` tables), it would be redundant. Rankings should only be updated in the `Player` table and referenced elsewhere.


### 9. Match Results Redundancy
● Winner information (which team won a match) and the `Player of the Match (POTM)` details could be redundant if stored in more than one place. If this information is repeated elsewhere, it should instead just reference the relevant `Match` record where the result and `Player of the Match` are already captured.


### 10. International Championship Data
● If details about the teams participating in a championship are repeated across multiple match records, it can result in redundancy. The `International Championship` entity should have references to the teams participating without storing redundant details like team names or rankings in every associated match.

---


# Normal Forms


### 1. Sponsorships Table:
● Attributes: `SponsorID` (PK), `SponsorName`, `Amount`, `StartDate`, `EndDate`  
● Primary Key: `SponsorID`  
● 1NF: Yes, all values are atomic.  
● 2NF: Yes, as the table has a single primary key (`SponsorID`), there can be no partial dependencies.  
● 3NF: Yes, no transitive dependencies exist. All non-prime attributes depend directly on the primary key.  
● BCNF: Yes, `SponsorID` is a superkey, and no other attribute determines anything else.


### 2. Coaches Table:
● Attributes: `CoachID` (PK), `HeadCoach`, `BowlingCoach`, `BattingCoach`, `FieldingCoach`  
● Primary Key: `CoachID`  
● 1NF: Yes, all values are atomic.  
● 2NF: Yes, there is a single key (`CoachID`), so no partial dependencies.  
● 3NF: Yes, all attributes are dependent on the primary key, no transitive dependencies.  
● BCNF: Yes, `CoachID` is a superkey, and no other attribute determines anything else.


### 3. GoverningBody Table:
● Attributes: `GoverningBodyName` (PK), `President`, `VicePresident`, `Secretary`, `Treasurer`, `Revenue`, `Headquarters`  
● Primary Key: `GoverningBodyName`  
● 1NF: Yes, all values are atomic.  
● 2NF: Yes, as there is a single primary key (`GoverningBodyName`), there can be no partial dependencies.  
● 3NF: Yes, no transitive dependencies are present.  
● BCNF: Yes, `GoverningBodyName` is a superkey, and no other attributes functionally determine any other attributes.


### 4. Team Table:
● Attributes: `TeamName` (PK), `CoachID` (FK), `GoverningBodyName` (FK), `YearOfFoundation`, `NumberOfChampionships`, `TeamODIRank`, `TeamT20Rank`, `TeamTestRank`  
● Primary Key: `TeamName`  
● 1NF: Yes, all values are atomic.  
● 2NF: Yes, the table has a single primary key, so no partial dependencies.  
● 3NF: Yes, all attributes are fully dependent on the primary key.  
● BCNF: Yes, `TeamName` is a superkey, and no other attributes (e.g., `CoachID`, `GoverningBodyName`) functionally determine other attributes.


### 5. Player Table:
● Attributes: `PlayerID` (PK), `FullName`, `DateOfBirth`, `Nationality`, `Role`, `BattingStyle`, `BowlingStyle`, `MatchesPlayed`, `Runs`, `Wickets`, `Average`, `StrikeRate`, `Economy`, `PlayerODIRank`, `PlayerT20Rank`, `PlayerTestRank`, `TeamName` (FK)  
● Primary Key: `PlayerID`  
● 1NF: Yes, all values are atomic.  
● 2NF: Yes, there is no partial dependency (since there is only one key, `PlayerID`).  
● 3NF: Yes, all attributes depend directly on the primary key `PlayerID`. `TeamName` is a foreign key but does not introduce transitive dependencies since it is related to a different entity.  
● BCNF: Yes, `PlayerID` is a superkey, and no other attributes (e.g., `TeamName`) functionally determine other attributes.


### 6. Venue Table:
● Attributes: `StadiumName` (PK), `City`, `Capacity`  
● Primary Key: `StadiumName`  
● 1NF: Yes, all values are atomic.  
● 2NF: Yes, since `StadiumName` is the primary key, there are no partial dependencies.  
● 3NF: Yes, all attributes are fully dependent on the primary key, and there are no transitive dependencies.  
● BCNF: Yes, `StadiumName` is a superkey, and no other attributes determine other attributes.


### 7. Match Table:
● Attributes: `MatchID` (PK), `Team1Name` (FK), `Team2Name` (FK), `StadiumName` (FK), `InChampID` (FK), `WinnerTeamName` (FK), `POTMPlayerID` (FK), `Date`, `Scorecard`, `Result`  
● Primary Key: `MatchID`  
● 1NF: Yes, all values are atomic.  
● 2NF: Yes, since the table has a single primary key (`MatchID`), there can be no partial dependencies.  
● 3NF: Yes, all non-key attributes are dependent on the primary key, and there are no transitive dependencies.  
● BCNF: Yes, `MatchID` is a superkey, and no other attributes determine anything else.


### 8. Umpire Table:
● Attributes: `UmpireID` (PK), `FullName`, `DateOfBirth`, `Nationality`, `Matches`  
● Primary Key: `UmpireID`  
● 1NF: Yes, all values are atomic.  
● 2NF: Yes, there is no partial dependency.  
● 3NF: Yes, no transitive dependencies exist.  
● BCNF: Yes, `UmpireID` is a superkey, and no other attributes determine other attributes.


### 9. Performance Table:
● Attributes: `PlayerID` (PK, FK), `MatchID` (PK, FK), `RunsScored`, `WicketsTaken`, `StrikeRate`, `Economy`, `CatchesTaken`, `RunOuts`  
● Primary Key: Composite key (`PlayerID`, `MatchID`)  
● 1NF: Yes, all values are atomic.  
● 2NF: Yes, since the composite key (`PlayerID`, `MatchID`) determines all non-prime attributes, there are no partial dependencies.  
● 3NF: Yes, all attributes are fully dependent on the composite key, and no transitive dependencies exist.  
● BCNF: Yes, the composite key (`PlayerID`, `MatchID`) is the superkey, and no other functional dependencies violate BCNF.


### 10. International Championship Table:
● Attributes: `InChampID` (PK), `StartDate`, `EndDate`, `PastWinners`, `Format`, `Teams`  
● Primary Key: `InChampID`  
● 1NF: Yes, all values are atomic.  
● 2NF: Yes, the single primary key `InChampID` determines all non-prime attributes.  
● 3NF: Yes, no transitive dependencies exist.  
● BCNF: Yes, `InChampID` is a superkey, and no other attributes determine other attributes.


---

### Conclusion: All Relations Are in BCNF

● **Reason:**  
- Every table satisfies 1NF, 2NF, and 3NF.  
- For each functional dependency `X → Y` in the tables, the left-hand side (`X`) is a superkey.  
- There are no cases of partial dependencies or transitive dependencies in any of the relations.  
- All attributes are fully dependent on their primary or composite keys, and no non-prime attributes are functionally determined by other non-prime attributes.


### Why No Further Normalization Is Needed:

1. **No anomalies exist:**  
   Since all relations are in BCNF, they do not suffer from any insert, update, or delete anomalies that arise from functional dependencies.

2. **Functional Dependencies are respected:**  
   All non-key attributes are functionally dependent on the whole key, meaning there are no unnecessary redundancies or dependencies to break down further.

3. **BCNF is sufficient:**  
   As BCNF is a stricter form of 3NF, achieving BCNF ensures the highest level of data integrity without needing further normalization. All functional dependencies have superkeys as their determinants, which means there are no violatio

