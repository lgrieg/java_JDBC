package lgrieg.service.dao;

import lgrieg.service.db.JdbcTemplate;
import lgrieg.domain.Aircraft;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
@AllArgsConstructor
public class AircraftDao {
    private final JdbcTemplate source;

    private Aircraft createAircraft(ResultSet resultSet) throws SQLException {
        return new Aircraft(resultSet.getString("aircraft_code"),
                resultSet.getString("model"),
                resultSet.getInt("range"));
    }

    public void saveAircraft(Collection<Aircraft> aircrafts) throws SQLException {
        source.preparedStatement("insert into aircrafts(aircraft_code, " +
                "model, " +
                "range) values (?, ?, ?)", insertAircraft -> {
            for (Aircraft aircraft : aircrafts) {
                insertAircraft.setString(1, aircraft.getAircraftCode());
                insertAircraft.setString(2, aircraft.getModel());
                insertAircraft.setInt(3, aircraft.getRange());
                insertAircraft.execute();
            }
        });
    }

    public Set<Aircraft> getAircrafts() throws SQLException {
        return source.statement(stmt -> {
            Set<Aircraft> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("select * from aircrafts");
            while (resultSet.next()) {
                result.add(createAircraft(resultSet));
            }
            return result;
        });
    }
}
