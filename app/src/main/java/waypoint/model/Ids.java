package waypoint.model;

/** Distinct id types so ticket and spot identifiers cannot be swapped. */
public final class Ids {
    private Ids() {}
}

record TicketId(String value) {
    TicketId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ticket id must not be blank");
        }
    }
}

record SpotId(String value) {
    SpotId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("id must not be blank");
        }
    }
}

record VehicleId(String value) {
    VehicleId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("vehicle id must not be blank");
        }
    }
}

