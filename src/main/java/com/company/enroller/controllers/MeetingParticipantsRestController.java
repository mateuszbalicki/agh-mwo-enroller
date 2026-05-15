package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/meetings/{id}/participants")
public class MeetingParticipantsRestController {

    @Autowired
    private MeetingService meetingService;
    @Autowired
    private ParticipantService participantService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetingParticipants(@PathVariable("id") long id) {
        Meeting meeting = meetingService.findById(id);
        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Collection<Participant>>(meeting.getParticipants(), HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> addParticipantToMeeting(@PathVariable("id") long id, @RequestBody Participant participant) {
        Meeting meeting = meetingService.findById(id);
        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Participant participantToAdd = participantService.findByLogin(participant.getLogin());
        if (participantToAdd == null) {
            return new ResponseEntity<>("Unable to add. Participant with login " + participant.getLogin() + " does not exist.", HttpStatus.NOT_FOUND);
        }

        meeting.addParticipant(participantToAdd);
        meetingService.update(meeting);
        return new ResponseEntity<Collection<Participant>>(meeting.getParticipants(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{login}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeParticipantFromMeeting(@PathVariable("id") long id, @PathVariable("login") String login) {
        Meeting meeting = meetingService.findById(id);
        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Participant participantToRemove = participantService.findByLogin(login);
        if (participantToRemove == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        meeting.removeParticipant(participantToRemove);
        meetingService.update(meeting);
        return new ResponseEntity<Collection<Participant>>(meeting.getParticipants(), HttpStatus.OK);
    }
}

