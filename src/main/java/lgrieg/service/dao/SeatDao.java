package lgrieg.service.dao;

import lgrieg.service.db.JdbcTemplate;
import lgrieg.domain.Seat;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
@AllArgsConstructor
public class SeatDao {
    private final JdbcTemplate source;

    private Seat createSeat(ResultSet resultSet) throws SQLException {
        return new Seat(resultSet.getString("aircraft_code"),
                resultSet.getString("seat_no"),
                resultSet.getString("fare_conditions"));
    }
    /*
 aircraft_code   | char(3)     | not null     | Код самолета, IATA
 seat_no         | varchar(4)  | not null     | Номер места
 fare_conditions | varchar(10) | not null     | Класс обслуживания

    */
    public void saveSeat(Collection<Seat> seats) throws SQLException {
        source.preparedStatement("insert into seats(aircraft_code, " +
                "seat_no, " +
                "fare_conditions) values (?, ?, ?)", insertSeat -> {
            for (Seat seat : seats) {
                insertSeat.setString(1, seat.getAircraftCode());
                insertSeat.setString(2, seat.getSeatNo());
                insertSeat.setString(3, seat.getFareConditions());
                insertSeat.execute();
            }
        });
    }

    public Set<Seat> getSeats() throws SQLException {
        return source.statement(stmt -> {
            Set<Seat> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("select * from seats");
            while (resultSet.next()) {
                result.add(createSeat(resultSet));
            }
            return result;
        });
    }
}
