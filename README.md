# quarkus-reactive-hibernate-graphql-crud 

This project demonstrates a graphql crud application together using smallrye-graphql, 
panache and hibernate reactive(with ORM). It presents a set of movies together with actors and can be queried 
and mutated accordingly.

This project uses Quarkus, the Supersonic Subatomic Java Framework.

This project is presented in the following article.

[Creating a CRUD app with Quarkus, Reactive Hibernate, Panache and GraphQL using a PostgreSQL database](https://dvddhln.medium.com/creating-a-crud-app-with-quarkus-reactive-hibernate-panache-and-graphql-using-a-postgresql-216ecd75ee52)


###  Run Postgres docker container

     docker run -d --rm --name my_reative_db -e POSTGRES_USER=user -e POSTGRES_PASSWORD=password -e POSTGRES_DB=my_db -p 5432:5432 postgres:10.5

### Clean DB

    docker rm $(docker ps -a -q) -f

    docker volume prune
    

### Run live coding

    ./mvnw compile quarkus:dev

### Native image build

    ./mvnw package -Pnative -Dquarkus.native.container-build=true
    
### Go to the GraphQL UI

    http://localhost:8080/q/graphql-ui/
    
### GraphQL for all movies

    query allMovies {
      allMovies {
    	title
      	releaseDate
            director
            id
      }
    }
### Response allMovies

    {
      "data": {
        "allMovies": [
          {
            "title": "Reservoir Dogs",
            "releaseDate": "1993-02-26",
            "director": "Quentin Tarantino",
            "id": 1
          },
          {
            "title": "Pulp Fiction",
            "releaseDate": "1994-11-25",
            "director": "Quentin Tarantino",
            "id": 2
          },
          {
            "title": "The Mask",
            "releaseDate": "1994-12-25",
            "director": "Chuck Russel",
            "id": 3
          }
        ]
      }
    }
    
### Query by movieId

    query getMovie {
      movie(movieId: 1) {
        title
        director
        releaseDate
      }
    }
### Response getMovie

    {
      "data": {
        "movie": {
          "title": "Reservoir Dogs",
          "director": "Quentin Tarantino",
          "releaseDate": "1993-02-26"
        }
      }
    }    
### Query all movies with movie ids 

      query allMovies {
        movie1: movie(movieId: 1) {
          title
          releaseDate
          director
        }
        movie2: movie(movieId: 2) {
          title
          releaseDate
          director
        }
      }
 }

### Response

    {
      "data": {
        "movie1": {
          "title": "Reservoir Dogs",
          "releaseDate": "1993-02-26",
          "director": "Quentin Tarantino"
        },
        "movie2": {
          "title": "Pulp Fiction",
          "releaseDate": "1994-11-25",
          "director": "Quentin Tarantino"
        }
      }
    }
### Query movie with actors

    query getMovie {
      movie(movieId: 1) {
        title
        director
        releaseDate
        actors {
          name
        }
      }
    }
### Response

    {
      "data": {
        "movie": {
          "title": "Reservoir Dogs",
          "director": "Quentin Tarantino",
          "releaseDate": "1993-02-26",
          "actors": [
            {
              "name": "John Travolta"
            }
          ]
        }
      }
    }
### Query all movies with actors

       query allMovies {
         allMovies {
           title
           releaseDate
           director
           id
           actors {
             name
           }
         }
       }

### Response
    
    {
      "data": {
        "allMovies": [
          {
            "title": "Reservoir Dogs",
            "releaseDate": "1993-02-26",
            "director": "Quentin Tarantino",
            "id": 1,
            "actors": [
              {
                "name": "John Travolta"
              }
            ]
          },
          {
            "title": "Pulp Fiction",
            "releaseDate": "1994-11-25",
            "director": "Quentin Tarantino",
            "id": 2,
            "actors": [
              {
                "name": "Quentin Tarantino"
              },
              {
                "name": "John Travolta"
              },
              {
                "name": "Samuel L Jackson"
              }
            ]
          },
          {
            "title": "The Mask",
            "releaseDate": "1994-12-25",
            "director": "Chuck Russel",
            "id": 3,
            "actors": [
              {
                "name": "Jim Carrey"
              }
            ]
          }
        ]
      }
    }
### Create movie

    mutation addMovie {
      createMovie(
        movie: {title: "The Mask", releaseDate: "1994-12-25", director: "Chuck Russell"}
      ) {
        title
        releaseDate
        director
      }
    }
### Response

    {
      "data": {
        "createMovie": {
          "title": "The Mask",
          "releaseDate": "1994-12-25",
          "director": "Chuck Russell"
        }
      }
    }
### Update Movie

    mutation updateMovie {
      updateMovie(
        movieId: 1
        movie: {title: "The One"}
      ) {
        title
      }
    }

### Response

    {
      "data": {
        "updateMovie": {
          "title": "The One"
        }
      }
    }
### Delete Movie

    mutation deleteMovie {
      deleteMovie(movieId: 1)
    }
### Response

    {
      "data": {
        "deleteMovie": true
      }
    }
### Get All Actors

    query allActors {
      allActors {
        name
        id
      }
    }

### Response

    {
      "data": {
        "allActors": [
          {
            "name": "Quentin Tarantino",
            "id": 1
          },
          {
            "name": "Quentin Tarantino",
            "id": 2
          },
          {
            "name": "John Travolta",
            "id": 3
          },
          {
            "name": "Samuel L Jackson",
            "id": 4
          }
        ]
      }
    }
### Query all actors with movies

    query allActors {
      allActors {
        name
        movies {
          title
          director
        }
      }
    }
### Response 

    {
      "data": {
        "allActors": [
          {
            "name": "Quentin Tarantino",
            "movies": [
              {
                "title": "Pulp Fiction",
                "director": "Quentin Tarantino"
              }
            ]
          },
          {
            "name": "John Travolta",
            "movies": [
              {
                "title": "Reservoir Dogs",
                "director": "Quentin Tarantino"
              },
              {
                "title": "Pulp Fiction",
                "director": "Quentin Tarantino"
              }
            ]
          },
          {
            "name": "Samuel L Jackson",
            "movies": [
              {
                "title": "Pulp Fiction",
                "director": "Quentin Tarantino"
              }
            ]
          },
          {
            "name": "Jim Carrey",
            "movies": [
              {
                "title": "The Mask",
                "director": "Chuck Russel"
              }
            ]
          }
        ]
      }
    }
### Add actor to movie

    mutation addActorToMovie {
      addActorToMovie(movieId: 1, actorId: 4) {
        title
        releaseDate
        director
        actors {
          name
        }
      }
    }
### Response

    {
      "data": {
        "addActorToMovie": {
          "title": "The One",
          "releaseDate": "1993-02-26",
          "director": "Quentin Tarantino",
          "actors": [
            {
              "name": "John Travolta"
            },
            {
              "name": "Jim Carrey"
            }
          ]
        }
      }
    }
### Add movie to actor

    mutation addMovieToActor {
      addMovieToActor(movieId: 2, actorId: 4) {
        name
        id
        movies {
          title
          actors {
            name
          }
        }
      }
    }

### Response
    {
      "data": {
        "addMovieToActor": {
          "name": "Jim Carrey",
          "id": 4,
          "movies": [
            {
              "title": "Pulp Fiction",
              "actors": [
                {
                  "name": "Quentin Tarantino"
                },
                {
                  "name": "John Travolta"
                },
                {
                  "name": "Samuel L Jackson"
                },
                {
                  "name": "Jim Carrey"
                }
              ]
            },
            {
              "title": "The Mask",
              "actors": [
                {
                  "name": "Jim Carrey"
                }
              ]
            }
          ]
        }
      }
    }
    