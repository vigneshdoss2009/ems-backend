CREATE TABLE employee (
                          id            BIGINT        NOT NULL AUTO_INCREMENT,
                          first_name    VARCHAR(50)   NOT NULL,
                          last_name     VARCHAR(50)   NOT NULL,
                          email         VARCHAR(100)  NOT NULL,
                          mobile_number VARCHAR(16)   NOT NULL,
                          date_of_birth DATE          NOT NULL,
                          joined_at     DATE          NOT NULL,
                          designation   VARCHAR(50)   NOT NULL,
                          salary        DECIMAL(10,2) NOT NULL,
                          created_at    DATETIME(6)   NOT NULL,
                          updated_at    DATETIME(6)   NOT NULL,

                          CONSTRAINT pk_employee PRIMARY KEY (id),
                          CONSTRAINT uk_employee_email UNIQUE (email),
                          CONSTRAINT uk_employee_mobile_number UNIQUE (mobile_number)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;