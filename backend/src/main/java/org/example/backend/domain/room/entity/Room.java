package org.example.backend.domain.room.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import org.example.backend.domain.message.entity.Message;

import java.util.List;

@Entity
@Getter
public class Room {
    @Id
    private Long id;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference // 순환 참조가 발생하기에
    private List<Message> message;

    public static class Builder{

        private Long id;
        private List<Message> message;
        public Builder setId(Long id){
            this.id=id;
            return this;
        }
        public Builder setMessage(List<Message> message){
            this.message = message;
            return this;
        }
        public Room build(){
            Room room = new Room();
            room.id = this.id;
            room.message=this.message;
            return room;
        }

    }
}
