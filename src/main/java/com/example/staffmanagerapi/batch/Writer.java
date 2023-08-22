package com.example.staffmanagerapi.batch;

import com.example.staffmanagerapi.model.Collaborator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Writer implements ItemWriter<Collaborator> {
    @Override
    public void write(Chunk<? extends Collaborator> chunk) throws Exception {

        log.info("je suis passé par le writer");

    }
}
