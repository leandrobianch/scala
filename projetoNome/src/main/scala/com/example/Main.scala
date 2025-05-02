import org.apache.spark.sql.{SparkSession, DataFrame, Row}
import org.apache.spark.sql.types._
import com.azure.storage.blob.{BlobServiceClientBuilder, BlobContainerClient}

object Main {
  def main(args: Array[String]): Unit = {
    println("Iniciando aplicação beta")
    if (args.isEmpty) {
      println("Erro: O nome da aplicação deve ser passado como argumento.")
      System.exit(1)
    }

    // Nome da aplicação passado como argumento
    val appName = args(0)
    println(s"Iniciando aplicação Spark: $appName")

    // Configuração do Spark
    val sparkMasterUrl = sys.env.getOrElse("SPARK_MASTER_URL", "spark://spark-master:7077")
    val spark = SparkSession.builder()
      .appName(appName) // Nome da aplicação
      .master(sparkMasterUrl)
      .config("spark.executor.memory", "1g") // Memória por executor
      .config("spark.executor.cores", "1")  // Núcleos por executor
      .getOrCreate()

    // Caminho do arquivo de entrada
    val inputPath = "src/fake.txt"

    // Ler o arquivo fake.txt
    val rawData = spark.read.text(inputPath).rdd

    // Processar os dados para criar um DataFrame
    val data = rawData.map { row =>
      val columns = row.getString(0).split(",").map(_.trim)
      Row(columns(0), columns(1))
    }

    val schema = StructType(Array(
      StructField("name", StringType, nullable = true),
      StructField("state", StringType, nullable = true)
    ))

    val df: DataFrame = spark.createDataFrame(data, schema)

    // Exibir o DataFrame
    df.show()

    // Caminho de saída (configurado via variável de ambiente)
    val outputPath = sys.env.getOrElse("STORAGE_PATH", "output.parquet")

    // Criar o container no Azure Storage, se necessário
    val connectionString = sys.env.getOrElse("AZURE_STORAGE_CONNECTION_STRING", 
      "DefaultEndpointsProtocol=http;AccountName=dev;AccountKey=devkey;BlobEndpoint=http://127.0.0.1:10000/dev;")
    val containerName = "dev"
    createContainerIfNotExists(connectionString, containerName)

    // Salvar como Parquet
    df.write.mode("overwrite").parquet(outputPath)

    println(s"Arquivo Parquet salvo em: $outputPath")

    spark.stop()
  }

  def createContainerIfNotExists(connectionString: String, containerName: String): Unit = {
    val blobServiceClient = new BlobServiceClientBuilder()
      .connectionString(connectionString)
      .buildClient()

    val containerClient: BlobContainerClient = blobServiceClient.getBlobContainerClient(containerName)

    if (!containerClient.exists()) {
      println(s"Container '$containerName' não encontrado. Criando...")
      containerClient.create()
      println(s"Container '$containerName' criado com sucesso.")
    } else {
      println(s"Container '$containerName' já existe.")
    }
  }
}