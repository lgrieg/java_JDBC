package lgrieg.domain;
import lombok.Data;
@Data
public class TicketFlight {
    private final String ticketNo;
    private final int flightId;
    private final String fareConditions;
    private final Double amount;
}
