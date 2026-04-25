package com.company.enroller.persistence;

import com.company.enroller.model.Participant;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("meetingParticipiants")
public class MeetingParticipiantService {

    DatabaseConnector connector;

    public MeetingParticipiantService() {
        connector = DatabaseConnector.getInstance();
    }

    public Collection<Participant> getMeetingParticipiants() {
        String hql = "FROM Meeting participiant";
        Query query = connector.getSession().createQuery(hql);
        return query.list();
    }
}
