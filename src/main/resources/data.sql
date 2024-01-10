insert into article (title, content, author, menu_id, created_at, updated_at) values ('제목 1', '내용 1', 'user1', 1, NOW(), NOW())
insert into article (title, content, author, menu_id, created_at, updated_at) values ('제목 2', '내용 2', 'user2', 1, NOW(), NOW())
insert into article (title, content, author, menu_id, created_at, updated_at) values ('제목 3', '내용 3', 'user3', 1, NOW(), NOW())

insert into category (name) values ('언어')
insert into category (name) values ('부트캠프')

insert into menu (name, category_id) values ('자바', 1)
insert into menu (name, category_id) values ('파이썬', 1)
insert into menu (name, category_id) values ('javascript', 1)
insert into menu (name, category_id) values ('우아한테크코스', 2)