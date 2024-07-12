package lgrieg.service.dao;

import lgrieg.service.db.JdbcTemplate;
import lgrieg.domain.BoardingPass;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
@AllArgsConstructor
public class BoardingPassDao {
    private final JdbcTemplate source;

    private BoardingPass createBoardingPass(ResultSet resultSet) throws SQLException {
        return new BoardingPass(resultSet.getString("ticket_no"),
                resultSet.getInt("flight_id"),
                resultSet.getInt("boarding_no"),
                resultSet.getString("seat_no"));
    }
    /*
          ticket_no   | char(13)   | not null     | Номер билета
 flight_id   | integer    | not null     | Идентификатор рейса
 boarding_no | integer    | not null     | Номер посадочного талона
 seat_no     | varchar(4) | not null     | Номер места

    */
    public void saveBoardingPass(Collection<BoardingPass> boardingPasses) throws SQLException {
        source.preparedStatement("insert into boarding_passes(ticket_no, " +
                "flight_id, " +
                "boarding_no, " +
                "seat_no) values (?, ?, ?, ?)", insertBoardingPass -> {
            for (BoardingPass boardingPass : boardingPasses) {
                insertBoardingPass.setString(1, boardingPass.getTicketNo());
                insertBoardingPass.setInt(2, boardingPass.getFlightId());
                insertBoardingPass.setInt(3, boardingPass.getBoardingNo());
                insertBoardingPass.setString(4, boardingPass.getSeatNo());
                insertBoardingPass.execute();
            }
        });
    }

    public Set<BoardingPass> getBoardingPasses() throws SQLException {
        return source.statement(stmt -> {
            Set<BoardingPass> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("select * from boarding_passes");
            while (resultSet.next()) {
                result.add(createBoardingPass(resultSet));
            }
            return result;
        });
    }
}
