package lgrieg.service.dao;

import lgrieg.service.db.JdbcTemplate;
import lgrieg.domain.Ticket;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
@AllArgsConstructor
public class TicketDao {
    private final JdbcTemplate source;

    private Ticket createTicket(ResultSet resultSet) throws SQLException {
        return new Ticket(resultSet.getString("ticket_no"),
                resultSet.getString("book_ref"),
                resultSet.getString("passenger_id"),
                resultSet.getString("passenger_name"),
                resultSet.getString("contact_data"));
    }
    /*
 ticket_no      | char(13)    | not null     | Номер билета
 book_ref       | char(6)     | not null     | Номер бронирования
 passenger_id   | varchar(20) | not null     | Идентификатор пассажира
 passenger_name | text        | not null     | Имя пассажира
 contact_data   | jsonb       |              | Контактные данные пассажира

    */
    public void saveTicket(Collection<Ticket> tickets) throws SQLException {
        source.preparedStatement("insert into tickets(ticket_no, " +
                "book_ref, " +
                "passenger_id, " +
                "passenger_name, " +
                "contact_data) values (?, ?, ?, ?, ?)", insertTicket -> {
            for (Ticket ticket : tickets) {
                insertTicket.setString(1, ticket.getTicketNo());
                insertTicket.setString(2, ticket.getBookRef());
                insertTicket.setString(3, ticket.getPassengerId());
                insertTicket.setString(4, ticket.getPassengerName());
                insertTicket.setString(5, ticket.getContactData());
                insertTicket.execute();
            }
        });
    }

    public Set<Ticket> getTickets() throws SQLException {
        return source.statement(stmt -> {
            Set<Ticket> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("select * from tickets");
            while (resultSet.next()) {
                result.add(createTicket(resultSet));
            }
            return result;
        });
    }
}
