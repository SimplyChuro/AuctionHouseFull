# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table product (
  id                            bigserial not null,
  name                          varchar(255),
  publish_date                  varchar(255),
  expire_date                   varchar(255),
  main_bid                      integer,
  status                        varchar(255),
  color                         varchar(255),
  size                          varchar(255),
  main_description              varchar(1500),
  additional_description        varchar(1500),
  starting_price                integer,
  constraint pk_product primary key (id)
);

create table product_category (
  id                            bigserial not null,
  category                      varchar(255),
  parent_category               varchar(255),
  product_category_reference_id bigint,
  constraint pk_product_category primary key (id)
);

create table product_picture (
  id                            bigserial not null,
  picture_name                  varchar(255),
  picture_directory             varchar(255),
  type                          varchar(255),
  product_picture_reference_id  bigint,
  constraint pk_product_picture primary key (id)
);

create table user_address_info (
  id                            bigserial not null,
  street                        varchar(255),
  city                          varchar(255),
  zip_code                      varchar(255),
  state                         varchar(255),
  country                       varchar(255),
  web_user_address_reference_id bigint,
  constraint uq_user_address_info_web_user_address_reference_id unique (web_user_address_reference_id),
  constraint pk_user_address_info primary key (id)
);

create table user_bid (
  id                            bigserial not null,
  user_bid_amount               integer,
  user_bid_date                 varchar(255),
  status                        varchar(255),
  web_user_bid_reference_id     bigint,
  product_bid_reference_id      bigint,
  constraint pk_user_bid primary key (id)
);

create table user_extended_personal_info (
  id                            bigserial not null,
  avatar_name                   varchar(255),
  gender                        varchar(255),
  date_of_birth                 varchar(255),
  phone_number                  varchar(255),
  phone_verified                boolean,
  web_user_ext_info_reference_id bigint,
  constraint uq_user_extended_personal_info_web_user_ext_info_referenc_1 unique (web_user_ext_info_reference_id),
  constraint pk_user_extended_personal_info primary key (id)
);

create table user_sale_item (
  id                            bigserial not null,
  address                       varchar(255),
  city                          varchar(255),
  zip_code                      varchar(255),
  phone                         varchar(255),
  country                       varchar(255),
  status                        varchar(255),
  web_user_sale_reference_id    bigint,
  product_sale_reference_id     bigint,
  constraint pk_user_sale_item primary key (id)
);

create table user_wishlist_item (
  id                            bigserial not null,
  status                        varchar(255),
  web_user_wishlist_reference_id bigint,
  product_wishlist_reference_id bigint,
  constraint pk_user_wishlist_item primary key (id)
);

create table web_user (
  id                            bigserial not null,
  name                          varchar(255),
  surname                       varchar(255),
  email                         varchar(255),
  password                      varchar(255),
  email_verified                boolean,
  constraint uq_web_user_email unique (email),
  constraint pk_web_user primary key (id)
);

alter table product_category add constraint fk_product_category_product_category_reference_id foreign key (product_category_reference_id) references product (id) on delete restrict on update restrict;
create index ix_product_category_product_category_reference_id on product_category (product_category_reference_id);

alter table product_picture add constraint fk_product_picture_product_picture_reference_id foreign key (product_picture_reference_id) references product (id) on delete restrict on update restrict;
create index ix_product_picture_product_picture_reference_id on product_picture (product_picture_reference_id);

alter table user_address_info add constraint fk_user_address_info_web_user_address_reference_id foreign key (web_user_address_reference_id) references web_user (id) on delete restrict on update restrict;

alter table user_bid add constraint fk_user_bid_web_user_bid_reference_id foreign key (web_user_bid_reference_id) references web_user (id) on delete restrict on update restrict;
create index ix_user_bid_web_user_bid_reference_id on user_bid (web_user_bid_reference_id);

alter table user_bid add constraint fk_user_bid_product_bid_reference_id foreign key (product_bid_reference_id) references product (id) on delete restrict on update restrict;
create index ix_user_bid_product_bid_reference_id on user_bid (product_bid_reference_id);

alter table user_extended_personal_info add constraint fk_user_extended_personal_info_web_user_ext_info_referenc_1 foreign key (web_user_ext_info_reference_id) references web_user (id) on delete restrict on update restrict;

alter table user_sale_item add constraint fk_user_sale_item_web_user_sale_reference_id foreign key (web_user_sale_reference_id) references web_user (id) on delete restrict on update restrict;
create index ix_user_sale_item_web_user_sale_reference_id on user_sale_item (web_user_sale_reference_id);

alter table user_sale_item add constraint fk_user_sale_item_product_sale_reference_id foreign key (product_sale_reference_id) references product (id) on delete restrict on update restrict;
create index ix_user_sale_item_product_sale_reference_id on user_sale_item (product_sale_reference_id);

alter table user_wishlist_item add constraint fk_user_wishlist_item_web_user_wishlist_reference_id foreign key (web_user_wishlist_reference_id) references web_user (id) on delete restrict on update restrict;
create index ix_user_wishlist_item_web_user_wishlist_reference_id on user_wishlist_item (web_user_wishlist_reference_id);

alter table user_wishlist_item add constraint fk_user_wishlist_item_product_wishlist_reference_id foreign key (product_wishlist_reference_id) references product (id) on delete restrict on update restrict;
create index ix_user_wishlist_item_product_wishlist_reference_id on user_wishlist_item (product_wishlist_reference_id);


# --- !Downs

alter table if exists product_category drop constraint if exists fk_product_category_product_category_reference_id;
drop index if exists ix_product_category_product_category_reference_id;

alter table if exists product_picture drop constraint if exists fk_product_picture_product_picture_reference_id;
drop index if exists ix_product_picture_product_picture_reference_id;

alter table if exists user_address_info drop constraint if exists fk_user_address_info_web_user_address_reference_id;

alter table if exists user_bid drop constraint if exists fk_user_bid_web_user_bid_reference_id;
drop index if exists ix_user_bid_web_user_bid_reference_id;

alter table if exists user_bid drop constraint if exists fk_user_bid_product_bid_reference_id;
drop index if exists ix_user_bid_product_bid_reference_id;

alter table if exists user_extended_personal_info drop constraint if exists fk_user_extended_personal_info_web_user_ext_info_referenc_1;

alter table if exists user_sale_item drop constraint if exists fk_user_sale_item_web_user_sale_reference_id;
drop index if exists ix_user_sale_item_web_user_sale_reference_id;

alter table if exists user_sale_item drop constraint if exists fk_user_sale_item_product_sale_reference_id;
drop index if exists ix_user_sale_item_product_sale_reference_id;

alter table if exists user_wishlist_item drop constraint if exists fk_user_wishlist_item_web_user_wishlist_reference_id;
drop index if exists ix_user_wishlist_item_web_user_wishlist_reference_id;

alter table if exists user_wishlist_item drop constraint if exists fk_user_wishlist_item_product_wishlist_reference_id;
drop index if exists ix_user_wishlist_item_product_wishlist_reference_id;

drop table if exists product cascade;

drop table if exists product_category cascade;

drop table if exists product_picture cascade;

drop table if exists user_address_info cascade;

drop table if exists user_bid cascade;

drop table if exists user_extended_personal_info cascade;

drop table if exists user_sale_item cascade;

drop table if exists user_wishlist_item cascade;

drop table if exists web_user cascade;
