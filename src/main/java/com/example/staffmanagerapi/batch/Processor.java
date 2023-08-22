package com.example.staffmanagerapi.batch;

import com.example.staffmanagerapi.model.Collaborator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class Processor implements ItemProcessor<Page<Collaborator>, Collaborator> {


    @Override
    public Collaborator process(Page<Collaborator> item) throws Exception {
        return null;
    }
}
