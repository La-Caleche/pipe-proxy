/* AlterBanTableAddUnbanAuthor

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/

alter table bans
    modify author_id int null;
alter table bans
    add unban_author_id int null after author_id;

alter table bans
    drop foreign key bans_clients_id_fk;
alter table bans
    drop foreign key bans_clients_id_fk2;

drop index bans_clients_id_fk on bans;
drop index bans_clients_id_fk2 on bans;

alter table bans
    add constraint bans_clients_id_fk
        foreign key (author_id) references clients (id);

alter table bans
    add constraint bans_clients_id_fk_2
        foreign key (client_id) references clients (id);

alter table bans
    add constraint bans_clients_id_fk_3
        foreign key (unban_author_id) references clients (id);

