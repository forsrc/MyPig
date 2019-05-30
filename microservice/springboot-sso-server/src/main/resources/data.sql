
-- oauth_client_details
-- INSERT INTO oauth_client_details (client_id, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES  ('forsrc', 'forsrc', 'forsrc', 'forsrc,read,write', 'authorization_code,client_credentials,refresh_token,password,implicit', null, null, 36000, 36000, null, true);
INSERT INTO oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES  ('sso',   'forsrc,api,sso,ui,tcc,user', '$2a$10$Smc1lKpNSr/MeX1ZTt0GVu0b6LzlbOBp8Lzy9JfriAb1Xp8rVBMsm', 'sso,read,write',    'authorization_code,client_credentials,refresh_token,password,implicit', null, null, 36000, 36000, null, true);
INSERT INTO oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES  ('tcc',   'forsrc,api,sso,ui,tcc,user', '$2a$10$Smc1lKpNSr/MeX1ZTt0GVu0b6LzlbOBp8Lzy9JfriAb1Xp8rVBMsm', 'tcc,read,write',    'authorization_code,client_credentials,refresh_token,password,implicit', null, null, 36000, 36000, null, true);
INSERT INTO oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES  ('ui',    'forsrc,api,sso,ui,tcc,user', '$2a$10$Smc1lKpNSr/MeX1ZTt0GVu0b6LzlbOBp8Lzy9JfriAb1Xp8rVBMsm', 'ui,read,write',     'authorization_code,client_credentials,refresh_token,password,implicit', null, null, 36000, 36000, null, true);
INSERT INTO oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES  ('user',  'forsrc,api,sso,ui,tcc,user', '$2a$10$Smc1lKpNSr/MeX1ZTt0GVu0b6LzlbOBp8Lzy9JfriAb1Xp8rVBMsm', 'user,read,write',   'authorization_code,client_credentials,refresh_token,password,implicit', null, null, 36000, 36000, null, true);
INSERT INTO oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES  ('api',   'forsrc,api,sso,ui,tcc,user', '$2a$10$Smc1lKpNSr/MeX1ZTt0GVu0b6LzlbOBp8Lzy9JfriAb1Xp8rVBMsm', 'user,read,write',   'authorization_code,client_credentials,refresh_token,password,implicit', null, null, 36000, 36000, null, true);







-- users
INSERT INTO users (username, password, enabled) SELECT * FROM (SELECT 'forsrc@gmail.com' username, '$2a$10$Wzme7qZtAsJZspQpNx3ee.qTu/IqRHiTb0jORWUOXCxptAkG3kf8e' password, 1 enabled) AS T WHERE NOT EXISTS (SELECT username FROM users WHERE username = 'forsrc@gmail.com');
INSERT INTO users (username, password, enabled) SELECT * FROM (SELECT 'user'             username, '$2a$10$SNKOBpTBuCbWukZ3Rc5DpuIHRP585Ss02fULAIX/m1NmFpWeJ8ic2' password, 1 enabled) AS T WHERE NOT EXISTS (SELECT username FROM users WHERE username = 'user');
INSERT INTO users (username, password, enabled) SELECT * FROM (SELECT 'tcc'              username, '$2a$10$lFUTwK/W3S3U8NI3cnqJPeVD3cZj6udLbW2W5GMvybtJw70N4WqFC' password, 1 enabled) AS T WHERE NOT EXISTS (SELECT username FROM users WHERE username = 'tcc');
INSERT INTO users (username, password, enabled) SELECT * FROM (SELECT 'test'             username, '$2a$10$uCchlP6N1q7ZOEMMifeZyOEOgqpddiVEIiIrM4k/76ftgLxtBaSXq' password, 1 enabled) AS T WHERE NOT EXISTS (SELECT username FROM users WHERE username = 'test');

-- authorities
INSERT INTO authorities (username, authority) SELECT * FROM (SELECT 'forsrc@gmail.com' username, 'ROLE_ADMIN' authorities) AS T WHERE NOT EXISTS (SELECT username FROM authorities WHERE username = 'forsrc@gmail.com' and authority = 'ROLE_ADMIN');
INSERT INTO authorities (username, authority) SELECT * FROM (SELECT 'forsrc@gmail.com' username, 'ROLE_USER'  authorities) AS T WHERE NOT EXISTS (SELECT username FROM authorities WHERE username = 'forsrc@gmail.com' and authority = 'ROLE_USER');
INSERT INTO authorities (username, authority) SELECT * FROM (SELECT 'user'             username, 'ROLE_USER'  authorities) AS T WHERE NOT EXISTS (SELECT username FROM authorities WHERE username = 'user');
INSERT INTO authorities (username, authority) SELECT * FROM (SELECT 'tcc'              username, 'ROLE_TCC'   authorities) AS T WHERE NOT EXISTS (SELECT username FROM authorities WHERE username = 'tcc');
INSERT INTO authorities (username, authority) SELECT * FROM (SELECT 'test'             username, 'ROLE_TEST'  authorities) AS T WHERE NOT EXISTS (SELECT username FROM authorities WHERE username = 'test');

-- t_sso_user_tcc
INSERT INTO t_sso_user_tcc (id, username, password, enabled, authorities, expire, status) SELECT * FROM (SELECT 1 id, 'A' username, '$2a$10$Wzme7qZtAsJZspQpNx3ee.qTu/IqRHiTb0jORWUOXCxptAkG3kf8e' password, 1 enabled, 'ROLE_USER' authorities, TIMESTAMPADD(MINUTE, 5, CURRENT_TIMESTAMP) expire, 0 status) AS T WHERE NOT EXISTS (SELECT username FROM t_sso_user_tcc WHERE id = 1);
UPDATE t_sso_user_tcc SET expire = TIMESTAMPADD(MINUTE, 5, CURRENT_TIMESTAMP), status = 0 WHERE id = 1;


