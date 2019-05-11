
-- t_sso_user_tcc
INSERT INTO t_tcc (id, expire, status, times) SELECT * FROM (SELECT 1 id, TIMESTAMPADD(MINUTE, 5, CURRENT_TIMESTAMP) expire, 0 status, 0 times) AS T WHERE NOT EXISTS (SELECT id FROM t_tcc WHERE id = 1);

INSERT INTO t_tcc_link (id, tcc_id, uri, resource_id, expire, status) SELECT * FROM (SELECT  1 id, 1 tcc_id, 'http://MYPIG-SSO-SERVER/sso/api/v1/tcc/user/' uri, 1 resource_id, TIMESTAMPADD(MINUTE, 5, CURRENT_TIMESTAMP) expire, 0 status) AS T WHERE NOT EXISTS (SELECT id FROM t_tcc_link WHERE id = 1);



UPDATE t_tcc      SET expire = TIMESTAMPADD(MINUTE, 5, CURRENT_TIMESTAMP), status = 0, times = 0 WHERE id = 1;
UPDATE t_tcc_link SET expire = TIMESTAMPADD(MINUTE, 5, CURRENT_TIMESTAMP), status = 0            WHERE id = 1;


