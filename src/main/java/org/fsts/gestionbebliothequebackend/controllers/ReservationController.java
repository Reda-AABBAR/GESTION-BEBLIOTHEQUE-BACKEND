package org.fsts.gestionbebliothequebackend.controllers;

import lombok.RequiredArgsConstructor;
import org.fsts.gestionbebliothequebackend.dtos.ReservationDTO;
import org.fsts.gestionbebliothequebackend.entities.Reservation;
import org.fsts.gestionbebliothequebackend.services.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody ReservationDTO reservationDTO) {
        try {
            ReservationDTO createdReservation = reservationService.createReservation(reservationDTO);
            return ResponseEntity.ok(createdReservation); // Return 200 OK if successful
        } catch (IllegalStateException e) {
            // Return 409 Conflict with error message if no exemplars are available
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "All exemplars of the document are currently unavailable.",
                            "details", e.getMessage()));
        } catch (Exception e) {
            // Handle any other unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred.",
                            "details", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable UUID id) {
        ReservationDTO reservationDTO = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservationDTO);
    }
    @GetMapping("/by-user/{id}")
    ResponseEntity<List<ReservationDTO>> getReservationsByUser(@PathVariable UUID id){
        return  ResponseEntity.ok(
          reservationService.getReservationsByUser(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        List<ReservationDTO> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationDTO> updateReservation(@PathVariable UUID id, @RequestBody ReservationDTO reservationDTO) {
        ReservationDTO updatedReservation = reservationService.updateReservation(id, reservationDTO);
        return ResponseEntity.ok(updatedReservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable UUID id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
