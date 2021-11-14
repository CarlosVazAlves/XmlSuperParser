class Extracto {
    lateinit var id: String
    lateinit var mesEmissao: String
    lateinit var total: String
    lateinit var totalIva: String
    lateinit var detalhes: List<Any>
}

class Identificador {
    lateinit var id: String
    lateinit var matricula: String
    lateinit var total: String
    lateinit var refPagamento: String
    lateinit var transacoes: List<Transaccao>
}

class Cliente {
    lateinit var id: String
    lateinit var nif: String
    lateinit var nome: String
    lateinit var morada: String
    lateinit var localidade: String
    lateinit var codigoPostal: String
}

class Transaccao {
    lateinit var dataEntrada: String
    lateinit var horaEntrada: String
    lateinit var entrada: String
    lateinit var dataSaida: String
    lateinit var horaSaida: String
    lateinit var saida: String
    lateinit var importancia: String
    lateinit var valorDesconto: String
    lateinit var taxaIva: String
    lateinit var operador: String
    lateinit var tipo: String
    lateinit var dataDebito: String
    lateinit var cartao: String
}
