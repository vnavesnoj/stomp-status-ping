package vnavesnoj.stomp_status_ping.data;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
public interface ActiveWsSessionRepository extends CrudRepository<ActiveWsSession, String> {

    List<ActiveWsSession> findAllByUsername(String username);

    long countActiveWsSessionByUsername(String username);
}
