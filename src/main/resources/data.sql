insert into category (name) values ('🛬 언어')
insert into category (name) values ('부트캠프')

insert into menu (name, category_id) values ('자바', 1)
insert into menu (name, category_id) values ('파이썬', 1)
insert into menu (name, category_id) values ('javascript', 1)
insert into menu (name, category_id) values ('우아한테크코스', 2)

insert into article (title, sub_title, content, author, menu_id, created_at, updated_at) values ('제목 1 긴 타이틀 테스트 가나다라마바사아자차카타파하', 'subTitle 예시입니다.', '내용 1', 'dustjq1005', 1, NOW(), NOW())
insert into article (title, sub_title, content, author, menu_id, created_at, updated_at) values ('제목 2', 'subTitle 예시입니다.', '내용 2', 'dustjq1005', 1, NOW(), NOW())
insert into article (title, sub_title, content, author, menu_id, created_at, updated_at) values ('제목 3', 'subTitle 예시입니다.', '내용 3', 'dustjq1005', 1, NOW(), NOW())
insert into article (title, sub_title, content, author, menu_id, created_at, updated_at) values ('제목 4', 'subTitle 예시입니다.', '내용 4', 'dustjq1005', 1, NOW(), NOW())
insert into article (title, sub_title, content, author, menu_id, created_at, updated_at) values ('제목 5', 'subTitle 예시입니다.', '내용 5', 'dustjq1005', 1, NOW(), NOW())
insert into article (title, sub_title, content, author, menu_id, created_at, updated_at) values ('제목 6', 'subTitle 예시입니다.', '내용 6', 'dustjq1005', 1, NOW(), NOW())
insert into article (title, sub_title, content, author, menu_id, created_at, updated_at) values ('제목 7', 'subTitle 예시입니다.', '내용 2', 'dustjq1005', 1, NOW(), NOW())
insert into article (title, sub_title, content, author, menu_id, created_at, updated_at) values ('제목 8', 'subTitle 예시입니다.', '내용 3', 'dustjq1005', 1, NOW(), NOW())
insert into article (title, sub_title, content, author, menu_id, created_at, updated_at) values ('제목 9', 'subTitle 예시입니다.', '내용 4', 'dustjq1005', 1, NOW(), NOW())
insert into article (title, sub_title, content, author, menu_id, created_at, updated_at) values ('제목 10', 'subTitle 예시입니다.', '내용 5', 'dustjq1005', 1, NOW(), NOW())
insert into article (title, sub_title, content, author, menu_id, created_at, updated_at) values ('제목11', 'subTitle 예시입니다.', '```
내용 6내용 6내용 6내용 6내용 6내용 6내용 6내용 6내용 6내용 6내용 6내용 6내용 6내용 6내용 6내용 6내용 6내용 6내용 6내용 6내용 6
```', 'user3', 1, NOW(), NOW())

insert into draft_article (title, sub_title, content, author, delete_yn, menu_id, created_at, updated_at) values ('제목 10', 'subTitle 예시입니다.', '내용 5', 'dustjq1005', false, 1, NOW(), NOW())
