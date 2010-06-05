-- drop references tables
drop table BOOKAUTHOR if exists;
drop table AUTHORITY_USER if exists;
drop table USERAUTHORITY if exists;

-- delete all acl/security tables
drop table authority if exists;
drop table acl_entry if exists;
drop table acl_object_identity if exists;
drop table acl_class if exists;
drop table acl_sid if exists;

-- delete all activity
drop table AccountActivity if exists;
drop table BookActivity if exists;
drop table ChapterActivity if exists;
drop table CommentActivity if exists;
drop table Activity if exists;
drop table AbtractChapterActivity if exists;
drop table AbstractBookActivity if exists;

-- delete all datas
drop table BOOKAUTHOR if exists;
drop table AUTHORITY_USER if exists;
drop table USERAUTHORITY if exists;
drop table comment if exists;
drop table publication if exists;
drop table chapter if exists;
drop table book if exists;
drop table user if exists;
