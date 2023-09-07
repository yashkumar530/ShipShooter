package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Represents a fleet in JSON format.
 */
public record FleetJson(
    @JsonProperty("fleet") List<ShipJson> ships) {
}

