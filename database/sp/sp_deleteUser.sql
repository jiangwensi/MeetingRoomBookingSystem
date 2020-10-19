drop procedure if exists deleteUser;
delimiter $$
create procedure deleteUser(in theEmail text)
begin
	declare userId int default -1;
	select id from user where email = theEmail into userId;
    if userId is null then
		select concat( "email ",email," doesn't exists");
	else 
		select concat("selected user id is ",userId);
    end if;
    delete from user_role where user_id = userId;
    select concat("deleted ",ROW_COUNT()," rows");
    delete from token where user_id = userId;
    select concat("deleted ",ROW_COUNT()," rows");
end$$
delimiter ;
