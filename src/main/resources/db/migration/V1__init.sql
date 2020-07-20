create table transactions_statuses (
    key                         int             not null,
    value                       varchar(255)    not null,
    primary key (key)
);

insert into transactions_statuses(key, value) values (0, 'Success');
insert into transactions_statuses(key, value) values (1, 'Pending');
insert into transactions_statuses(key, value) values (2, 'BadQueryForm');
insert into transactions_statuses(key, value) values (3, 'BadSign');
insert into transactions_statuses(key, value) values (4, 'NotEnoughBalance');
insert into transactions_statuses(key, value) values (5, 'Revert');
insert into transactions_statuses(key, value) values (6, 'Failed');

create table transactions_types (
    key                         int             not null,
    value                       varchar(255)    not null,
    primary key (key)
);

insert into transactions_types(key, value) values (0, 'None');
insert into transactions_types(key, value) values (1, 'Transfer');
insert into transactions_types(key, value) values (2, 'ContractCall');
insert into transactions_types(key, value) values (3, 'ContractCreation');

create table blocks (
    number                      bigint          not null,
    hash                        varchar(255)    not null,
    hash_in_base64              varchar(255)    not null,
    timestamp                   timestamp,
    nonce                       int,
    prev_block_hash             varchar(255),
    prev_block_hash_in_base64   varchar(255),
    block_data_in_json          varchar(5000)   not null,
    primary key (number)
);

create table accounts (
    address                     varchar(255)    not null,
    address_in_base58           varchar(255)    not null,
    balance                     bigint,
    nonce                       int,
    type                        varchar(255),
    primary key (address)
);

create table transactions (
    hash                        varchar(255)    not null,
    hash_in_base64              varchar(255)    not null,
    timestamp                   timestamp,
    address_to                  varchar(255),
    address_to_in_base58        varchar(255),
    address_from                varchar(255),
    address_from_in_base58      varchar(255),
    data                        bytea,
    amount                      bigint,
    status                      int,
    sign                        varchar(500),
    sign_in_base64              varchar(500),
    fee                         bigint,
    block_height                bigint,
    type                        int,
    message                     varchar(500),
    message_in_base64           varchar(500),
    primary key (hash),
    foreign key (address_to) references accounts(address),
    foreign key (address_from) references accounts(address),
    foreign key (status) references transactions_statuses(key),
    foreign key (block_height) references blocks(number),
    foreign key (type) references transactions_types(key)
);

create index on blocks (number);
create index on blocks (hash);
create index on blocks (hash_in_base64);
create index on blocks (timestamp);
create index on blocks (nonce);
create index on blocks (prev_block_hash);
create index on blocks (prev_block_hash_in_base64);

create index on transactions (hash);
create index on transactions (hash_in_base64);
create index on transactions (timestamp);
create index on transactions (address_to);
create index on transactions (address_to_in_base58);
create index on transactions (address_from);
create index on transactions (address_from_in_base58);
create index on transactions (amount);
create index on transactions (status);
create index on transactions (fee);
create index on transactions (block_height);
create index on transactions (message);
create index on transactions (message_in_base64);

create index on accounts (address);
create index on accounts (address_in_base58);
create index on accounts (balance);
create index on accounts (nonce);
create index on accounts (type);

