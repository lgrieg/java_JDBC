package lgrieg.service.dao;

import lgrieg.service.db.JdbcTemplate;
import lgrieg.domain.Flight;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
@AllArgsConstructor
public class FlightDao {
    private final JdbcTemplate source;

    private Flight createFlight(ResultSet resultSet) throws SQLException {
        return new Flight(resultSet.getInt("flight_id"),
                resultSet.getString("flight_no"),
                resultSet.getString("scheduled_departure"),
                resultSet.getString("scheduled_arrival"),
                resultSet.getString("departure_airport"),
                resultSet.getString("arrival_airport"),
                resultSet.getString("status"),
                resultSet.getString("aircraft_code"),
                resultSet.getString("actual_departure"),
                resultSet.getString("actual_arrival"));
    }
    /*
       flight_id           | serial      | not null     | Идентификатор рейса
 flight_no           | char(6)     | not null     | Номер рейса
 scheduled_departure | timestamptz | not null     | Время вылета по расписанию
 scheduled_arrival   | timestamptz | not null     | Время прилёта по расписанию
 departure_airport   | char(3)     | not null     | Аэропорт отправления
 arrival_airport     | char(3)     | not null     | Аэропорт прибытия
 status              | varchar(20) | not null     | Статус рейса
 aircraft_code       | char(3)     | not null     | Код самолета, IATA
 actual_departure    | timestamptz |              | Фактическое время вылета
 actual_arrival      | timestamptz |              | Фактическое время прилёта

    */
    public void saveFlight(Collection<Flight> flights) throws SQLException {
        source.preparedStatement("insert into flights(flight_id, " +
                "flight_no, " +
                "scheduled_departure, " +
                "scheduled_arrival, " +
                "departure_airport, " +
                "arrival_airport, " +
                "status, " +
                "aircraft_code, " +
                "actual_departure, " +
                "actual_arrival) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", insertFlight -> {
            for (Flight flight : flights) {
                insertFlight.setInt(1, flight.getFlightId());
                insertFlight.setString(2, flight.getFlightNo());
                insertFlight.setString(3, flight.getScheduledDeparture());
                insertFlight.setString(4, flight.getScheduledArrival());
                insertFlight.setString(5, flight.getDepartureAirport());
                insertFlight.setString(6, flight.getArrivalAirport());
                insertFlight.setString(7, flight.getStatus());
                insertFlight.setString(8, flight.getAircraftCode());
                insertFlight.setString(9, flight.getActualDeparture());
                insertFlight.setString(10, flight.getActualArrival());
                insertFlight.execute();
            }
        });
    }

    public Set<Flight> getFlights() throws SQLException {
        return source.statement(stmt -> {
            Set<Flight> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("select * from flights");
            while (resultSet.next()) {
                result.add(createFlight(resultSet));
            }
            return result;
        });
    }
}
