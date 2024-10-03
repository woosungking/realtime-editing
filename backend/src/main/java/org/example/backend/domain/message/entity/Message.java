package org.example.backend.domain.message.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import org.example.backend.domain.room.entity.Room;

@Entity
@Getter
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private String sender;
    @ManyToOne
    @JoinColumn(name="room_id")
    @JsonBackReference
    private Room room;

    public static class Builder{
        private Long id;

        private String content;
        private String sender;

        private Room room;

       public Builder setId(Long id){
           this.id=id;
           return this;
       }
       public Builder setContent(String content){
           this.content=content;
           return this;
       }
       public Builder setSender(String sender){
           this.sender=sender;
           return this;
       }

       public Builder setRoomId(Room room){
           this.room=room;
           return this;
       }
       public Message build(){
           Message message = new Message();
           message.id = this.id;
           message.content=this.content;
           message.sender=this.sender;
           message.room=this.room;
            return message;
    }

    // Getter, Setter 등 필요한 메서드 추가
}

}
