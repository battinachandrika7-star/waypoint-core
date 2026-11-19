package waypoint.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class TicketTest {
    static final Instant T10 = Instant.parse("2026-03-01T10:00:00Z");
    static final Instant T09 = Instant.parse("2026-03-01T09:00:00Z");
    static final Instant T12 = Instant.parse("2026-03-01T12:00:00Z");
    static final FarePolicy FLAT = (entry, exit, spot) -> Money.rupees(40);

    @Test
    void exitBeforeEntryIsImpossible() {
        Ticket ticket = Ticket.open(new TicketId("T1"), new SpotId("S1"), T10);
        assertThrows(IllegalArgumentException.class, () -> ticket.close(T09, FLAT));
        assertTrue(ticket.isOpen(), "a rejected close must leave the ticket untouched");
    }

    @Test
    void closingTwiceIsRejected() {
        Ticket ticket = Ticket.open(new TicketId("T1"), new SpotId("S1"), T10);
        ticket.close(T12, FLAT);
        assertThrows(TicketAlreadyClosed.class, () -> ticket.close(T12, FLAT));
    }

    @Test
    void fareAndExitAppearTogether() {
        Ticket ticket = Ticket.open(new TicketId("T1"), new SpotId("S1"), T10);
        assertNull(ticket.fare());
        assertEquals(Money.rupees(40), ticket.close(T12, FLAT));
        assertFalse(ticket.isOpen());
    }

    @Test
    void classExposesNoSetters() {
        assertTrue(Arrays.stream(Ticket.class.getMethods())
                .noneMatch(method -> method.getName().startsWith("set")));
    }
}

