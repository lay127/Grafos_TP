import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class BuscaEmProfundidade {
    private boolean[] marcado; // Confirma se um vertice ja foi marcado ou n
    private int count; // Contador global da busca

    BuscaEmProfundidade(Graph g){
        this.marcado = new boolean[g.V()];
    }

    public void busca(Graph g, int v){
        validarVertice(v);
        dfs(g, v);
    }

    private void dfs(Graph g, int v){
        count++;
        marcado[v] = true;
        for (Integer w : g.adj(v)) {
            if(!marcado(w)) { dfs(g, w); }
        }
    }

    public void verticesVisitados(Graph g){
        System.out.printf("Vertices visitados na busca: ");
        for (int v = 1; v < marcado.length; v++) {
            if (marcado(v)) { System.out.printf("%d ", v); }
        }
        System.out.println();
    }

    private boolean marcado(int v) {
        validarVertice(v);
        return marcado[v];
    }

    private void validarVertice(int v){
        int V = marcado.length;
        if (v < 0 || v >= V){
            throw new IllegalArgumentException("vertex " + v + " is not between 1 and " + (V-1));
        }
    }

    public List<List<Integer>> encontrarPontes(Graph G) throws Exception{
        // Inicialização de listas para guardar pontes e arestas do grafo
        List<List<Integer>> pontes = new ArrayList<>();
        List<List<Integer>> arestas = new ArrayList<>();
        
        // Coleta todas as arestas uma única vez
        for (int v = 1; v < G.V(); v++) {
            for (int w : G.getAdj(v)) {
                if (v < w) { // Garante que a aresta só é contada uma vez
                    arestas.add(Arrays.asList(v, w));
                }
            }
        }

        // Se o grafo tiver 0 ou 1 nó, não há pontes
        if (G.V() <= 1) {
            return pontes;
        }

        // Itera sobre cada aresta, remove e testa a conectividade
        for (List<Integer> aresta : arestas) {
            int v = aresta.get(0);
            int w = aresta.get(1);
            G.removerAresta(v, w);

            // Reinicializa 'marcado' para o novo teste de conectividade
            Arrays.fill(this.marcado, false);
            
            dfs(G, v);

            // Condição de Ponte: Se 'w' não foi marcado
            if (!this.marcado[w]) { 
                pontes.add(aresta);
            }
            
            // Restaura a aresta
            G.addEdge(v, w);
        }

        return pontes;
    }

    public boolean[] getMarcado() {
        return marcado;
    }

    public int getCount() {
        return count;
    }
    
}


public class Naive {    
    public static void main(String[] args) throws Exception {
        // ler nome do arquivo
        Scanner sc = new Scanner(System.in);
        File arquivo = new File(sc.nextLine());

        // inicializar variaveis com valor padrao
        Graph grafo = null;
        int n = 0, m = 0;

        // extrair dados do grafo do arquivo
        try {
            sc = new Scanner(arquivo);
            // ler n e m do arquivo
            n = sc.nextInt(); m = sc.nextInt();
            // inicializar grafo com n° de vertices
            grafo = new Graph(n);
            // ler resto do arquivo
            sc.nextLine(); // pular newline
            for (int i = 0; i < m; i++){
                int origem = sc.nextInt();
                int destino = sc.nextInt();
                grafo.addEdge(origem, destino);
            }
        } catch (FileNotFoundException e) {
           System.out.println(e);
        }

        if (grafo != null){
            BuscaEmProfundidade dfs = new BuscaEmProfundidade(grafo);
            List<List<Integer>> pontes = dfs.encontrarPontes(grafo);
            System.out.println("Pontes no Grafo: " + pontes);
        }

        sc.close();

    }
}
