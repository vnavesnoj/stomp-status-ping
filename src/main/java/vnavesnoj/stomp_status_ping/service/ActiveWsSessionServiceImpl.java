package vnavesnoj.stomp_status_ping.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vnavesnoj.stomp_status_ping.data.ActiveWsSessionRepository;
import vnavesnoj.stomp_status_ping.dto.ActiveWsSessionCreateDto;
import vnavesnoj.stomp_status_ping.dto.ActiveWsSessionReadDto;

import java.util.List;
import java.util.Optional;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@RequiredArgsConstructor
@Service
public class ActiveWsSessionServiceImpl implements ActiveWsSessionService {

    private final ActiveWsSessionRepository repository;

    @Override
    public Optional<ActiveWsSessionReadDto> findByUsernameAndSessionId(String username, String sessionId) {
        return Optional.empty();
    }

    @Override
    public List<ActiveWsSessionReadDto> findAllByUsername(String username) {
        return null;
    }

    @Override
    public ActiveWsSessionReadDto create(ActiveWsSessionCreateDto session) {
        return null;
    }

    @Override
    public Optional<ActiveWsSessionReadDto> updateLastAccessedTime(String username, String sessionId) {
        return Optional.empty();
    }

    @Override
    public boolean delete(String username, String sessionId) {
        return false;
    }
}
