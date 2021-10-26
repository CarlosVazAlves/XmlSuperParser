class Movimentos {
    lateinit var documentos: List<Documento>
}

class Documento {
    lateinit var operador: String
    lateinit var tipoDocumento: String
    lateinit var ano: String
    lateinit var numero: String
    lateinit var movimentos: List<Movimento>
}

class Movimento {
    lateinit var numMov: String
    lateinit var numVoo: String
    lateinit var matricula: String
    lateinit var dataMov: String
    lateinit var horaMov: String
    lateinit var origem: String
    lateinit var destino: String
    lateinit var tipoPax: String
    lateinit var quant: String
    lateinit var taxa: String
    lateinit var total: String
}