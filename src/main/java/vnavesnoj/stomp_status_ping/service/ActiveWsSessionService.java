package vnavesnoj.stomp_status_ping.service;


import vnavesnoj.stomp_status_ping.dto.ActiveWsSessionCreateDto;
import vnavesnoj.stomp_status_ping.dto.ActiveWsSessionReadDto;
import vnavesnoj.stomp_status_ping.dto.EntityExistsResponse;

import java.util.List;
import java.util.Optional;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
public interface ActiveWsSessionService {

    Optional<ActiveWsSessionReadDto> findBySessionId(String sessionId);

    List<ActiveWsSessionReadDto> findAllByUsername(String username);

    EntityExistsResponse existsByUsername(String username);

    ActiveWsSessionReadDto create(ActiveWsSessionCreateDto session);

    Optional<ActiveWsSessionReadDto> updateLastAccessedTime(String sessionId);

    boolean delete(String sessionId);
}
