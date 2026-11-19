package waypoint.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SpotTest {
    @Test
    void aSpotCannotBeAssignedTwice() {
        Spot spot = new Spot(new SpotId("S1"));
        assertTrue(spot.tryAssign(new VehicleId("V1")));
        assertFalse(spot.tryAssign(new VehicleId("V2")),
                "second claim must fail, not overwrite");
    }

    @Test
    void releaseIsIdempotent() {
        Spot spot = new Spot(new SpotId("S1"));
        spot.release(new VehicleId("V1"));
        assertTrue(spot.isFree());
    }

    @Test
    void spotIdentityIsItsIdNotItsState() {
        Spot a = new Spot(new SpotId("S1"));
        Spot b = new Spot(new SpotId("S1"));
        a.tryAssign(new VehicleId("V1"));
        assertEquals(a, b, "an entity stays the same entity when its state changes");
    }
}

