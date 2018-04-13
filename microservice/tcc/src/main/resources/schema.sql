

DROP TABLE IF EXISTS t_tcc;
CREATE TABLE t_tcc (
  id           UUID          NOT NULL,
  microservice varchar(200)  NOT NULL DEFAULT '',
--times        tinyint(2)    NOT NULL DEFAULT 0,
--status       tinyint(2)    NOT NULL DEFAULT 0,
  times        smallint      NOT NULL DEFAULT 0,
  status       smallint      NOT NULL DEFAULT 0,
  version      int           NOT NULL DEFAULT 0,
  expire       TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "create"     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update       TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ; -- ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS t_tcc_link;
CREATE TABLE t_tcc_link (
  id          UUID          NOT NULL,
  tcc_id      UUID          NOT NULL,
  uri         varchar(200)  NOT NULL,
  path        varchar(500)  NOT NULL,
--status      tinyint(2)    NOT NULL DEFAULT 0,
  status      smallint      NOT NULL DEFAULT 0,
  version     int           NOT NULL DEFAULT 0,
  expire      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "create"    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ; -- ENGINE=InnoDB DEFAULT CHARSET=latin1;
