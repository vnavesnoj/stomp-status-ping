package vnavesnoj.stomp_status_ping.service;


import vnavesnoj.stomp_status_ping.dto.ActiveWsSessionCreateDto;
import vnavesnoj.stomp_status_ping.dto.ActiveWsSessionDeletedDto;
import vnavesnoj.stomp_status_ping.dto.ActiveWsSessionReadDto;

import java.util.List;
import java.util.Optional;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
public interface ActiveWsSessionService {

    Optional<ActiveWsSessionReadDto> findByUsernameAndSessionId(String username, String sessionId);

    List<ActiveWsSessionReadDto> findAllByUsername(String username);

    ActiveWsSessionReadDto create(ActiveWsSessionCreateDto session);

    Optional<ActiveWsSessionReadDto> updateLastAccessedTime(String username, String sessionId);

    boolean delete(String username, String sessionId);

    Optional<ActiveWsSessionDeletedDto> deleteWithResponse(String username, String sessionId);
}
