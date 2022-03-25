# gsm

How to start docker

`docker run --name gymdb -e POSTGRES_PASSWORD=gymDB -d -p 5432:5432 postgres`\
`docker exec -it gymdb bash`\
`psql -U postgres`\
`create database gymdb`

And that is all
