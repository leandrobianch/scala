FROM sbtscala/scala-sbt:eclipse-temurin-21.0.5_11_1.10.6_3.5.2

WORKDIR /app

# Instalar dependências do Spark
RUN apt-get update && apt-get install -y curl 
# Copy the project files directly into /app
COPY ./projetoNome ./

# Initial compilation
#RUN sbt clean compile
# Compilar o projeto e gerar o JAR
RUN sbt clean package

#EXPOSE 5005

#ENTRYPOINT ["sbt"]

#CMD ["run"]