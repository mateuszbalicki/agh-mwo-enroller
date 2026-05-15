package com.company.enroller.controllers;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

	@Autowired
	ParticipantService participantService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipants(@RequestParam (value = "sortBy", defaultValue = "") String sortBy,
                                             @RequestParam (value = "sortOrder",  defaultValue = "") String sortOrder,
                                             @RequestParam (value = "key", defaultValue = "") String key) {
        Collection<Participant> participants = participantService.getAll(sortBy, sortOrder, key);

        return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipant(@PathVariable("id") String login) {
        Participant participant = participantService.findByLogin(login);
        if (participant == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Participant>(participant, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> registerParticipant(@RequestBody Participant participant) {
        Participant findParticipant = participantService.findByLogin(participant.getLogin());
        if (findParticipant != null) {
            return new ResponseEntity("Unable to create. A participant with login " + participant.getLogin() + " already exist.", HttpStatus.CONFLICT);
        }

        String hashedPassword = passwordEncoder.encode(participant.getPassword());
        participant.setPassword(hashedPassword);

        participantService.add(participant);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteParticipant(@PathVariable("id") String login) {
        Participant participant = participantService.findByLogin(login);
        if (participant == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        participantService.delete(participant);
        return new ResponseEntity<>("Participant " + login + " deleted successfully.", HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateParticipant(@PathVariable("id") String login, @RequestBody Participant participant) {
        Participant findParticipant = participantService.findByLogin(login);
        if (findParticipant == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String hashedNewPassword = passwordEncoder.encode(participant.getPassword());
        findParticipant.setPassword(hashedNewPassword);

        participantService.update(findParticipant);
        return new ResponseEntity<>("Login credentials updated", HttpStatus.OK);
    }

}
