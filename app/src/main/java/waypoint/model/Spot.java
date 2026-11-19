package waypoint.model;

import java.util.Objects;

/** Entity whose identity is its spot id. */
public final class Spot {
    private final SpotId id;
    private VehicleId occupant;

    public Spot(SpotId id) {
        this.id = Objects.requireNonNull(id, "id");
    }

    public SpotId id() {
        return id;
    }

    public boolean tryAssign(VehicleId vehicle) {
        Objects.requireNonNull(vehicle, "vehicle");
        if (occupant != null) {
            return false;
        }
        occupant = vehicle;
        return true;
    }

    public void release(VehicleId vehicle) {
        if (occupant != null && occupant.equals(Objects.requireNonNull(vehicle, "vehicle"))) {
            occupant = null;
        }
    }

    public boolean isFree() {
        return occupant == null;
    }

    public VehicleId occupant() {
        return occupant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Spot spot)) {
            return false;
        }
        return id.equals(spot.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
