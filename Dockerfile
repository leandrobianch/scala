FROM sbtscala/scala-sbt:eclipse-temurin-21.0.5_11_1.10.6_3.5.2

WORKDIR /app

# Instalar dependências do Spark
RUN apt-get update && apt-get install -y curl 
# Copy the project files directly into /app
COPY ./projetoNome ./

# Verificar o conteúdo do diretório antes de compilar (para depuração)
RUN ls -la /app

# Initial compilation
#RUN sbt clean compile
RUN sbt clean package

# Verificar o conteúdo do diretório de saída (para depuração)
RUN ls -la /app/target

EXPOSE 5005
CMD ["sh", "-c", "while true; do if [ -d '/app/target' ]; then echo 'working'; ls /app/target; else echo '/app/target does not exist'; exit 1 fi; sleep 5; done"]
