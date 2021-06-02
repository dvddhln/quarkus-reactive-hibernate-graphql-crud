insert into movie(title,director,releaseDate) values ('Reservoir Dogs','Quentin Tarantino','1993-02-26');
insert into movie(title,director,releaseDate) values ('Pulp Fiction','Quentin Tarantino','1994-11-25');
insert into movie(title,director,releaseDate) values ('The Mask','Chuck Russel','1994-12-25');

insert into actor(name) values ('Quentin Tarantino');
insert into actor(name) values ('John Travolta');
insert into actor(name) values ('Samuel L Jackson');
insert into actor(name) values ('Jim Carrey');

insert into actormovieentity(movie_id,actor_id) values (2,1);
insert into actormovieentity(movie_id,actor_id) values (2,2);
insert into actormovieentity(movie_id,actor_id) values (2,3);
insert into actormovieentity(movie_id,actor_id) values (1,2);
insert into actormovieentity(movie_id,actor_id) values (3,4);