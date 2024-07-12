package lgrieg.service.db;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;


@AllArgsConstructor
public class Requests {
    final JdbcTemplate source;
    public void citiesWithMultipleAirports() throws SQLException {
        source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("select city, group_concat(airport_code) from" +
                    "(select distinct city, airport_code from airports) " +
                    "group by city having count(city) > 1");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("city") + resultSet.getString(2));
            }
        });
    }

    public void citiesWithCancelledFlights() throws SQLException {
        source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("select city, count(flight_id) as count from " +
                    "(select distinct departure_airport, flight_id, status from flights) as flight join " +
                    "((select distinct city, airport_code from airports) as airport) on " +
                    "departure_airport = airport_code where flight.status = 'Cancelled'" +
                    "group by city having count(city) > 0 order by count(city) desc");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("city") + resultSet.getInt("count"));
            }
        });
    }
}
