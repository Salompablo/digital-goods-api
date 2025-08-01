-- Roles
INSERT INTO roles (role) VALUES ('ROLE_ADMIN');
INSERT INTO roles (role) VALUES ('ROLE_CLIENT');

-- Permits
INSERT INTO permits (permit) VALUES ('VIEW_PRODUCTS');
INSERT INTO permits (permit) VALUES ('CREATE_PRODUCT');
INSERT INTO permits (permit) VALUES ('DELETE_PRODUCT');
INSERT INTO permits (permit) VALUES ( 'PURCHASE_PRODUCT');
INSERT INTO permits (permit) VALUES ( 'VIEW_USERS');
INSERT INTO permits (permit) VALUES ( 'MANAGE_USERS');
INSERT INTO permits (permit) VALUES ( 'VIEW_SELF');
INSERT INTO permits (permit) VALUES ( 'UPDATE_SELF');
INSERT INTO permits (permit) VALUES ( 'DEACTIVATE_SELF');
INSERT INTO permits (permit) VALUES ( 'REACTIVATE_SELF');
INSERT INTO permits (permit) VALUES ( 'CHANGE_PASSWORD');
INSERT INTO permits (permit) VALUES ('CHECKOUT_CART');
INSERT INTO permits (permit) VALUES ('VIEW_ORDERS');
INSERT INTO permits (permit) VALUES ('CANCEL_CART');
-- Role-Permits

-- ADMIN
INSERT INTO role_permits (role_id, permit_id) VALUES (1, 1);
INSERT INTO role_permits (role_id, permit_id) VALUES (1, 2);
INSERT INTO role_permits (role_id, permit_id) VALUES (1, 3);
INSERT INTO role_permits (role_id, permit_id) VALUES (1, 4);
INSERT INTO role_permits (role_id, permit_id) VALUES (1, 5);
INSERT INTO role_permits (role_id, permit_id) VALUES (1, 6);
INSERT INTO role_permits (role_id, permit_id) VALUES (1, 7);
INSERT INTO role_permits (role_id, permit_id) VALUES (1, 8);
INSERT INTO role_permits (role_id, permit_id) VALUES (1, 9);
INSERT INTO role_permits (role_id, permit_id) VALUES (1, 10);
INSERT INTO role_permits (role_id, permit_id) VALUES (1, 11);

-- CLIENT
INSERT INTO role_permits (role_id, permit_id) VALUES (2, 1);  -- VIEW_PRODUCTS
INSERT INTO role_permits (role_id, permit_id) VALUES (2, 4);  -- PURCHASE_PRODUCT
INSERT INTO role_permits (role_id, permit_id) VALUES (2, 7);  -- VIEW_SELF
INSERT INTO role_permits (role_id, permit_id) VALUES (2, 8);  -- UPDATE_SELF
INSERT INTO role_permits (role_id, permit_id) VALUES (2, 9);  -- DEACTIVATE_SELF
INSERT INTO role_permits (role_id, permit_id) VALUES (2, 10); -- REACTIVATE_SELF
INSERT INTO role_permits (role_id, permit_id) VALUES (2, 11); -- CHANGE_PASSWORD
INSERT INTO role_permits (role_id, permit_id) VALUES (2, 12); -- CHECKOUT_CART
INSERT INTO role_permits (role_id, permit_id) VALUES (2, 13); -- VIEW_ORDERS
INSERT INTO role_permits (role_id, permit_id) VALUES (2, 14); -- CANCEL_CART

