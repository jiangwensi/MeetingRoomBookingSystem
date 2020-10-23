DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `createOrgTestData`(in size int)
begin
	begin
    
		drop temporary table if exists orgName;
		create temporary table orgName (n text);
		insert into orgName (n) values ("Ang Mo Kio Public Library"),("Bedok Public Library"),("Bishan Public Library"),("Bukit Batok Public Library"),("Bukit Panjang Public Library"),("Central Public Library"),("Cheng San Public Library"),("Choa Chu Kang Public Library"),("Clementi Public Library"),("Geylang East Public Library"),("Jurong West Public Library"),("Marine Parade Public Library"),("Pasir Ris Public Library"),("Queenstown Public Library"),("Sembawang Public Library"),("Serangoon Public Library"),("Sengkang Public Library"),("Toa Payoh Public Library"),("Yishun Public Library"),("ACE The Place Community Club"),("Aljunied Community Centre"),("Anchorvale Community Club"),("Ang Mo Kio Community Centre"),("Ayer Rajah Community Club"),("Bedok Community Centre"),("Bishan Community Club"),("Boon Lay Community Club"),("Braddell Heights Community Club"),("Bukit Batok Community Club"),("Bukit Batok East Community Club"),("Bukit Merah Community Centre"),("Bukit Panjang Community Club"),("Bukit Timah Community Club"),("Buona Vista Community Club"),("Cairnhill Community Club"),("Canberra Community Club"),("Changi Simei Community Club"),("Cheng San Community Club"),("Chong Pang Community Club");
	end;

	begin
		declare userIndex int;
        declare orgNameIndex int;
        declare theDesc text;
        declare theName text;
        declare orgId int;
        declare userId int;
        declare count int default 0;
        declare exit handler for 1062 begin select theName; end;
		select max(id) from org into orgId;
        l:loop
			if count > size then
				leave l;
            end if;
            
			set orgId:=orgId + 1;
			select round(count(*)*rand()) from user into userIndex;
			select round(count(*)*rand()) from orgName into orgNameIndex;
			select n from orgName limit orgNameIndex,1 into theName;
            select id from user limit userIndex,1 into userId;
			select concat(n," Description") from orgName limit orgNameIndex,1 into theDesc;
			insert into org (id,active,description,name,public_id) values(orgId, 1, theDesc,theName,UUID());
			insert into org_admin(org_id,user_id) values(orgId,userId);
            set count := count + 1;
        end loop l;
        
	end;
    
	begin
		drop temporary table orgName;
	end;
end$$
DELIMITER ;
