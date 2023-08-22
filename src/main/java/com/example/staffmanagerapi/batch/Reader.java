package com.example.staffmanagerapi.batch;

import com.example.staffmanagerapi.model.Collaborator;
import com.example.staffmanagerapi.repository.CollaboratorRepository;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
@Slf4j
public class Reader implements ItemReader<Page<Collaborator>> {


    private final CollaboratorRepository collaboratorRepository;
    List<Collaborator> idDemandes = Lists.newArrayList();
    Iterator<Page<Collaborator>> collaboratorIterator;
    int currentPage = 0;
    int numberOfElements = 10;

    public Reader(CollaboratorRepository collaboratorRepository) {
        this.collaboratorRepository = collaboratorRepository;
    }


    @BeforeStep
    public void before(StepExecution stepExecution) {
        idDemandes.add(Collaborator.builder().id(1L).phone("1").email("me.chekini@gmail.com").build());
        idDemandes.add(Collaborator.builder().id(2L).phone("2").email("me.chekini@gmail.com").build());
        idDemandes.add(Collaborator.builder().id(3L).phone("3").email("me.chekini@gmail.com").build());
        idDemandes.add(Collaborator.builder().id(4L).phone("4").email("me.chekini@gmail.com").build());
        idDemandes.add(Collaborator.builder().id(5L).phone("5").email("me.chekini@gmail.com").build());
        idDemandes.add(Collaborator.builder().id(6L).phone("6").email("me.chekini@gmail.com").build());
        idDemandes.add(Collaborator.builder().id(7L).phone("7").email("me.chekini@gmail.com").build());


//        collaboratorIterator = Pageable.idDemandes.iterator();
    }


    @Override
    public Page<Collaborator> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        log.info("je suis passé par le reader");

        List<Collaborator> collaborators = collaboratorRepository.findAll();

        if (collaboratorIterator != null && collaboratorIterator.hasNext()) {
            return collaboratorIterator.next();
        } else {
            return null;
        }
    }
}
