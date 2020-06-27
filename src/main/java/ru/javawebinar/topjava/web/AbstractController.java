package ru.javawebinar.topjava.web;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.StringJoiner;

public class AbstractController {
    public ResponseEntity<String> checkResult(BindingResult result){
        if (result.hasErrors()) {
            StringJoiner joiner = new StringJoiner("<br>");
            result.getFieldErrors().forEach(
                    fe -> joiner.add(String.format("[%s] %s", fe.getField(), fe.getDefaultMessage()))
            );
            return ResponseEntity.unprocessableEntity().body(joiner.toString());
        }
        return ResponseEntity.ok().build();
    }
}
