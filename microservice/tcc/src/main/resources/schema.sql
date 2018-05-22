

DROP TABLE IF EXISTS t_tcc;
CREATE TABLE t_tcc (
  id           bigint        NOT NULL,
  microservice varchar(200)  NOT NULL DEFAULT '',
  times        tinyint(2)    NOT NULL DEFAULT 0,
  status       tinyint(2)    NOT NULL DEFAULT 0,
  version      int           NOT NULL DEFAULT 0,
  expire       TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  create       TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update       TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ; -- ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS t_tcc_link;
CREATE TABLE t_tcc_link (
  id          bigint        NOT NULL,
  tcc_id      bigint        NOT NULL,
  uri         varchar(200)  NOT NULL,
  resource_id bigint        NOT NULL,
  status      tinyint(2)    NOT NULL DEFAULT 0,
  version     int           NOT NULL DEFAULT 0,
  expire      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  create      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE  KEY uk_tcc_resource_id (resource_id),
  PRIMARY KEY (id)
) ; -- ENGINE=InnoDB DEFAULT CHARSET=latin1;
