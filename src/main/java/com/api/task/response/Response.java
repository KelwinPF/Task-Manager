package com.api.task.response;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Response<T> {

    private T data;
    private List<String> Errors;

    public void setData(T convertEntityToDTO) {
        this.data = convertEntityToDTO;
    }

    public List<String> getErrors() {
        if(this.Errors == null){
            this.Errors  = new ArrayList<String>();
        }
        return Errors;
    }
}