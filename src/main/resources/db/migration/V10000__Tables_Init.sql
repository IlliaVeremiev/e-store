create sequence bills_pk_sequence_generator start 1 increment 1;
create sequence brands_pk_sequence start 1 increment 1;
create sequence categories_pk_sequence start 1 increment 1;
create sequence category_classification_parameters_pk_sequence start 1 increment 1;
create sequence cities_pk_sequence start 1 increment 1;
create sequence customers_pk_sequence start 1 increment 1;
create sequence employees_pk_sequence start 1 increment 1;
create sequence images_pk_sequence start 1 increment 1;
create sequence invoices_pk_sequence start 1 increment 1;
create sequence invoices_templates_pk_sequence start 1 increment 1;
create sequence orders_pk_sequence start 1 increment 1;
create sequence product_variant_items_pk_sequence start 1 increment 1;
create sequence product_variants_pk_sequence start 1 increment 1;
create sequence products_pk_sequence start 1 increment 1;
create sequence group_entries_pk_sequence start 1 increment 1;
create sequence groups_pk_sequence start 1 increment 1;
create sequence stakeholders_pk_sequence start 1 increment 1;
create sequence stores_pk_sequence start 1 increment 1;
create sequence transaction_pk_sequence start 1 increment 1;
create sequence payment_accounts_pk_sequence start 1 increment 1;
create sequence warehouse_products_pk_sequence start 1 increment 1;
create sequence warehouses_pk_sequence start 1 increment 1;
create sequence kassa_sessions_pk_sequence start 1 increment 1;
create sequence receipts_pk_sequence start 1 increment 1;
create sequence payable_unit_id_sequence start 1 increment 1;
create sequence payment_account_operations_pk_sequence start 1 increment 1;

create table bills (
    id int8 not null,
    amount numeric(19,2) not null,
    creation_date timestamp,
    contractor_id int8,
    created_by_id int8,
    invoice_id int8 not null,
    primary key (id)
);

create table brands (
    id int8 not null,
    creation_date timestamp,
    last_update_date timestamp,
    name text not null,
    uid text not null,
    created_by_id int8,
    updated_by_id int8,
    primary key (id)
);

create table categories (
    id int8 not null,
    creation_date timestamp,
    folder boolean not null,
    last_update_date timestamp,
    localized_name jsonb not null,
    uid text not null,
    created_by_id int8,
    icon_id int8,
    image_id int8,
    parent_id int8,
    updated_by_id int8,
    primary key (id)
);

create table category_classification_parameters (
    id int8 not null,
    creation_date timestamp,
    key text not null,
    last_update_date timestamp,
    localized_name jsonb not null,
    measure text,
    type text not null,
    category_id int8 not null,
    created_by_id int8,
    updated_by_id int8,
    primary key (id)
);

create table cities (
    id int8 not null,
    creation_date timestamp,
    last_update_date timestamp,
    localized_name jsonb not null,
    nova_poshta_warehouses jsonb not null,
    np_area text,
    np_area_description_localized jsonb,
    np_cityid text,
    np_ref text not null,
    np_settlement_type_description_localized jsonb,
    created_by_id int8,
    updated_by_id int8,
    primary key (id)
);

create table contractors (
    id int8 not null,
    payable_uid text not null,
    creation_date timestamp,
    last_update_date timestamp,
    name text not null,
    created_by_id int8,
    updated_by_id int8,
    primary key (id)
);

create table customers (
    id int8 not null,
    authorization_token text,
    email text not null,
    enabled boolean not null,
    first_name text not null,
    last_name text not null,
    lock_reason text,
    locked boolean not null,
    password text,
    uid text not null,
    patronymic text,
    phone_number text,
    registration_date timestamp,
    primary key (id)
);

create table employees (
    id int8 not null,
    payable_uid text not null,
    authorization_token text,
    creation_date timestamp,
    date_of_birth date,
    email text not null,
    enabled boolean not null,
    first_name text not null,
    last_name text not null,
    last_update_date timestamp,
    lock_reason text,
    locked boolean not null,
    password text,
    uid text not null,
    patronymic text,
    phone_number text not null,
    registration_date timestamp,
    created_by_id int8,
    updated_by_id int8,
    primary key (id)
);

create table images (
    id int8 not null,
    external_id text,
    path text not null,
    primary key (id)
);

create table invoice_templates (
    id int8 not null,
    creation_date timestamp,
    fields jsonb,
    last_update_date timestamp,
    name text not null,
    uid text not null,
    created_by_id int8,
    updated_by_id int8,
    primary key (id)
);

create table invoices (
    invoice_type text not null,
    id int8 not null,
    creation_date timestamp,
    currency text not null,
    date date not null,
    items jsonb not null,
    last_update_date timestamp,
    name text not null,
    status text not null,
    created_by_id int8,
    updated_by_id int8,
    from_warehouse_id int8,
    to_warehouse_id int8,
    contractor_id int8,
    contractor_payment_account_id int8,
    warehouse_id int8,
    primary key (id)
);

create table orders (
    id int8 not null,
    creation_date timestamp,
    delivery_information jsonb not null,
    delivery_method text not null,
    email text not null,
    first_name text not null,
    items jsonb,
    last_name text not null,
    patronymic text,
    payment_method text not null,
    phone_number text,
    delivery_first_name text,
    delivery_last_name text,
    delivery_patronymic text,
    delivery_phone_number text,
    status text not null,
    status_updated_date timestamp,
    uid text not null,
    customer_id int8 not null,
    delivery_city_id int8 not null,
    status_updated_by_id int8,
    primary key (id)
);

create table product_variant_items (
    id int8 not null,
    creation_date timestamp,
    last_update_date timestamp,
    parameters_values jsonb not null,
    created_by_id int8,
    product_id int8 not null,
    product_variant_id int8 not null,
    updated_by_id int8,
    primary key (id)
);

create table product_variants (
    id int8 not null,
    creation_date timestamp,
    last_update_date timestamp,
    localized_parameters_name jsonb not null,
    created_by_id int8,
    updated_by_id int8,
    primary key (id)
);

create table products (
    id int8 not null,
    active boolean not null,
    classification_parameters jsonb,
    country_of_origin text,
    creation_date timestamp,
    dimension_height int4,
    dimension_length int4,
    dimension_width int4,
    internal_code text,
    last_update_date timestamp,
    localized_description jsonb,
    localized_name jsonb not null,
    manufacturer_code text,
    manufacturer_part_number text,
    model_name text,
    notes text,
    images jsonb,
    price numeric(19,2) not null,
    specification jsonb,
    stock_keeping_unit text,
    universal_product_code text,
    warranty int4,
    weight numeric(19,2),
    brand_id int8 not null,
    category_id int8 not null,
    created_by_id int8,
    updated_by_id int8,
    primary key (id)
);

create table group_authorities (
    group_id int8 not null,
    authority text not null
);

create table groups (
    id int8 not null,
    creation_date timestamp,
    description text,
    last_update_date timestamp,
    name text not null,
    uid text not null,
    type text not null,
    system boolean not null,
    created_by_id int8,
    updated_by_id int8,
    primary key (id)
);

create table store_role_authorities (
    store_role_id int8 not null,
    store_authority text not null
);

create table store_role_entries (
    id int8 not null,
    creation_date timestamp,
    last_update_date timestamp,
    created_by_id int8,
    employee_id int8 not null,
    role_id int8 not null,
    store_id int8 not null,
    updated_by_id int8,
    primary key (id)
);

create table store_roles (
    id int8 not null,
    creation_date timestamp,
    description text,
    last_update_date timestamp,
    name text not null,
    created_by_id int8,
    updated_by_id int8,
    primary key (id)
);

create table store_warehouses (
    store_id int8 not null,
    warehouse_id int8 not null
);

create table stores (
    id int8 not null,
    creation_date timestamp,
    last_update_date timestamp,
    name text not null,
    uid text not null,
    created_by_id int8,
    updated_by_id int8,
    primary key (id)
);

create table transactions (
    id int8 not null,
    commission numeric(19,2) not null,
    creation_date timestamp not null,
    to_income_amount numeric(19,2) not null,
    to_currency text not null,
    date date,
    from_outcome_amount numeric(19,2) not null,
    from_currency text not null,
    description text,
    exchange_rate numeric(19,2) not null,
    last_update_date timestamp,
    status text not null,
    created_by_id int8,
    sender_payment_account_id int8 not null,
    sender_payable_uid text not null,
    image_id int8,
    receiver_payment_account_id int8 not null,
    receiver_payable_uid text not null,
    updated_by_id int8,
    primary key (id)
);

create table employee_groups (
    employee_id int8 not null,
    group_id int8 not null
);

create table payment_accounts (
    id int8 not null,
    balance numeric(19,2) not null,
    creation_date timestamp,
    currency text not null,
    last_update_date timestamp,
    name text not null,
    owner_payable_uid text not null,
    created_by_id int8,
    updated_by_id int8,
    primary key (id)
);

create table payment_account_operations (
    id int8 not null,
    payment_account_id int8 not null,
    description text,
    operation_type text not null,
    purpose_type text not null,
    business_key text not null,
    amount numeric(19,2) not null,
    result_balance numeric(19,2) not null,
    creation_date timestamp,
    created_by_id int8,
    primary key (id)
);

create table warehouse_products (
    id int8 not null,
    count numeric(19,2) not null,
    product_id int8 not null,
    warehouse_id int8 not null,
    primary key (id)
);

create table warehouses (
    id int8 not null,
    address text not null,
    creation_date timestamp,
    last_update_date timestamp,
    name text not null,
    uid text not null,
    created_by_id int8,
    updated_by_id int8,
    primary key (id)
);

create table kassa_sessions(
    id int8 not null,
    employee_id int8 not null,
    store_id int8 not null,
    date date not null,
    state text not null,
    creation_date timestamp,
    last_update_date timestamp,
    created_by_id int8,
    updated_by_id int8,
    open_date_time timestamp not null,
    open_cache_amount numeric(19,2) not null,
    uuid text not null,
    close_date_time timestamp,
    close_cache_amount numeric(19,2),

    primary key (id)
);

create table receipts(
    id int8 not null,
    uuid text not null,
    number int4 not null,
    items jsonb not null,
    store_id int8 not null,
    employee_id int8 not null,
    kassa_session_id int8 not null,
    creation_date timestamp not null,
    total numeric(19,2) not null,
    payment_type text not null,
    primary key (id)
);

alter table if exists brands add constraint uk_brands__uid unique (uid);
alter table if exists categories add constraint uk_categories__uid unique (uid);
alter table if exists cities add constraint uk_cities__np_ref unique (np_ref);
alter table if exists contractors add constraint uk_contractors__name unique (name);
alter table if exists customers add constraint uk_customers__email unique (email);
alter table if exists customers add constraint uk_customers__uid unique (uid);
alter table if exists employees add constraint uk_employees__email unique (email);
alter table if exists employees add constraint uk_employees__uid unique (uid);
alter table if exists invoice_templates add constraint uk_invoice_templates__name unique (name);
alter table if exists invoice_templates add constraint uk_invoice_templates__uid unique (uid);
alter table if exists orders add constraint uk_orders__uid unique (uid);
alter table if exists groups add constraint uk_groups__name unique (name);
alter table if exists groups add constraint uk_groups__uid unique (uid);
alter table if exists store_roles add constraint uk_store_roles__name unique (name);
alter table if exists stores add constraint uk_stores__name unique (name);
alter table if exists stores add constraint uk_stores__uid unique (uid);
alter table if exists warehouses add constraint uk_warehouses__name unique (name);
alter table if exists warehouses add constraint uk_warehouses__uid unique (uid);
alter table if exists category_classification_parameters add constraint uk_category_classification_parameters__key__category_id unique (key, category_id);
alter table if exists warehouse_products add constraint uk_warehouse_products__product_id__warehouse_id unique (product_id, warehouse_id);
alter table if exists kassa_sessions add constraint uk_kassa_sessions__uuid unique (uuid);
alter table if exists receipts add constraint uk_receipts__uuid unique (uuid);
alter table if exists receipts add constraint uk_receipts__kassa_session_id__number unique (kassa_session_id, number);
alter table if exists employees add constraint uk_employees__payable_uid unique (payable_uid);
alter table if exists contractors add constraint uk_contractors__payable_uid unique (payable_uid);



alter table if exists bills add constraint fk_bills__contractor_id foreign key (contractor_id) references contractors;
alter table if exists bills add constraint fk_bills__created_by_id foreign key (created_by_id) references employees;
alter table if exists bills add constraint fk_bills__invoice_id foreign key (invoice_id) references invoices;
alter table if exists brands add constraint fk_brands__created_by_id foreign key (created_by_id) references employees;
alter table if exists brands add constraint fk_brands__updated_by_id foreign key (updated_by_id) references employees;
alter table if exists categories add constraint fk_categories__created_by_id foreign key (created_by_id) references employees;
alter table if exists categories add constraint fk_categories__icon_id foreign key (icon_id) references images;
alter table if exists categories add constraint fk_categories__image_id foreign key (image_id) references images;
alter table if exists categories add constraint fk_categories__parent_id foreign key (parent_id) references categories;
alter table if exists categories add constraint fk_categories__updated_by_id foreign key (updated_by_id) references employees;
alter table if exists category_classification_parameters add constraint fk_category_classification_parameters__category_id foreign key (category_id) references categories;
alter table if exists category_classification_parameters add constraint fk_category_classification_parameters__created_by_id foreign key (created_by_id) references employees;
alter table if exists category_classification_parameters add constraint fk_category_classification_parameters__updated_by_id foreign key (updated_by_id) references employees;
alter table if exists cities add constraint fk_cities__created_by_id foreign key (created_by_id) references employees;
alter table if exists cities add constraint fk_cities__updated_by_id foreign key (updated_by_id) references employees;
alter table if exists contractors add constraint fk_contractors__created_by_id foreign key (created_by_id) references employees;
alter table if exists contractors add constraint fk_contractors__updated_by_id foreign key (updated_by_id) references employees;
alter table if exists employees add constraint fk_employees__created_by_id foreign key (created_by_id) references employees;
alter table if exists employees add constraint fk_employees__updated_by_id foreign key (updated_by_id) references employees;
alter table if exists invoice_templates add constraint fk_invoice_templates__created_by_id foreign key (created_by_id) references employees;
alter table if exists invoice_templates add constraint fk_invoice_templates__updated_by_id foreign key (updated_by_id) references employees;
alter table if exists invoices add constraint fk_invoices__created_by_id foreign key (created_by_id) references employees;
alter table if exists invoices add constraint fk_invoices__updated_by_id foreign key (updated_by_id) references employees;
alter table if exists invoices add constraint fk_invoices__from_warehouse_id foreign key (from_warehouse_id) references warehouses;
alter table if exists invoices add constraint fk_invoices__to_warehouse_id foreign key (to_warehouse_id) references warehouses;
alter table if exists invoices add constraint fk_invoices__contractor_id foreign key (contractor_id) references contractors;
alter table if exists invoices add constraint fk_invoices__warehouse_id foreign key (warehouse_id) references warehouses;
alter table if exists orders add constraint fk_orders__customer_id foreign key (customer_id) references customers;
alter table if exists orders add constraint fk_orders__delivery_city_id foreign key (delivery_city_id) references cities;
alter table if exists orders add constraint fk_orders__status_updated_by_id foreign key (status_updated_by_id) references employees;
alter table if exists product_variant_items add constraint fk_product_variant_items__created_by_id foreign key (created_by_id) references employees;
alter table if exists product_variant_items add constraint fk_product_variant_items__product_id foreign key (product_id) references products;
alter table if exists product_variant_items add constraint fk_product_variant_items__product_variant_id foreign key (product_variant_id) references product_variants;
alter table if exists product_variant_items add constraint fk_product_variant_items__updated_by_id foreign key (updated_by_id) references employees;
alter table if exists product_variants add constraint fk_product_variants__created_by_id foreign key (created_by_id) references employees;
alter table if exists product_variants add constraint fk_product_variants__updated_by_id foreign key (updated_by_id) references employees;
alter table if exists products add constraint fk_products__brand_id foreign key (brand_id) references brands;
alter table if exists products add constraint fk_products__category_id foreign key (category_id) references categories;
alter table if exists products add constraint fk_products__created_by_id foreign key (created_by_id) references employees;
alter table if exists products add constraint fk_products__updated_by_id foreign key (updated_by_id) references employees;
alter table if exists group_authorities add constraint fk_group_authorities__group_id foreign key (group_id) references groups;
alter table if exists groups add constraint fk_groups__created_by_id foreign key (created_by_id) references employees;
alter table if exists groups add constraint fk_groups__updated_by_id foreign key (updated_by_id) references employees;
alter table if exists store_role_authorities add constraint fk_store_role_authorities__store_role_id foreign key (store_role_id) references store_roles;
alter table if exists store_role_entries add constraint fk_store_role_entries__created_by_id foreign key (created_by_id) references employees;
alter table if exists store_role_entries add constraint fk_store_role_entries__employee_id foreign key (employee_id) references employees;
alter table if exists store_role_entries add constraint fk_store_role_entries__role_id foreign key (role_id) references store_roles;
alter table if exists store_role_entries add constraint fk_store_role_entries__store_id foreign key (store_id) references stores;
alter table if exists store_role_entries add constraint fk_store_role_entries__updated_by_id foreign key (updated_by_id) references employees;
alter table if exists store_roles add constraint fk_store_roles__created_by_id foreign key (created_by_id) references employees;
alter table if exists store_roles add constraint fk_store_roles__updated_by_id foreign key (updated_by_id) references employees;
alter table if exists store_warehouses add constraint fk_store_warehouses__warehouse_id foreign key (warehouse_id) references warehouses;
alter table if exists store_warehouses add constraint fk_store_warehouses__store_id foreign key (store_id) references stores;
alter table if exists stores add constraint fk_stores__created_by_id foreign key (created_by_id) references employees;
alter table if exists stores add constraint fk_stores__updated_by_id foreign key (updated_by_id) references employees;
alter table if exists transactions add constraint fk_transactions__created_by_id foreign key (created_by_id) references employees;
alter table if exists transactions add constraint fk_transactions__sender_payment_account_id foreign key (sender_payment_account_id) references payment_accounts;
alter table if exists transactions add constraint fk_transactions__image_id foreign key (image_id) references images;
alter table if exists transactions add constraint fk_transactions__receiver_payment_account_id foreign key (receiver_payment_account_id) references payment_accounts;
alter table if exists transactions add constraint fk_transactions__updated_by_id foreign key (updated_by_id) references employees;
alter table if exists employee_groups add constraint fk_employee_groups__group_id foreign key (group_id) references groups;
alter table if exists employee_groups add constraint fk_employee_groups__user_id foreign key (employee_id) references employees;
alter table if exists payment_accounts add constraint fk_payment_accounts__created_by_id foreign key (created_by_id) references employees;
alter table if exists payment_accounts add constraint fk_payment_accounts__updated_by_id foreign key (updated_by_id) references employees;
alter table if exists payment_account_operations add constraint fk_payment_account_operations__payment_account_id foreign key (payment_account_id) references payment_accounts;
alter table if exists payment_account_operations add constraint fk_payment_account_operations__created_by_id foreign key (created_by_id) references employees;
alter table if exists warehouse_products add constraint fk_warehouse_products__product_id foreign key (product_id) references products;
alter table if exists warehouse_products add constraint fk_warehouse_products__warehouse_id foreign key (warehouse_id) references warehouses;
alter table if exists warehouses add constraint fk_warehouses__created_by_id foreign key (created_by_id) references employees;
alter table if exists warehouses add constraint fk_warehouses__updated_by_id foreign key (updated_by_id) references employees;
alter table if exists kassa_sessions add constraint fk_kassa_sessions__employee_id foreign key (employee_id) references employees;
alter table if exists kassa_sessions add constraint fk_kassa_sessions__store_id foreign key (store_id) references stores;
alter table if exists receipts add constraint fk_kassa_sessions__employee_id foreign key (employee_id) references employees;
alter table if exists receipts add constraint fk_kassa_sessions__store_id foreign key (store_id) references stores;
alter table if exists receipts add constraint fk_kassa_sessions__kassa_session_id foreign key (kassa_session_id) references stores;

