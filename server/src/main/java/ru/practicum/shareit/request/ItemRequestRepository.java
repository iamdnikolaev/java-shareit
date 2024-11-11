package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findByRequestorIdOrderByCreatedDesc(long requestorId);

    @Query(value = "select r " +
            "from ItemRequest r " +
            "where r.requestor.id <> :requestorId " +
            "order by r.created desc")
    List<ItemRequest> findByOtherUsers(@Param("requestorId") long requestorId);
}
