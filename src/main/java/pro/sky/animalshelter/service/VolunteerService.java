package pro.sky.animalshelter.service;

import org.springframework.stereotype.Service;
import pro.sky.animalshelter.model.Volunteer;
import pro.sky.animalshelter.repository.VolunteerRepository;
/**
 * Сервис, отвечающий за работу с волонтерами
 */
@Service
public class VolunteerService {
    private final VolunteerRepository volunteerRepository;

    public VolunteerService(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    public Volunteer createVolunteer(Volunteer volunteer) {
        return volunteerRepository.save(volunteer);
    }

    public Volunteer findVolunteer(long id) {
        return volunteerRepository.findById(id).get();
    }

    public Volunteer editVolunteer(Volunteer volunteer) {
        return volunteerRepository.save(volunteer);
    }

    public void deleteVolunteer(long id) {
        volunteerRepository.deleteById(id);
    }
}
