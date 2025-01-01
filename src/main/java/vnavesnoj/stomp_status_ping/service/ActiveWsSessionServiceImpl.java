package vnavesnoj.stomp_status_ping.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vnavesnoj.stomp_status_ping.config.properties.ActiveWsSessionEntityProperties;
import vnavesnoj.stomp_status_ping.data.ActiveWsSession;
import vnavesnoj.stomp_status_ping.data.ActiveWsSessionRepository;
import vnavesnoj.stomp_status_ping.dto.ActiveWsSessionCreateDto;
import vnavesnoj.stomp_status_ping.dto.ActiveWsSessionReadDto;
import vnavesnoj.stomp_status_ping.mapper.Mapper;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Transactional(readOnly = true)
@Service
public class ActiveWsSessionServiceImpl implements ActiveWsSessionService {

    private final ActiveWsSessionRepository repository;
    private final Mapper<ActiveWsSession, ActiveWsSessionReadDto> readMapper;
    private final Mapper<ActiveWsSessionCreateDto, ActiveWsSession> createMapper;
    private final Long entityTtl;

    public ActiveWsSessionServiceImpl(ActiveWsSessionRepository repository,
                                      Mapper<ActiveWsSession, ActiveWsSessionReadDto> readMapper,
                                      Mapper<ActiveWsSessionCreateDto, ActiveWsSession> createMapper,
                                      ActiveWsSessionEntityProperties properties) {
        this.repository = repository;
        this.readMapper = readMapper;
        this.createMapper = createMapper;
        this.entityTtl = properties.getTtl();
    }

    @Override
    public Optional<ActiveWsSessionReadDto> findBySessionId(String sessionId) {
        return repository.findById(sessionId)
                .map(readMapper::map);
    }

    @Override
    public List<ActiveWsSessionReadDto> findAllByUsername(String username) {
        return repository.findAllByUsername(username).stream()
                .map(readMapper::map)
                .toList();
    }

    @Override
    public boolean existsByUsername(String username) {
        return !repository.findAllByUsername(username).isEmpty();
    }

    @Transactional
    @Override
    public ActiveWsSessionReadDto create(ActiveWsSessionCreateDto session) {
        return Optional.ofNullable(session)
                .map(createMapper::map)
                .map(repository::save)
                .map(readMapper::map)
                .orElseThrow(NullPointerException::new);
    }

    @Transactional
    @Override
    public Optional<ActiveWsSessionReadDto> updateLastAccessedTime(String sessionId) {
        return repository.findById(sessionId)
                .map(item -> {
                    item.setLastAccessedTime(Instant.now().getEpochSecond());
                    item.setTtl(entityTtl);
                    return item;
                })
                .map(repository::save)
                .map(readMapper::map);
    }

    @Transactional
    @Override
    public boolean delete(String sessionId) {
        return repository.findById(sessionId)
                .map(item -> {
                    repository.delete(item);
                    return true;
                })
                .orElse(false);
    }
}
