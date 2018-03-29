package com.forsrc.tcc.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.forsrc.common.core.tcc.exception.TccException;
import com.forsrc.tcc.domain.entity.Tcc;
import com.forsrc.tcc.service.TccService;

@Component
public class TccSchedule {

    private static final Logger LOGGER = LoggerFactory.getLogger(TccSchedule.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @Autowired
    @Qualifier("tccOAuth2RestTemplate")
    public OAuth2RestTemplate tccOAuth2RestTemplate;

    @Autowired
    private TccService tccService;

    @Scheduled(cron = "0,10,20,30,40,50 * * * * *")
    public void tcc() throws TccException{
        List<Tcc> list = tccService.getTryStatusList();
        LOGGER.info("--> TccSchedule {} -> size: {}", dateFormat.format(new Date()), list.size());
        for (Tcc tcc : list) {
            LOGGER.info("--> TccSchedule tcc: {}", tcc);
            asyncTcc(tcc);
        }
    }

    @Async
    public void asyncTcc(Tcc tcc) throws TccException {
        LOGGER.info("--> TccSchedule tcc: {}", tcc);
        String accessToken = getAccessToken();
        if (tcc.getExpire() != null && tcc.getExpire().compareTo(new Date()) < 0) {
            tccService.cancel(tcc.getId(), accessToken);
            return;
        }
        tccService.confirm(tcc.getId(), accessToken);
    }

    private String getAccessToken() {
        return tccOAuth2RestTemplate.getAccessToken().getValue();
    }
}
