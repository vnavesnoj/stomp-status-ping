package vnavesnoj.stomp_status_ping.data;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
public interface ActiveWsUserSessionRepository extends CrudRepository<ActiveWsUserSession, String> {

    List<ActiveWsUserSession> findAllByUsername(String username);
}
