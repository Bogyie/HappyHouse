
# Authority
INSERT INTO authority (id, `created_date`, `modified_date`, `role`) VALUES (1, NOW(), NOW(), 'ADMIN');
INSERT INTO authority (id, `created_date`, `modified_date`, `role`) VALUES (2, NOW(), NOW(), 'USER');

# Member
INSERT INTO member (id, created_date, modified_date, activate, address, email, nickname, `password`, password_update_date, username) VALUES (1, NOW(), NOW(), TRUE, 'Admin Address', 'admin@ssafy.com', 'admin', 'ssafy', NOW(), 'IamAdmin');
INSERT INTO member (id, created_date, modified_date, activate, address, email, nickname, `password`, password_update_date, username) VALUES (2, NOW(), NOW(), TRUE, 'User Address', 'user@ssafy.com', 'user', 'ssafy', NOW(), 'IamUser');

# Member - Authority
INSERT INTO member_authority (member_id, authority_id) VALUES (1, 1);
INSERT INTO member_authority (member_id, authority_id) VALUES (1, 2);
INSERT INTO member_authority (member_id, authority_id) VALUES (2, 2);

# Board
INSERT INTO board (id, created_date, modified_date, `name`) VALUES (1, NOW(), NOW(), '공지사항');
INSERT INTO board (id, created_date, modified_date, `name`) VALUES (2, NOW(), NOW(), 'QnA');

# Article
INSERT INTO article (id, created_date, modified_date, content, secret, title, views, author, board) VALUES (1, NOW(), NOW(), '{"type":"doc","content":[{"type":"paragraph","content":[{"type":"text","text":"공지사항 컨텐츠"}]}]}', false, '공지사항 - admin', 0, 1, 1);
INSERT INTO article (id, created_date, modified_date, content, secret, title, views, author, board) VALUES (2, NOW(), NOW(), '{"type":"doc","content":[{"type":"paragraph","content":[{"type":"text","text":"공지사항 컨텐츠"}]}]}', false, '공지사항 - user', 0, 2, 1);
INSERT INTO article (id, created_date, modified_date, content, secret, title, views, author, board) VALUES (3, NOW(), NOW(), '{"type":"doc","content":[{"type":"paragraph","content":[{"type":"text","text":"QnA 컨텐츠"}]}]}', false, 'QnA - admin', 0, 1, 2);
INSERT INTO article (id, created_date, modified_date, content, secret, title, views, author, board) VALUES (4, NOW(), NOW(), '{"type":"doc","content":[{"type":"paragraph","content":[{"type":"text","text":"QnA 컨텐츠"}]}]}', false, 'QnA - user', 0, 2, 2);

# Comment
INSERT INTO comment (id, created_date, modified_date, content, secret, article, author) VALUES (1, NOW(), NOW(), '공지사항 - admin : comment from admin', false, 1, 1);
INSERT INTO comment (id, created_date, modified_date, content, secret, article, author) VALUES (2, NOW(), NOW(), '공지사항 - admin : comment from user', false, 1, 2);
INSERT INTO comment (id, created_date, modified_date, content, secret, article, author) VALUES (3, NOW(), NOW(), 'QnA - user : comment from admin', false, 4, 1);
INSERT INTO comment (id, created_date, modified_date, content, secret, article, author) VALUES (4, NOW(), NOW(), 'QnA - user : comment from user', false, 4, 2);
