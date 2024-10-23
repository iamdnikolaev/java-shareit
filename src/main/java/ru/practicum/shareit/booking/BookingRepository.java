package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "select count(b) " +
            "from Booking b " +
            "join b.item i " +
            "where i.id = :itemId " +
            "and b.start < :end " +
            "and b.end > :start " +
            "and b.status not in (REJECTED, CANCELED)")
    long crossingCount(@Param("itemId") long itemId,
                       @Param("start") LocalDateTime start,
                       @Param("end") LocalDateTime end);

    @Query(value = "select o.id " +
            "from Booking b " +
            "join b.item i " +
            "join i.owner o " +
            "where b.id = :bookingId")
    Long getItemOwnerId(@Param("bookingId") long bookingId);

    @Query(value = "select b from Booking b " +
            "join fetch b.item " +
            "join fetch b.booker " +
            "where b.id = :bookingId")
    Optional<Booking> getBookingById(@Param("bookingId") long bookingId);

    @Query(value = "select b from Booking b " +
            "join fetch b.item i " +
            "join fetch b.booker bo " +
            "where bo.id = :bookerId " +
            "order by b.start desc")
    List<Booking> findAllBookingsByBookerId(@Param("bookerId") long bookerId);

    @Query(value = "select b from Booking b " +
            "join fetch b.item i " +
            "join fetch b.booker bo " +
            "where bo.id = :bookerId " +
            "and :onTime between b.start and b.end " +
            "order by b.start desc")
    List<Booking> findCurrentBookingsByBookerId(@Param("bookerId") long bookerId, @Param("onTime") LocalDateTime onTime);

    @Query(value = "select b from Booking b " +
            "join fetch b.item i " +
            "join fetch b.booker bo " +
            "where bo.id = :bookerId " +
            "and b.end < :onTime " +
            "order by b.start desc")
    List<Booking> findPastBookingsByBookerId(@Param("bookerId") long bookerId, @Param("onTime") LocalDateTime onTime);

    @Query(value = "select b from Booking b " +
            "join fetch b.item i " +
            "join fetch b.booker bo " +
            "where bo.id = :bookerId " +
            "and b.end < :onTime " +
            "and b.status in :statusList " +
            "order by b.start desc")
    List<Booking> findPastBookingsByBookerIdAndStatus(@Param("bookerId") long bookerId,
                                                      @Param("onTime") LocalDateTime onTime,
                                                      @Param("statusList") List<BookingStatus> statusList);

    @Query(value = "select b from Booking b " +
            "join fetch b.item i " +
            "join fetch b.booker bo " +
            "where bo.id = :bookerId " +
            "and b.start > :onTime " +
            "order by b.start desc")
    List<Booking> findFutureBookingsByBookerId(@Param("bookerId") long bookerId, @Param("onTime") LocalDateTime onTime);

    @Query(value = "select b from Booking b " +
            "join fetch b.item i " +
            "join fetch b.booker bo " +
            "where bo.id = :bookerId " +
            "and b.status in :statusList " +
            "order by b.start desc")
    List<Booking> findBookingsByBookerIdAndStatus(@Param("bookerId") long bookerId,
                                                  @Param("statusList") List<BookingStatus> statusList);

    @Query(value = "select b from Booking b " +
            "join fetch b.item i " +
            "join i.owner o " +
            "join fetch b.booker bo " +
            "where o.id = :ownerId " +
            "order by b.start desc")
    List<Booking> findAllBookingsByOwnerId(@Param("ownerId") long ownerId);

    @Query(value = "select b from Booking b " +
            "join fetch b.item i " +
            "join i.owner o " +
            "join fetch b.booker bo " +
            "where o.id = :ownerId " +
            "and :onTime between b.start and b.end " +
            "order by b.start desc")
    List<Booking> findCurrentBookingsByOwnerId(@Param("ownerId") long ownerId, @Param("onTime") LocalDateTime onTime);

    @Query(value = "select b from Booking b " +
            "join fetch b.item i " +
            "join i.owner o " +
            "join fetch b.booker bo " +
            "where o.id = :ownerId " +
            "and b.end < :onTime " +
            "order by b.start desc")
    List<Booking> findPastBookingsByOwnerId(@Param("ownerId") long ownerId, @Param("onTime") LocalDateTime onTime);

    @Query(value = "select b from Booking b " +
            "join fetch b.item i " +
            "join i.owner o " +
            "join fetch b.booker bo " +
            "where o.id = :ownerId " +
            "and b.start > :onTime " +
            "order by b.start desc")
    List<Booking> findFutureBookingsByOwnerId(@Param("ownerId") long ownerId, @Param("onTime") LocalDateTime onTime);

    @Query(value = "select b from Booking b " +
            "join fetch b.item i " +
            "join i.owner o " +
            "join fetch b.booker bo " +
            "where o.id = :ownerId " +
            "and b.status in :statusList " +
            "order by b.start desc")
    List<Booking> findBookingsByOwnerIdAndStatus(@Param("ownerId") long ownerId,
                                                 @Param("statusList") List<BookingStatus> statusList);

    @Query(value = "select b " +
            "from Booking b " +
            "join b.item i " +
            "where i.id in :itemIds " +
            "and b.start < :onTime " +
            "and b.start = (select max(b1.start) " +
            "  from Booking b1 " +
            "  join b1.item i1 " +
            "  where i1.id = i.id " +
            "  and b1.start < :onTime)")
    List<Booking> lastBookingsByItemIds(@Param("itemIds") List<Long> itemIds, @Param("onTime") LocalDateTime onTime);

    @Query(value = "select b " +
            "from Booking b " +
            "join b.item i " +
            "where i.id in :itemIds " +
            "and b.start > :onTime " +
            "and b.start = (select min(b1.start) " +
            "  from Booking b1 " +
            "  join b1.item i1 " +
            "  where i1.id = i.id " +
            "  and b1.start > :onTime)")
    List<Booking> nearestBookingsByItemIds(@Param("itemIds") List<Long> itemIds, @Param("onTime") LocalDateTime onTime);
}
