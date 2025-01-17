# crawler-likelib

Crawler - `Особенностью записи данных кроулером в Indexed LikeLib database является порция данных 
размером в 1 блок. Каждый блок и все его транзакции, адреса, должны записываться атомарно. При этом 
возникает сложность в получении всеобъемлющего набора данных для атомарной записи, поскольку LikeLib 
fullnode не позволяет по http получить многие необходимые связи. Мы решили эту проблему засчет хэша 
внутри кроулера и механизма матчинга.`

## Требования

Запуск напрямую:
* java 11
* maven

Запуск в контейнере:
* Docker

## Запуск

Запуск напрямую:
* `mvn package -DskipTests`
* `java -jar /opt/app.jar --spring.profiles.active=prod`

Запуск в контейнере: 
* `docker build -t crawler-likelib:v1 .`
* `docker run -it -d --network="host" crawler-likelib:v1`

## License

MIT License

A short and simple permissive license with conditions only requiring preservation of copyright and license notices. Licensed works, modifications, and larger works may be distributed under different terms and without source code.