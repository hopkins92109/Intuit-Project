package com.personalinfo.controller;

import com.personalinfo.dto.PersonalInfoDto;
import com.personalinfo.service.PersonalService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/personal")
@AllArgsConstructor
public class PersonalController {

    private PersonalService personalService;

    @PostMapping
    public ResponseEntity<String> addPersonalInfo(@RequestBody PersonalInfoDto personalInfo) {

        if ( personalService.sendPersonalInfo(personalInfo) ) {
            return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("", HttpStatus.NOT_ACCEPTABLE);

    }


}
