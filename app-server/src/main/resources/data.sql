INSERT INTO roles (id,name) VALUES
    ('1','ROLE_USER'),
    ('2','ROLE_ADMIN')
ON DUPLICATE KEY UPDATE name = VALUES(name);