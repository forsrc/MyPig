package com.forsrc.tcc;

import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forsrc.MyApplicationTests;
import com.forsrc.common.utils.StringUtils;
import com.forsrc.tcc.dao.TccDao;
import com.forsrc.tcc.dao.TccLinkDao;
import com.forsrc.tcc.dao.mapper.TccMapper;
import com.forsrc.tcc.domain.entity.Tcc;

public class TransactionTest extends MyApplicationTests {

    @Autowired
    private TestTransactionService testTransactionService;

    @Test(expected = Exception.class)
    //@Test
    public void testTransaction() throws Exception {
        testTransactionService.testTransaction();
    }

    @Transactional
    @Service
    static class TestTransactionService {
        @Autowired
        private TccDao tccDao;

        @Autowired
        private TccLinkDao tccLinkDao;

        @Autowired
        private TccMapper tccMapper;

        @Transactional
        public void testTransaction() throws Exception {
            UUID id = StringUtils.toUuid("d4e55207-db0a-4b8e-9691-90305cb51a44");
            Tcc tcc = tccDao.getOne(id);
            System.out.println(tcc);
            assertNotNull(tcc);
            tcc.setStatus(20);
            tccDao.save(tcc);
            System.out.println(tcc);
            tcc.setStatus(30);
            tccMapper.updateStatus(tcc);
            System.out.println(tcc);
            throw new Exception("testTransaction");
        }
    }
}
