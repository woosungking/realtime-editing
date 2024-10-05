package org.example.backend.common;

import org.springframework.http.HttpStatus;

public class RestApiResponse {
    private  HttpStatus status;

    private  String code;

    private  String message;
    private  Object data;


    public static class Builder{
        private HttpStatus status;

        private  String code;

        private  String message;
        private   Object data;

        public Builder setStatus(HttpStatus httpStatus){
            this.status=httpStatus;
            return this;

        }
        public Builder setMessage(String message){
            this.message=message;
            return this;
        }

        public Builder setCode(String code){
            this.code = code;
            return this;
        }

        public Builder setData(Object data){
            this.data=data;
            return this;
        }
        public RestApiResponse build(){
            RestApiResponse restApiResponse = new RestApiResponse();
            restApiResponse.status=this.status;
            restApiResponse.code=this.code;
            restApiResponse.message=this.message;
            restApiResponse.data=this.data;
            return restApiResponse;

        }
    }
}
