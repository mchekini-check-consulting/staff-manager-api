package com.example.staffmanagerapi.batch;

import com.example.staffmanagerapi.model.Collaborator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class Processor implements ItemProcessor<Collaborator, Collaborator> {
    @Override
    public Collaborator process(Collaborator item) throws Exception {
        log.info("je suis passé par le processor");

        Collaborator collaborator = item;
        collaborator.setEmail(item.getEmail().toUpperCase());

        return collaborator;
    }
}
