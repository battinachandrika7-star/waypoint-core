package waypoint.model;

import java.time.Instant;
import java.util.Objects;

/** Entity representing an open or closed parking stay. */
public final class Ticket {
    private final TicketId id;
    private final SpotId spotId;
    private final Instant entry;
    private Instant exit;
    private Money fare;

    private Ticket(TicketId id, SpotId spotId, Instant entry) {
        this.id = Objects.requireNonNull(id, "id");
        this.spotId = Objects.requireNonNull(spotId, "spotId");
        this.entry = Objects.requireNonNull(entry, "entry");
    }

    public static Ticket open(TicketId id, SpotId spotId, Instant entry) {
        return new Ticket(id, spotId, entry);
    }

    public TicketId id() {
        return id;
    }

    public SpotId spotId() {
        return spotId;
    }

    public Instant entry() {
        return entry;
    }

    public Instant exit() {
        return exit;
    }

    public Money fare() {
        return fare;
    }

    public boolean isOpen() {
        return exit == null;
    }

    public Money close(Instant at, FarePolicy policy) {
        if (!isOpen()) {
            throw new TicketAlreadyClosed("ticket is already closed");
        }
        Objects.requireNonNull(at, "exit");
        Objects.requireNonNull(policy, "policy");
        if (at.isBefore(entry)) {
            throw new IllegalArgumentException("exit cannot be before entry");
        }

        Money calculatedFare = policy.fareFor(entry, at, spotId);
        if (calculatedFare == null) {
            throw new IllegalArgumentException("fare policy returned null");
        }
        if (calculatedFare.isNegative()) {
            throw new IllegalArgumentException("fare cannot be negative");
        }

        exit = at;
        fare = calculatedFare;
        return fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ticket ticket)) {
            return false;
        }
        return id.equals(ticket.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

class TicketAlreadyClosed extends RuntimeException {
    public TicketAlreadyClosed(String message) {
        super(message);
    }
}

