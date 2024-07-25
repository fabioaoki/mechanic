package br.com.mechanic.mechanic.job;

import br.com.mechanic.mechanic.service.RevisionServiceBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotifyExpectedRevisions {

    @Autowired
    private RevisionServiceBO revisionServiceBO;
}
