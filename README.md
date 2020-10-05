# API Restaurant Sales

![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)

Este documento describe los siguientes temas concernientes a la ejecución y modificación del proyecto "Restaurant Sales".

  - Prerrequisitos
  - Ejecución sin compilación local
  - Ejecución con compilación local
  - Ejecución de pruebas unitarias
  - Comportamiento general del flujo

## Prerrequisitos

  - JDK 1.8
  - Docker
  - Maven
  - [Lombok](https://projectlombok.org/)
  
Se debe considerar que el único elemento necesario para ejecutarlo sin compilación local es Docker. Con respecto a los demás elementos, estos son necesarios en la ejecución del proyecto con compilación local.

## Ejecución sin compilación local

Con esta modalidad, no es necesario contar con el código fuente de la aplicación. Solo basta con tener el archivo [docker-compose.yml](https://github.com/luisorellana777/spring_boot_restaurant-sales/blob/master/restaurant-sales/docker-compose.yml) y ejecutarlo en la misma ruta donde se encuentre:

```sh
$ docker-compose up
```

Esto dará paso a un pull y run de tres imágenes:
  - [luisorellanaa/restaurant-sales:0.0.1-SNAPSHOT](https://hub.docker.com/repository/docker/luisorellanaa/restaurant-sales)
  - rabbitmq:management
  - mysql:5.7

## Ejecución con compilación local

Para este paso, es necesario contar con el código fuente del servicio.
Como primer paso, se debe ejecutar el siguiente comando en la ruta donde se encuentre el archivo [docker-compose.yml](https://github.com/luisorellana777/spring_boot_restaurant-sales/blob/master/restaurant-sales/docker-compose.yml):

```sh
$ docker-compose up mysql rabbit
```
Esto dejara en ejecución dos imágenes; Mysql y RabbitMQ.
Luego, en la ruta donde se encuentre el archivo [pom.xml](https://github.com/luisorellana777/spring_boot_restaurant-sales/blob/master/restaurant-sales/pom.xml), ejecutar:
```sh
$ ./mvnw spring-boot:run
```
De esta manera, se podrá modificar sin problemas el código base del servicio.

## Ejecución de pruebas unitarias
Por otro lado, si se desea ejecutar las pruebas unitarias, es necesario ejecutar el siguiente comando en la ruta donde se encuentre el archivo [docker-compose.yml](https://github.com/luisorellana777/spring_boot_restaurant-sales/blob/master/restaurant-sales/docker-compose.yml):

```sh
$ docker-compose up rabbit
```
Y luego ejecutar el siguiente comando donde se encuentre el archivo [pom.xml](https://github.com/luisorellana777/spring_boot_restaurant-sales/blob/master/restaurant-sales/pom.xml):
```sh
$ mvn clean package
```
Esto realizara dos pasos:
  - Ejecutara los test unitarios, y luego la creación del jar.
  - Creara una imagen Docker local, utilizando el archivo [Dockerfile](https://github.com/luisorellana777/spring_boot_restaurant-sales/blob/master/restaurant-sales/Dockerfile). Esto se realiza por medio de un plugin inserto en el archivo [pom.xml](https://github.com/luisorellana777/spring_boot_restaurant-sales/blob/master/restaurant-sales/pom.xml). Este plugin es [dockerfile-maven-plugin](https://mvnrepository.com/artifact/com.spotify/dockerfile-maven-plugin).

Para realizar las pruebas unitarias, es necesario contar con RabbitMQ, ya que en este paso se crea una cola de mensajería especial para dicho test. Luego de que finalizan estas pruebas, la cola de test es eliminada de manera automática. Por otra parte, no es necesario utilizar la imagen Docker MySQL, ya que en la fase de pruebas, se crea una base de datos en memoria. Esta base de datos es [H2](https://www.h2database.com/html/main.html).

## Comportamiento general del flujo
### Endpoints

Los endpoints disponibles son:
  - GET: http://localhost:8091/actuator
  - GET: http://localhost:8091/swagger-ui.html
  - PUT: http://localhost:8091/login
  - GET: http://localhost:8091/sale
  - PUT: http://localhost:8091/sale
  - GET: http://localhost:8091/sales
  - PUT: http://localhost:8091/sales

Todos los endpoint se encuentran bloqueados por medio de autenticación, por medio de [JWT](https://jwt.io/), con excepción de http://localhost:8091/actuator, http://localhost:8091/swagger-ui.html y http://localhost:8091/login.

Con el objetivo de simplificar la utilización del servicio, se detallarán las consultas de los endpoints por medio de [Swagger](http://localhost:8091/swagger-ui.html):
Como primer paso, se debe consultar el endpoint http://localhost:8091/login.
Las credenciales son:
```
email:    arroba@punto.ceele
password: restorrtt
```
Con esto se obtendrá un mensaje, el cual contendrá un token como:

```diff
{"message":"Credencial Correcto.",
"token":"Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJrZXlJZEpXVCIsInN1YiI6ImFycm9iYUBwdW50by5jZWVsZSIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2MDE5MTk4MDMsImV4cCI6MTYwMTkyMDQwM30.-aioy6JbQzWdA9AGNRcxjIDNkNJDs-_HzlEBaI8sejbIzgk6ecvMZzyr7mLWL-0bGEk0qIoP6caVUv7TO7P8Xg"}
```
Se debe ingresar el token en conjunto con la palabra "Bearer" en el pop up desplegado al presionar el botón "Autorize", en el costado superior derecho de [Swagger](http://localhost:8091/swagger-ui.html).

Luego de esto, es posible consultar todos los endpoints anteriormente descritos.

#### Descripción de Endpoints

Los siguientes endpoints realizan pull de elemento(s) a la cola de mensajería implementada en RabbitMQ:
  - GET: http://localhost:8091/sale
  - GET: http://localhost:8091/sales

Por otra parte, los métodos que permiten realizar push de elemento(s) a la cola de mensajería son:
  - PUT: http://localhost:8091/sale
  - PUT: http://localhost:8091/sales

La estructura json de la entidad "Sale" que se debe enviar es:

```
{
  "products": [
    {
      "price": 0,
      "sku": "string"
    }
  ],
  "waiter": {
    "fisrtName": "string",
    "lastName": "string",
    "rut": "string",
    "tip": 0
  }
}
```

Se deben considerar las siguientes validaciones:
  - products[...]: Debe tener al menos un elemento.
  - products[...].price: Debe tener un monto mayor a cero.
  - products[...].sku: Debe contener algún valor alfa-numérico.
  - waiter.fisrtName: Debe contener algún valor alfa-numérico.
  - waiter.lastName: Debe contener algún valor alfa-numérico.
  - waiter.rut: El Rut debe ser válido: Ejemplo (00012348-k).
  - waiter.tip: La propina no puede ser menor a cero.

Por otra parte, la entidad "Sale" contiene un atributo, el cual no debe ser enviado al momento de hacer un push a la cola. Esta entidad es:
```
  "amounts": {
    "neto": 0,
    "tax": 0,
    "total": 0
  }
```
Esta entidad no debe ser enviada como parte de "Sale", ya que todos estos valores son auto calculados.

#### Tolerancia a Fallos
En el caso de que se deseé realizar un push a la cola, y RabbitMQ no se encuentra disponible, el servicio realizara un control de excepción, el cual persiste la venta en MySQL con un estado determinado.
Por otra parte, un scheduler es ejecutado de manera paralela (al momento de la auto configuración de spring), el cual consulta de manera frecuente (una vez por hora, de lunes a sábado) las ventas persistidas en Mysql.
En el caso de que este proceso encuentre una venta disponible, intentara realizar un push a la cola. Si esta acción es exitosa, se elimina el registro(venta) de Mysql de manera suave (hibernate soft delete), modificando el estado de la entidad.
