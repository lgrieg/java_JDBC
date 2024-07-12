package lgrieg.service.dao;

import lgrieg.service.db.JdbcTemplate;
import lgrieg.domain.TicketFlight;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
@AllArgsConstructor
public class TicketFlightDao {
    private final JdbcTemplate source;

    private TicketFlight createTicketFlight(ResultSet resultSet) throws SQLException {
        return new TicketFlight(resultSet.getString("ticket_no"),
                resultSet.getInt("flight_id"),
                resultSet.getString("fare_conditions"),
                resultSet.getDouble("amount"));
    }
    /*
 ticket_no       | char(13)      | not null     | Номер билета
 flight_id       | integer       | not null     | Идентификатор рейса
 fare_conditions | varchar(10)   | not null     | Класс обслуживания
 amount          | numeric(10,2) | not null     | Стоимость перелета

    */
    public void saveTicketFlight(Collection<TicketFlight> ticketFlights) throws SQLException {
        source.preparedStatement("insert into ticket_flights(ticket_no, " +
                "flight_id, " +
                "fare_conditions, " +
                "amount) values (?, ?, ?, ?)", insertTicketFlight -> {
            for (TicketFlight ticketFlight : ticketFlights) {
                insertTicketFlight.setString(1, ticketFlight.getTicketNo());
                insertTicketFlight.setInt(2, ticketFlight.getFlightId());
                insertTicketFlight.setString(3, ticketFlight.getFareConditions());
                insertTicketFlight.setDouble(4, ticketFlight.getAmount());
                insertTicketFlight.execute();
            }
        });
    }

    public Set<TicketFlight> getTicketFlights() throws SQLException {
        return source.statement(stmt -> {
            Set<TicketFlight> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("select * from ticket_flights");
            while (resultSet.next()) {
                result.add(createTicketFlight(resultSet));
            }
            return result;
        });
    }
}
