public class Main {
    public static void main(String[] args) {
        long tempoInicial = System.currentTimeMillis();

        // execução do método
        System.out.println("oi");

        long tempoFinal = System.currentTimeMillis();
        System.out.println("O método foi executado em " + (tempoFinal - tempoInicial) + " ms");
    }
}

// Fonte: https://pt.stackoverflow.com/questions/311872/como-fazer-um-algoritmo-em-java-que-possa-medir-o-tempo-de-execu%C3%A7%C3%A3o-do-algoritmo