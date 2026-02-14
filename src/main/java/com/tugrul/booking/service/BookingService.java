package com.tugrul.booking.service;

import com.tugrul.booking.model.Room;
import com.tugrul.booking.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

    private final RoomRepository roomRepository;

    // Dependency Injection: Spring automatically injects the repository here.
    public BookingService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // Method to add a new room to the system for testing purposes.
    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    @Transactional
    public String bookRoom(Long roomId) {

        // 1. Find the room and apply a Pessimistic Lock (The 2nd concurrent request will be forced to wait).
        Room room = roomRepository.findByIdWithPessimisticLock(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found!"));

        // 2. Check if the room has already been booked by someone else.
        if (!room.isAvailable()) {
            throw new RuntimeException("Sorry, this room was just booked by another user!");
        }

        // 3. Assign the room to the customer and set status to 'occupied'.
        room.setAvailable(false);
        roomRepository.save(room); // Persist the change to the database.

        return "Reservation completed successfully! Lock released.";
    }
}