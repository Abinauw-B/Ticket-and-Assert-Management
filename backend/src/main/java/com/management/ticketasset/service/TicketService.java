package com.management.ticketasset.service;

import com.management.ticketasset.model.Asset;
import com.management.ticketasset.model.Role;
import com.management.ticketasset.model.Ticket;
import com.management.ticketasset.model.TicketStatus;
import com.management.ticketasset.model.User;
import com.management.ticketasset.repository.AssetRepository;
import com.management.ticketasset.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final AssetRepository assetRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository, AssetRepository assetRepository) {
        this.ticketRepository = ticketRepository;
        this.assetRepository = assetRepository;
    }

    /**
     * Helper method to get the currently authenticated user from SecurityContext.
     */
    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        }
        throw new IllegalStateException("No authenticated user found in security context.");
    }

    /**
     * Retrieves tickets based on user role.
     * Admins see all tickets. Employees see only their own.
     */
    public List<Ticket> getAllTickets() {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() == Role.ROLE_ADMIN) {
            return ticketRepository.findAll();
        } else {
            return ticketRepository.findByCreatedBy(currentUser);
        }
    }

    /**
     * Retrieves a single ticket by id with role-based visibility checks.
     */
    public Ticket getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found with id: " + id));

        User currentUser = getCurrentUser();
        
        // If employee, they must be the owner of the ticket
        if (currentUser.getRole() == Role.ROLE_EMPLOYEE && 
            !ticket.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have permission to view this ticket.");
        }

        return ticket;
    }

    /**
     * Creates a new ticket. Creator is automatically set to the authenticated user.
     */
    public Ticket createTicket(Ticket ticket, Long assetId) {
        User currentUser = getCurrentUser();
        
        ticket.setCreatedBy(currentUser);
        ticket.setStatus(TicketStatus.OPEN); // Default state
        
        if (assetId != null) {
            Asset asset = assetRepository.findById(assetId)
                    .orElseThrow(() -> new IllegalArgumentException("Asset not found with id: " + assetId));
            ticket.setAsset(asset);
        }

        return ticketRepository.save(ticket);
    }

    /**
     * Updates an existing ticket with role-based security checks.
     */
    public Ticket updateTicket(Long id, Ticket ticketDetails, Long assetId) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found with id: " + id));

        User currentUser = getCurrentUser();

        // 1. Employees can only update their own tickets
        if (currentUser.getRole() == Role.ROLE_EMPLOYEE) {
            if (!ticket.getCreatedBy().getId().equals(currentUser.getId())) {
                throw new AccessDeniedException("You do not have permission to update this ticket.");
            }
            
            // Employees can update title, description, priority, and link/change the asset
            ticket.setTitle(ticketDetails.getTitle());
            ticket.setDescription(ticketDetails.getDescription());
            ticket.setPriority(ticketDetails.getPriority());
            
            // Employees can also close their own ticket
            if (ticketDetails.getStatus() == TicketStatus.CLOSED) {
                ticket.setStatus(TicketStatus.CLOSED);
            }
            
        } else if (currentUser.getRole() == Role.ROLE_ADMIN) {
            // 2. Admins can update everything, including status transitions
            ticket.setTitle(ticketDetails.getTitle());
            ticket.setDescription(ticketDetails.getDescription());
            ticket.setPriority(ticketDetails.getPriority());
            ticket.setStatus(ticketDetails.getStatus());
        }

        // Link asset if specified
        if (assetId != null) {
            Asset asset = assetRepository.findById(assetId)
                    .orElseThrow(() -> new IllegalArgumentException("Asset not found with id: " + assetId));
            ticket.setAsset(asset);
        } else if (ticketDetails.getAsset() == null && currentUser.getRole() == Role.ROLE_ADMIN) {
            // Admin can explicitly remove an asset link
            ticket.setAsset(null);
        }

        return ticketRepository.save(ticket);
    }

    /**
     * Deletes a ticket. Only allowed for the ticket creator (if open) or an Admin.
     */
    public void deleteTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found with id: " + id));

        User currentUser = getCurrentUser();

        if (currentUser.getRole() == Role.ROLE_EMPLOYEE) {
            if (!ticket.getCreatedBy().getId().equals(currentUser.getId())) {
                throw new AccessDeniedException("You do not have permission to delete this ticket.");
            }
            if (ticket.getStatus() != TicketStatus.OPEN) {
                throw new IllegalArgumentException("Employees can only delete tickets that are still in OPEN status.");
            }
        }
        
        ticketRepository.delete(ticket);
    }
}
