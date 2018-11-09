# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table address (
  id                            bigserial not null,
  street                        varchar(255),
  city                          varchar(255),
  zip_code                      varchar(255),
  state                         varchar(255),
  country                       varchar(255),
  user_id                       bigint,
  constraint uq_address_user_id unique (user_id),
  constraint pk_address primary key (id)
);

create table bids (
  id                            bigserial not null,
  amount                        float,
  date                          timestamptz,
  status                        varchar(255),
  user_id                       bigint,
  product_id                    bigint,
  constraint pk_bids primary key (id)
);

create table category (
  id                            bigserial not null,
  name                          varchar(255),
  parent_id                     bigint,
  constraint pk_category primary key (id)
);

create table pictures (
  id                            bigserial not null,
  name                          varchar(255),
  directory                     varchar(255),
  product_id                    bigint,
  constraint pk_pictures primary key (id)
);

create table product_category (
  id                            bigserial not null,
  category_id                   bigint,
  product_id                    bigint,
  constraint pk_product_category primary key (id)
);

create table products (
  id                            bigserial not null,
  name                          varchar(255),
  publish_date                  timestamptz,
  expire_date                   timestamptz,
  main_bid                      float,
  status                        varchar(255),
  color                         varchar(255),
  size                          varchar(255),
  description                   varchar(2048),
  starting_price                float,
  constraint pk_products primary key (id)
);

create table sales (
  id                            bigserial not null,
  address                       varchar(255),
  city                          varchar(255),
  zip_code                      varchar(255),
  phone                         varchar(255),
  country                       varchar(255),
  status                        varchar(255),
  user_id                       bigint,
  product_id                    bigint,
  constraint pk_sales primary key (id)
);

create table users (
  id                            bigserial not null,
  auth_token                    varchar(255),
  name                          varchar(256) not null,
  surname                       varchar(256) not null,
  email                         varchar(256) not null,
  password                      varchar(255),
  email_verified                boolean,
  avatar                        varchar(255),
  gender                        varchar(255),
  date_of_birth                 timestamptz,
  phone_number                  varchar(255),
  phone_verified                boolean,
  constraint uq_users_email unique (email),
  constraint pk_users primary key (id)
);

create table wishlists (
  id                            bigserial not null,
  status                        varchar(255),
  user_id                       bigint,
  product_id                    bigint,
  constraint pk_wishlists primary key (id)
);

alter table address add constraint fk_address_user_id foreign key (user_id) references users (id) on delete restrict on update restrict;

alter table bids add constraint fk_bids_user_id foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_bids_user_id on bids (user_id);

alter table bids add constraint fk_bids_product_id foreign key (product_id) references products (id) on delete restrict on update restrict;
create index ix_bids_product_id on bids (product_id);

alter table pictures add constraint fk_pictures_product_id foreign key (product_id) references products (id) on delete restrict on update restrict;
create index ix_pictures_product_id on pictures (product_id);

alter table product_category add constraint fk_product_category_category_id foreign key (category_id) references category (id) on delete restrict on update restrict;
create index ix_product_category_category_id on product_category (category_id);

alter table product_category add constraint fk_product_category_product_id foreign key (product_id) references products (id) on delete restrict on update restrict;
create index ix_product_category_product_id on product_category (product_id);

alter table sales add constraint fk_sales_user_id foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_sales_user_id on sales (user_id);

alter table sales add constraint fk_sales_product_id foreign key (product_id) references products (id) on delete restrict on update restrict;
create index ix_sales_product_id on sales (product_id);

alter table wishlists add constraint fk_wishlists_user_id foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_wishlists_user_id on wishlists (user_id);

alter table wishlists add constraint fk_wishlists_product_id foreign key (product_id) references products (id) on delete restrict on update restrict;
create index ix_wishlists_product_id on wishlists (product_id);


# --- !Downs

alter table if exists address drop constraint if exists fk_address_user_id;

alter table if exists bids drop constraint if exists fk_bids_user_id;
drop index if exists ix_bids_user_id;

alter table if exists bids drop constraint if exists fk_bids_product_id;
drop index if exists ix_bids_product_id;

alter table if exists pictures drop constraint if exists fk_pictures_product_id;
drop index if exists ix_pictures_product_id;

alter table if exists product_category drop constraint if exists fk_product_category_category_id;
drop index if exists ix_product_category_category_id;

alter table if exists product_category drop constraint if exists fk_product_category_product_id;
drop index if exists ix_product_category_product_id;

alter table if exists sales drop constraint if exists fk_sales_user_id;
drop index if exists ix_sales_user_id;

alter table if exists sales drop constraint if exists fk_sales_product_id;
drop index if exists ix_sales_product_id;

alter table if exists wishlists drop constraint if exists fk_wishlists_user_id;
drop index if exists ix_wishlists_user_id;

alter table if exists wishlists drop constraint if exists fk_wishlists_product_id;
drop index if exists ix_wishlists_product_id;

drop table if exists address cascade;

drop table if exists bids cascade;

drop table if exists category cascade;

drop table if exists pictures cascade;

drop table if exists product_category cascade;

drop table if exists products cascade;

drop table if exists sales cascade;

drop table if exists users cascade;

drop table if exists wishlists cascade;

