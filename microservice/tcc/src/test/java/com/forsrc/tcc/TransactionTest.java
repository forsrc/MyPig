package com.forsrc.tcc;

import static org.junit.Assert.assertNotNull;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forsrc.MyApplicationTests;
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

        @Transactional(rollbackOn = {Exception.class})
        public void testTransaction() throws Exception {
            //Tcc tcc = tccDao.getOne(id);
            Tcc tcc = new Tcc();
            tcc.setId(1L);
            System.out.println(tcc);
            assertNotNull(tcc);
            tcc.setStatus(20);
            //tccDao.save(tcc);
            System.out.println(tcc);
            tcc.setStatus(30);
            tccMapper.updateStatus(tcc);
            System.out.println(tcc);
            throw new Exception("testTransaction");
        }
    }
}
