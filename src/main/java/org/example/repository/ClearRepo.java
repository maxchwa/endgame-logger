package org.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.example.modal.Clear;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ClearRepo extends JpaRepository<Clear, Long>{

    List<Clear> findAll();

    List<Clear> findByEndgame(String endgame);

    List<Clear> findByZeroCycleTrue();

    List<Clear> findByScore(int score);

    List<Clear> findByEndgameAndZeroCycleTrue(String endgame);

    Optional<Clear> findClearByEndgameAndVersionAndSide(String endgame, double version, int side);

    @Query("""
    SELECT DISTINCT c
    FROM Clear c
    JOIN c.members m
    WHERE m.name = :character
    """)
    List<Clear> findByCharacter(@Param("character") String character);

    @Query("""
    SELECT DISTINCT c
    FROM Clear c
    JOIN c.members m
    WHERE m.name = :character
      AND c.endgame = :endgame
    """)
    List<Clear> findByCharacterAndEndgame(
            @Param("character") String character,
            @Param("endgame") String endgame
    );

    @Query("""
    SELECT c
    FROM Clear c
    WHERE c.endgame = :endgame
      AND c.version >= :from
      AND c.version < :to
    """)
    List<Clear> findByEndgameAndMajorVersion(
            @Param("endgame") String endgame,
            @Param("from") double from,
            @Param("to") double to
    );

    @Query("""
    SELECT DISTINCT c
    FROM Clear c
    JOIN c.members m
    WHERE m.name = :character
      AND c.endgame = :endgame
      AND c.version >= :from
      AND c.version < :to
    """)

    List<Clear> findByCharacterAndEndgameAndMajorVersion(
            @Param("character") String character,
            @Param("endgame") String endgame,
            @Param("from") double from,
            @Param("to") double to
    );

}