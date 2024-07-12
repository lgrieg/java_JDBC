package lgrieg.service.db;
import lombok.AllArgsConstructor;
import java.util.HashSet;
import java.util.Set;
import lgrieg.service.dao.*;
import lgrieg.domain.*;
import java.sql.*;
import java.net.*;
import java.io.*;
import java.util.regex.*;

@AllArgsConstructor
public class FillDb {
    private final String aircraftUrl = new String("https://storage.yandexcloud.net/airtrans-small/aircrafts.csv");
    private final String airportUrl = new String("https://storage.yandexcloud.net/airtrans-small/airports.csv");
    private final String boardingPassUrl = new String("https://storage.yandexcloud.net/airtrans-small/boarding_passes.csv");
    private final String bookingUrl = new String("https://storage.yandexcloud.net/airtrans-small/bookings.csv");
    private final String flightUrl = new String("https://storage.yandexcloud.net/airtrans-small/flights.csv");
    private final String seatUrl = new String("https://storage.yandexcloud.net/airtrans-small/seats.csv");
    private final String ticketUrl = new String("https://storage.yandexcloud.net/airtrans-small/tickets.csv");
    private final String ticketFlightUrl = new String("https://storage.yandexcloud.net/airtrans-small/ticket_flights.csv");

    final JdbcTemplate source;
    private void fillAircraft() throws IOException, SQLException {
        URL url = new URL(aircraftUrl);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        String regexp = "(.*),\"(.*)\",(.*)";
        Pattern pattern = Pattern.compile(regexp);
        Set<Aircraft> set = new HashSet<>();
        while ((inputLine = in.readLine()) != null) {
            //System.out.println(inputLine);
            Matcher matcher = pattern.matcher(inputLine);
            if (matcher.find()) {
                set.add(new Aircraft(matcher.group(1),
                        matcher.group(2),
                        Integer.parseInt(matcher.group(3))));
            }
        }
        in.close();
        System.out.println("aircraft is fine");
        new AircraftDao(source).saveAircraft(set);
        System.out.println("I MEAN IT aircraft is fine");
    }

    private void fillAirport() throws IOException, SQLException {
        URL url = new URL(airportUrl);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        String regexp = "(.*),\"(.*)\",\"(.*)\",(.*),(.*)";
        Pattern pattern = Pattern.compile(regexp);
        Set<Airport> set = new HashSet<>();
        while ((inputLine = in.readLine()) != null) {
            //System.out.println(inputLine);
            Matcher matcher = pattern.matcher(inputLine);
            if (matcher.find()) {
                set.add(new Airport(matcher.group(1),
                        matcher.group(2),
                        matcher.group(3),
                        matcher.group(4),
                        matcher.group(5)));
            }
        }
        in.close();
        System.out.println("airport is fine");
        new AirportDao(source).saveAirport(set);
        System.out.println("airport is totally fine");
    }
    private void fillBoardingPass() throws IOException, SQLException {
        URL url = new URL(boardingPassUrl);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        String regexp = "(.*),(.*),(.*),(.*)";
        Pattern pattern = Pattern.compile(regexp);
        Set<BoardingPass> set = new HashSet<>();
        while ((inputLine = in.readLine()) != null) {
            //System.out.println(inputLine);
            Matcher matcher = pattern.matcher(inputLine);
            if (matcher.find()) {
                set.add(new BoardingPass(matcher.group(1),
                        Integer.parseInt(matcher.group(2)),
                        Integer.parseInt(matcher.group(3)),
                        matcher.group(4)));
            }
        }
        in.close();
        System.out.println("boarding pass is fine");
        new BoardingPassDao(source).saveBoardingPass(set);
        System.out.println("boarding pass is fine");
    }
    private void fillBooking() throws IOException, SQLException {
        URL url = new URL(bookingUrl);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        String regexp = "(.*),(.*),(.*)";
        Pattern pattern = Pattern.compile(regexp);
        Set<Booking> set = new HashSet<>();
        while ((inputLine = in.readLine()) != null) {
            //System.out.println(inputLine);
            Matcher matcher = pattern.matcher(inputLine);
            if (matcher.find()) {
                set.add(new Booking(matcher.group(1),
                        matcher.group(2),
                        Double.parseDouble(matcher.group(3))));
            }
        }
        in.close();
        System.out.println("booking is fine");
        new BookingDao(source).saveBooking(set);
        System.out.println("booking is fine");
    }

    private void fillFlight() throws IOException, SQLException {
        URL url = new URL(flightUrl);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        String regexp = "(.*),(.*),(.*),(.*),(.*),(.*),(.*),(.*),(.*),(.*)";
        Pattern pattern = Pattern.compile(regexp);
        Set<Flight> set = new HashSet<>();
        while ((inputLine = in.readLine()) != null) {
            //System.out.println(inputLine);
            Matcher matcher = pattern.matcher(inputLine);
            if (matcher.find()) {
                set.add(new Flight(Integer.parseInt(matcher.group(1)),
                        matcher.group(2),
                        matcher.group(3),
                        matcher.group(4),
                        matcher.group(5),
                        matcher.group(6),
                        matcher.group(7),
                        matcher.group(8),
                        matcher.group(9).equals("") ? null : matcher.group(9),
                        matcher.group(10).equals("") ? null : matcher.group(10)));
            }
        }
        in.close();
        new FlightDao(source).saveFlight(set);
    }

    private void fillSeat() throws IOException, SQLException {
        URL url = new URL(seatUrl);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        String regexp = "(.*),(.*),(.*)";
        Pattern pattern = Pattern.compile(regexp);
        Set<Seat> set = new HashSet<>();
        while ((inputLine = in.readLine()) != null) {
            //System.out.println(inputLine);
            Matcher matcher = pattern.matcher(inputLine);
            if (matcher.find()) {
                set.add(new Seat(matcher.group(1),
                        matcher.group(2),
                        matcher.group(3)));
            }
        }
        in.close();
        new SeatDao(source).saveSeat(set);
    }

    private void fillTicket() throws IOException, SQLException {
        URL url = new URL(ticketUrl);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        String regexp = "(.*),(.*),(.*),(.*),\"(.*)\"";
        Pattern pattern = Pattern.compile(regexp);
        Set<Ticket> set = new HashSet<>();
        while ((inputLine = in.readLine()) != null) {
            //System.out.println(inputLine);
            Matcher matcher = pattern.matcher(inputLine);
            if (matcher.find()) {
                set.add(new Ticket(matcher.group(1),
                        matcher.group(2),
                        matcher.group(3),
                        matcher.group(4),
                        matcher.group(5)));
            }
        }
        in.close();
        new TicketDao(source).saveTicket(set);
    }


    private void fillTicketFlight() throws IOException, SQLException {
        URL url = new URL(ticketFlightUrl);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        String regexp = "(.*),(.*),(.*),(.*)";
        Pattern pattern = Pattern.compile(regexp);
        Set<TicketFlight> set = new HashSet<>();
        while ((inputLine = in.readLine()) != null) {
            //System.out.println(inputLine);
            Matcher matcher = pattern.matcher(inputLine);
            if (matcher.find()) {
                set.add(new TicketFlight(matcher.group(1),
                        Integer.parseInt(matcher.group(2)),
                        matcher.group(3),
                        Double.parseDouble(matcher.group(4))));
            }
        }
        in.close();
        new TicketFlightDao(source).saveTicketFlight(set);
    }
    public void fill(String name) throws IOException, SQLException {
        switch (name) {
            case  ("Airport"):
                fillAirport();
                break;
            case ("Aircraft"):
                fillAircraft();
                break;
            case ("BoardingPass"):
                fillBoardingPass();
                break;
            case ("Booking"):
                fillBooking();
                break;
            case ("Flight"):
                fillFlight();
                break;
            case ("Seat"):
                fillSeat();
                break;
            case ("Ticket"):
                fillTicket();
                break;
            case ("TicketFlight"):
                fillTicketFlight();
                break;
            default:
                System.out.println("You`re not very smart, are you?");
                break;
        }
    }
}
