package org.example.controller;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.modal.Clear;
import org.example.service.ClearService1;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class ClearController {

        @Autowired
        private ClearService1 clearService;

        @PostMapping("/")
        public void add(@RequestBody Clear clear) {
            clearService.addClear(clear);
        }

        @GetMapping("/findall")
        public ArrayList<Clear> getAllClear() {
            return clearService.findAllClear();
        }

        @GetMapping("/findbyid/{id}")
        public Clear getEmployeeUsingId(@PathVariable long id) {
            return clearService.findClearByID(id);
        }

}
