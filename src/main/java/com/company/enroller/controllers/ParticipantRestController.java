package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

	@Autowired
	ParticipantService participantService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipants(@RequestParam(defaultValue = "login") String sortBy,
                                             @RequestParam(defaultValue = "ASC") String sortOrder) {
		Collection<Participant> participants = participantService.getAll();
        if (sortBy != null && sortOrder != null) {

        }
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
            return new ResponseEntity("Already Exists", HttpStatus.CONFLICT);
        }

        participantService.add(participant);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteParticipant(@RequestBody Participant participant) {
        Participant findParticipant = participantService.findByLogin(participant.getLogin());
        if (findParticipant == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        participantService.delete(findParticipant);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public ResponseEntity<?> updateParticipant(@PathVariable("id") String login, @RequestBody Participant participant) {
        Participant findParticipant = participantService.findByLogin(login);
        if (findParticipant == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        findParticipant.setPassword(participant.getPassword());
        participantService.update(findParticipant);
        return new ResponseEntity<>("Login credencials updated", HttpStatus.OK);
    }

}
