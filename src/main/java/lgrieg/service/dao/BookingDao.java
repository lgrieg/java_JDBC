package lgrieg.service.dao;

import lgrieg.service.db.JdbcTemplate;
import lgrieg.domain.Booking;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
@AllArgsConstructor
public class BookingDao {
    private final JdbcTemplate source;

    private Booking createBooking(ResultSet resultSet) throws SQLException {
        return new Booking(resultSet.getString("book_ref"),
                resultSet.getString("book_date"),
                resultSet.getDouble("total_amount"));
    }
    /*
        Столбец    |      Тип      | Модификаторы |         Описание
--------------+---------------+--------------+---------------------------
 book_ref     | char(6)       | not null     | Номер бронирования
 book_date    | timestamptz   | not null     | Дата бронирования
 total_amount | numeric(10,2) | not null     | Полная сумма бронирова

    */
    public void saveBooking(Collection<Booking> bookings) throws SQLException {
        source.preparedStatement("insert into bookings(book_ref, " +
                "book_date, " +
                "total_amount) values (?, ?, ?)", insertBooking -> {
            for (Booking booking : bookings) {
                insertBooking.setString(1, booking.getBookRef());
                insertBooking.setString(2, booking.getBookDate());
                insertBooking.setDouble(3, booking.getTotalAmount());
                insertBooking.execute();
            }
        });
    }

    public Set<Booking> getBookings() throws SQLException {
        return source.statement(stmt -> {
            Set<Booking> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("select * from bookings");
            while (resultSet.next()) {
                result.add(createBooking(resultSet));
            }
            return result;
        });
    }
}
