package habr.demo.app.service.event;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public class EventRepository {

    @Value("${cassandra.host}")
    private String host;
    @Value("${cassandra.port}")
    private int port;
    @Value("${cassandra.keyspace}")
    private String keyspace;

    private Cluster cluster;
    private Session session;

    @Autowired
    private EventMapper eventMapper;

    @PostConstruct
    public void init() {
        cluster = Cluster.builder().withoutMetrics().withoutJMXReporting().addContactPoint(host).withPort(port).build();
        session = cluster.connect();
    }

    @PreDestroy
    public void destroy() {
        session.close();
        cluster.close();
    }

    public void save(Event event) {
        session.execute("INSERT INTO " + keyspace + ".events(user_id, id, time, type) VALUES(?, ?, ?, ?)",
                eventMapper.toParameters(event));
    }

    public Stream<Event> findByUserId(UUID userId) {
        return session.execute("SELECT * FROM " + keyspace + ".events WHERE user_id = ?", userId).all().stream()
                .map(eventMapper::fromResultRow);
    }
}
