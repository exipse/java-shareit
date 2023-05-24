DROP TABLE IF EXISTS COMMENTS;
DROP TABLE IF EXISTS BOOKINGS;
DROP TABLE IF EXISTS ITEMS;
DROP TABLE IF EXISTS REQUESTS;
DROP TABLE IF EXISTS USERS;


CREATE TABLE IF NOT EXISTS USERS (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
    );


CREATE TABLE IF NOT EXISTS REQUESTS(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    description VARCHAR(255) NOT NULL,
    requestor_id BIGINT REFERENCES USERS (id)  ,
    CONSTRAINT pk_requests PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS ITEMS (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(512) NOT NULL,
    is_available bool NOT NULL,
    owner_id BIGINT REFERENCES USERS (id) ON DELETE CASCADE,
    request_id BIGINT,
    CONSTRAINT pk_item PRIMARY KEY (id)
    );


CREATE TABLE IF NOT EXISTS BOOKINGS(
   id BIGINT GENERATED BY DEFAULT AS IDENTITY,
   start_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   end_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   item_id  BIGINT NOT NULL REFERENCES ITEMS (id) ON DELETE CASCADE ,
   booker_id BIGINT NOT NULL REFERENCES USERS (id) ON DELETE CASCADE,
   status VARCHAR(20) NOT NULL,
   CONSTRAINT pk_booking PRIMARY KEY (id)
);

    CREATE TABLE IF NOT EXISTS COMMENTS (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    text VARCHAR(255) NOT NULL,
    item_id BIGINT NOT NULL REFERENCES ITEMS(id) ON DELETE CASCADE,
    created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    author_id BIGINT NOT NULL REFERENCES USERS(id) ,
    CONSTRAINT pk_comments PRIMARY KEY (id)
    );


