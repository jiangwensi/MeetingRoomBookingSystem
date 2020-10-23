DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `createUserTestData`(in size int)
begin
#password 123
	declare password text default "$2a$10$ZgW/I03WqkrnsgkbCEsfZO2y5.lkUZuUyYT5NXWm5Xd.Ia7vibeW.";
    declare base text default "abcdefghijklmnopqrstuvwxyz1234567890";
    declare email text default '';
    declare theName text default '';
    declare count int default 1;
    declare userId int;
	select max(id) from `user` into userId;
    l:loop
		if count > size then 
			leave l;
        end if;
        set userId:=userId+1;
        set theName:= concat(
			substring(base,round(rand()*length(base)),1),
			substring(base,round(rand()*length(base)),1),
			substring(base,round(rand()*length(base)),1),
			substring(base,round(rand()*length(base)),1),
			substring(base,round(rand()*length(base)),1),
			substring(base,round(rand()*length(base)),1),
			substring(base,round(rand()*length(base)),1),
			substring(base,round(rand()*length(base)),1)
		);
        set email = concat(theName,'@abctestabc.com');
        insert into user (id,active,email,email_verified,name,password,public_id) values (userId,1,email,1,theName,password,UUID());
        insert into user_role (user_id,role_id) values (userId,1);
        set count := count +1;
    end loop l;
end$$
DELIMITER ;
