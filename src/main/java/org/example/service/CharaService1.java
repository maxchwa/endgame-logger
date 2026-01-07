package org.example.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.example.modal.Chara;
import org.example.repository.CharaRepo;
import org.example.service.FandomScraper;
import org.example.client.MediaWikiClient;

@Service
public class CharaService1 implements CharaService {

    @Autowired
    private CharaRepo charaRepository;

    @Autowired
    private FandomScraper scraper;

    private final MediaWikiClient mediaWikiClient;

    public CharaService1(MediaWikiClient mediaWikiClient) {
        this.mediaWikiClient = mediaWikiClient;
    }

    public List<Chara> findAllChara() {
        return (List<Chara>) charaRepository.findAll();
    }

    @Override
    public Chara findCharaByID(long id) {
        List<Chara> chars = charaRepository.findById(id).stream().toList();
        return chars.isEmpty() ? null : chars.get(0);
    }

    public Chara findCharaByName(String name) {
        List<Chara> chars = charaRepository.findCharaByName(name);
        return chars.isEmpty() ? null : chars.get(0);
    }

    public List<Chara> findCharaByRole(String role) {
        List<Chara> chars = charaRepository.findByRole(role);
        return chars != null ? chars : new ArrayList<>();
    }

    @Override
    public void addChara(Chara chara) {
        scraper.enrichChara(chara);
        charaRepository.save(chara);
    }

    public void deleteChara(String name) {
        List<Chara> chars = charaRepository.findCharaByName(name);
        if (chars != null && !chars.isEmpty()) {
            charaRepository.delete(chars.get(0));
        }
    }
}
