public class TempoDeExecução {
    public static void main(String[] args) {
        long tempoInicial = System.currentTimeMillis();

        // execução do método


        long tempoFinal = System.currentTimeMillis();
        System.out.println("O metodo foi executado em " + (tempoFinal - tempoInicial) + " ms");
    }
}

// Fonte: https://pt.stackoverflow.com/questions/311872/como-fazer-um-algoritmo-em-java-que-possa-medir-o-tempo-de-execu%C3%A7%C3%A3o-do-algoritmo