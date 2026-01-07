package org.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.modal.Clear;
import org.example.modal.Chara;
import org.example.modal.PartyMember;
import org.example.repository.ClearRepo;

@Service
public class ClearService1 implements ClearService {

    @Autowired
    ClearRepo clearRepository;

    @Override
    public void addClear(Clear clear) {

        for (PartyMember pm : clear.getPartyMembers()) {
            pm.setClear(clear);
        }

        clearRepository.save(clear);

    }

    public void deleteClear(String endgame, double version, int side) {

        Optional<Clear> opt = clearRepository.findClearByEndgameAndVersionAndSide(endgame, version, side);
        if (opt.isPresent()) {
            clearRepository.delete(opt.get());
        }

    }

    @Override
    public Clear findClearByID(long id) {
        Optional<Clear> opt = clearRepository.findById(id);
        if (opt.isPresent())
            return opt.get();
        else
            return null;
    }

    public Clear findClearByEndgameVerSide(String endgame, double version, int side) {
        Optional<Clear> opt = clearRepository.findClearByEndgameAndVersionAndSide(endgame, version, side);
        if (opt.isPresent())
            return opt.get();
        else
            return null;
    }

    //FIND LIST OF CLEARS (FOR FURTHER EDIITNG ETC.), NOT SPECIFIC CHAR

    @Override
    public ArrayList<Clear> findAllClears() {
        return (ArrayList<Clear>) clearRepository.findAll();
    }

    public List<Clear> getClearsByEndgame(String endgame) {
        return clearRepository.findByEndgame(endgame);
    }

    public List<Clear> getZeroCycleClears() {
        return clearRepository.findByZeroCycleTrue();
    }

    public List<Clear> getPFFullScoreClears() {
        return clearRepository.findByScore(40000);
    }

    public List<Clear> getZeroCycleClearsByEndgame(String endgame) {
        return clearRepository.findByEndgameAndZeroCycleTrue(endgame);
    }

    public List<Clear> getClearsByEndgameAndMajorVersion(String endgame, int ver) {
        return clearRepository.findByEndgameAndMajorVersion(endgame, (double) ver, (double) ver + 1);
    }

    //FIND LIST OF CLEARS (FOR FURTHER EDITING), SPECIFIC CHAR

    public List<Clear> getClearsByCharacter(String character) {
        return clearRepository.findByCharacter(character);
    }

    public List<Clear> getClearsByCharacterAndEndgame(String character, String endgame) {
        return clearRepository.findByCharacterAndEndgame(character, endgame);
    }

    public List<Clear> getClearsByCharacterEndgameAndMajorVersion(String chara, String endgame, int ver) {
        return clearRepository.findByCharacterAndEndgameAndMajorVersion(chara, endgame, ver, ver + 1);
    }

    //GET TOP X MANY. String → member name, Long → number of clears that member appeared in

    public List<Map.Entry<String, Long>> top3Members(List<Clear> clears) {
        return clears.stream()
                .flatMap(c -> c.getPartyMembers().stream())
                .collect(Collectors.groupingBy(
                        PartyMember::getName,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .toList();
    }

    public List<Map.Entry<String, Long>> top5Members(List<Clear> clears) {
        return clears.stream()
                .flatMap(c -> c.getPartyMembers().stream())
                .collect(Collectors.groupingBy(
                        PartyMember::getName,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .toList();
    }

    public List<Clear> top3ClearsByScore(List<Clear> clears) {
        return clears.stream()
                .sorted(Comparator.comparingLong(Clear::getScore).reversed())
                .limit(3)
                .toList();
    }

    public List<Clear> top5ClearsByScore(List<Clear> clears) {
        return clears.stream()
                .sorted(Comparator.comparingLong(Clear::getScore).reversed())
                .limit(5)
                .toList();
    }

    public List<Map.Entry<Double, Long>> top3VersionsByTotalScore(List<Clear> clears) {
        return clears.stream()
                .collect(Collectors.groupingBy(
                        Clear::getVersion,
                        Collectors.summingLong(Clear::getScore)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<Double, Long>comparingByValue().reversed())
                .limit(3)
                .toList();
    }

    public List<Map.Entry<Double, Long>> top5VersionsByTotalScore(List<Clear> clears) {
        return clears.stream()
                .collect(Collectors.groupingBy(
                        Clear::getVersion,
                        Collectors.summingLong(Clear::getScore)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<Double, Long>comparingByValue().reversed())
                .limit(5)
                .toList();
    }



    //filtered as in -- filtered by role
    public List<Map.Entry<String, Long>> top3MembersFiltered(
            List<Clear> clears,
            List<Chara> allowedChars
    ) {
        Set<String> allowedNames = allowedChars.stream()
                .map(Chara::getName)
                .collect(Collectors.toSet());

        return clears.stream()
                .flatMap(c -> c.getPartyMembers().stream())
                .map(PartyMember::getName)
                .filter(allowedNames::contains)
                .collect(Collectors.groupingBy(
                        name -> name,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .toList();
    }

    public List<Map.Entry<String, Long>> top5MembersFiltered(
            List<Clear> clears,
            List<Chara> allowedChars
    ) {
        Set<String> allowedNames = allowedChars.stream()
                .map(Chara::getName)
                .collect(Collectors.toSet());

        return clears.stream()
                .flatMap(c -> c.getPartyMembers().stream())
                .map(PartyMember::getName)
                .filter(allowedNames::contains)
                .collect(Collectors.groupingBy(
                        name -> name,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .toList();
    }



    //FINDING THE AVERAGE

    public double averageScore(List<Clear> clears) {
        return clears.stream()
                .mapToInt(Clear::getScore)
                .average()
                .orElse(0.0);
    }

}
