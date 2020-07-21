CREATE SCHEMA likelib;
CREATE TABLE likelib.account_types (
    type character varying(255) NOT NULL
);
CREATE TABLE likelib.accounts (
    address character varying(255) NOT NULL,
    balance bigint NOT NULL,
    nonce double precision NOT NULL,
    type character varying(255) NOT NULL
);
CREATE TABLE likelib.blocks (
    height bigint NOT NULL,
    hash character varying(255) NOT NULL,
    "timestamp" timestamp without time zone NOT NULL,
    nonce double precision NOT NULL,
    prev_block_hash character varying(255) NOT NULL,
    coinbase character varying(255) NOT NULL
);
CREATE TABLE likelib.transaction_statuses (
    status character varying(255) NOT NULL
);
CREATE TABLE likelib.transaction_types (
    type character varying(255) NOT NULL
);
CREATE TABLE likelib.transactions (
    hash character varying(255) NOT NULL,
    "timestamp" timestamp without time zone NOT NULL,
    account_to character varying(255) NOT NULL,
    account_from character varying(255) NOT NULL,
    data character varying,
    amount bigint NOT NULL,
    sign character varying(500) NOT NULL,
    fee bigint NOT NULL,
    block_height bigint NOT NULL,
    message character varying(500),
    status character varying(255) NOT NULL,
    type character varying(255) NOT NULL,
    fee_left bigint NOT NULL
);
ALTER TABLE ONLY likelib.account_types
    ADD CONSTRAINT account_types_pkey PRIMARY KEY (type);
ALTER TABLE ONLY likelib.accounts
    ADD CONSTRAINT accounts_pkey PRIMARY KEY (address);
ALTER TABLE ONLY likelib.blocks
    ADD CONSTRAINT blocks_hash_key UNIQUE (hash);
ALTER TABLE ONLY likelib.blocks
    ADD CONSTRAINT blocks_nonce_key UNIQUE (nonce);
ALTER TABLE ONLY likelib.blocks
    ADD CONSTRAINT blocks_pkey PRIMARY KEY (height);
ALTER TABLE ONLY likelib.blocks
    ADD CONSTRAINT blocks_prev_block_hash_key UNIQUE (prev_block_hash);
ALTER TABLE ONLY likelib.blocks
    ADD CONSTRAINT blocks_timestamp_key UNIQUE ("timestamp");
ALTER TABLE ONLY likelib.transaction_statuses
    ADD CONSTRAINT transaction_statuses_pkey PRIMARY KEY (status);
ALTER TABLE ONLY likelib.transaction_types
    ADD CONSTRAINT transaction_types_pkey PRIMARY KEY (type);
ALTER TABLE ONLY likelib.transactions
    ADD CONSTRAINT transactions_pkey PRIMARY KEY (hash);
CREATE INDEX accounts_address_idx ON likelib.accounts USING btree (address);
CREATE INDEX accounts_balance_idx ON likelib.accounts USING btree (balance);
CREATE INDEX accounts_nonce_idx ON likelib.accounts USING btree (nonce);
CREATE INDEX accounts_type_idx ON likelib.accounts USING btree (type);
CREATE INDEX blocks_coinbase_idx ON likelib.blocks USING btree (coinbase);
CREATE INDEX blocks_hash_idx ON likelib.blocks USING btree (hash);
CREATE INDEX blocks_nonce_idx ON likelib.blocks USING btree (nonce);
CREATE INDEX blocks_number_idx ON likelib.blocks USING btree (height);
CREATE INDEX blocks_prev_block_hash_idx ON likelib.blocks USING btree (prev_block_hash);
CREATE INDEX blocks_timestamp_idx ON likelib.blocks USING btree ("timestamp");
CREATE INDEX transactions_address_from_idx ON likelib.transactions USING btree (account_from);
CREATE INDEX transactions_address_to_idx ON likelib.transactions USING btree (account_to);
CREATE INDEX transactions_amount_idx ON likelib.transactions USING btree (amount);
CREATE INDEX transactions_block_height_idx ON likelib.transactions USING btree (block_height);
CREATE INDEX transactions_fee_idx ON likelib.transactions USING btree (fee);
CREATE INDEX transactions_hash_idx ON likelib.transactions USING btree (hash);
CREATE INDEX transactions_status_idx ON likelib.transactions USING btree (status);
CREATE INDEX transactions_timestamp_idx ON likelib.transactions USING btree ("timestamp");
CREATE INDEX transactions_type_idx ON likelib.transactions USING btree (type);
ALTER TABLE ONLY likelib.accounts
    ADD CONSTRAINT accounts_type_fkey FOREIGN KEY (type) REFERENCES likelib.account_types(type) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE ONLY likelib.blocks
    ADD CONSTRAINT blocks_coinbase_fkey FOREIGN KEY (coinbase) REFERENCES likelib.accounts(address) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE ONLY likelib.transactions
    ADD CONSTRAINT transactions_address_from_fkey FOREIGN KEY (account_from) REFERENCES likelib.accounts(address);
ALTER TABLE ONLY likelib.transactions
    ADD CONSTRAINT transactions_address_to_fkey FOREIGN KEY (account_to) REFERENCES likelib.accounts(address);
ALTER TABLE ONLY likelib.transactions
    ADD CONSTRAINT transactions_block_height_fkey FOREIGN KEY (block_height) REFERENCES likelib.blocks(height) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE ONLY likelib.transactions
    ADD CONSTRAINT transactions_status_fkey FOREIGN KEY (status) REFERENCES likelib.transaction_statuses(status) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE ONLY likelib.transactions
    ADD CONSTRAINT transactions_type_fkey FOREIGN KEY (type) REFERENCES likelib.transaction_types(type) ON UPDATE RESTRICT ON DELETE RESTRICT;

INSERT INTO likelib.transaction_statuses(status) VALUES ('Success');
INSERT INTO likelib.transaction_statuses(status) VALUES ('Pending');
INSERT INTO likelib.transaction_statuses(status) VALUES ('BadQueryForm');
INSERT INTO likelib.transaction_statuses(status) VALUES ('BadSign');
INSERT INTO likelib.transaction_statuses(status) VALUES ('NotEnoughBalance');
INSERT INTO likelib.transaction_statuses(status) VALUES ('Revert');
INSERT INTO likelib.transaction_statuses(status) VALUES ('Failed');
INSERT INTO likelib.transaction_types(type) VALUES ('None');
INSERT INTO likelib.transaction_types(type) VALUES ('Transfer');
INSERT INTO likelib.transaction_types(type) VALUES ('ContractCall');
INSERT INTO likelib.transaction_types(type) VALUES ('ContractCreation');
INSERT INTO likelib.account_types(type) VALUES ('Client');
INSERT INTO likelib.account_types(type) VALUES ('Contract');