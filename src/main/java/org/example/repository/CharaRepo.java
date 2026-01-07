package org.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.example.modal.Chara;
import org.example.modal.Clear;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface CharaRepo extends JpaRepository<Chara, Long>{
    List<Chara> findAll();
    List<Chara> findCharaByName(String name);
    List<Chara> findByRole(String role);
}