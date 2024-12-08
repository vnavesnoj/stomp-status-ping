package vnavesnoj.stomp_status_ping.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vnavesnoj.stomp_status_ping.data.ActiveWsSession;
import vnavesnoj.stomp_status_ping.data.ActiveWsSessionRepository;
import vnavesnoj.stomp_status_ping.dto.ActiveWsSessionCreateDto;
import vnavesnoj.stomp_status_ping.dto.ActiveWsSessionReadDto;
import vnavesnoj.stomp_status_ping.mapper.Mapper;

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
    private final Mapper<ActiveWsSession, ActiveWsSessionReadDto> readMapper;
    private final Mapper<ActiveWsSessionCreateDto, ActiveWsSession> createMapper;

    @Override
    public Optional<ActiveWsSessionReadDto> findByUsernameAndSessionId(String username, String sessionId) {
        final String id = username + ActiveWsSession.ID_DELIMITER + sessionId;
        return repository.findById(id)
                .map(readMapper::map);
    }

    @Override
    public List<ActiveWsSessionReadDto> findAllByUsername(String username) {
        return repository.findAllByUsername(username).stream()
                .map(readMapper::map)
                .toList();
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
