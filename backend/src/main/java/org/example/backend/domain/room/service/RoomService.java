package org.example.backend.domain.room.service;

import org.example.backend.domain.room.entity.Room;
import org.example.backend.domain.room.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room doesExistRoom(Long roomId) {
        Optional<Room> optionalRoom = roomRepository.findById(roomId);
        if (optionalRoom.isPresent()) {
            return optionalRoom.get();
        } else {
            Room room = new Room.Builder().setId(roomId).build();
            System.out.println(room.getId());
            roomRepository.save(room);

            return room;
        }
    }
}
