package org.example.service;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.modal.Clear;
import org.example.modal.PartyMember;
import org.example.repository.ClearRepo;

@Service
public class ClearService1 implements ClearService {

    @Autowired
    ClearRepo clearRepository;

    @Override
    public ArrayList<Clear> findAllClear() {
        return (ArrayList<Clear>) clearRepository.findAll();
    }

    @Override
    public Clear findClearByID(long id) {
        Optional<Clear> opt = clearRepository.findById(id);
        if (opt.isPresent())
            return opt.get();
        else
            return null;
    }

    @Override
    public void addClear(Clear clear) {

        System.out.println("Number of members: " + clear.getPartyMembers().size());

        for (PartyMember pm : clear.getPartyMembers()) {
            pm.setClear(clear);
        }

        clearRepository.save(clear);

    }
}
