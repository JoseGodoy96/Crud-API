# Imagen base oficial de Eclipse Temurin con Java 17 instalado
# Será el sistema operativo + JVM sobre el que correrá la aplicación
FROM eclipse-temurin:17-jdk

# Directorio de trabajo dentro del contenedor
# Los siguientes comandos se ejecutarán desde /app
WORKDIR /app

# Copia el archivo .jar generado por Maven
# Desde:
#   target/mi-aplicacion.jar
# Hacia:
#   /app/app.jar
COPY target/*.jar app.jar

# Documenta que la aplicación escucha en el puerto 8080
# No abre el puerto, solo informa a Docker
EXPOSE 8080

# Comando que se ejecutará automáticamente
# cuando el contenedor arranque
#
# Equivale a ejecutar:
# java -jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]