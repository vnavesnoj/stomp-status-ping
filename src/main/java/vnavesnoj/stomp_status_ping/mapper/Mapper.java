package vnavesnoj.stomp_status_ping.mapper;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
public interface Mapper<F, T> {

    T map(F object);
}
