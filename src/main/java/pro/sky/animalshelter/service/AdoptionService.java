package pro.sky.animalshelter.service;

import org.springframework.stereotype.Service;
import pro.sky.animalshelter.model.Adoption;
import pro.sky.animalshelter.repository.AdoptionRepository;

import java.util.Collection;

@Service
public class AdoptionService {
    private final AdoptionRepository adoptionRepository;

    public AdoptionService(AdoptionRepository adoptionRepository) {
        this.adoptionRepository = adoptionRepository;
    }


    public Adoption createAdoption(Adoption adoption) {
        return adoptionRepository.save(adoption);
    }

    public Collection<Adoption> allAdoptions() {
        return adoptionRepository.findAll();
    }

    public Adoption findById(Long id) {
        return adoptionRepository.findById(id).get();
    }
}
