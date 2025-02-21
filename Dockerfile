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


COPY ./mvnw /root


#DESCARGAR LAS DEPENDENCIAS
RUN ./mvnw dependency:go-offline

# COPIAR EL CODIGO FUENTE DENTRO DEL CONTENEDOR
COPY ./src /root/src

# CONSTRUIR NUESTRA APLICACION
#GENERA UN TARGET CON UN JAR
RUN ./mvnw clean install -DskipTests

# LEVANTAR UNA APLICACION CUANDO EL CONTENEDOR INICIE
#para generar el jar se debe hacer   mvn clean package -DskipTests y rezar que salga bien
ENTRYPOINT ["java","-jar","/root/target/spring-app-coworking-0.0.1-SNAPSHOT.jar"]




