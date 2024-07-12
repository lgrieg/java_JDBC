package lgrieg.service.dao;

import lgrieg.service.db.JdbcTemplate;
import lgrieg.domain.Airport;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
@AllArgsConstructor
public class AirportDao {
    private final JdbcTemplate source;

    private Airport createAirport(ResultSet resultSet) throws SQLException {
        return new Airport(resultSet.getString("airport_code"),
                resultSet.getString("airport_name"),
                resultSet.getString("city"),
                resultSet.getString("coordinates"),
                resultSet.getString("timezone"));
    }
/*
     airport_code | char(3) | not null     | Код аэропорта
 airport_name | jsonb   | not null     | Название аэропорта
 city         | jsonb   | not null     | Город
 coordinates  | point   | not null     | Координаты аэропорта (долгота и широта)
 timezone
*/
    public void saveAirport(Collection<Airport> airports) throws SQLException {
        source.preparedStatement("insert into airports(airport_code, " +
                "airport_name, " +
                "city, " +
                "coordinates, " +
                "timezone) values (?, ?, ?, ?, ?)", insertAirport -> {
            for (Airport airport : airports) {
                insertAirport.setString(1, airport.getAirportCode());
                insertAirport.setString(2, airport.getAirportName());
                insertAirport.setString(3, airport.getCity());
                insertAirport.setString(4, airport.getCoordinates());
                insertAirport.setString(5, airport.getTimezone());
                insertAirport.execute();
            }
        });
    }

    public Set<Airport> getAirports() throws SQLException {
        return source.statement(stmt -> {
            Set<Airport> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("select * from airports");
            while (resultSet.next()) {
                result.add(createAirport(resultSet));
            }
            return result;
        });
    }
}
