package org.example.service;
import java.util.List;
import org.example.modal.Chara;

public interface CharaService {
    List<Chara> findAllChara();
    List<Chara> findCharaByRole(String role);
    Chara findCharaByID(long id);
    void addChara(Chara chara);
}