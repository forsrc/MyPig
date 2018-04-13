
-- t_sso_user_tcc
--INSERT INTO t_tcc (id, expire, status, times) SELECT * FROM (SELECT 'd4e55207db0a4b8e969190305cb51a44' id, TIMESTAMPADD(MINUTE, 5, CURRENT_TIMESTAMP) expire, 0 status, 0 times) AS T WHERE NOT EXISTS (SELECT id FROM t_tcc WHERE id = 'd4e55207db0a4b8e969190305cb51a44');
INSERT INTO t_tcc (id, expire, status, times) SELECT * FROM (SELECT uuid_in('d4e55207db0a4b8e969190305cb51a44') id, (CURRENT_TIMESTAMP + '5 minute') expire, 0 status, 0 times) AS T WHERE NOT EXISTS (SELECT id FROM t_tcc WHERE id = 'd4e55207db0a4b8e969190305cb51a44');

--INSERT INTO t_tcc_link (id, tcc_id, uri, path, expire, status) SELECT * FROM (SELECT  '36ef1f69e4c942f7ab55f1c77231c582' id, 'd4e55207db0a4b8e969190305cb51a44' tcc_id, 'http://SPRINGBOOT-SSO-SERVER/sso/api/v1/tcc/user/' uri, '5112cdecfec04f2e8d364ed1d14b7882' entity_id, TIMESTAMPADD(MINUTE, 5, CURRENT_TIMESTAMP) expire, 0 status) AS T WHERE NOT EXISTS (SELECT id FROM t_tcc_link WHERE id = '36ef1f69e4c942f7ab55f1c77231c582');
INSERT INTO t_tcc_link (id, tcc_id, uri, path, expire, status) SELECT * FROM (SELECT  uuid_in('36ef1f69e4c942f7ab55f1c77231c582') id, 'd4e55207db0a4b8e969190305cb51a44' tcc_id, 'http://SPRINGBOOT-SSO-SERVER/sso/api/v1/tcc/user/' uri, '5112cdecfec04f2e8d364ed1d14b7882' entity_id, (CURRENT_TIMESTAMP + '5 minute') expire, 0 status) AS T WHERE NOT EXISTS (SELECT id FROM t_tcc_link WHERE id = '36ef1f69e4c942f7ab55f1c77231c582');



--UPDATE t_tcc      SET expire = TIMESTAMPADD(MINUTE, 5, CURRENT_TIMESTAMP), status = 0, times = 0 WHERE id = 'd4e55207db0a4b8e969190305cb51a44';
--UPDATE t_tcc_link SET expire = TIMESTAMPADD(MINUTE, 5, CURRENT_TIMESTAMP), status = 0            WHERE id = '36ef1f69e4c942f7ab55f1c77231c582';

UPDATE t_tcc      SET expire = (CURRENT_TIMESTAMP + '5 minute'), status = 0, times = 0 WHERE id = 'd4e55207db0a4b8e969190305cb51a44';
UPDATE t_tcc_link SET expire = (CURRENT_TIMESTAMP + '5 minute'), status = 0            WHERE id = '36ef1f69e4c942f7ab55f1c77231c582';

