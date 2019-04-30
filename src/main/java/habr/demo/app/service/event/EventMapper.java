package habr.demo.app.service.event;

import com.datastax.driver.core.Row;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class EventMapper {

    public Object[] toParameters(Event event) {
        return new Object[] {
                event.getUserId(),
                event.getId(),
                event.getTime().toEpochMilli(),
                event.getType()
        };
    }

    public Event fromResultRow(Row row) {
        return new Event(
                row.getUUID("user_id"),
                row.getUUID("id"),
                Instant.ofEpochMilli(row.getLong("time")),
                row.getString("type")
        );
    }
}
