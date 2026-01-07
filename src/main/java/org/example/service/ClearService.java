package org.example.service;
import java.util.ArrayList;
import org.example.modal.Clear;

public interface ClearService {
    ArrayList<Clear> findAllClears();
    Clear findClearByID(long id);
    void addClear(Clear clear);
}