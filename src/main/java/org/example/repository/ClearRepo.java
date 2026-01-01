package org.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.example.modal.Clear;
import java.util.ArrayList;

@Repository
public interface ClearRepo extends JpaRepository<Clear, Long>{
}