insert into category (id, description) values(1001, 'Comic Books');
insert into category (id, description) values(1002, 'Movies');
insert into category (id, description) values(1003, 'Books');

insert into supplier (id, name) values(1001, 'Panini Comics');
insert into supplier (id, name) values(1002, 'Amazon');

insert into product (id, name, fk_supplier, fk_category, qty_Available) values(1001, 'Crise nas infinitas terras', 1001, 1001, 10);
insert into product (id, name, fk_supplier, fk_category, qty_Available) values(1002, 'Interestelar', 1002, 1002, 5);
insert into product (id, name, fk_supplier, fk_category, qty_Available) values(1003, 'Harry Potter e a pedra filosofal', 1002, 1003, 3);
