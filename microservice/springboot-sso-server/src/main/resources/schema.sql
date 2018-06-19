DROP TABLE IF EXISTS ClientDetails;
CREATE TABLE ClientDetails (
  appId                  varchar(256)  NOT NULL,
  resourceIds            varchar(256)  DEFAULT NULL,
  appSecret              varchar(256)  DEFAULT NULL,
  scope                  varchar(256)  DEFAULT NULL,
  grantTypes             varchar(256)  DEFAULT NULL,
  redirectUrl            varchar(256)  DEFAULT NULL,
  authorities            varchar(256)  DEFAULT NULL,
  access_token_validity  int(11)       DEFAULT NULL,
  refresh_token_validity int(11)       DEFAULT NULL,
  additionalInformation  varchar(4096) DEFAULT NULL,
  create                 TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update                 TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (appId)
) ; -- ENGINE=InnoDB DEFAULT CHARSET=latin1;




DROP TABLE IF EXISTS authorities;
CREATE TABLE authorities (
  username  varchar(50)  NOT NULL,
  authority varchar(50)  NOT NULL,
  version   int          NOT NULL DEFAULT 0,
  create    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_auth_username (username,authority)
) ; -- ENGINE=InnoDB DEFAULT CHARSET=latin1;



DROP TABLE IF EXISTS oauth_access_token;
CREATE TABLE oauth_access_token (
  token_id          varchar(256) DEFAULT NULL,
  token             blob,
  authentication_id varchar(256) DEFAULT NULL,
  user_name         varchar(256) DEFAULT NULL,
  client_id         varchar(256) DEFAULT NULL,
  authentication    blob,
  refresh_token     varchar(256) DEFAULT NULL,
  create            TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update            TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_authentication_id (authentication_id)
) ; -- ENGINE=InnoDB DEFAULT CHARSET=latin1;



DROP TABLE IF EXISTS oauth_client_details;
CREATE TABLE oauth_client_details (
  client_id               varchar(256)  NOT NULL,
  resource_ids            varchar(256)  DEFAULT NULL,
  client_secret           varchar(256)  DEFAULT NULL,
  scope                   varchar(256)  DEFAULT NULL,
  authorized_grant_types  varchar(256)  DEFAULT NULL,
  web_server_redirect_uri varchar(256)  DEFAULT NULL,
  authorities             varchar(256)  DEFAULT NULL,
  access_token_validity   int(11)       DEFAULT NULL,
  refresh_token_validity  int(11)       DEFAULT NULL,
  additional_information  varchar(4096) DEFAULT NULL,
  autoapprove             varchar(256)  DEFAULT NULL,
  create                  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update                  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (client_id)
) ; -- ENGINE=InnoDB DEFAULT CHARSET=latin1;



DROP TABLE IF EXISTS oauth_code;
CREATE TABLE oauth_code (
  code           varchar(256) DEFAULT NULL,
  authentication blob,
  create         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
) ; -- ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS oauth_refresh_token;
CREATE TABLE oauth_refresh_token (
  token_id       varchar(256) DEFAULT NULL,
  token          blob,
  authentication blob,
  create         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
) ; -- ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS users;
CREATE TABLE users (
  username varchar(50)  NOT NULL,
  password varchar(200) NOT NULL,
  enabled  tinyint(1)   NOT NULL DEFAULT 0,
  version  int          NOT NULL DEFAULT 0,
  create   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (username)
) ; -- ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS t_sso_user_tcc;
CREATE TABLE t_sso_user_tcc (
  id          bigint       NOT NULL,
  username    varchar(50)  NOT NULL,
  password    varchar(200) NOT NULL,
  enabled     tinyint(1)   NOT NULL DEFAULT 0,
  authorities varchar(200) NOT NULL DEFAULT 'ROLE_USER',
  status      tinyint(2)   NOT NULL DEFAULT 0,
  version     int          NOT NULL DEFAULT 0,
  expire      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  create      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ; -- ENGINE=InnoDB DEFAULT CHARSET=latin1;
