package com.company.enroller.controllers;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingParticipiantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/meetings/{id}/participiants")
public class MeetingParticipiantsRestController {

    @Autowired
    private MeetingParticipiantService meetingParticipiantService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetingParticipiants(@PathVariable long id) {
        Collection<Participant> meetingParticipiants = meetingParticipiantService.getMeetingParticipiants();
        return new ResponseEntity<Collection<Participant>>(meetingParticipiants, HttpStatus.OK);
    }

}
