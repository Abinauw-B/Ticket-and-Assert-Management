package com.management.ticketasset.repository;

import com.management.ticketasset.model.Ticket;
import com.management.ticketasset.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByCreatedBy(User createdBy);
}
