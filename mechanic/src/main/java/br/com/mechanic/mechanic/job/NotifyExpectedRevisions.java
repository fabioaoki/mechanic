package br.com.mechanic.mechanic.job;

import br.com.mechanic.mechanic.service.RevisionServiceBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotifyExpectedRevisions {

    @Autowired
    private RevisionServiceBO revisionServiceBO;

//    @Scheduled(cron = "0 0 7 * * ?")
    @Scheduled(cron = "0 * * * * ?")  // Executa no início de cada minuto
    public void raffleWinner() {
        revisionServiceBO.senRevisionNotification();

    }
}
