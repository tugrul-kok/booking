package com.tugrul.booking.controller;

import com.tugrul.booking.model.Room;
import com.tugrul.booking.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // Tells Spring that this is a web endpoint and will return JSON.
@RequestMapping("/api/v1/bookings") // The base URL address for external access.
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingService bookingService;

    // Connecting the Service layer to our Controller (the "waiter" of our application).
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // 1. FOR TESTING: Endpoint to add a new room to the system.
    // Usage: POST http://localhost:8080/api/v1/bookings/rooms
    @PostMapping("/rooms")
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        Room savedRoom = bookingService.createRoom(room);
        return ResponseEntity.ok(savedRoom);
    }

    // 2. THE STAR OF THE INTERVIEW: Endpoint to reserve a room.
    // Usage: POST http://localhost:8080/api/v1/bookings/1/book
    @PostMapping("/{roomId}/book")
    public ResponseEntity<String> bookRoom(@PathVariable Long roomId) {
        try {
            // Invoking the locking and reservation logic in the service layer.
            String result = bookingService.bookRoom(roomId);
            return ResponseEntity.ok(result); // Returns 200 OK and success message if successful.
        } catch (RuntimeException e) {
            // Returns 400 Bad Request if the room is occupied or not found.
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}