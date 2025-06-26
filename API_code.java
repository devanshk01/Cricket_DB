import java.sql.*;
import java.util.Scanner;

public class API_code {
    public static void main(String[] args) {
        try {
            int flag = 0; //to check whether user wants to exit or not.
            Scanner s = new Scanner(System.in);
            Class.forName("org.postgresql.Driver");

            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "Devansh$$";  // Add your original password

            Connection conn = DriverManager.getConnection(url, user, password);

            Statement stmt = conn.createStatement();
            stmt.execute("SET search_path TO cricket_db");

            System.out.println("Enter the query number that you want to execute (1-20) (ENTER 0 TO EXIT!): ");
            System.out.println(
                    "1. Find the player with the most wickets in a given series or tournament and their team information.\n");
            System.out.println(
                    "2. List teams and players who share the same sponsor, showing the sponsor’s name and sponsorship amount.\n");
            System.out.println(
                    "3. Find umpires who have officiated in matches where a specific team (e.g., 'India', ‘Australia’) played.\n");
            System.out.println("4. List venues where a specific team (e.g., 'India') has played atleast 1 match.\n");
            System.out.println(
                    "5. Find players who have taken more than 2 wickets and have an average economy rate below 6.0 runs per over in the matches they played.\n");
            System.out.println(
                    "6. Find teams that have an average number of matches won per year (since foundation) greater than 0. (Since we have inserted less number of rows in Match Table, we have to keep matches won per year > 0, but with enough number of rows, it could be kept a desired value)\n");
            System.out.println(
                    "7. List the players who have won more than 1 \"Player of the Match\" awards and have an average of at least 1 wickets per match.\n");
            System.out.println(
                    "8. List teams with an average player strike rate greater than 80, along with their average strike rate and number of players.\n");
            System.out.println(
                    "9. Find the names and the number of matches of the stadiums that have hosted more number of matches than the average matches played at each stadium.\n");
            System.out.println("10. Find the player with the most wickets in each team.\n");
            System.out.println(
                    "11. Find umpires who have officiated more matches than the average number of matches officiated by all umpires.\n");
            System.out.println(
                    "12. Find the matches where the total runs are greater than the average total runs in all matches.\n");
            System.out.println("13. Find the player with most runs in each team.\n");
            System.out.println(
                    "14. Find teams with total sponsorship amount greater than the average sponsorship amount.\n");
            System.out.println("15. Find the coaches of the teams in the Top 3 for T20 Rankings.\n");
            System.out.println(
                    "16. Find the players with more career Wickets and Runs than their team's average performance metrics.\n");
            System.out.println("17. Find the number of distinct venues where players have played.\n");
            System.out.println("18. Find teams that have participated in every international championship.\n");
            System.out.println("19. Find coaches of the teams that have played in all international championships.\n");
            System.out.println(
                    "20. Find players who have played more matches than the average number of matches played by all players.\n");
            
            String sql;
            ResultSet rs;
            while (true) {
                int choice = s.nextInt();
                switch (choice) {
                    case 0:
                        System.out.println("Thank You for using this database! Exiting Now...");
                        flag = 1;
                        break;
                    case 1:
                        sql = "SELECT p.fullname, t.teamname, SUM(MP.WICKETStaken) AS TOTAL_WICKETS FROM MATCH M JOIN performance MP ON M.MATCHID = MP.MATCHID JOIN PLAYER P ON MP.PLAYERID = P.PLAYERID JOIN TEAM T ON P.TEAMNAME = T.TEAMNAME GROUP BY P.FULLNAME, T.TEAMNAME ORDER BY TOTAL_WICKETS DESC limit 1";

                        rs = stmt.executeQuery(sql);

                        while (rs.next()) {
                            String name = rs.getString("fullname"); // Not "p.fullname"
                            String name2 = rs.getString("teamname"); // Not "t.teamname"
                            int wkt = rs.getInt("total_wickets"); // Not "total_wickets"
                            System.out.println("Full Name: " + name + ", Team Name: " + name2 +
                                    ", Total Wickets: " + wkt);
                        }

                        rs.close();
                        break;

                    case 2:
                        sql = "SELECT T.TEAMNAME, P.FULLNAME, S.SPONSORNAME, S.Amount FROM Team T JOIN Sponsors SP ON T.TEAMNAME = SP.TeamName JOIN Sponsorships S ON SP.SponsorID = S.SponsorID JOIN SponsoredBy SB ON S.SponsorID = SB.SponsorID JOIN Player P ON SB.PlayerID = P.PlayerID";

                        rs = stmt.executeQuery(sql);

                        while (rs.next()) {
                            int amt = rs.getInt("amount");
                            String tname = rs.getString("teamname");
                            String fname = rs.getString("fullname");
                            String sname = rs.getString("sponsorname");
                            System.out.println("Teamname: " + tname + ", Fullname: " + fname + ", Sponsorname: " + sname
                                    + ", Amount: " + amt);
                        }
                        rs.close();
                        break;

                    case 3:
                        sql = "SELECT U.FullName AS UmpireName, M.MATCHID, M.DATE, M.Team1Name, M.Team2Name " +
                                "FROM Umpire U " +
                                "JOIN Umpires UM ON U.UmpireID = UM.UmpireID " +
                                "JOIN Match M ON UM.MatchID = M.MatchID " +
                                "WHERE M.Team1Name = 'India' OR M.Team2Name = 'India' OR M.Team1Name = 'Australia' OR M.Team2Name = 'Australia' "
                                +
                                "ORDER BY M.DATE DESC";

                        rs = stmt.executeQuery(sql);

                        while (rs.next()) {
                            String umpireName = rs.getString("UmpireName");
                            int matchID = rs.getInt("MATCHID");
                            Date matchDate = rs.getDate("DATE");
                            String team1 = rs.getString("Team1Name");
                            String team2 = rs.getString("Team2Name");

                            System.out
                                    .println("Umpire: " + umpireName + ", MatchID: " + matchID + ", Date: " + matchDate
                                            + ", Team 1: " + team1 + ", Team 2: " + team2);
                        }
                        rs.close();

                        break;

                    case 4:
                        sql = "SELECT V.StadiumName, V.City, COUNT(M.MatchID) AS MatchesPlayed " +
                                "FROM Venue V " +
                                "JOIN Match M ON V.StadiumName = M.StadiumName " +
                                "WHERE M.Team1Name = 'India' OR M.Team2Name = 'India' " +
                                "GROUP BY V.StadiumName, V.City " +
                                "HAVING COUNT(M.MatchID) > 0";

                        rs = stmt.executeQuery(sql);

                        while (rs.next()) {
                            String stadiumName = rs.getString("StadiumName");
                            String city = rs.getString("City");
                            int matchesPlayed = rs.getInt("MatchesPlayed");

                            System.out.println(
                                    "Stadium: " + stadiumName + ", City: " + city + ", Matches Played: "
                                            + matchesPlayed);
                        }
                        rs.close();

                        break;

                    case 5:
                        sql = "SELECT P.FULLNAME, P.TEAMNAME, SUM(Perf.WicketsTaken) AS TotalWickets, AVG(Perf.Economy) AS AvgEconomyRate "
                                +
                                "FROM Player P " +
                                "JOIN Performance Perf ON P.PlayerID = Perf.PlayerID " +
                                "GROUP BY P.FULLNAME, P.TEAMNAME " +
                                "HAVING SUM(Perf.WicketsTaken) > 2 AND AVG(Perf.Economy) < 6.0";

                        rs = stmt.executeQuery(sql);

                        while (rs.next()) {
                            String fullName = rs.getString("FULLNAME");
                            String teamName = rs.getString("TEAMNAME");
                            int totalWickets = rs.getInt("TotalWickets");
                            double avgEconomyRate = rs.getDouble("AvgEconomyRate");

                            System.out.println("Player: " + fullName + ", Team: " + teamName + ", Total Wickets: "
                                    + totalWickets + ", Average Economy Rate: " + avgEconomyRate);
                        }
                        rs.close();

                        break;

                    case 6:
                        sql = "SELECT T.TEAMNAME, COUNT(M.MATCHID) AS TotalWins, " +
                                "(COUNT(M.MATCHID) / (EXTRACT(YEAR FROM CURRENT_DATE) - T.YearOfFoundation)) AS WinsPerYear "
                                +
                                "FROM Team T " +
                                "JOIN Match M ON T.TEAMNAME = M.WinnerTeamName " +
                                "GROUP BY T.TEAMNAME, T.YearOfFoundation " +
                                "HAVING (COUNT(M.MATCHID) / (EXTRACT(YEAR FROM CURRENT_DATE) - T.YearOfFoundation)) > 0";

                        rs = stmt.executeQuery(sql);

                        while (rs.next()) {
                            String teamName = rs.getString("TEAMNAME");
                            int totalWins = rs.getInt("TotalWins");
                            double winsPerYear = rs.getDouble("WinsPerYear");

                            System.out.println(
                                    "Team: " + teamName + ", Total Wins: " + totalWins + ", Wins Per Year: "
                                            + winsPerYear);
                        }
                        rs.close();

                        break;

                    case 7:
                        sql = "SELECT P.FULLNAME, COUNT(M.POTMPlayerID) AS PlayerOfTheMatchAwards, AVG(Perf.WicketsTaken) AS AvgWicketsPerMatch "
                                +
                                "FROM Player P " +
                                "JOIN Match M ON P.PlayerID = M.POTMPlayerID " +
                                "JOIN Performance Perf ON P.PlayerID = Perf.PlayerID " +
                                "GROUP BY P.FULLNAME " +
                                "HAVING COUNT(M.POTMPlayerID) > 1 AND AVG(Perf.WicketsTaken) >= 1";

                        rs = stmt.executeQuery(sql);

                        while (rs.next()) {
                            String fullName = rs.getString("FULLNAME");
                            int playerOfTheMatchAwards = rs.getInt("PlayerOfTheMatchAwards");
                            double avgWicketsPerMatch = rs.getDouble("AvgWicketsPerMatch");

                            System.out.println("Player: " + fullName + ", Player of the Match Awards: "
                                    + playerOfTheMatchAwards + ", Average Wickets per Match: " + avgWicketsPerMatch);
                        }
                        rs.close();

                        break;

                    case 8:
                        sql = "SELECT T.TEAMNAME, AVG(P.StrikeRate) AS AvgStrikeRate, COUNT(P.PlayerID) AS NumberOfPlayers "
                                +
                                "FROM Team T " +
                                "JOIN Player P ON T.TEAMNAME = P.TEAMNAME " +
                                "GROUP BY T.TEAMNAME " +
                                "HAVING AVG(P.StrikeRate) > 80";

                        rs = stmt.executeQuery(sql);

                        while (rs.next()) {
                            String teamName = rs.getString("TEAMNAME");
                            double avgStrikeRate = rs.getDouble("AvgStrikeRate");
                            int numberOfPlayers = rs.getInt("NumberOfPlayers");

                            System.out.println("Team: " + teamName + ", Average Strike Rate: " + avgStrikeRate
                                    + ", Number of Players: " + numberOfPlayers);
                        }
                        rs.close();

                        break;

                    case 9:
                        sql = "SELECT stadiumname, COUNT(matchid) AS cnt " +
                                "FROM MATCH " +
                                "GROUP BY stadiumname " +
                                "HAVING COUNT(matchid) > ( " +
                                "    SELECT AVG(match_count) " +
                                "    FROM ( " +
                                "        SELECT stadiumname, COUNT(matchid) AS match_count " +
                                "        FROM MATCH " +
                                "        GROUP BY stadiumname " +
                                "    ) AS stadium_matches " +
                                ")";

                        rs = stmt.executeQuery(sql);

                        while (rs.next()) {
                            String stadiumName = rs.getString("stadiumname");
                            int matchCount = rs.getInt("cnt");

                            System.out.println("Stadium: " + stadiumName + ", Matches Played: " + matchCount);
                        }
                        rs.close();

                        break;

                    case 10:
                        sql = "SELECT P.FULLNAME, P.TEAMNAME, SUM(Perf.WicketsTaken) AS TotalWickets " +
                                "FROM Player P " +
                                "JOIN Performance Perf ON P.PlayerID = Perf.PlayerID " +
                                "GROUP BY P.FULLNAME, P.TEAMNAME " +
                                "HAVING SUM(Perf.WicketsTaken) = (SELECT MAX(TotalWickets) " +
                                "FROM (SELECT SUM(Perf2.WicketsTaken) AS TotalWickets " +
                                "FROM Player P2 " +
                                "JOIN Performance Perf2 ON P2.PlayerID = Perf2.PlayerID " +
                                "WHERE P2.TEAMNAME = P.TEAMNAME " +
                                "GROUP BY P2.PlayerID) AS TeamWicketCount)";

                        rs = stmt.executeQuery(sql);

                        while (rs.next()) {
                            String fullName = rs.getString("FULLNAME");
                            String teamName = rs.getString("TEAMNAME");
                            int totalWickets = rs.getInt("TotalWickets");

                            System.out.println(
                                    "Player: " + fullName + ", Team: " + teamName + ", Total Wickets: " + totalWickets);
                        }
                        rs.close();

                        break;

                    case 11:
                        sql = "SELECT U.FullName, COUNT(UM.MatchID) AS MatchesOfficiated " +
                                "FROM Umpire U " +
                                "JOIN Umpires UM ON U.UmpireID = UM.UmpireID " +
                                "GROUP BY U.FullName " +
                                "HAVING COUNT(UM.MatchID) > (SELECT AVG(MatchesOfficiated) " +
                                "FROM (SELECT COUNT(UM2.MatchID) AS MatchesOfficiated " +
                                "FROM Umpires UM2 " +
                                "GROUP BY UM2.UmpireID) AS UmpireMatches)";

                        rs = stmt.executeQuery(sql);

                        while (rs.next()) {
                            String fullName = rs.getString("FullName");
                            int matchesOfficiated = rs.getInt("MatchesOfficiated");

                            System.out.println("Umpire: " + fullName + ", Matches Officiated: " + matchesOfficiated);
                        }
                        rs.close();

                        break;

                    case 12:
                        sql = "SELECT MATCHID, TOTAL_MATCH_RUNS " +
                                "FROM (" +
                                "SELECT MATCHID, SUM(RUNSSCORED) AS TOTAL_MATCH_RUNS " +
                                "FROM PERFORMANCE " +
                                "GROUP BY MATCHID " +
                                ") AS MATCH_RUNS " +
                                "WHERE TOTAL_MATCH_RUNS > ( " +
                                "SELECT AVG(TOTAL_MATCH_RUNS) " +
                                "FROM ( " +
                                "SELECT SUM(RUNSSCORED) AS TOTAL_MATCH_RUNS " +
                                "FROM PERFORMANCE " +
                                "GROUP BY MATCHID " +
                                ") AS AVG_MATCH_RUNS " +
                                ")";

                        rs = stmt.executeQuery(sql);

                        while (rs.next()) {
                            int matchID = rs.getInt("MATCHID");
                            int totalMatchRuns = rs.getInt("TOTAL_MATCH_RUNS");

                            System.out.println("MatchID: " + matchID + ", Total Match Runs: " + totalMatchRuns);
                        }
                        rs.close();

                        break;

                    case 13:
                        sql = "SELECT p.playerid, p.fullname, p.teamname, p.runs " +
                                "FROM player AS p " +
                                "JOIN ( " +
                                "SELECT teamname, MAX(runs) AS max_runs " +
                                "FROM player " +
                                "GROUP BY teamname " +
                                ") AS max_runs_per_team " +
                                "ON p.teamname = max_runs_per_team.teamname " +
                                "AND p.runs = max_runs_per_team.max_runs";

                        rs = stmt.executeQuery(sql);

                        while (rs.next()) {
                            int playerId = rs.getInt("playerid");
                            String fullName = rs.getString("fullname");
                            String teamName = rs.getString("teamname");
                            int runs = rs.getInt("runs");

                            System.out.println(
                                    "Player ID: " + playerId + ", Full Name: " + fullName + ", Team: " + teamName
                                            + ", Runs: " + runs);
                        }
                        rs.close();

                        break;

                    case 14:
                        sql = "SELECT TEAMNAME, TOTAL_SPONSORSHIP " +
                                "FROM ( " +
                                "SELECT T.TEAMNAME, SUM(SP.AMOUNT) AS TOTAL_SPONSORSHIP " +
                                "FROM SPONSORS S " +
                                "JOIN SPONSORSHIPS SP ON S.SPONSORID = SP.SPONSORID " +
                                "JOIN TEAM T ON S.TEAMNAME = T.TEAMNAME " +
                                "GROUP BY T.TEAMNAME " +
                                ") AS TEAM_SPONSORSHIPS " +
                                "WHERE TOTAL_SPONSORSHIP > ( " +
                                "SELECT AVG(TOTAL_SPONSORSHIP) " +
                                "FROM ( " +
                                "SELECT SUM(SP.AMOUNT) AS TOTAL_SPONSORSHIP " +
                                "FROM SPONSORS S " +
                                "JOIN SPONSORSHIPS SP ON S.SPONSORID = SP.SPONSORID " +
                                "JOIN TEAM T ON S.TEAMNAME = T.TEAMNAME " +
                                "GROUP BY T.TEAMNAME " +
                                ") AS AVG_SPONSORSHIP " +
                                ")";

                        rs = stmt.executeQuery(sql);

                        while (rs.next()) {
                            String teamName = rs.getString("TEAMNAME");
                            double totalSponsorship = rs.getDouble("TOTAL_SPONSORSHIP");

                            System.out.println("Team: " + teamName + ", Total Sponsorship: " + totalSponsorship);
                        }
                        rs.close();

                        break;

                    case 15:
                        sql = "SELECT C.COACHID, C.HEADCOACH " +
                                "FROM COACHES C " +
                                "WHERE EXISTS ( " +
                                "SELECT 1 " +
                                "FROM TEAM T " +
                                "WHERE T.COACHID = C.COACHID " +
                                "AND T.TEAMT20RANK <= 3 " +
                                ")";

                        rs = stmt.executeQuery(sql);

                        while (rs.next()) {
                            int coachId = rs.getInt("COACHID");
                            String headCoach = rs.getString("HEADCOACH");

                            System.out.println("Coach ID: " + coachId + ", Head Coach: " + headCoach);
                        }
                        rs.close();

                        break;

                    case 16:
                        sql = "SELECT PLAYERID, FULLNAME, RUNS, WICKETS " +
                                "FROM PLAYER P1 " +
                                "WHERE RUNS > ( " +
                                "SELECT AVG(RUNS) " +
                                "FROM PLAYER P2 " +
                                "WHERE P2.TEAMNAME = P1.TEAMNAME " +
                                ") AND WICKETS > ( " +
                                "SELECT AVG(WICKETS) " +
                                "FROM PLAYER P3 " +
                                "WHERE P3.TEAMNAME = P1.TEAMNAME " +
                                ")";

                        rs = stmt.executeQuery(sql);

                        while (rs.next()) {
                            int playerId = rs.getInt("PLAYERID");
                            String fullName = rs.getString("FULLNAME");
                            int runs = rs.getInt("RUNS");
                            int wickets = rs.getInt("WICKETS");

                            System.out.println("Player ID: " + playerId + ", Full Name: " + fullName + ", Runs: " + runs
                                    + ", Wickets: " + wickets);
                        }
                        rs.close();

                        break;

                    case 17:
                        sql = "SELECT P.FULLNAME, P.TEAMNAME, COUNT(DISTINCT M.StadiumName) AS VenuesPlayed " +
                                "FROM Player P " +
                                "JOIN Performance Perf ON P.PlayerID = Perf.PlayerID " +
                                "JOIN Match M ON Perf.MatchID = M.MatchID " +
                                "GROUP BY P.FULLNAME, P.TEAMNAME " +
                                "ORDER BY VenuesPlayed DESC";

                        rs = stmt.executeQuery(sql);

                        while (rs.next()) {
                            String fullName = rs.getString("FULLNAME");
                            String teamName = rs.getString("TEAMNAME");
                            int venuesPlayed = rs.getInt("VenuesPlayed");

                            System.out.println(
                                    "Player: " + fullName + ", Team: " + teamName + ", Venues Played: " + venuesPlayed);
                        }
                        rs.close();

                        break;

                    case 18:
                        sql = "SELECT T.TeamName " +
                                "FROM Team T " +
                                "WHERE NOT EXISTS ( " +
                                "SELECT IC.InChampID " +
                                "FROM (SELECT 1 AS InChampID UNION ALL SELECT 2 AS InChampID) IC " +
                                "WHERE NOT EXISTS ( " +
                                "SELECT 1 " +
                                "FROM Plays P " +
                                "JOIN Match M ON P.MatchID = M.MatchID " +
                                "WHERE T.TeamName = P.TeamName " +
                                "AND M.InChampID = IC.InChampID " +
                                ") " +
                                ")";

                        rs = stmt.executeQuery(sql);

                        while (rs.next()) {
                            String teamName = rs.getString("TeamName");
                            System.out.println("Team: " + teamName);
                        }
                        rs.close();

                        break;

                    case 19:
                        sql = "SELECT C.COACHID, C.HEADCOACH " +
                                "FROM COACHES C " +
                                "WHERE NOT EXISTS ( " +
                                "SELECT IC.INCHAMPID " +
                                "FROM INTERNATIONALCHAMPIONSHIP IC " +
                                "WHERE EXISTS ( " +
                                "SELECT 1 " +
                                "FROM MATCH M " +
                                "JOIN TEAM T ON M.WINNERTEAMNAME = T.TEAMNAME " +
                                "WHERE T.COACHID = C.COACHID " +
                                "AND M.INCHAMPID = IC.INCHAMPID " +
                                ") " +
                                "AND NOT EXISTS ( " +
                                "SELECT 1 " +
                                "FROM MATCH M " +
                                "WHERE M.INCHAMPID = IC.INCHAMPID " +
                                "AND M.WINNERTEAMNAME <> ( " +
                                "SELECT T2.TEAMNAME " +
                                "FROM TEAM T2 " +
                                "WHERE T2.COACHID = C.COACHID " +
                                ") " +
                                ") " +
                                ")";

                        rs = stmt.executeQuery(sql);

                        while (rs.next()) {
                            int coachId = rs.getInt("COACHID");
                            String headCoach = rs.getString("HEADCOACH");

                            System.out.println("Coach ID: " + coachId + ", Head Coach: " + headCoach);
                        }
                        rs.close();

                        break;

                    case 20:
                        sql = "SELECT P.FULLNAME, COUNT(Perf.MatchID) AS MatchesPlayed " +
                                "FROM Player P " +
                                "JOIN Performance Perf ON P.PlayerID = Perf.PlayerID " +
                                "GROUP BY P.FULLNAME " +
                                "HAVING COUNT(Perf.MatchID) > " +
                                "(SELECT AVG(MatchCount) " +
                                "FROM (SELECT COUNT(Perf2.MatchID) AS MatchCount " +
                                "FROM Player P2 " +
                                "JOIN Performance Perf2 ON P2.PlayerID = Perf2.PlayerID " +
                                "GROUP BY P2.PlayerID) AS PlayerMatches)";

                        rs = stmt.executeQuery(sql);

                        while (rs.next()) {
                            String fullName = rs.getString("FULLNAME");
                            int matchesPlayed = rs.getInt("MatchesPlayed");

                            System.out.println("Player: " + fullName + ", Matches Played: " + matchesPlayed);
                        }
                        rs.close();

                        break;
                }
                if(flag == 1) break;
            }
            s.close();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }
}

/*
 * cd "D:\Java Programs\jdk-21.0.1"
 * javac --release 8 -cp ".;D:\Java Programs\jdbc\postgresql-42.7.4.jar"
 * api.java
 * java -cp ".;D:\Java Programs\jdbc\postgresql-42.7.4.jar" api
 */