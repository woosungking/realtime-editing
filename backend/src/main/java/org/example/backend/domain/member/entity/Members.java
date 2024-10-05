package org.example.backend.domain.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Members {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;

    public static class Builder{
        private Long id;
        private String email;
        private String password;

        public Builder setId(Long id){
            this.id = id;
            return this;
        }
        public Builder setEmail(String email){
            this.email = email;
            return this;
        }
        public Builder setPassword(String password){
            this.password = password;
            return this;
        }
        public Members build(){
            Members member = new Members();
            member.id = this.id;
            member.email = this.email;
            member.password = this.password;
            return member;
        }
    }
}
