# TODO ESTO SE EJECUTA DE MANERA SECUENCIAL AL HACER CORRER CON DOCKER BUILD -T "MI BACKEND IMAGE"
# IMAGEN MODELO
FROM eclipse-temurin:21.0.3_9-jdk

# INFORMAR EL PUERTO DONDE SE EJECUTA EL CONTENEDOR (INFORMATIVO)
EXPOSE 8080

#definir directorio raiz de nuestro contenedor
WORKDIR /root

#COPIAR Y PEGAR ARCHIVOS DENTRO DEL CONTENEDOR
COPY ./pom.xml /root

#AGREGAR MVN
COPY ./.mvn /root/.mvn

#copiamos el mvnw para poder usar comandos run en docker ESTO ES CLAVE  para poder ejecutar comandos siguientes en el contenedor
COPY ./mvnw /root

#DESCARGAR LAS DEPENDENCIAS DE MAVEN
#comando mvnw es un script para ejecutar maven en linux/mac, para descargar lo necesario para construir el jar
#EQUIVALENTE A mvn clean package -DskipTests
RUN ./mvnw dependency:go-offline

# COPIAR TODO EL CODIGO FUENTE DENTRO DEL CONTENEDOR
COPY ./src /root/src

# CONSTRUIR NUESTRA APLICACION
#GENERA UN TARGET CON UN JAR, GENERA EL BYTECODE Y LO EMPAQUETA EN UN .JAR
RUN ./mvnw clean install -DskipTests

# LEVANTAR UNA APLICACION CUANDO EL CONTENEDOR INICIE
#para generar el jar se debe hacer   mvn clean package -DskipTests y rezar que salga bien
ENTRYPOINT ["java","-jar","/root/target/spring-app-coworking-0.0.1-SNAPSHOT.jar"]




