declare
  n number;
begin
  select 1 into n from dual;
end;
/
declare
  n number;
begin
  select count(*) into n from &ORADCTM..dm_user_s;
end;
/

