package lgrieg;
import lgrieg.domain.Aircraft;
import lgrieg.domain.Flight;
import lgrieg.service.dao.AircraftDao;
import lgrieg.service.dao.FlightDao;
import lgrieg.service.db.DbInit;
import lgrieg.service.db.FillDb;
import lgrieg.service.db.JdbcTemplate;
import lgrieg.service.db.Requests;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.*;

public class DatabaseTest {
    private final String url = "jdbc:sqlite:src/main/resources/database.sqlite";
    private final String path = "src/main/resources/dbcreate.sql";
    private final JdbcTemplate source = new JdbcTemplate(Driver.createDataSource(url));

    @BeforeEach
    void setupDB() throws IOException, SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        new DbInit(source).create(path);
    }

    /**
     * @throws SQLException
     * @throws IOException
     * Since it takes ages to load 600k strings to database we will ignore this method
     * however it is necessary if tests change the database in any way
     */
    @AfterEach
    void tearDownDB() throws SQLException, IOException {
    }
    private int getCount(String table) throws SQLException {
        return source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("select count(*) from " + table);
            resultSet.next();
            return resultSet.getInt(1);
        });
    }

    @Test
    void FillAircraft() throws SQLException, IOException {
        new FillDb(source).fill("Aircraft");
        System.out.println("Aircraft is loaded");
    }

    @Test
    void FillAirport() throws SQLException, IOException {
        new FillDb(source).fill("Airport");
        System.out.println("Airport is loaded");
    }

    @Test
    void FillFlight() throws SQLException, IOException {
        new FillDb(source).fill("Flight");
        System.out.println("Flight is loaded");
    }

    @Test
    void FillBoardingPass() throws SQLException, IOException {
        new FillDb(source).fill("BoardingPass");
        System.out.println("BoardingPass is loaded");
    }

    @Test
    void FillBooking() throws SQLException, IOException {
        new FillDb(source).fill("Booking");
        System.out.println("Booking is loaded");
    }

    @Test
    void FillSeat() throws SQLException, IOException {
        new FillDb(source).fill("Seat");
        System.out.println("Seat is loaded");
    }

    @Test
    void FillTicket() throws SQLException, IOException {
        new FillDb(source).fill("Ticket");
        System.out.println("Ticket is loaded");
    }

    @Test
    void FillTicketFlight() throws SQLException, IOException {
        new FillDb(source).fill("TicketFlight");
        System.out.println("TicketFlight is loaded");
    }

    @Test
    void CreatedAll() throws SQLException, IOException {
        System.out.println("Aircraft tablesize:"+ getCount("aircrafts"));
        System.out.println("Airports tablesize:"+ getCount("airports"));
        System.out.println("Boarding passes tablesize:"+ getCount("boarding_passes"));
        System.out.println("Bookings tablesize:"+ getCount("bookings"));
        System.out.println("Flights tablesize:"+ getCount("flights"));
        System.out.println("Seats tablesize:"+ getCount("seats"));
        System.out.println("Tickets tablesize:"+ getCount("tickets"));
        System.out.println("ticketflights tablesize:"+ getCount("ticket_flights"));
    }

    @Test
    void CitiesWithMultipleAirports() throws SQLException{
        new Requests(source).citiesWithMultipleAirports();
    }

    @Test
    void CitiesWithMostCancelledFlights() throws SQLException{
        new Requests(source).citiesWithCancelledFlights();
    }
}
