package org.example.controller;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.example.modal.Clear;
import org.example.modal.Chara;
import org.example.modal.PartyMember;
import org.example.service.ClearService1;
import org.example.service.CharaService1;

@RestController
@RequestMapping("/api")
public class ClearController {

    @Autowired
    private ClearService1 clearService;

    private final CharaService1 charaService;

    public ClearController(CharaService1 charaService) {
        this.charaService = charaService;
    }

    private Map<String, Object> toClearResponse(Clear c) {
        Map<String, Object> map = new HashMap<>();

        map.put("id", c.getId());
        map.put("endgame", c.getEndgame());
        map.put("version", c.getVersion());
        map.put("side", c.getSide());
        map.put("stars", c.getStars());
        map.put("score", c.getScore());
        map.put("zeroCycle", c.isZeroCycle());
        map.put("notes", c.getNotes());

        List<Map<String, Object>> members = c.getPartyMembers().stream()
                .map(pm -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("name", pm.getName());
                    m.put("eidolon", pm.getEidolon());
                    m.put("signature", pm.getSignature());
                    return m;
                })
                .toList();

        map.put("members", members);

        return map;
    }

    @PostMapping("/clear")
    public void addClear(@RequestBody Clear clear) {
        clearService.addClear(clear);
    }

    @DeleteMapping("/clear/{endgame}/{version:.+}/{side}")
    public void deleteClear(
            @PathVariable String endgame,
            @PathVariable double version,
            @PathVariable int side
    ) {
        clearService.deleteClear(endgame, version, side);
    }

    @GetMapping("/clear")
    public List<Map<String, Object>> getAllClears() {
        return clearService.findAllClears().stream()
                .map(this::toClearResponse)
                .toList();
    }

    @GetMapping("/clear/{id}")
    public Map<String, Object> getClearById(@PathVariable long id) {
        return toClearResponse(clearService.findClearByID(id));
    }

    @GetMapping("/clear/{endgame}/{version}/{side}")
    public Map<String, Object> getClearByCompositeKey(
            @PathVariable String endgame,
            @PathVariable double version,
            @PathVariable int side
    ) {
        return toClearResponse(
                clearService.findClearByEndgameVerSide(endgame, version, side)
        );
    }

    @PostMapping("/chara")
    public void addChar(@RequestBody Chara chara) {
        charaService.addChara(chara);
    }

    @DeleteMapping("/chara/{name}")
    public void deleteChar(@PathVariable String name) {
        charaService.deleteChara(name);
    }

    @GetMapping("/stats/global/top-characters")
    public List<Map.Entry<String, Long>> getGlobalTopCharacters(
            @RequestParam(defaultValue = "3") int topN
    ) {
        List<Clear> clears = clearService.findAllClears();
        return topN == 3
                ? clearService.top3Members(clears)
                : clearService.top5Members(clears);
    }

    @GetMapping("/stats/global/top-characters/by-role")
    public List<Map.Entry<String, Long>> getGlobalTopCharactersByRole(
            @RequestParam String role,
            @RequestParam(defaultValue = "3") int topN
    ) {
        List<Clear> clears = clearService.findAllClears();
        List<Chara> charsByRole = charaService.findCharaByRole(role);

        return topN == 3
                ? clearService.top3MembersFiltered(clears, charsByRole)
                : clearService.top5MembersFiltered(clears, charsByRole);
    }

    @GetMapping("/stats/global/zero-cycles")
    public List<Map<String, Object>> getGlobalZeroCycles() {
        return clearService.getZeroCycleClears().stream()
                .map(this::toClearResponse)
                .toList();
    }

    @GetMapping("/stats/endgame/clears")
    public List<Map<String, Object>> getEndgameClears(
            @RequestParam String endgame,
            @RequestParam(required = false) Integer version
    ) {
        List<Clear> clears = version == null
                ? clearService.getClearsByEndgame(endgame)
                : clearService.getClearsByEndgameAndMajorVersion(endgame, version);

        return clears.stream()
                .map(this::toClearResponse)
                .toList();
    }

    @GetMapping("/stats/endgame/average-score")
    public double getEndgameAverageScore(
            @RequestParam String endgame,
            @RequestParam(required = false) Integer version
    ) {
        List<Clear> clears = version == null
                ? clearService.getClearsByEndgame(endgame)
                : clearService.getClearsByEndgameAndMajorVersion(endgame, version);

        return clearService.averageScore(clears);
    }

    @GetMapping("/stats/endgame/top-clears")
    public List<Map<String, Object>> getTopEndgameClears(
            @RequestParam String endgame,
            @RequestParam(required = false) Integer version,
            @RequestParam(defaultValue = "3") int topN
    ) {
        List<Clear> clears = version == null
                ? clearService.getClearsByEndgame(endgame)
                : clearService.getClearsByEndgameAndMajorVersion(endgame, version);

        List<Clear> top = topN == 3
                ? clearService.top3ClearsByScore(clears)
                : clearService.top5ClearsByScore(clears);

        return top.stream()
                .map(this::toClearResponse)
                .toList();
    }

    @GetMapping("/stats/endgame/top-versions")
    public List<Map<String, Object>> getTopVersions(
            @RequestParam String endgame
    ) {
        return clearService.getClearsByEndgame(endgame).stream()
                .collect(Collectors.groupingBy(
                        Clear::getVersion,
                        Collectors.summingInt(Clear::getScore)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<Double, Integer>comparingByValue().reversed())
                .limit(3)
                .map(e -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("version", e.getKey());
                    map.put("totalScore", e.getValue());
                    return map;
                })
                .toList();
    }

    @GetMapping("/stats/endgame/top-characters")
    public List<Map.Entry<String, Long>> getTopCharactersInEndgame(
            @RequestParam String endgame,
            @RequestParam(required = false) Integer version,
            @RequestParam(defaultValue = "3") int topN
    ) {
        List<Clear> clears = version == null
                ? clearService.getClearsByEndgame(endgame)
                : clearService.getClearsByEndgameAndMajorVersion(endgame, version);

        return topN == 3
                ? clearService.top3Members(clears)
                : clearService.top5Members(clears);
    }

    @GetMapping("/stats/endgame/moc/zero-cycles")
    public List<Map.Entry<String, Long>> getMoCZeroCycles() {
        return clearService.top3Members(
                clearService.getZeroCycleClearsByEndgame("MoC")
        );
    }

    @GetMapping("/stats/endgame/as/zero-cycles")
    public List<Map.Entry<String, Long>> getASZeroCycles() {
        return clearService.top3Members(
                clearService.getZeroCycleClearsByEndgame("AS")
        );
    }

    @GetMapping("/stats/endgame/pf/full-score")
    public List<Map.Entry<String, Long>> getPFFullScoreCharacters() {
        return clearService.top5Members(
                clearService.getPFFullScoreClears()
        );
    }

    @GetMapping("/character/{name}")
    public Chara getCharacter(@PathVariable String name) {
        return charaService.findCharaByName(name);
    }

    @GetMapping("/character/{name}/clears")
    public List<Map<String, Object>> getCharacterClears(
            @PathVariable String name,
            @RequestParam String endgame
    ) {
        return clearService.getClearsByCharacterAndEndgame(name, endgame).stream()
                .map(this::toClearResponse)
                .toList();
    }

    @GetMapping("/character/{name}/average")
    public double getCharacterAverage(
            @PathVariable String name,
            @RequestParam String endgame
    ) {
        return clearService.averageScore(
                clearService.getClearsByCharacterAndEndgame(name, endgame)
        );
    }

    @GetMapping("/character/{name}/top-partners")
    public List<Map.Entry<String, Long>> getCharacterTopPartners(
            @PathVariable String name,
            @RequestParam(defaultValue = "3") int limit
    ) {
        return clearService.getClearsByCharacter(name).stream()
                .flatMap(c -> c.getPartyMembers().stream()
                        .map(PartyMember::getName)
                        .filter(n -> !n.equals(name))
                        .distinct()
                )
                .collect(Collectors.groupingBy(n -> n, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(limit)
                .toList();
    }
}
