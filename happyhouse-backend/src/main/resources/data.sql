#
# # Authority
# INSERT INTO authority (`created_date`, `modified_date`, `role`) VALUES (NOW(), NOW(), 'ADMIN');
# INSERT INTO authority (`created_date`, `modified_date`, `role`) VALUES (NOW(), NOW(), 'USER');
#
#
# # Member
# INSERT INTO member (id, created_date, modified_date, activate, address, email, nickname, `password`, password_update_date, username) VALUES
#     (1, NOW(), NOW(), TRUE, 'Admin Address', 'admin@ssafy.com', 'admin', 'ssafy', NOW(), 'IamAdmin'),
#     (2, NOW(), NOW(), TRUE, 'User Address', 'user@ssafy.com', 'user', 'ssafy', NOW(), 'IamUser');
#
#
# # Board
# INSERT INTO board (id, created_date, modified_date, `name`) VALUES
#     (1, NOW(), NOW(), '공지사항'),
#     (2, NOW(), NOW(), 'QnA');
#
#
# commit;
#
#
# # Article
# # INSERT INTO article (id, created_date, modified_date, content, secret, title, views, author, board) VALUES
# #     ()
