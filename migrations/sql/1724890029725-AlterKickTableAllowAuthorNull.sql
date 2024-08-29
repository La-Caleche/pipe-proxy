/* AlterKickTableAllowAuthorNull

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/
alter table kicks
    modify author_id int null;

alter table kicks
    drop foreign key kicks_clients_id_fk;
alter table kicks
    drop foreign key kicks_clients_id_fk2;

drop index kicks_clients_id_fk on kicks;
drop index kicks_clients_id_fk2 on kicks;

alter table kicks
    add constraint kicks_clients_id_fk
        foreign key (author_id) references clients (id);

alter table kicks
    add constraint kicks_clients_id_fk_2
        foreign key (client_id) references clients (id);

