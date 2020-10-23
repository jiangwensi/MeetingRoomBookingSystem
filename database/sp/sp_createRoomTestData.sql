DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `createRoomTestData`(in size int)
begin
	begin
		drop temporary table if exists roomName;
		create temporary table roomName (n text);
		insert into roomName (n) values ("Meeting Room"),("Activity Room"),("Function Room"),("Discussion Cornor"),("Interview Room"),("Auditorium"),("Tutorial Room");
		
		drop temporary table if exists locationName;
        create temporary table locationName (n text);
		insert into locationName (n) values ("Level 1"),("Level 2"),("Level 3"),("Level 4"),("Level 5");
	end;

	begin
		declare userIndex1 int;
		declare userIndex2 int;
		declare userIndex3 int;
		declare userIndex4 int;
		declare adminIndex int;
        declare user1 int;
        declare user2 int;
        declare user3 int;
        declare user4 int;
        declare theAdmin int;
        declare roomIndex int;
        declare locIndex int;
        declare orgIndex int;
        declare roomId int;
        declare orgId int;
        declare theDesc text;
        declare theName text;
        declare count int default 0;
        
		select max(id) from room into roomId;
        l:loop
			set roomId :=roomId + 1;
			if count>size then leave l; end if;
			select round(count(*)*rand()) from user into userIndex1;
			select id from user limit userIndex1,1 into user1;
            select round(count(*)*rand()) from user into userIndex2;
			select id from user limit userIndex2,1 into user2;
			select round(count(*)*rand()) from user into userIndex3;
			select id from user limit userIndex3,1 into user3;
			select round(count(*)*rand()) from user into userIndex4;
			select id from user limit userIndex4,1 into user4;
			select round(count(*)*rand()) from user into adminIndex;
			select id from user limit adminIndex,1 into theAdmin;
			select round(count(*)*rand()) from room into roomIndex;
			select round(count(*)*rand()) from locationName into locIndex;
			select round(count(*)*rand()) from org into orgIndex;
            
			
			select n from roomName limit roomIndex,1 into theName;
			select id from org limit orgIndex,1 into orgId;
			select concat(n," Description") from roomName limit roomIndex,1 into theDesc;
			insert into room (id,active,capacity,description, facilities,name,public_id,org_id) 
			values(roomId, 1, round(rand()*10)+2, theDesc,"chairs, tables, whiteboards, projectors",theName,UUID(),orgId);
            insert into room_admin(room_id,user_id)values(roomId,theAdmin);
            insert into room_user(room_id,user_id)values(roomId,user1);
            insert into room_user(room_id,user_id)values(roomId,user2);
            insert into room_user(room_id,user_id)values(roomId,user3);
            insert into room_user(room_id,user_id)values(roomId,user4);
            set count:=count+1;
        end loop l;
	end;
    
	begin
		drop temporary table roomName;
		drop temporary table locationName;
	end;
end$$
DELIMITER ;
