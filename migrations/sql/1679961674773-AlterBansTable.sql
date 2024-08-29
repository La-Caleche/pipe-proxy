/* AlterBansTable

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/

alter table bans
    change author author_id int not null;

alter table bans
    change target client_id int not null;

alter table bans
    alter column end_at set default null;

alter table bans
    add constraint bans_clients_id_fk
        foreign key (author_id) references clients (id);

alter table bans
    add constraint bans_clients_id_fk2
        foreign key (client_id) references clients (id);
